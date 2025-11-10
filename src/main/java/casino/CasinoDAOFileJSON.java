package casino;

import jakarta.json.*;
import jakarta.json.stream.JsonGenerator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//ToDo[!1]: Comentar Codigo
public class CasinoDAOFileJSON implements CasinoDAO {
    //Rutas y Archivos
    //Clientes
    Path pathCliente = Path.of("src", "main", "java", "casino", "recursos", "json", "cliente.json");
    File fileCliente = new File(pathCliente.toString());

    //Log
    Path pathLog = Path.of("src", "main", "java", "casino","recursos", "json", "log.json");
    File fileLog = new File(pathLog.toString());

    //Servicio
    Path pathServicio = Path.of("src", "main", "java", "casino", "recursos", "json", "servicio.json");
    File fileServicio = new File(pathServicio.toString());



    private void escribirCliente(List<Cliente> listaClientes) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Cliente cliente : listaClientes) {
            JsonObject jobj = Json.createObjectBuilder()
                    .add("dni", cliente.getDni())
                    .add("nombre", cliente.getNombre())
                    .add("apellido", cliente.getApellidos())
                    .build();
            jsonArrayBuilder.add(jobj);
        }

        // 3 líneas de configuración para el pretty printing
        Map<String, Object> config = new HashMap <>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(config);

        try (FileWriter fileWriter = new FileWriter(fileCliente);
             JsonWriter jsonWriter = writerFactory.createWriter(fileWriter)){
            jsonWriter.writeArray(jsonArrayBuilder.build());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addCliente(Cliente cliente) throws IOException {
        //Obtener ArrayList<Cliente> del archivo
        ArrayList<Cliente> listaClientes = (ArrayList<Cliente>) this.leerListaClientes();

        //Agregar el objeto cliente
        listaClientes.add(cliente);

        escribirCliente(listaClientes);
    }

    //Ejemplo de metodo que no está en la interfaz
    public void addCliente(List<Cliente> clientes) throws IOException {
        ArrayList<Cliente> listaClient = (ArrayList<Cliente>) this.leerListaClientes();

        for (Cliente cliente : clientes) {
            listaClient.add(cliente);
        }

        escribirCliente(listaClient);

    }

    private void escribirServicio(List<Servicio> listaServicio) {
        //Crear el Objeto JsonArrayBuilder
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        //Recorrer la lista de clientes, transformar cada elemento en un JsonObject y agregarlo al JsonArrayBuilder
        for (Servicio ser : listaServicio) {
            //Json Array Clientes
            JsonArrayBuilder jsonArrCliBui = Json.createArrayBuilder();
            for (Cliente cli : ser.getListaClientes()){
                jsonArrCliBui.add((Json.createObjectBuilder())
                        .add("dni", cli.getDni())
                        .add("nombre", cli.getNombre())
                        .add("apellidos", cli.getApellidos())
                        .build()
                );
            }

            JsonObject jobj = Json.createObjectBuilder()
                    .add("codigo", ser.getCodigo())
                    .add("tipoServicio", ser.getTipo().toString())
                    .add("nombreServicio", ser.getNombreServicio())
                    .add("listaClientes", jsonArrCliBui.build())
                    .add("capacidadMaxima",  ser.getCapacidadMaxima())
                    .build();
            jsonArrayBuilder.add(jobj);
        }

        // 3 líneas de configuración para el pretty printing - Copiado de los apuntes
        Map<String, Object> config = new HashMap <>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(config);

        //Abrir el archivo Servicio.json y usar un JsonWriter para escribir el JsonArray
        try (FileWriter fileWriter = new FileWriter(fileServicio);
             JsonWriter jsonWriter = writerFactory.createWriter(fileWriter)){

            jsonWriter.writeArray(jsonArrayBuilder.build());

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addServicio(Servicio servicio) {
        //Obtener ArrayList<Servicio> del archivo
        ArrayList<Servicio> listaServicio = (ArrayList<Servicio>) this.leerListaServicios();

        //Agregar el objeto cliente
        listaServicio.add(servicio);

        escribirServicio(listaServicio);

    }

    public void addServicio(List<Servicio> servicios) {

        //Obtener ArrayList<Servicio> del archivo
        ArrayList<Servicio> listaServicio = (ArrayList<Servicio>) this.leerListaServicios();
        //Agregar los objetos Servicio
        for (Servicio ser : servicios) {
            listaServicio.add(ser);
        }

        escribirServicio(listaServicio);
    }

    private void escribirLog(List<Log> listaLog){
        //Crear el Objeto JsonArrayBuilder
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        //Recorrer la lista de clientes, transformar cada elemento en un JsonObject y agregarlo al JsonArrayBuilder
        for (Log logFor : listaLog) {
            //Json Cliente
            JsonObject jsonClient = Json.createObjectBuilder()
                    .add("dni", logFor.getCliente().getDni())
                    .add("nombre", logFor.getCliente().getNombre())
                    .add("apellido", logFor.getCliente().getApellidos())
                    .build();



            //Json Servicio
            //Json Array listaClientes
            JsonArrayBuilder jsonArrCliBui = Json.createArrayBuilder();
            for (Cliente cli : logFor.getServicio().getListaClientes()){
                jsonArrCliBui.add((Json.createObjectBuilder())
                        .add("dni", cli.getDni())
                        .add("nombre", cli.getNombre())
                        .add("apellidos", cli.getApellidos())
                        .build()
                );
            }

            JsonObject jsonServicio = Json.createObjectBuilder()
                    .add("codigo", logFor.getServicio().getCodigo())
                    .add("tipoServicio", logFor.getServicio().getTipo().toString())
                    .add("nombreServicio", logFor.getServicio().getNombreServicio())
                    .add("listaClientes", jsonArrCliBui.build())
                    .add("capacidadMaxima", logFor.getServicio().getCapacidadMaxima())
                    .build();


            //JsonLog
            JsonObject jobj = Json.createObjectBuilder()
                    .add("cliente", jsonClient)
                    .add("servicio", jsonServicio)
                    .add("fecha", logFor.getFechaStr())
                    .add("hora", logFor.getHoraStr())
                    .add("tipoConcepto",  logFor.getConcepto().toString())
                    .add("cantidadConcepto", logFor.getCantidadConcepto())
                    .build();
            jsonArrayBuilder.add(jobj);
        }

        // 3 líneas de configuración para el pretty printing - Copiado de los apuntes
        Map<String, Object> config = new HashMap <>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(config);

        //Abrir el archivo log.json y usar un JsonWriter para escribir el JsonArray
        try (FileWriter fileWriter = new FileWriter(fileLog);
             JsonWriter jsonWriter = writerFactory.createWriter(fileWriter)){

            jsonWriter.writeArray(jsonArrayBuilder.build());

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addLog(Log log) {
        //Obtener ArrayList<SLog> del archivo
        ArrayList<Log> listaLog = (ArrayList<Log>) this.leerListaLog();

        //Agregar el objeto Log
        listaLog.add(log);

        escribirLog(listaLog);

    }

    public void addLog(List<Log> listaLogAgregar) {
        //Obtener ArrayList<SLog> del archivo
        ArrayList<Log> listaLog = (ArrayList<Log>) this.leerListaLog();

        //Agregar los Objetos Log
        for  (Log logFor : listaLogAgregar) {
            listaLog.add(logFor);
        }

        escribirLog(listaLog);
    }

    @Override
    public String consultaServicio(String codigo) {
        return "";
    }

    @Override
    public List<Servicio> leerListaServicios() {
        List<Servicio> listaServicios = new ArrayList<>();

        try (FileReader fileReader = new FileReader(fileServicio);
             JsonReader jsonReader = Json.createReader(fileReader)) {

            var jsonArray = jsonReader.readArray();

            jsonArray.forEach(jsonValue -> {
                JsonObject jsonObject = jsonValue.asJsonObject();

                String codigo =  jsonObject.getString("codigo");
                TipoServicio tipoServicio = TipoServicio.valueOf(jsonObject.getString("tipoServicio"));
                String nombreServicio =  jsonObject.getString("nombreServicio");

                //Lista CLientes
                ArrayList<Cliente> clientesServicios = new ArrayList<>();
                JsonArray arr = jsonObject.getJsonArray("listaClientes");
                for (int i = 0; i < arr.size(); i++) {
                    JsonObject jobjCliente = arr.getJsonObject(i);
                    String dni = jobjCliente.getString("dni");
                    String nombre = jobjCliente.getString("nombre");
                    String apellido = jobjCliente.getString("apellido");
                    clientesServicios.add(new  Cliente(dni, nombre, apellido));
                }

                int capacidadMaxima = jsonObject.getInt("capacidadMaxima");


                Servicio auxServicio = new Servicio(
                        codigo, tipoServicio, nombreServicio, clientesServicios, capacidadMaxima
                );
                listaServicios.add(auxServicio);
            });
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return listaServicios;
    }

    @Override
    public String consultaCliente(String dni) throws IOException {
        ArrayList<Cliente> listaClientes =  (ArrayList<Cliente>) this.leerListaClientes();

        for (Cliente cliente : listaClientes) {
            if (cliente.getDni().equals(dni)) {
                return cliente.toString();
            }
        }
        return "No se ha encontrado el cliente";
    }

    @Override
    public List<Cliente> leerListaClientes() throws IOException {
        List<Cliente> listaClientes = new ArrayList<>();

        try (FileReader fileReader = new FileReader(fileCliente);
             JsonReader jsonReader = Json.createReader(fileReader)) {

            var jsonArray = jsonReader.readArray();

            jsonArray.forEach(jsonValue -> {
                JsonObject jsonObject = jsonValue.asJsonObject();

                Cliente auxCliente = new Cliente(
                        jsonObject.getString("dni"),
                        jsonObject.getString("nombre"),
                        jsonObject.getString("apellido")
                );
                listaClientes.add(auxCliente);
            });
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return listaClientes;
    }

    @Override
    public String consultaLog(String codigo, String dni, LocalDate fecha) {
        ArrayList<Log> listaLog = (ArrayList<Log>) this.leerListaLog();
        String fechaStr = fecha.toString();
        for (Log log : listaLog) {
            if (log.getCliente().getDni().equals(dni) && log.getServicio().getCodigo().equals(codigo) && log.getFechaStr().equals(fechaStr)) {
                return log.toString();
            }
        }
        return "No se ha encontrado el log";
    }

    @Override
    public List<Log> leerListaLog() {
        List<Log> listaLog = new ArrayList<>();

        try (FileReader fileReader = new FileReader(fileLog);
             JsonReader jsonReader = Json.createReader(fileReader)) {

            var jsonArray = jsonReader.readArray();

            jsonArray.forEach(jsonValue -> {
                JsonObject jsonObject = jsonValue.asJsonObject();

                JsonObject jsonCliente =  jsonObject.getJsonObject("cliente");

                String dni =  jsonCliente.getString("dni");
                String nombre =  jsonCliente.getString("nombre");
                String apellido =  jsonCliente.getString("apellido");


                JsonObject jsonServicio = jsonObject.getJsonObject("servicio");

                String codigo  =  jsonServicio.getString("codigo");
                TipoServicio tipoServicio = TipoServicio.valueOf(jsonServicio.getString("tipoServicio"));
                String nombreServicio =  jsonServicio.getString("nombreServicio");

                //Lista CLientes
                ArrayList<Cliente> clientesServicios = new ArrayList<>();
                JsonArray arr = jsonObject.getJsonObject("servicio").getJsonArray("listaClientes");
                for (int i = 0; i < arr.size(); i++) {
                    JsonObject jobjCliente = arr.getJsonObject(i);
                    String dniCli = jobjCliente.getString("dni");
                    String nombreCli = jobjCliente.getString("nombre");
                    String apellidoCli = jobjCliente.getString("apellido");
                    clientesServicios.add(new Cliente(dniCli, nombreCli, apellidoCli));
                }
                int capacidadMaxima = jsonServicio.getInt("capacidadMaxima");

                Cliente clie = new Cliente(dni, nombre, apellido);
                Servicio serv = new Servicio(
                        codigo, tipoServicio, nombreServicio, clientesServicios, capacidadMaxima
                );

                String fechastr = jsonObject.getString("fecha");
                LocalDate fecha = LocalDate.parse(fechastr);

                String horastr = jsonObject.getString("hora");
                LocalTime hora= LocalTime.parse(horastr);

                TipoConcepto tipoConcepto = TipoConcepto.valueOf(jsonObject.getString("tipoConcepto"));
                double cantidadConcepto = jsonObject.getJsonNumber("cantidadConcepto").doubleValue();

                Log auxLog = new Log(clie, serv, fecha, hora, tipoConcepto, cantidadConcepto);
                listaLog.add(auxLog);
            });
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return listaLog;
    }

    @Override
    public boolean actualizarServicio(String codigo, Servicio servicioActualizado) {
        return false;
    }

    @Override
    public boolean actualizarCliente(String dni, Cliente clienteActualizado) {
        return false;
    }

    @Override
    public boolean borrarServicio(Servicio servicio) {
        boolean borrado = false;
        ArrayList<Servicio>  listaServicios = (ArrayList<Servicio>) this.leerListaServicios();
        for (Servicio servicios : listaServicios) {
            if (servicios.equals(servicio)) {
                borrado = true;
                int index =  listaServicios.indexOf(servicios);
            }
        }
        if (borrado) {
            listaServicios.remove(servicio);
            escribirServicio(listaServicios);

        }
        return borrado;
    }

    @Override
    public boolean borrarCliente(Cliente cliente) throws IOException {
        boolean borrado = false;
        ArrayList<Cliente> listaClientes = (ArrayList<Cliente>) this.leerListaClientes();
        for (Cliente clientes : listaClientes){
            if (clientes.equals(cliente)) {
                borrado = true;
                int index = listaClientes.indexOf(clientes);
            }
        }
        if (borrado) {
            listaClientes.remove(cliente);
            escribirCliente(listaClientes);
        }
        return borrado;
    }

    @Override
    public double gananciasAlimentos(String dni) throws IllegalArgumentException, IOException {
        return 0;
    }

    @Override
    public double dineroInvertidoClienteEnDia(String dni, LocalDate fecha) {
        return 0;
    }

    //ToDo: ??
    public double GanaciasAlimentos(String dni, String concepto) {
        ArrayList<Log> listaLog = (ArrayList<Log>) this.leerListaLog();
        double ganado = 0;
        for (Log log : listaLog) {
            if (log.getCliente().getDni().equals(dni) && log.getConcepto().toString().equals(concepto)) {
                ganado += log.getCantidadConcepto();
            }
        }
        return 0;
    }

    public double dineroGanadoClienteEnDia(String dni, LocalDate fecha) {
        ArrayList<Log> listaLog = (ArrayList<Log>) this.leerListaLog();
        double ganado = 0;
        String fechaStr = fecha.toString();

        for (Log log : listaLog) {
            if (log.getCliente().getDni().equals(dni) && log.getFechaStr().equals(fechaStr) && log.getConcepto().equals(TipoConcepto.APUESTACLIENTEGANA)) {
                ganado +=  log.getCantidadConcepto();
            }
        }
        return ganado;
    }

    @Override
    public int vecesClienteJuegaMesa(String dni, String codigo) {
        return 0;
    }

    @Override
    public double ganadoMesas() {
        return 0;
    }

    @Override
    public double ganadoEstablecimientos() {
        return 0;
    }

    @Override
    public List<Servicio> devolverServiciosTipo(TipoServicio tipoServicio) {
        ArrayList<Servicio> listaServicios = (ArrayList<Servicio>) this.leerListaServicios();

        ArrayList<Servicio> listaTipoServicios = new ArrayList<>();
        for (Servicio servicios : listaServicios) {
            if (servicios.getTipo().equals(tipoServicio)) {
                listaTipoServicios.add(servicios);
            }
        }
        return listaTipoServicios;
    }
}
