package casino;

import jakarta.json.*;
import jakarta.json.stream.JsonGenerator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    /**
     * Agrega un objeto cliente al archivo cliente.json
     *
     * @param cliente objeto que recibe como parámetro para agregar al archivo JSON
     */
    @Override
    public void addCliente(Cliente cliente) {
        //Obtener ArrayList<Cliente> del archivo
        ArrayList<Cliente> listaClientes = (ArrayList<Cliente>) this.leerListaClientes();

        //Agregar el objeto cliente
        listaClientes.add(cliente);

        //Crear el Objeto JsonArrayBuilder
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        //Recorrer la lista de clientes, transformar cada elemento en un JsonObject y agregarlo al JsonArrayBuilder
        for (Cliente cli : listaClientes) {
            JsonObject jobj = Json.createObjectBuilder()
                    .add("dni", cli.getDni())
                    .add("nombre", cli.getNombre())
                    .add("apellido", cli.getApellidos())
                    .build();
            jsonArrayBuilder.add(jobj);
        }

        // 3 líneas de configuración para el pretty printing - Copiado de los apuntes
        Map<String, Object> config = new HashMap <>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(config);

        //Abrir el archivo Cliente.json y usar un JsonWriter para escribir el JsonArray
        try (FileWriter fileWriter = new FileWriter(fileCliente);
             JsonWriter jsonWriter = writerFactory.createWriter(fileWriter)){

            jsonWriter.writeArray(jsonArrayBuilder.build());

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    //Ejemplo de metodo que no está en la interfaz
    public void addCliente(List<Cliente> clientes) {
        ArrayList<Cliente> listaClient = (ArrayList<Cliente>) this.leerListaClientes();

        for (Cliente cliente : clientes) {
            listaClient.add(cliente);
        }
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Cliente cliente : listaClient) {
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
    public void addServicio(Servicio servicio) {

    }

    @Override
    public void addLog(Log log) {

    }

    @Override
    public String consultaServicio(String codigo) {
        return "";
    }

    @Override
    public List<Servicio> leerListaServicios() {
        return List.of();
    }

    @Override
    public String consultaCliente(String dni) {
        ArrayList<Cliente> listaClientes =  (ArrayList<Cliente>) this.leerListaClientes();

        for (Cliente cliente : listaClientes) {
            if (cliente.getDni().equals(dni)) {
                return cliente.toString();
            }
        }
        return "No se ha encontrado el cliente";
    }


    /**
     * Lee el archivo cliente.json y devuelve una Lista con los clientes del archivo
     *
     * @return List<Cliente> del archivo cliente.json
     */
    @Override
    public List<Cliente> leerListaClientes() {
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
        return "";
    }

    @Override
    public List<Log> leerListaLog() {
        return List.of();
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
        return false;
    }

    @Override
    public boolean borrarCliente(Cliente cliente) {
        return false;
    }

    @Override
    public double GanaciasAlimentos(String dni, String concepto) {
        return 0;
    }

    @Override
    public double dineroGanadoClienteEnDia(String dni, LocalDate fecha) {
        return 0;
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
        return List.of();
    }
}
