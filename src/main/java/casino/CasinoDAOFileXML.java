package casino;

import exceptions.ClientNotFoundException;
import exceptions.LogNotFoundException;
import exceptions.ServiceNotFoundException;
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

    /*He borrado las listas que hay aqui (O al menos eso estaba haciendo) porque he tomado la decisión de trabajar leyendo el XML continuamente,
    quizás no sea lo más eficiente pero no ahorra muchos problemas, entre ellos no estar pendientes de que la lista que tenemos aqui esté sincronizada
    con los clientes del xml
    */

    private List<Log> logs = new ArrayList<>();

    @Override
    public void addCliente(Cliente cliente) {
        //Todo: Exceptions
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
                System.out.println("Cliente con DNI " + cliente.getDni() + " ya existe");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Metodo para añadir una lista de clientes, no estaba en la DAO
    public void addListaClientes(List<Cliente> nuevosClientes) {
        try {
            List<Cliente> listaClientes = leerListaClientes();
            int clientesAniadidos = 0;

            for (Cliente nuevo : nuevosClientes) {
                boolean existe = listaClientes.stream()
                        .anyMatch(c -> c.getDni().equalsIgnoreCase(nuevo.getDni()));

                if (!existe) {
                    listaClientes.add(nuevo);
                    clientesAniadidos++;
                    System.out.println("Cliente " + nuevo.getDni() + " añadido");
                } else {
                    System.out.println("Cliente repetido: " + nuevo.getDni());
                }
            }

            if (clientesAniadidos > 0) {
                guardarClientesEnXML(listaClientes);
                System.out.println(clientesAniadidos + " clientes añadidos correctamente");
            } else {
                System.out.println("No se añadió ningún cliente nuevo");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Metodo auxiliar para guardar los clientes de una lista al archivo XML
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

    @Override
    public void addServicio(Servicio servicio) {
        //Todo: Exceptions, y ver que pasa con el atributo de listaClientes de la clase Servicio, puesto que el XML no lo guarda porque esta vacío
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
                System.out.println("Servicio con código " + servicio.getCodigo() +" y nombre "+ servicio.getNombreServicio() + " ya existe");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addListaServicios(List<Servicio> nuevosServicios){
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
                } else {
                    System.out.println("Servicio repetido: " + nuevo.getCodigo());
                }
            }

            if (serviciosAniadidos > 0) {
                guardarServiciosEnXML(listaServicios);
                System.out.println(serviciosAniadidos + " servicios añadidos correctamente");
            } else {
                System.out.println("No se añadió ningún servicio nuevo");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void guardarServiciosEnXML(List <Servicio> listaServicios){
        try {
            JAXBContext context = JAXBContext.newInstance(ServiciosListWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);

            ServiciosListWrapper wrapper = new ServiciosListWrapper();
            wrapper.setServicios(listaServicios);

            marshaller.marshal(wrapper,fileServicio);

        } catch (JAXBException e){
            e.printStackTrace();
        }
    }

    @Override
    public void addLog(Log log) {
        //Todo: Exceptions, y ver que pasa con el atributo de listaClientes de la clase Servicio, puesto que el XML no lo guarda porque esta vacío
        try {
            //Leo del XML para tener datos actualizados
            List<Log> listaLogs = leerListaLog();
            listaLogs.add(log);
            guardarLogsEnXML(listaLogs);
            System.out.println("Log añadido correctamente");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void guardarLogsEnXML(List<Log> listaLogs){
        try {
            JAXBContext context = JAXBContext.newInstance(LogsListWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);

            LogsListWrapper wrapper = new LogsListWrapper();
            wrapper.setLogs(listaLogs);

            marshaller.marshal(wrapper,fileLog);

        } catch (JAXBException e){
            e.printStackTrace();
        }
    }

    public void addListaLogs(List<Log> nuevosLogs){
        //TODO: Test, exceptions, si salen duplicados hay que decir cuales ya existen, igual con Cliente y Servicio
        try {
            List<Log> listaLogs = leerListaLog();
            listaLogs.addAll(nuevosLogs);
            guardarLogsEnXML(listaLogs);
            System.out.println("Lista de logs guardada con éxito");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String consultaServicio(String codigo) throws IllegalArgumentException,ServiceNotFoundException {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException ("El codigo no puede ser nulo o estar vacío");
        }

        List <Servicio> listaServicios = leerListaServicios();

        for (Servicio temp: listaServicios){
            if (temp.getCodigo().equalsIgnoreCase(codigo)){
                return temp.toString();
            }
        }
        throw new ServiceNotFoundException("ERROR AL CONSULTAR: No se ha encontrado ningún servicio con el código " + codigo);
    }

    @Override
    public List<Servicio> leerListaServicios() {
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
            e.printStackTrace();
        }
        return serviciosTemporal;
    }

    @Override
    public String consultaCliente(String dni) throws ClientNotFoundException {
        if (dni == null || dni.isBlank()) {
            throw new IllegalArgumentException("DNI no puede ser nulo o vacío");
        }

        if (!Cliente.validarDni(dni)) {
            throw new IllegalArgumentException("DNI no válido");
        }

        //Siempre consulto datos del XML para tener los más actualizados
        List <Cliente> clientesActuales = leerListaClientes();

        for(Cliente c: clientesActuales){
            if (c.getDni().equals(dni)){
               return c.toString();
            }
        }
        throw new ClientNotFoundException("ERROR AL CONSULTAR: Cliente no encontrado");
    }

    @Override
    public List<Cliente> leerListaClientes() {
        //TODO: Exceptions
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
        List<Log> listaLogs = leerListaLog();
        //TODO: Excepciones, Test
        if (codigoServicio == null || codigoServicio.isBlank() || dni == null || dni.isBlank() || fecha == null) {
            throw new IllegalArgumentException("Error: parámetros inválidos. Asegúrate de introducir un código, un DNI y una fecha válidos.");
        }

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
    }

    @Override
    public List<Log> leerListaLog() {
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
            e.printStackTrace();
        }
        return logsTemporal;
    }

    @Override
    public boolean actualizarServicio(String codigo, Servicio servicioActualizado) {
        try {
            List<Servicio> listaServicios = leerListaServicios();
            boolean encontrado = false;

            for (int i = 0; i < listaServicios.size(); i++) {
                Servicio temp = listaServicios.get(i);
                if (temp.getCodigo().equalsIgnoreCase(codigo)) {
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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizarCliente(String dni, Cliente clienteActualizado) {
        //TODO: Exceptions
        try {
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
                throw new ClientNotFoundException("No se ha encontrado el cliente deseado");
            }
            return encontrado;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean borrarServicio(Servicio servicio) {
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
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean borrarCliente(Cliente cliente) {

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
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public double ganaciasAlimentos(String dni, String concepto) {
        List<Log> listaLog = leerListaLog();
        double totalGanancias = 0;

        TipoConcepto tipoConcepto = TipoConcepto.valueOf(concepto.toUpperCase());

        if (tipoConcepto != TipoConcepto.COMPRACOMIDA &&
            tipoConcepto != TipoConcepto.COMPRABEBIDA){
            //TODO:trow exception
            return -1;
        }

        for (Log l : listaLog){
            if (l.getCliente() != null && l.getCliente().getDni().equals(dni) && l.getConcepto() == tipoConcepto){
                totalGanancias += l.getCantidadConcepto();
            }
        }
        return totalGanancias;
    }

    @Override
    public double dineroInvertidoClienteEnDia(String dni, LocalDate fecha) {
        //Todo: Exceptions
        if (dni == null || dni.isBlank() || fecha == null){
            throw new IllegalArgumentException("DNI y Fecha no pueden ser nulos");
        }

        List<Log> listaLog = leerListaLog();
        double totalGanado = 0.0;

        for (Log l : listaLog){
            //Verifico que el log tenga todos los datos necesarios
            if (l != null && l.getCliente() != null && l.getCliente().getDni() != null &&
            l.getFecha() != null && l.getConcepto() != null){
                //Verificar si el cliente coincide
                if (l.getCliente().getDni().equalsIgnoreCase(dni) &&
                l.getFecha().equals(fecha)){
                    //Sumo segun el tipo de concepto
                    switch (l.getConcepto()){
                        case APUESTACLIENTEGANA,DARSEDEBAJA -> totalGanado -= l.getCantidadConcepto();
                        case COMPRACOMIDA, COMPRABEBIDA, DARSEDEALTA -> totalGanado += l.getCantidadConcepto();
                    }
                } else {
                    throw new LogNotFoundException("No se ha encontrado un log con DNI: " + l.getCliente().getDni() + " y Fecha: " + l.getFecha());
                }
            }
        }
        return totalGanado;
    }

    @Override
    public int vecesClienteJuegaMesa(String dni, String codigo) {
        //TODO: Excepciones
        if (dni == null || dni.isBlank() || codigo == null || codigo.isBlank()){
            throw new IllegalArgumentException("El DNI y el Código no pueden ser nulos");
        }

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

                //Comprobar que coincida
                if (logDni.equalsIgnoreCase(dniLimpio) && logCodigo.equalsIgnoreCase(codigoLimpio)){
                    if (l.getConcepto() == TipoConcepto.DARSEDEALTA ||
                            l.getConcepto() == TipoConcepto.APUESTACLIENTEGANA){
                        cantidadJugado++;
                    }
                }
            }
        }
        return cantidadJugado;
    }

    @Override
    public double ganadoMesas() {
        List<Log> listaLog = leerListaLog();
        double totalGanado = 0.0;

        for (Log l : listaLog){
            if (l != null && l.getServicio() != null && l.getServicio().getTipo() != null &&
                    l.getConcepto() != null) {

                // Verificar que es un servicio de tipo MESA
                TipoServicio tipo = l.getServicio().getTipo();
                boolean esMesa = tipo == TipoServicio.MESABLACKJACK || tipo == TipoServicio.MESAPOKER;

                if (esMesa){
                    switch (l.getConcepto()){
                        case APUESTACLIENTEGANA,DARSEDEBAJA -> totalGanado -= l.getCantidadConcepto();
                        case DARSEDEALTA -> totalGanado += l.getCantidadConcepto();
                    }
                }
            }
        }
        return totalGanado;
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
