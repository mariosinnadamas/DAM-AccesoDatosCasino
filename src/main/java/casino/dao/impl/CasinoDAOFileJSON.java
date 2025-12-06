package casino.dao.impl;

import casino.dao.CasinoDAO;
import casino.model.*;
import exceptions.*;
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


public class CasinoDAOFileJSON implements CasinoDAO {
    //Rutas y Archivos
    //Clientes
    public Path pathCliente = Path.of("src", "main", "resources", "data", "json", "cliente.json");
    public File fileCliente = new File(pathCliente.toString());

    //Log
    Path pathLog = Path.of("src", "main", "resources", "data", "json", "log.json");
    File fileLog = new File(pathLog.toString());

    //Servicio
    Path pathServicio = Path.of("src", "main", "resources", "data", "json", "servicio.json");
    File fileServicio = new File(pathServicio.toString());

    public void setFileCliente(File fileCliente) {
        this.fileCliente = fileCliente;
    }

    public void setFileLog(File fileLog) {
        this.fileLog = fileLog;
    }

    public void setFileServicio(File fileServicio) {
        this.fileServicio = fileServicio;
    }

    /**
     * Devuelve una lista de clientes del archivo cliente.json
     * @return List de Cliente del archivo cliente.json
     * @throws IOException
     */
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

