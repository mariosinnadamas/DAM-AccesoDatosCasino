package casino;

import exceptions.ClientNotFoundException;
import jakarta.xml.bind.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CasinoDAOFileXML implements CasinoDAO {

    Path pathCliente = Path.of("src", "main", "java", "casino", "recursos", "xml", "cliente.xml");
    File fileCliente = new File(pathCliente.toString());

    Path pathLog = Path.of("src", "main", "java", "casino", "recursos", "xml", "log.xml");
    File fileLog = new File(pathLog.toString());

    Path pathServicio = Path.of("src", "main", "java", "casino", "recursos", "xml", "servicio.xml");
    File fileServicio = new File(pathServicio.toString());

    private List<Cliente> clientes = new ArrayList<>();
    private final List<Servicio> servicios = new ArrayList<>();
    private final List<Log> logs = new ArrayList<>();

    public CasinoDAOFileXML(){
        clientes = leerListaClientes();
    }
    @Override
    public void addCliente(Cliente cliente) {
        try {
            //Si existe el XML, lo cargo
            if (fileCliente.exists()) {
                JAXBContext context = JAXBContext.newInstance(ClienteListWrapper.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                ClienteListWrapper wrapper = (ClienteListWrapper) unmarshaller.unmarshal(fileCliente);

                clientes.clear();
                clientes.addAll(wrapper.getClientes());
            }

            //Añado el cliente a la lista creada
            clientes.add(cliente);

            //Guardo la lista completa en el XML
            guardarClientesEnXML();

            System.out.println("Cliente añadido correctamente");
        } catch (JAXBException e){
            e.printStackTrace();
        }
    }

    private void guardarClientesEnXML(){
        try {
            JAXBContext context = JAXBContext.newInstance(ClienteListWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);

            ClienteListWrapper wrapper = new ClienteListWrapper();
            wrapper.setClientes(clientes);

            marshaller.marshal(wrapper,fileCliente);
        } catch (JAXBException e){
            e.printStackTrace();
        }
    }

    @Override
    public void addServicio(Servicio servicio) {
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

    @Override
    public void addLog(Log log) {
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

    @Override
    public String consultaServicio(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return "Error: Código invalido";
        }

        for(Servicio s: servicios){
            if (s.getCodigo().equals(codigo)){
                return s.toString();

            }
        }

        //ToDo: throw ServiceNotFound
        return"";
    }

    @Override
    public List<Servicio> leerListaServicios() {
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
            return "Error: Nulo o vacío";
        }

        if (!Cliente.validarDni(dni)) {
            return "Error: DNI no válido";
        }

        for(Cliente c: clientes){
            if (c.getDni().equals(dni)){
               return c.toString();

            }
        }

        throw new ClientNotFoundException("Cliente no encotrado");
    }

    @Override
    public List<Cliente> leerListaClientes() {
        try{
            JAXBContext context = JAXBContext.newInstance(ClienteListWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ClienteListWrapper wrapper = (ClienteListWrapper) unmarshaller.unmarshal(fileCliente);

            for (Cliente c: wrapper.getClientes()){
                clientes.add(c);
            }

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    @Override
    public String consultaLog(String codigo, String dni, LocalDate fecha) {
        if (codigo == null || codigo.isBlank() || dni == null || dni.isBlank() || fecha == null) {
            return "Error: parámetros inválidos. Asegúrate de introducir un código, un DNI y una fecha válidos.";

        }

        for(Servicio s: servicios){
            if (s.getCodigo().equals(codigo)){
                return s.toString();

            }
        }

        //ToDo: throw ...
        return"";
    }

    @Override
    public List<Log> leerListaLog() {
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
        return false;
    }

    @Override
    public boolean actualizarCliente(String dni) {
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

    //Clases interna para JAXB
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
