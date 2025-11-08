package casino;

import exceptions.ClientNotFoundException;
import exceptions.ServiceNotFoundException;
import jakarta.xml.bind.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CasinoDAOFileXML implements CasinoDAO {

    Path pathCliente = Path.of("src", "main", "java", "casino", "recursos", "xml", "cliente.xml");
    File fileCliente = new File(pathCliente.toString());

    Path pathLog = Path.of("src", "main", "java", "casino", "recursos", "xml", "log.xml");
    File fileLog = new File(pathLog.toString());

    Path pathServicio = Path.of("src", "main", "java", "casino", "recursos", "xml", "servicio.xml");
    File fileServicio = new File(pathServicio.toString());

    private List<Cliente> clientes = new ArrayList<>();
    private List<Servicio> servicios = new ArrayList<>();
    private List<Log> logs = new ArrayList<>();

    @Override
    public void addCliente(Cliente cliente) {
        //Todo: Exceptions
        try {
            List<Cliente> listaClientes = leerClientesDelXML();

            boolean existe = listaClientes.stream()
                    .anyMatch(c -> c.getDni().equalsIgnoreCase(cliente.getDni()));

            if (!existe) {
                listaClientes.add(cliente);
                guardarClientesEnXML(listaClientes);
                this.clientes = listaClientes; // Actualizar estado interno si es necesario
                System.out.println("Cliente añadido correctamente");
            } else {
                System.out.println("Cliente con DNI " + cliente.getDni() + " ya existe");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void guardarClientesEnXML(List<Cliente> listaClientes){
        try {
            JAXBContext context = JAXBContext.newInstance(ClienteListWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            ClienteListWrapper wrapper = new ClienteListWrapper();
            wrapper.setClientes(listaClientes);

            marshaller.marshal(wrapper, fileCliente);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    //Metodo de lectura seguro (PROBANDO, MI IDEA ES HACERLO CON SERVICIOS Y LOG, Y ASI ELIMINAR LAS LISTAS DE ARRIBA Y ELIMINAR PROBLEMAS)
    private List<Cliente> leerClientesDelXML() {
        try {
            if (fileCliente.exists()) {
                JAXBContext context = JAXBContext.newInstance(ClienteListWrapper.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                ClienteListWrapper wrapper = (ClienteListWrapper) unmarshaller.unmarshal(fileCliente);
                return new ArrayList<>(wrapper.getClientes());
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void addServicio(Servicio servicio) {
        //Todo: Exceptions, y ver que pasa con el atributo de listaClientes de la clase Servicio, puesto que el XML no lo guarda porque esta vacío
        try {
            if (fileServicio.exists()){
                JAXBContext context = JAXBContext.newInstance(ServiciosListWrapper.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                ServiciosListWrapper wrapper = (ServiciosListWrapper) unmarshaller.unmarshal(fileServicio);

                servicios.clear();
                servicios.addAll(wrapper.getServicios());
            }

            //Añado el cliente a la lista creada
            servicios.add(servicio);

            //Guardo la lista completa en el XML
            guardarServiciosEnXML();
        } catch (JAXBException e){
            e.printStackTrace();
        }
    }

    private void guardarServiciosEnXML(){
        try {
            JAXBContext context = JAXBContext.newInstance(ServiciosListWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);

            ServiciosListWrapper wrapper = new ServiciosListWrapper();
            wrapper.setServicios(servicios);

            marshaller.marshal(wrapper,fileServicio);

        } catch (JAXBException e){
            e.printStackTrace();
        }
    }

    public void addListaServicios(List<Servicio> listaServicios){
        try {
            //Cargo los datos ya existentes en el fichero
            if (fileServicio.exists()){
                JAXBContext context = JAXBContext.newInstance(ServiciosListWrapper.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                ServiciosListWrapper wrapper = (ServiciosListWrapper) unmarshaller.unmarshal(fileServicio);

                servicios.clear();
                servicios.addAll(wrapper.getServicios());
            }

            //Añadir todos los clientes sin duplicar
            for (Servicio nuevo : listaServicios){
                //Busca dentro de clientes si existe un cliente con el mismo DNI
                //Stream es una secuencia de elementos que puedes recorrer sin necesidad de bucles
                boolean existe = servicios.stream().anyMatch(s -> s.getCodigo().equalsIgnoreCase(nuevo.getCodigo()));

                if (!existe) {
                    servicios.add(nuevo);
                } else {
                    System.out.println("Servicio repetido");
                }
            }

            //Guardo la lista en el XML
            guardarServiciosEnXML();

            System.out.println("Lista de servicios guardada con éxito");
        } catch (JAXBException e){
            e.printStackTrace();
        }
    }

    @Override
    public void addLog(Log log) {
        // Todo: Exceptions
        try {
            if (fileLog.exists()){
                JAXBContext context = JAXBContext.newInstance(LogsListWrapper.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                LogsListWrapper wrapper = (LogsListWrapper) unmarshaller.unmarshal(fileLog);

                logs.clear();
                logs.addAll(wrapper.getLogs());
            }

            //Añado el cliente a la lista creada
            logs.add(log);

            //Guardo la lista completa en el XML
            guardarLogsEnXML();
        } catch (JAXBException e){
            e.printStackTrace();
        }
    }

    private void guardarLogsEnXML(){
        try {
            JAXBContext context = JAXBContext.newInstance(LogsListWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);

            LogsListWrapper wrapper = new LogsListWrapper();
            wrapper.setLogs(logs);

            marshaller.marshal(wrapper,fileLog);

        } catch (JAXBException e){
            e.printStackTrace();
        }
    }

    public void addListaLogs(List<Log> listaLogs){
        //TODO: Test, exceptions, si salen duplicados hay que decir cuales ya existen, igual con Cliente y Servicio
        try {
            //Cargo los datos ya existentes en el fichero
            if (fileLog.exists()){
                JAXBContext context = JAXBContext.newInstance(LogsListWrapper.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                LogsListWrapper wrapper = (LogsListWrapper) unmarshaller.unmarshal(fileLog);

                logs.clear();
                logs.addAll(wrapper.getLogs());
            }

            //Añadir todos los clientes sin duplicar
            for (Log nuevo : listaLogs){
                //Busca dentro de clientes si existe un cliente con el mismo DNI
                //Stream es una secuencia de elementos que puedes recorrer sin necesidad de bucles
                boolean existe = logs.stream().anyMatch(l ->
                        l.getCliente().getDni().equalsIgnoreCase(nuevo.getCliente().getDni()) &&
                        l.getServicio().getCodigo().equalsIgnoreCase(nuevo.getServicio().getCodigo()) &&
                        l.getFecha().equals(nuevo.getFecha()));

                if (!existe) {
                    logs.add(nuevo);
                } else {
                    System.out.println("Log repetido");
                }
            }

            //Guardo la lista en el XML
            guardarLogsEnXML();

            System.out.println("Lista de logs guardada con éxito");
        } catch (JAXBException e){
            e.printStackTrace();
        }
    }

    @Override
    public String consultaServicio(String codigo) throws ServiceNotFoundException {
        if (codigo == null || codigo.isBlank()) {
            return "Error: Código invalido"; //TODO: Cambiar este mensaje por una excepción? IllegalArgumentException por ejemplo
        }

        for(Servicio s: servicios){
            if (s.getCodigo().equals(codigo)){
                return s.toString();
            } else {
                throw new ServiceNotFoundException("Servicio no encontrado, no coincide ningún código");
            }
        }
        return"";
    }

    @Override
    public List<Servicio> leerListaServicios() {
        //TODO: Exceptions
        try{
            JAXBContext context = JAXBContext.newInstance(ServiciosListWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ServiciosListWrapper wrapper = (ServiciosListWrapper) unmarshaller.unmarshal(fileServicio);

            for (Servicio s: wrapper.getServicios()){
                servicios.add(s);
            }

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return servicios;
    }

    @Override
    public String consultaCliente(String dni) throws ClientNotFoundException {
        if (dni == null || dni.isBlank()) {
            return "Error: Nulo o vacío"; //TODO: Cambiar este mensaje por una excepción? IllegalArgumentException por ejemplo
        }

        if (!Cliente.validarDni(dni)) {
            return "Error: DNI no válido"; //TODO: Cambiar este mensaje por una excepción? IllegalArgumentException por ejemplo
        }

        for(Cliente c: clientes){
            if (c.getDni().equals(dni)){
               return c.toString();
            }
        }
        throw new ClientNotFoundException("Cliente no encontrado");
    }

    @Override
    public List<Cliente> leerListaClientes() {
        //TODO: Exceptions
        //Usamos una nueva lista para no modificar la principal
        List<Cliente> clientesTemporal = new ArrayList<>();
        try{
            if (fileCliente.exists()) {
                JAXBContext context = JAXBContext.newInstance(ClienteListWrapper.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                ClienteListWrapper wrapper = (ClienteListWrapper) unmarshaller.unmarshal(fileCliente);
                clientesTemporal.addAll(wrapper.getClientes());
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return clientesTemporal;
    }

    @Override
    public String consultaLog(String codigoServicio, String dni, LocalDate fecha) {
        //TODO: Test
        if (codigoServicio == null || codigoServicio.isBlank() || dni == null || dni.isBlank() || fecha == null) {
            return "Error: parámetros inválidos. Asegúrate de introducir un código, un DNI y una fecha válidos."; //TODO: Cambiar mensaje por excepción
        }

        for(Log l: logs){
            if (l.getServicio().getCodigo().equals(codigoServicio)){
                return l.toString();
            }
        }

        //ToDo: throw ...
        return"";
    }

    @Override
    public List<Log> leerListaLog() {
        //TODO: Test, Exceptions
        try{
            JAXBContext context = JAXBContext.newInstance(LogsListWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            LogsListWrapper wrapper = (LogsListWrapper) unmarshaller.unmarshal(fileCliente);

            for (Log l: wrapper.getLogs()){
                logs.add(l);
            }

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return logs;
    }

    @Override
    public boolean actualizarServicio(String codigo) {
        List <Servicio> listaServiciosParaBuscar = leerListaServicios();

        return false;
    }

    @Override
    public boolean actualizarCliente(String dni, Cliente clienteActualizado) {
        //TODO: Da sale muchas veces DNI duplicado, estoy arreglandolo
        List<Cliente> listaClientes = leerListaClientes();
        boolean encontrado = false;

        for (int i = 0; i < listaClientes.size(); i++) {
            Cliente temp = listaClientes.get(i);
            if (temp.getDni().equalsIgnoreCase(dni)) {
                // Reemplazar el cliente completo
                listaClientes.set(i, clienteActualizado);
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            guardarClientesEnXML(listaClientes);
            System.out.println("Cliente actualizado correctamente");
        } else {
            System.out.println("Cliente no encontrado para actualizar");
        }

        return encontrado;
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

    //Clases interna para JAXB, contenedora del XML, necesaria porque no se puede agregar un elemento al final del xml
    @XmlRootElement(name = "clientes")
    private static class ClienteListWrapper {
        private List<Cliente> clientes = new ArrayList<>();

        @XmlElement(name = "cliente")
        public List<Cliente> getClientes() { return clientes; }

        public void setClientes(List<Cliente> clientes) { this.clientes = clientes; }
    }

    @XmlRootElement(name = "servicios")
    private static class ServiciosListWrapper {
        private List<Servicio> servicios = new ArrayList<>();

        @XmlElement(name = "servicio")
        public List<Servicio> getServicios() { return servicios; }

        public void setServicios(List<Servicio> servicios) { this.servicios = servicios; }
    }

    @XmlRootElement(name = "logs")
    private static class LogsListWrapper {
        private List<Log> logs = new ArrayList<>();

        @XmlElement(name = "log")
        public List<Log> getLogs() { return logs; }

        public void setLogs(List<Log> logs) { this.logs = logs; }
    }
}
