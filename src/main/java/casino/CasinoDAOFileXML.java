package casino;

import exceptions.*;
import jakarta.xml.bind.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.File;
import java.io.IOException;
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

    @Override
    public void addCliente(Cliente cliente) throws IllegalArgumentException, ClientAlreadyExistsException, IOException {
        //Validación de parámetros
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }

        try {
            //Leo del XML para tener datos actualizados
            List<Cliente> listaClientes = leerListaClientes();

            boolean existe = listaClientes.stream()
                    .anyMatch(c -> c.getDni().equalsIgnoreCase(cliente.getDni()));

            if (!existe) {
                listaClientes.add(cliente);
                guardarClientesEnXML(listaClientes);
                System.out.println("Cliente añadido correctamente");
            } else {
                throw new ClientAlreadyExistsException("Cliente con DNI " + cliente.getDni() + " ya existe");
            }
        } catch (IOException e) {
            throw new IOException("Error de E/S al acceder al archivo de clientes " + e.getMessage());
        }
    }

    //Metodo para añadir una lista de clientes (para pruebas), no estaba en la DAO
    public void addListaClientes(List<Cliente> nuevosClientes) throws IllegalArgumentException,IOException{
        //Validación de la lista
        if (nuevosClientes == null){
            throw new IllegalArgumentException("La lista no puede ser nulo o estar vacía");
        }

        try {
            List<Cliente> listaClientes = leerListaClientes();
            int clientesAniadidos = 0;

            //Recorro la lista nueva
            for (Cliente nuevo : nuevosClientes) {
                //Por cada cliente de nuevoCliente recorre listaClientes, si coinciden en DNI devuelve true
                boolean existe = listaClientes.stream()
                        .anyMatch(c -> c.getDni().equalsIgnoreCase(nuevo.getDni()));

                //Si no existe lo añado, de lo contrario lo salta
                if (!existe) {
                    listaClientes.add(nuevo);
                    clientesAniadidos++;
                    System.out.println("Cliente con dni: " + nuevo.getDni() + " añadido");
                }
            }

            //Si se ha añadido 1 cliente o más escribe en el fichero y lo actualiza
            if (clientesAniadidos > 0) {
                guardarClientesEnXML(listaClientes);
                System.out.println(clientesAniadidos + " clientes añadidos correctamente"); //Mensaje para pruebas
            } else {
                System.out.println("No se añadió ningún cliente nuevo");
            }

        } catch (IOException e) {
            throw new IOException("Error de E/S al añadir lista en el fichero de clientes", e);
        }
    }

    //Metodo auxiliar para guardar los clientes de una lista al archivo XML
    private void guardarClientesEnXML(List<Cliente> listaClientes) throws IOException{
        try {
            JAXBContext context = JAXBContext.newInstance(ClienteListWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            ClienteListWrapper wrapper = new ClienteListWrapper();
            wrapper.setClientes(listaClientes);

            marshaller.marshal(wrapper, fileCliente);
        } catch (JAXBException e) {
            throw new IOException("Error al guardar en el fichero XML de clientes", e);
        }
    }

    @Override
    public void addServicio(Servicio servicio) throws IllegalArgumentException, IOException{
        //Todo: Ver que pasa con el atributo de listaClientes de la clase Servicio, puesto que el XML no lo guarda porque esta vacío
        if (servicio == null){
            throw new IllegalArgumentException("El servicio no puede ser nulo");
        }

        try {
            //Leo del XML para tener datos actualizados
            List<Servicio> listaServicios = leerListaServicios();

            boolean existe = listaServicios.stream()
                    .anyMatch(c -> c.getCodigo().equalsIgnoreCase(servicio.getCodigo()) ||
                            c.getNombreServicio().equalsIgnoreCase(servicio.getNombreServicio()));

            if (!existe) {
                listaServicios.add(servicio);
                guardarServiciosEnXML(listaServicios);
                System.out.println("Servicio añadido correctamente");
            } else {
                throw new ServiceAlreadyExistsException("Servicio con código " + servicio.getCodigo() +" y nombre "+ servicio.getNombreServicio() + " ya existe");
            }
        } catch (IOException e) {
            throw new IOException("Error de E/S al leer el fichero XML de servicio" + e);
        }
    }

    public void addListaServicios(List<Servicio> nuevosServicios) throws IllegalArgumentException, IOException{

        if (nuevosServicios == null){
            throw new IllegalArgumentException("La lista de los nuevos servicios no puede ser nula");
        }

        try {
            List<Servicio> listaServicios = leerListaServicios();
            int serviciosAniadidos = 0;

            for (Servicio nuevo : nuevosServicios) {
                boolean existe = listaServicios.stream()
                        .anyMatch(s -> s.getCodigo().equalsIgnoreCase(nuevo.getCodigo()));

                if (!existe) {
                    listaServicios.add(nuevo);
                    serviciosAniadidos++;
                    System.out.println("Servicio " + nuevo.getCodigo() + " añadido");
                }
            }

            if (serviciosAniadidos > 0) {
                guardarServiciosEnXML(listaServicios);
                System.out.println(serviciosAniadidos + " servicios añadidos correctamente");
            }
        } catch (IOException e){
            throw new IOException("Error al añadir la lista de servicios en el archivo XML: ", e);
        }
    }

    private void guardarServiciosEnXML(List <Servicio> listaServicios) throws IOException{

        if (listaServicios == null){
            throw new IllegalArgumentException("La lista de los servicios no puede ser nula");
        }

        try {
            JAXBContext context = JAXBContext.newInstance(ServiciosListWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);

            ServiciosListWrapper wrapper = new ServiciosListWrapper();
            wrapper.setServicios(listaServicios);

            marshaller.marshal(wrapper,fileServicio);

        } catch (JAXBException e){
            throw new IOException("Error al escribir en el fichero XML de servicios");
        }
    }

    @Override
    public void addLog(Log log) throws IllegalArgumentException, IOException{
        //Todo: Ver que pasa con el atributo de listaClientes de la clase Servicio, puesto que el XML no lo guarda porque esta vacío
        if (log == null){
            throw new IllegalArgumentException("El log no puede ser nulo");
        }

        try {
            //Leo del XML para tener datos actualizados
            List<Log> listaLogs = leerListaLog();
            listaLogs.add(log);
            guardarLogsEnXML(listaLogs);
            System.out.println("Log añadido correctamente");

        } catch (IOException e) {
            throw new IOException("Error de E/S al acceder al archivo de log", e);
        }
    }

    private void guardarLogsEnXML(List<Log> listaLogs) throws IllegalArgumentException,IOException{
        if (listaLogs == null){
            throw new IllegalArgumentException("Lista logs no puede ser nula");
        }

        try {
            JAXBContext context = JAXBContext.newInstance(LogsListWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);

            LogsListWrapper wrapper = new LogsListWrapper();
            wrapper.setLogs(listaLogs);

            marshaller.marshal(wrapper,fileLog);

        } catch (JAXBException e){
            throw new IOException("Error al guardar en el fichero XML de log", e);
        }
    }

    public void addListaLogs(List<Log> nuevosLogs) throws IllegalArgumentException, IOException{
        //TODO: Test, exceptions, si salen duplicados hay que decir cuales ya existen, igual con Cliente y Servicio

        if (nuevosLogs == null){
            throw new IllegalArgumentException("La lista de nuevos logs no puede ser nulo");
        }

        try {
            List<Log> listaLogs = leerListaLog();
            listaLogs.addAll(nuevosLogs);
            guardarLogsEnXML(listaLogs);
            System.out.println("Lista de logs guardada con éxito");
        } catch (IOException e){
            throw new IOException("Error al añadir lista de logs al XML", e);
        }
    }

    @Override
    public String consultaServicio(String codigo) throws IllegalArgumentException, ServiceNotFoundException, IOException {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException ("El codigo no puede ser nulo o estar vacío");
        }
        String codigoLimpio = codigo.trim();

        try {
            List <Servicio> listaServicios = leerListaServicios();

            for (Servicio temp: listaServicios){
                if (temp.getCodigo().equalsIgnoreCase(codigoLimpio)){
                    return temp.toString();
                }
            }

            throw new ServiceNotFoundException("ERROR AL CONSULTAR: No se ha encontrado ningún servicio con el código " + codigo);

        } catch (IOException e) {
            throw new IOException("Error al leer el fichero XML de servicios", e);
        }
    }

    @Override
    public List<Servicio> leerListaServicios() throws IOException{
        //TODO: Exceptions
        List<Servicio> serviciosTemporal = new ArrayList<>();
        try {
            if (fileServicio.exists()) {
                JAXBContext context = JAXBContext.newInstance(ServiciosListWrapper.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                ServiciosListWrapper wrapper = (ServiciosListWrapper) unmarshaller.unmarshal(fileServicio);
                serviciosTemporal.addAll(wrapper.getServicios());
            }
        } catch (JAXBException e) {
            throw new IOException("Error al leer el fichero XML de servicios", e);
        }
        return serviciosTemporal;
    }

    @Override
    public String consultaCliente(String dni) throws IllegalArgumentException,ClientNotFoundException, IOException {
        //TODO: Test
        if (dni == null || dni.isBlank()) {
            throw new IllegalArgumentException("DNI no puede ser nulo o vacío");
        }

        if (!Cliente.validarDni(dni)) {
            throw new IllegalArgumentException("DNI no válido");
        }

        //Siempre consulto datos del XML para tener los más actualizados
        try {
            List <Cliente> clientesActuales = leerListaClientes();

            for(Cliente c: clientesActuales){
                if (c.getDni().equals(dni)){
                   return c.toString();
                }
            }
        } catch (IOException e) {
            throw new IOException("Error al leer el fichero de clientes");
        }
        throw new ClientNotFoundException("ERROR AL CONSULTAR: Cliente no encontrado");
    }

    @Override
    public List<Cliente> leerListaClientes() throws IOException {
        List<Cliente> clientesTemporal = new ArrayList<>();
        try{
            if (fileCliente.exists()) {
                JAXBContext context = JAXBContext.newInstance(ClienteListWrapper.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                ClienteListWrapper wrapper = (ClienteListWrapper) unmarshaller.unmarshal(fileCliente);
                clientesTemporal.addAll(wrapper.getClientes());
            }
        } catch (JAXBException e) {
            throw new IOException("Error al leer el archivo XML de cliente: " + e);
        }
        return clientesTemporal;
    }

    @Override
    public String consultaLog(String codigoServicio, String dni, LocalDate fecha) throws IllegalArgumentException, LogNotFoundException, IOException {
        //TODO: Test

        if (codigoServicio == null || codigoServicio.isBlank() || dni == null || dni.isBlank() || fecha == null) {
            throw new IllegalArgumentException("Error: parámetros inválidos. Asegúrate de introducir un código, un DNI y una fecha válidos.");
        }

        try {
            List<Log> listaLogs = leerListaLog();
            for(Log l: listaLogs){
                // Validar que ningún objeto sea null antes de acceder a sus métodos
                if (l != null &&
                        l.getServicio() != null && l.getServicio().getCodigo() != null &&
                        l.getCliente() != null && l.getCliente().getDni() != null &&
                        l.getFecha() != null) {

                    if (l.getServicio().getCodigo().equals(codigoServicio) &&
                            l.getCliente().getDni().equals(dni) &&
                            l.getFecha().equals(fecha)) {
                        return l.toString();
                    }
                }
            }
            throw new LogNotFoundException("ERROR EN CONSULTA: Log no encontrado");
        } catch (IOException e) {
            throw new IOException("Error al consultar el Log: ", e);
        }
    }

    @Override
    public List<Log> leerListaLog() throws IOException{
        List<Log> logsTemporal = new ArrayList<>();
        //TODO: Test, Exceptions
        try{
            if (fileLog.exists()) {
                JAXBContext context = JAXBContext.newInstance(LogsListWrapper.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                LogsListWrapper wrapper = (LogsListWrapper) unmarshaller.unmarshal(fileLog);

                logsTemporal.addAll(wrapper.getLogs());
            }
        } catch (JAXBException e) {
            throw new IOException("Error al leer el archivo XML de Log", e);
        }
        return logsTemporal;
    }

    @Override
    public boolean actualizarServicio(String codigo, Servicio servicioActualizado) throws IllegalArgumentException, ServiceNotFoundException, IOException {

        if (codigo == null || codigo.isBlank()){
            throw new IllegalArgumentException("El codigo no puede ser nulo");
        }

        if (servicioActualizado == null){
            throw new IllegalArgumentException("El servicio no puede ser nulo");
        }

        String codigoLimpio = codigo.trim();

        try {
            List<Servicio> listaServicios = leerListaServicios();
            boolean encontrado = false;

            for (int i = 0; i < listaServicios.size(); i++) {
                Servicio temp = listaServicios.get(i);
                if (temp.getCodigo().equalsIgnoreCase(codigoLimpio)) {
                    // Reemplazar el cliente completo
                    listaServicios.set(i, servicioActualizado);
                    encontrado = true;
                    break;
                }
            }

            if (encontrado) {
                guardarServiciosEnXML(listaServicios);
                System.out.println("Servicio actualizado correctamente");
            } else {
                throw new ServiceNotFoundException("No se ha encontrado el servicio deseado");
            }
            return encontrado;
        } catch (IOException e) {
            throw new IOException("Error al actualizar el servicio: ", e);
        }
    }

    @Override
    public boolean actualizarCliente(String dni, Cliente clienteActualizado) throws IllegalArgumentException, ClientNotFoundException, IOException {
        //TODO: Test

        if (dni == null || dni.isBlank() || clienteActualizado == null){
            throw new IllegalArgumentException("El dni/cliente no puede ser nulo");
        }

        String dniLimpio = dni.trim();

        //En caso de que nos pasen un DNI que no coincida con el dni del cliente pasado
        if (!dniLimpio.equalsIgnoreCase(clienteActualizado.getDni().trim())) {
            throw new IllegalArgumentException("El DNI del cliente actualizado no coincide con el DNI buscado");
        }

        try {
            List<Cliente> listaClientes = leerListaClientes();
            boolean encontrado = false;

            for (int i = 0; i < listaClientes.size(); i++) {
                Cliente temp = listaClientes.get(i);
                if (temp.getDni().equalsIgnoreCase(dniLimpio)) {
                    // Reemplazar el cliente completo
                    listaClientes.set(i, clienteActualizado);
                    encontrado = true;
                    break;
                }
            }

            if (encontrado) {
                guardarClientesEnXML(listaClientes);
            } else {
                throw new ClientNotFoundException("No se ha encontrado el cliente deseado");
            }
            return encontrado;
        } catch (IOException e) {
            throw new IOException ("Error al actualizar el cliente: ", e);
        }
    }

    @Override
    public boolean borrarServicio(Servicio servicio) throws IllegalArgumentException,ServiceNotFoundException,IOException{
        if (servicio == null){
            throw new IllegalArgumentException("El servicio no puede ser nulo");
        }

        try {
            List <Servicio> listaServicios = leerListaServicios();
            int tamanoInicial = listaServicios.size();

            //Elimino todos los servicios que coincidan en ese codigo, por si hay duplicados
            boolean eliminado = listaServicios.removeIf(s ->
                    s.getCodigo().equalsIgnoreCase(servicio.getCodigo()));

            if (eliminado){
                guardarServiciosEnXML(listaServicios);
                int serviciosEliminados = tamanoInicial - listaServicios.size();
                System.out.println(serviciosEliminados + " servicios con codigo " + servicio.getCodigo() +" eliminados");
                return true;
            } else {
                throw new ServiceNotFoundException("ERRROR AL BORRAR: No se ha encontrado ningun servicio con código" + servicio.getCodigo());
            }
        } catch (IOException e){
            throw new IOException("Error al borrar el servicio: ", e);
        }
    }

    @Override
    public boolean borrarCliente(Cliente cliente) throws IllegalArgumentException, ClientNotFoundException, IOException {

        if (cliente == null){
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }

        try {
            List <Cliente> listaClientes = leerListaClientes();
            int tamanoInicial = listaClientes.size();

            //Elimino todos los clientes que coincidan en ese DNI, por si hay duplicados
            boolean eliminado = listaClientes.removeIf(c ->
                    c.getDni().equalsIgnoreCase(cliente.getDni()));

            if (eliminado){
                guardarClientesEnXML(listaClientes);
                int clientesEliminados = tamanoInicial - listaClientes.size();
                System.out.println(clientesEliminados + " clientes con DNI " + cliente.getDni() +" eliminados");
                return true;
            } else {
                throw new ClientNotFoundException("No se ha encontrado ningun cliente con DNI" + cliente.getDni());
            }
        } catch (IOException e){
            throw new IOException("Error al borrar un cliente: ", e);
        }
    }

    @Override
    public double gananciasAlimentos(String dni) throws IllegalArgumentException, IOException {
        Boolean encontrado = false;

        // TODO: Test, test dni = ""
        if (dni == null || dni.isBlank()) {
            throw new IllegalArgumentException("DNI no puede ser nulo o vacío");
        }

        if (!Cliente.validarDni(dni)) {
            throw new IllegalArgumentException("DNI no válido");
        }

        try {
            List<Log> listaLog = leerListaLog();
            double totalGanancias = 0;

            for (Log l : listaLog){
                if (l.getCliente() != null && l.getCliente().getDni().equals(dni)){
                    encontrado = true;
                    if (l.getConcepto() == TipoConcepto.COMPRABEBIDA || l.getConcepto() == TipoConcepto.COMPRACOMIDA) {
                        totalGanancias += l.getCantidadConcepto();
                    }
                }
            }

            if(!encontrado){
                throw new IllegalArgumentException("DNI no encontrado");
            }

            return totalGanancias;
        } catch (IOException e) {
            throw new IOException("Error al calcular las ganancias de alimentos: ", e);
        }
    }

    @Override
    public double dineroInvertidoClienteEnDia(String dni, LocalDate fecha)throws IllegalArgumentException, LogNotFoundException, IOException {
        //Todo: Exceptions, Test
        if (dni == null || dni.isBlank() || fecha == null){
            throw new IllegalArgumentException("DNI/Fecha no pueden ser nulos");
        }

        try {
            List<Log> listaLog = leerListaLog();
            double totalGanado = 0;

            for (Log l : listaLog){
                //Verifico que el log tenga todos los datos necesarios
                if (l != null && l.getCliente() != null && l.getCliente().getDni() != null &&
                l.getFecha() != null && l.getConcepto() != null){
                    //Verificar si el cliente coincide
                    if (l.getCliente().getDni().equalsIgnoreCase(dni) &&
                    l.getFecha().equals(fecha)){
                        //Sumo segun el tipo de concepto
                        switch (l.getConcepto()){
                            case APUESTACLIENTEGANA,RETIRADA -> totalGanado -= l.getCantidadConcepto();
                            case COMPRACOMIDA, COMPRABEBIDA, APOSTAR -> totalGanado += l.getCantidadConcepto();
                        }
                    } else {
                        throw new LogNotFoundException("No se ha encontrado un log con DNI: " + l.getCliente().getDni() + " y Fecha: " + l.getFecha());
                    }
                }
            }
            return totalGanado;
        } catch (IOException e) {
            throw new IOException("Error al calcular el dinero invertido de un cliente en un dia: ", e);
        }
    }

    @Override
    public int vecesClienteJuegaMesa(String dni, String codigo) throws IllegalArgumentException, IOException {
        //TODO: Excepciones
        if (dni == null || dni.isBlank() || codigo == null || codigo.isBlank()){
            throw new IllegalArgumentException("El DNI y el Código no pueden ser nulos");
        }

        try {
            List<Log> listaLog = leerListaLog();
            int cantidadJugado = 0;
            String dniLimpio = dni.trim();
            String codigoLimpio = codigo.trim();

            for (Log l : listaLog){
                //Verificaciones de que el XML esté bien
                if (l != null && l.getCliente() != null && l.getCliente().getDni() != null &&
                        l.getServicio() != null && l.getServicio().getCodigo() != null && l.getConcepto() != null){

                    String logDni = l.getCliente().getDni().trim();
                    String logCodigo = l.getServicio().getCodigo().trim();

                    //Comprobar que coincida, si no es así no se cuenta
                    if (logDni.equalsIgnoreCase(dniLimpio) && logCodigo.equalsIgnoreCase(codigoLimpio)){
                        if (l.getConcepto() == TipoConcepto.APOSTAR ||
                                l.getConcepto() == TipoConcepto.APUESTACLIENTEGANA){
                            cantidadJugado++;
                        }
                    }
                }
            }
            return cantidadJugado;
        } catch (IOException e) {
            throw new IOException("Error al contar cuantas veces ha jugado un cliente en una mesa", e);
        }
    }

    @Override
    public double ganadoMesas() throws IOException{
        //TODO: Test
        try {
            List<Log> listaLog = leerListaLog();
            double totalGanado = 0;

            for (Log l : listaLog){
                if (l != null && l.getServicio() != null && l.getServicio().getTipo() != null &&
                        l.getConcepto() != null) {

                    // Verificar que es un servicio de tipo MESA
                    TipoServicio tipo = l.getServicio().getTipo();
                    boolean esMesa = tipo == TipoServicio.MESABLACKJACK || tipo == TipoServicio.MESAPOKER;

                    if (esMesa){
                        switch (l.getConcepto()){
                            case APUESTACLIENTEGANA,RETIRADA -> totalGanado -= l.getCantidadConcepto();
                            case APOSTAR -> totalGanado += l.getCantidadConcepto();
                        }
                    }
                }
            }
            return totalGanado;
        } catch (IOException e) {
            throw new IOException("Error al calcular las ganancias de las mesas: ", e);
        }
    }

    @Override
    public double ganadoEstablecimientos() throws IOException{
        //TODO: Exceptions, Test
        try {
            List <Log> listaLog = leerListaLog();
            double totalGanado = 0.0;

            for (Log l : listaLog){
                if (l != null && l.getServicio() != null && l.getServicio().getTipo() != null && l.getConcepto() != null) {
                    TipoServicio tipo = l.getServicio().getTipo();

                    boolean esEstablecimiento = tipo == TipoServicio.BAR || tipo == TipoServicio.RESTAURANTE;

                    if (esEstablecimiento){
                        //Sumo según el concepto, no tengo en cuenta las apuestas
                        if (l.getConcepto() == TipoConcepto.COMPRABEBIDA ||l.getConcepto()==TipoConcepto.COMPRACOMIDA){
                            totalGanado +=l.getCantidadConcepto();
                        }
                    }
                }
            }
            return totalGanado;
        } catch (IOException e) {
            throw new IOException("Error al calcular las ganancias de los establecimiento", e);
        }
    }

    @Override
    public List<Servicio> devolverServiciosTipo(TipoServicio tipoServicio) throws IllegalArgumentException, IOException{
        if (tipoServicio == null) {
            throw new IllegalArgumentException("El tipo de servicio no puede ser nulo");
        }

        try {
            List<Servicio> listaServicios = leerListaServicios();
            List<Servicio> listaDeUnTipoDeServicio = new ArrayList<>();

            for (Servicio temp : listaServicios){
                if (temp.getTipo().equals(tipoServicio)){
                    listaDeUnTipoDeServicio.add(temp);
                }
            }

            return listaDeUnTipoDeServicio;

        } catch (IOException e) {
            throw new IOException("Error al obtener servicios del tipo " + tipoServicio + ": ", e);
        }
    }

    //Clases interna para JAXB, contenedora del XML, define la cómo es la LISTA COMPLETA, NO EL OBJETO
    @XmlRootElement(name = "clientes")
    private static class ClienteListWrapper {
        //Lista que almacenará los objeto Cliente
        private List<Cliente> clientes = new ArrayList<>();

        //Define como se serializa cada elemento de la lista
        @XmlElement(name = "cliente")
        public List<Cliente> getClientes() { return clientes; }

        //Esto lo utiliza para poder leer de XML a Java
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
