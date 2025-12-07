package casino.db;

import casino.dao.CasinoDAO;
import casino.model.*;
import exceptions.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CasinoDAODB implements CasinoDAO {

    ConexionDB conn;

    public CasinoDAODB() {
        this.conn = new ConexionDB();
    }

    public CasinoDAODB(String schema) {
        this.conn = new ConexionDB(schema);
    }

    @Override
    public void addCliente(Cliente cliente) throws ValidacionException, ClientAlreadyExistsException, IOException, AccesoDenegadoException{
        if (cliente == null){
            throw new ValidacionException("El cliente no puede ser nulo");
        }

        String consulta = "INSERT INTO clientes(dni, nombre, apellido) VALUES(?, ?, ?)";

        try (Connection connection = conn.conectarBaseDatos();
                PreparedStatement stm = connection.prepareStatement(consulta)) {

            stm.setString(1, cliente.getDni());
            stm.setString(2, cliente.getNombre());
            stm.setString(3, cliente.getApellidos());

            try {
                stm.execute();
            } catch (SQLException e) {
                throw new ClientAlreadyExistsException(e.getMessage());
            }

        } catch (SQLException e) {
            throw new AccesoDenegadoException(e.getMessage());
        }
    }

    //TODO: Revisar este método por las excepciones
    public void addClientes(ArrayList<Cliente> listaClientes) throws IOException, ClientAlreadyExistsException, ValidacionException {
        if (listaClientes == null || listaClientes.isEmpty() || listaClientes.contains(null)) {
            throw new ValidacionException("El cliente no puede ser nulo");
        }

        for  (Cliente cliente : listaClientes) {
            addCliente(cliente);
        }
    }

    //TODO: Revisar y Test
    @Override
    public void addServicio(Servicio servicio) throws ValidacionException, ServiceAlreadyExistsException, IOException {
        if (servicio == null){
            throw new ValidacionException("ERROR: El servicio no puede ser nulo");
        }

        String consulta = "INSERT INTO servicios (codigo, nombre, tipo, capacidad, lista_clientes) VALUES (?,?,?,?,?::json)";

        try (Connection connection = conn.conectarBaseDatos();
             PreparedStatement stm = connection.prepareStatement(consulta);){

            stm.setString(1,servicio.getCodigo());
            stm.setString(2,servicio.getNombreServicio());
            stm.setString(3,servicio.getTipo().toString());
            stm.setInt(4,servicio.getCapacidadMaxima());
            stm.setString(5, listaClientesToJSON((ArrayList<Cliente>) servicio.getListaClientes()));

            try {
                stm.execute();
            } catch (SQLException e) {
                throw new ServiceAlreadyExistsException("ERROR: Servicio duplicado " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new AccesoDenegadoException("Ha habido un problema al conectar con la BdD " + e.getMessage());
        }
    }

    //TODO: Revisar y Test
    @Override
    public void addLog(Log log) throws ValidacionException, IOException {
        if (log == null){
            throw new ValidacionException("ERROR: Log inválido o nulo");
        }

        String consulta = "INSERT INTO logs(dni,codigo,fecha,hora,concepto,cantidad_concepto, lista_clientes) VALUES (?,?,?,?,?,?,?::json)";

        try (Connection connection = conn.conectarBaseDatos();
             PreparedStatement stm = connection.prepareStatement(consulta);){

            stm.setString(1,log.getCliente().getDni());
            stm.setString(2,log.getServicio().getCodigo());
            stm.setDate(3, Date.valueOf(log.getFecha()));
            stm.setTime(4, Time.valueOf(log.getHora()));
            stm.setString(5,log.getConcepto().toString());
            stm.setDouble(6,log.getCantidadConcepto());
            stm.setString(7, listaClientesToJSON((ArrayList<Cliente>) log.getServicio().getListaClientes()));

            stm.execute();
        } catch (SQLException e) {
            throw new AccesoDenegadoException("Ha habido un problema al conectar a la BdD "+ e.getMessage());
        }
    }

    //TODO: Revisar y test
    @Override
    public String consultaServicio(String codigo) throws ValidacionException, IOException {
        if (codigo.isEmpty()){
            throw new ValidacionException("El codigo no es válido o está vacío");
        }

        String query = "SELECT codigo, nombre, tipo, capacidad, lista_clientes FROM servicios WHERE codigo = ?";
        Servicio s = new Servicio();
        try (Connection connection = conn.conectarBaseDatos();
             PreparedStatement stm = connection.prepareStatement(query);){

            stm.setString(1,codigo);
            try {
                ResultSet rs = stm.executeQuery();
                rs.next();
                s.setCodigo(rs.getString("codigo"));
                s.setNombreServicio(rs.getString("nombre"));
                s.setTipo(TipoServicio.valueOf(rs.getString("tipo")));
                s.setCapacidadMaxima(rs.getInt("capacidad"));
                String clientes = rs.getString("lista_clientes");
                System.out.println(clientes);
                ArrayList<Cliente> listaClientes = jsonToClientes(clientes);
                for (Cliente cliente : listaClientes){
                    System.out.println(cliente);
                }
                s.setListaClientes(listaClientes);
                System.out.println(s);
            } catch (SQLException e) {
                throw new ServiceNotFoundException("No se ha encontrado ningún servicio con ese código " + e.getMessage());
            } catch (IllegalArgumentException e) {
                //TODO: Ni idea de como tratar esta excepción, ayuda. Me lo ha generado al rodear todo con try-catch
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new AccesoDenegadoException("Ha habido un error al conectar con la BdD: " + e.getMessage());
        }
        return s.toString();
    }

    @Override
    public List<Servicio> leerListaServicios() throws IOException {
        String query = "SELECT codigo,nombre,tipo,capacidad,lista_clientes FROM servicios";
        List <Servicio> listaServicios = new ArrayList<>();
        try (Connection connection = conn.conectarBaseDatos();
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(query)){
            while (rs.next()){
                Servicio s = new Servicio(
                        rs.getString("codigo"),
                        TipoServicio.valueOf(rs.getString("tipo")),
                        rs.getString("nombre"),
                        jsonToClientes(rs.getString("lista_clientes")),
                        rs.getInt("capacidad"));

                listaServicios.add(s);
            }
        } catch (SQLException e) {
            throw new AccessDeniedException("Error al conectar a la BdD: " + e.getMessage());
        }
        return listaServicios;
    }

    //TODO: Revisar excepciones y Test
    @Override
    public String consultaCliente(String dni) throws ValidacionException, ClientNotFoundException, IOException {
        if (dni.isEmpty()){
            throw new ValidacionException("El dni no es válido o está vacío");
        }

        String sql = "SELECT dni,nombre, apellido FROM clientes WHERE dni = ?";
        Cliente c = new Cliente();
        try {
            PreparedStatement stm = conn.conectarBaseDatos().prepareStatement(sql);
            stm.setString(1,dni);

            try {
                ResultSet rs = stm.executeQuery();
                rs.next();
                String dniQuery = rs.getString("dni");
                String nombreQuery = rs.getString("nombre");
                String apellidosQuery = rs.getString("apellido");
                c.setDni(dniQuery);
                c.setNombre(nombreQuery);
                c.setApellidos(apellidosQuery);
            } catch (SQLException e) {
                throw new ClientNotFoundException("No se ha encontrado ningún cliente con ese DNI " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new AccesoDenegadoException("Ha ocurrido un error al acceder a la BdD " + e.getMessage());
        }
        return c.toString();
    }

    @Override
    public List<Cliente> leerListaClientes() throws IOException {
        String query = "SELECT dni, nombre, apellido FROM clientes";
        List <Cliente> listaClientes = new ArrayList<>();
        try (Statement stm = conn.conectarBaseDatos().createStatement();
             ResultSet rs = stm.executeQuery(query)) {
            while (rs.next()){
                Cliente c = new Cliente(rs.getString("dni"), rs.getString("nombre"), rs.getString("apellido"));
                listaClientes.add(c);
            }
        } catch (SQLException e) {
            throw new AccesoDenegadoException("Ha habido un error al conectar a la BdD: " + e.getMessage());
        }
        return listaClientes;
    }

    @Override
    public List<Log> consultaLog(String codigoServicio, String dni, LocalDate fecha) throws ValidacionException, LogNotFoundException, IOException {
        List<Log>listaLogs = new ArrayList<>();

        String consulta = "SELECT dni, codigo, fecha, hora, concepto, cantidad_concepto, lista_clientes " +
                "FROM logs " +
                "WHERE dni = ? AND codigo = ? AND fecha = ?" ;

        try {
            PreparedStatement stm = conn.conectarBaseDatos().prepareStatement(consulta);
            stm.setString(1, dni);
            stm.setString(2, codigoServicio);
            stm.setDate(3, Date.valueOf(fecha));
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Cliente clienteLog = obtenerCliente(rs.getString("dni"));
                    Servicio servicioLog = obtenerServicio(rs.getString("codigo"));
                    LocalDate fechaLog = rs.getDate("fecha").toLocalDate();
                    LocalTime horaLog = rs.getTime("hora").toLocalTime();
                    TipoConcepto conceptoLog = TipoConcepto.valueOf(rs.getString("concepto"));
                    Double cantidadLog =  rs.getDouble("cantidad_concepto");

                    servicioLog.setListaClientes(jsonToClientes(rs.getString("lista_clientes")));
                    Log log = new Log();
                    log.setCliente(clienteLog);
                    log.setServicio(servicioLog);
                    log.setFecha(fechaLog);
                    log.setHora(horaLog);
                    log.setConcepto(conceptoLog);
                    log.setCantidadConcepto(cantidadLog);

                    listaLogs.add(log);
                }

            } catch (SQLException e) {
                throw new AccesoDenegadoException("Ocurrio un problema en la conexion");
            }
        } catch (SQLException e) {
            throw new AccesoDenegadoException("Error al conectar con la BdD " + e.getMessage());
        }
        return listaLogs;
    }

    //Todo: Illo esto que
    @Override
    public List<Log> leerListaLog() throws IOException {
        return List.of();
    }

    @Override
    public boolean actualizarServicio(String codigo, Servicio servicioActualizado) throws ValidacionException, ServiceNotFoundException, IOException {
        String consulta = "UPDATE servicios SET codigo = ?, nombre = ?, tipo = ?, capacidad = ?, lista_clientes = ?::json WHERE codigo = ?";

        if (codigo == null || codigo.isEmpty() || servicioActualizado == null) {
            throw new ValidacionException("Codigo no válido");
        }

        if (consultaServicio(codigo).isEmpty()) {
            throw new ServiceNotFoundException("Servicio no encontrado");
        } else {
            try {
                PreparedStatement stm = conn.conectarBaseDatos().prepareStatement(consulta);
                stm.setString(1, servicioActualizado.getCodigo());
                stm.setString(2, servicioActualizado.getNombreServicio());
                stm.setString(3, servicioActualizado.getTipo().toString());
                stm.setInt(4, servicioActualizado.getCapacidadMaxima());
                stm.setString(5, listaClientesToJSON((ArrayList<Cliente>) servicioActualizado.getListaClientes()));
                stm.setString(6, codigo);


                try {
                    stm.executeUpdate();
                    return true;
                } catch (Exception e) {
                    throw new ServiceNotFoundException("Servicio no encontrado");
                }
            } catch (SQLException e) {
                throw new AccesoDenegadoException("Error al conectar con la BdD " + e.getMessage());
            }
        }

    }

    @Override
    public boolean actualizarCliente(String dni, Cliente clienteActualizado) throws ValidacionException, ClientNotFoundException, IOException {
        return false;
    }

    @Override
    public boolean borrarServicio(Servicio servicio) throws ValidacionException, ServiceNotFoundException, IOException {
        return false;
    }

    @Override
    public boolean borrarCliente(Cliente cliente) throws ValidacionException, ClientNotFoundException, IOException {
        return false;
    }

    @Override
    public double gananciasAlimentos(String dni) throws ValidacionException, IOException {
        return 0;
    }

    @Override
    public double dineroInvertidoClienteEnDia(String dni, LocalDate fecha) throws ValidacionException, LogNotFoundException, IOException {
        return 0;
    }

    @Override
    public int vecesClienteJuegaMesa(String dni, String codigo) throws ValidacionException, IOException {
        return 0;
    }

    @Override
    public double ganadoMesas() throws IOException {
        return 0;
    }

    @Override
    public double ganadoEstablecimientos() throws IOException {
        return 0;
    }

    @Override
    public List<Servicio> devolverServiciosTipo(TipoServicio tipoServicio) throws ValidacionException, IOException {
        return List.of();
    }

    public double dineroGanadoClienteEnDia(String s, LocalDate dateFecha) {
        return 0;
    }

    public String listaClientesToJSON(ArrayList<Cliente> clientes) {
        ArrayList<String> dniList = new ArrayList<>();
        String json = "";
        if (clientes == null || clientes.contains(null)) {
            throw new ValidacionException("No se permiten valores NULL");
        }

        if (clientes.isEmpty()) {
            return "[]";
        } else {
            for (Cliente c : clientes) {
                dniList.add("\""+c.getDni()+"\"");
            }
            String dniSting = String.join(", ", dniList);

            json = "[" +  dniSting + "]";
        }

        return json;
    }

    public ArrayList<Cliente> jsonToClientes(String json) throws IOException, SQLException {
        ArrayList<Cliente> clientes = new ArrayList<>();

        if (json.equals("[]")) {
            return clientes;
        } else {
            json = json.replace("[", "")
                    .replace("]", "")
                    .replace(" ", "")
                    .replace("\"", "")
                    .replace("'", "")
                    .trim();

            String[] dniArray = json.split(",");
            String placeholders = "";
            for (int  i = 0; i < dniArray.length; i++) {
                placeholders += "?";
                if (i < dniArray.length - 1) {
                    placeholders += ",";
                }
            }

            String consulta = "SELECT dni, nombre, apellido FROM clientes WHERE dni IN (" + placeholders + ")";

            PreparedStatement stm = conn.conectarBaseDatos().prepareStatement(consulta);

            for (int i = 0; i < dniArray.length; i++) {
                stm.setString(i + 1, dniArray[i]);
            }

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                clientes.add(new Cliente(
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellido")
                ));
            }

        }
        return clientes;
    }

    private Cliente obtenerCliente(String dni){
        String sql = "SELECT dni,nombre, apellido FROM clientes WHERE dni = ?";
        Cliente c = new Cliente();
        try {
            PreparedStatement stm = conn.conectarBaseDatos().prepareStatement(sql);
            stm.setString(1,dni);

            try {
                ResultSet rs = stm.executeQuery();
                rs.next();
                String dniQuery = rs.getString("dni");
                String nombreQuery = rs.getString("nombre");
                String apellidosQuery = rs.getString("apellido");
                c.setDni(dniQuery);
                c.setNombre(nombreQuery);
                c.setApellidos(apellidosQuery);
            } catch (SQLException e) {
                throw new ClientNotFoundException("No se ha encontrado ningún cliente con ese DNI " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new AccesoDenegadoException("Ha ocurrido un error al acceder a la BdD " + e.getMessage());
        }
        return c;
    }

    private Servicio obtenerServicio(String codigo) throws IOException {
        if (codigo.isEmpty()){
            throw new ValidacionException("El codigo no es válido o está vacío");
        }

        String query = "SELECT codigo, nombre, tipo, capacidad, lista_clientes FROM servicios WHERE codigo = ?";
        Servicio s = new Servicio();
        try {
            PreparedStatement stm = conn.conectarBaseDatos().prepareStatement(query);
            stm.setString(1,codigo);
            try {
                ResultSet rs = stm.executeQuery();
                rs.next();
                s.setCodigo(rs.getString("codigo"));
                s.setNombreServicio(rs.getString("nombre"));
                s.setTipo(TipoServicio.valueOf(rs.getString("tipo")));
                s.setCapacidadMaxima(rs.getInt("capacidad"));
                String clientes = rs.getString("lista_clientes");
                ArrayList<Cliente> listaClientes = jsonToClientes(clientes);
                s.setListaClientes(listaClientes);
            } catch (SQLException e) {
                throw new ServiceNotFoundException("No se ha encontrado ningún servicio con ese código " + e.getMessage());
            } catch (IllegalArgumentException e) {
                //TODO: Ni idea de como tratar esta excepción, ayuda. Me lo ha generado al rodear todo con try-catch
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new AccesoDenegadoException("Ha habido un error al conectar con la BdD: " + e.getMessage());
        }
        return s;
    }
}