    /**
     * Sobreescribe en el archivo cliente.json por los clientes introducidos por parámetros.
     * @param listaClientes ArrayList de clientes
     * @throws IOException cuando no se en
     */
    private void escribirCliente(List<Cliente> listaClientes) throws IOException {
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

    /**
     * Añade un cliente al archivo json, no sobreescribe los clientes del archivo
     * @param cliente objeto que recibe como parámetro para agregar al almacén
     * @throws IOException
     * @throws ClientAlreadyExistsException Lanza excepción si el cliente ya estaba en el archivo
     */
    @Override
    public void addCliente(Cliente cliente) throws IOException, ClientAlreadyExistsException, ValidacionException {
        //Validación de parámetros
        if (cliente == null) {
            throw new ValidacionException("ERROR: El cliente no puede ser nulo");
        }
        //Obtener ArrayList<Cliente> del archivo
        ArrayList<Cliente> listaClientes = (ArrayList<Cliente>) this.leerListaClientes();

        if (listaClientes.contains(cliente)) {
            throw new ClientAlreadyExistsException("Cliente existente");
        } else {
            //Agregar el objeto cliente
            listaClientes.add(cliente);

            escribirCliente(listaClientes);
        }
    }

    /**
     * Añade una lista de clientes al archivo json, no sobreescribe los clientes del archivo
     * @param clientes Lista de clientes a agregar al json
     * @throws IOException
     * @throws ClientAlreadyExistsException Lanza la excepción si un cliente ya estaba en el archivo
     */
    public void addListaClientes(List<Cliente> clientes) throws IOException, ClientAlreadyExistsException, ValidacionException {
        ArrayList<Cliente> listaClient = (ArrayList<Cliente>) this.leerListaClientes();

        for (Cliente cliente : clientes) {
            if (cliente == null) {
                throw new ValidacionException("El cliente no puede ser nulo");
            }
            if (listaClient.contains(cliente)) {
                throw new  ClientAlreadyExistsException("El cliente ya está en el archivo");
            }
            listaClient.add(cliente);
        }

        escribirCliente(listaClient);

    }

    /**
     * Devuelve una lista de servicios del archivo servicios.json
     * @return Lista de servicios del archivo servicios.json
     * @throws IOException
     */
    @Override
    public List<Servicio> leerListaServicios() throws IOException{
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

    /**
     * Escribe una lista de servicios en el archivo servicios. Sobreescribe el contenido
     * @param listaServicio Lista de servicios a escribir en el json
     * @throws IOException
     */
    private void escribirServicio(List<Servicio> listaServicio) throws IOException {
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

    /**
     * Añade un Servicio al archivo json de servicios. No sobreescribe los existentes
     * @param servicio objeto que recibe como parámetro para agregar al almacén
     * @throws IOException
     * @throws ServiceAlreadyExistsException Lanza la excepción si el servicio está presente en el archivo
     */
    @Override
    public void addServicio(Servicio servicio) throws IOException, ServiceAlreadyExistsException, ValidacionException {
        if (servicio==null){
            throw new ValidacionException("Servicio no puede ser nulo");
        }

        //Obtener ArrayList<Servicio> del archivo
        ArrayList<Servicio> listaServicio = (ArrayList<Servicio>) this.leerListaServicios();
        boolean existe = listaServicio.stream()
                .anyMatch(c -> c.getCodigo().equalsIgnoreCase(servicio.getCodigo()));

        if (existe){
            throw new ServiceAlreadyExistsException("Servicio existente");
        } else {
            listaServicio.add(servicio);
            escribirServicio(listaServicio);
        }
    }

    /**
     * Añade una lista de servicios al archivo json. No sobreescribe el archivo
     * @param servicios Lista de servicios a agregar en el json
     * @throws IOException
     * @throws ServiceAlreadyExistsException Lanza excepción si ya está presente en el json
     */
    public void addListaServicios(List<Servicio> servicios) throws IOException, ServiceAlreadyExistsException, ValidacionException {

        //Obtener ArrayList<Servicio> del archivo
        ArrayList<Servicio> listaServicio = (ArrayList<Servicio>) this.leerListaServicios();
        //Agregar los objetos Servicio
        for (Servicio ser : servicios) {
            if (ser == null){
                throw new ValidacionException("Servicio no puede ser nulo");
            }
            if (listaServicio.contains(ser)){
                throw new ServiceAlreadyExistsException("Servicio ya existente");
            }
            listaServicio.add(ser);
        }

        escribirServicio(listaServicio);
    }

    /**
     * Devuelve una lista de logs del archivo log.json
     * @return List de log del archivo log.json
     * @throws IOException
     */
    @Override
    public List<Log> leerListaLog() throws IOException{
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

    /**
     * Escribe una lista de log en el archivo json. Sobreescribe el contenido
     * @param listaLog Lista de log a escribir en el archivo
     * @throws IOException
     */
    private void escribirLog(List<Log> listaLog) throws IOException {
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

    /**
     * Añade un log al archivo json. No sobreeescribe el contenido
     * @param log objeto que recibe como parámetro para agregar al almacén
     * @throws IOException
     */
    @Override
    public void addLog(Log log) throws IOException, ValidacionException{
        if (log == null) {
            throw new  ValidacionException("Log no puede ser null");
        }

        //Obtener ArrayList<SLog> del archivo
        ArrayList<Log> listaLog = (ArrayList<Log>) this.leerListaLog();

        //Agregar el objeto Log
        listaLog.add(log);

        escribirLog(listaLog);

    }

    /**
     * Añade una lista de logs al archivo json. No sobreescribe el contenido.
     * @param logs Lista de log a agregar al archivo
     * @throws IOException
     */
    public void addListaLogs(List<Log> logs) throws IOException, ValidacionException {
        //Obtener ArrayList<SLog> del archivo
        ArrayList<Log> listaLog = (ArrayList<Log>) this.leerListaLog();

        //Agregar los Objetos Log
        for (Log logFor : logs) {
            if  (logFor == null) {
                throw new  ValidacionException("Log no puede ser null");
            }
            listaLog.add(logFor);
        }

        escribirLog(listaLog);
    }


    /**
     * Consulta los datos de un objeto cliente
     * @param dni String único de un cliente
     * @return El cliente en formato String
     * @throws IOException
     * @throws ClientNotFoundException Si no se encuentra el dni del cliente en el archivo
     */
    @Override
    public String consultaCliente(String dni) throws IOException, ClientNotFoundException {
        ArrayList<Cliente> listaClientes =  (ArrayList<Cliente>) this.leerListaClientes();

        for (Cliente cliente : listaClientes) {
            if (cliente.getDni().equals(dni)) {
                return cliente.toString();
            }
        }
        throw new ClientNotFoundException("Cliente no encontrado");
    }

    /**
     * Consulta los datos de un objeto servicio
     * @param codigo único del servicio del que se quiere consultar la información
     * @return El servicio en formato String
     * @throws IOException
     * @throws ServiceNotFoundException Si no se encuentra el código del servicio en el archivo
     */
    @Override
    public String consultaServicio(String codigo) throws IOException, ServiceNotFoundException {
        ArrayList<Servicio> listaServicio = (ArrayList<Servicio>) this.leerListaServicios();
        for (Servicio servicio : listaServicio){
            if (servicio.getCodigo().equals(codigo)){
                return servicio.toString();
            }
        }
        throw new ServiceNotFoundException("Servicio no encontrado");
    }


    /**
     * Consulta el primer log de un cliente en un servicio específico en un dia
     * @param codigo del servicio a buscar
     * @param dni del cliente a buscar en el log
     * @param fecha del log
     * @return log encontrado en formato String
     * @throws IOException
     * @throws LogNotFoundException Si no se encuentra el log especificado
     */
    @Override
    public String consultaLog(String codigo, String dni, LocalDate fecha) throws IOException, LogNotFoundException{
        ArrayList<Log> listaLog = (ArrayList<Log>) this.leerListaLog();
        String fechaStr = fecha.toString();
        for (Log log : listaLog) {
            if (log.getCliente().getDni().equals(dni) && log.getServicio().getCodigo().equals(codigo) && log.getFechaStr().equals(fechaStr)) {
                return log.toString();
            }
        }
        throw new LogNotFoundException("No se ha encontrado el log especificado");
    }

    /**
     * Actualiza un cliente de la lista clientes
     * @param dni único que permite identificar al Cliente
     * @param clienteActualizado objeto del cliente actualizado
     * @return
     * @throws IOException
     * @throws ClientNotFoundException
     */
    @Override
    public boolean actualizarCliente(String dni, Cliente clienteActualizado) throws IOException, ClientNotFoundException{
        ArrayList<Cliente> listaCliente = (ArrayList<Cliente>) this.leerListaClientes();

        int index = -1;
        for (Cliente cliente : listaCliente) {
            if (cliente.getDni().equals(dni)){
                index = listaCliente.indexOf(cliente);
            }
        }
        if (index == -1){
            throw new ClientNotFoundException("Cliente no encontrado");
        } else {
            listaCliente.set(index, clienteActualizado);
            escribirCliente(listaCliente);
            return true;
        }
    }

    /**
     * Actualiza un servicio de la lista servicios
     * @param codigo único de un Servicio
     * @param servicioActualizado objeto servicio por el que se actualiza
     * @return True si el servicio ha sido actualizado
     * @throws IOException
     * @throws ServiceNotFoundException Lanza excepción si no se encuentra el servicio
     */
    @Override
    public boolean actualizarServicio(String codigo, Servicio servicioActualizado) throws IOException, ServiceNotFoundException{
        ArrayList<Servicio> listaServicio = (ArrayList<Servicio>) this.leerListaServicios();

        int index = -1;
        for (Servicio servicio : listaServicio) {
            if (servicio.getCodigo().equals(codigo)){
                index = listaServicio.indexOf(servicio);
            }
        }
        if (index == -1){
            throw new ServiceNotFoundException("Servicio no encontrado");
        } else {
            listaServicio.set(index, servicioActualizado);
            escribirServicio(listaServicio);
            return true;
        }
    }


    /**
     * Borra un servicio del archivo servicio.json
     * @param servicio recibe el objeto Mesa
     * @return True si el servicio ha sido borrado
     * @throws IOException
     * @throws ServiceNotFoundException
     */
    @Override
    public boolean borrarServicio(Servicio servicio) throws IOException, ServiceNotFoundException{
        try {
            List <Servicio> listaServicios = leerListaServicios();

            //Elimino todos los servicios que coincidan en ese codigo, por si hay duplicados
            boolean eliminado = listaServicios.removeIf(s ->
                    s.getCodigo().equalsIgnoreCase(servicio.getCodigo()));

            if (eliminado){
                escribirServicio(listaServicios);
                return true;
            } else {
                throw new ServiceNotFoundException("ERROR AL BORRAR: No se ha encontrado ningun servicio con código" + servicio.getCodigo());
            }
        } catch (IOException e){
            throw new IOException("Error al borrar el servicio: ", e);
        }
    }

    /**
     * Borra un cliente del archivo cliente.json
     * @param cliente Cliente a borrar del archivo json
     * @return true si el cliente es borrado
     * @throws IOException
     * @throws ClientNotFoundException Lanza excepción si el cliente no se encuentra en el archivo
     */
    @Override
    public boolean borrarCliente(Cliente cliente) throws IOException, ClientNotFoundException{
        boolean borrado = false;
        ArrayList<Cliente> listaClientes = (ArrayList<Cliente>) this.leerListaClientes();
        for (Cliente clientes : listaClientes){
            if (clientes.equals(cliente)) {
                borrado = true;
            }
        }
        if (borrado) {
            listaClientes.remove(cliente);
            escribirCliente(listaClientes);
        } else {
            throw new ClientNotFoundException("Cliente no encontrado");
        }
        return borrado;
    }

    /**
     * Devuelve lo invertido por un cliente en un dia en concreto
     * @param dni del cliente del que se quiere consultar la información
     * @param fecha del dia que se quiera consultar la información
     * @return Total de lo invertido por el cliente restando lo ganado.
     * @throws IOException
     * @throws ClientNotFoundException
     */
    @Override
    public double dineroInvertidoClienteEnDia(String dni, LocalDate fecha) throws IOException, ClientNotFoundException{
        ArrayList<Log> listaLog = (ArrayList<Log>) this.leerListaLog();

        boolean clienteExiste = false;
        double totalInvertido = 0;
        for (Log log : listaLog){
            if (log.getCliente().getDni().equals(dni) ) {
                clienteExiste = true;
                if (log.getFecha().equals(fecha)) {
                    if (log.getConcepto().equals(TipoConcepto.COMPRABEBIDA) ||
                            (log.getConcepto().equals(TipoConcepto.COMPRACOMIDA)) ||
                            log.getConcepto().equals(TipoConcepto.APOSTAR)) {
                        totalInvertido += log.getCantidadConcepto();
                    } else if (log.getConcepto().equals(TipoConcepto.APUESTACLIENTEGANA) ||
                            log.getConcepto().equals(TipoConcepto.RETIRADA)) {
                        totalInvertido -= log.getCantidadConcepto();
                    }
                }
            }
        }

        if (!clienteExiste) {
            throw new  ClientNotFoundException("Cliente no encontrado");
        } else {
            return totalInvertido;
        }


    }

    /**
     * Devuelve lo invertido por un cliente en bebida y comida
     * @param dni Del cliente del que queremos saber la información
     * @return Lo invertido por un cliente en alimento
     * @throws IOException
     * @throws ClientNotFoundException Lanza excepción si no encuentra el cliente
     */
    public double gananciasAlimentos(String dni) throws IOException, ClientNotFoundException {
        ArrayList<Log> listaLog = (ArrayList<Log>) this.leerListaLog();
        boolean clienteExiste = false;
        double ganado = 0;
        for (Log log : listaLog) {
            if (log.getCliente().getDni().equals(dni)) {
                clienteExiste = true;
                if (log.getConcepto().equals(TipoConcepto.COMPRABEBIDA)
                    || log.getConcepto().equals(TipoConcepto.COMPRACOMIDA)) {
                    ganado += log.getCantidadConcepto();
                }
            }

        }
        if (!clienteExiste) {
            throw new ClientNotFoundException("Cliente no encontrado");
        }
        return ganado;
    }

    /**
     * Devuelve lo ganado por un cliente en un dia en concreto
     * @param dni Dni del cliente
     * @param fecha fecha del dia a buscar
     * @return Lo ganado apostado menos lo perdido apostando
     * @throws IOException
     * @throws ClientNotFoundException Lanza la excepción si no se encuentra el cliente
     */
    public double dineroGanadoClienteEnDia(String dni, LocalDate fecha) throws IOException, ClientNotFoundException {
        ArrayList<Log> listaLog = (ArrayList<Log>) this.leerListaLog();
        boolean  clienteExiste = false;
        double perdidoApuesta = 0;
        double ganadoApuesta = 0;
        String fechaStr = fecha.toString();

        for (Log log : listaLog) {
            if (log.getCliente().getDni().equals(dni)) {
                clienteExiste = true;
                if (log.getFechaStr().equals(fechaStr) && log.getConcepto().equals(TipoConcepto.APUESTACLIENTEGANA)) {
                    ganadoApuesta +=  log.getCantidadConcepto();
                } else if (log.getFechaStr().equals(fechaStr) && log.getConcepto().equals(TipoConcepto.APOSTAR)){
                    perdidoApuesta +=  log.getCantidadConcepto();
                }

            }
        }
        if (!clienteExiste) {
            throw new ClientNotFoundException("Cliente no encontrado");
        }
        return ganadoApuesta -  perdidoApuesta;
    }

    /**
     * Devuelve la cantidad de veces que un cliente ha jugado en una mesa
     * @param dni Del cliente del que se quiere consultar la información
     * @param codigo De la mesa en la que ha jugado el cliente
     * @return La cantidad de veces que un cliente ha apostado en una mesa
     * @throws IOException
     * @throws ClientNotFoundException Lanza la excepción si no se encuentra el cliente
     * @throws ServiceNotFoundException Lanza la excepción si no se encuentra el servicio
     */
    @Override
    public int vecesClienteJuegaMesa(String dni, String codigo) throws  IOException, ClientNotFoundException, ServiceNotFoundException {
        ArrayList<Log> listaLogs = (ArrayList<Log>) this.leerListaLog();
        boolean clienteExiste = false;
        boolean servicioExiste = false;
        int contador = 0;
        for (Log lo : listaLogs){
            if (lo.getCliente().getDni().equals(dni)){
                clienteExiste = true;
                if (lo.getServicio().getCodigo().equals(codigo)){
                    servicioExiste = true;
                    if (lo.getConcepto().equals(TipoConcepto.APOSTAR) || lo.getConcepto().equals(TipoConcepto.APUESTACLIENTEGANA)){
                        contador++;
                    }
                }
            }
        }
        if (!clienteExiste) {
            throw new ClientNotFoundException("Cliente no encontrado");
        } else if (!servicioExiste) {
            throw new ServiceNotFoundException("Servicio no encontrado");
        }
        return contador;
    }

    /**
     * Devuelve el total ganado por las mesas
     * @return Lo ganado por las apuesta menos lo que han ganado los clientes
     * @throws IOException
     */
    @Override
    public double ganadoMesas() throws IOException{
        ArrayList<Log> listaLogs = (ArrayList<Log>) this.leerListaLog();
        double totalApostado = 0;
        double totalPerdido = 0;
        for (Log lo : listaLogs) {
            if (lo.getServicio().getTipo().equals(TipoServicio.MESABLACKJACK) ||
            lo.getServicio().getTipo().equals(TipoServicio.MESAPOKER)) {
                if (lo.getConcepto().equals(TipoConcepto.APOSTAR)) {
                    totalApostado += lo.getCantidadConcepto();
                } else if (lo.getConcepto().equals(TipoConcepto.APUESTACLIENTEGANA)) {
                    totalPerdido += lo.getCantidadConcepto();
                }
            }
        }
        return totalApostado - totalPerdido;
    }

    /**
     * Devuelve lo ganado por los bares y restaurantes
     * @return La suma de lo ganado por las bebidas y las comidas
     * @throws IOException
     */
    @Override
    public double ganadoEstablecimientos() throws IOException{
        ArrayList<Log> listaLogs = (ArrayList<Log>) this.leerListaLog();
        double totalGanado = 0;
        for (Log lo : listaLogs) {
            if (lo.getConcepto().equals(TipoConcepto.COMPRACOMIDA) || lo.getConcepto().equals(TipoConcepto.COMPRABEBIDA)) {
                totalGanado += lo.getCantidadConcepto();
            }
        }
        return totalGanado;
    }

    /**
     * Devuelve una lista de servicios del tipo que se haya especificado
     * @param tipoServicio a buscar en el archivo
     * @return Lista de servicios del tipo especificado
     * @throws IOException
     */
    @Override
    public List<Servicio> devolverServiciosTipo(TipoServicio tipoServicio) throws IOException{
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
