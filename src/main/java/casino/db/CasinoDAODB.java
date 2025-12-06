package casino.db;

import casino.dao.CasinoDAO;
import casino.model.Cliente;
import casino.model.Log;
import casino.model.Servicio;
import casino.model.TipoServicio;
import exceptions.*;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
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

        try {
            PreparedStatement stm = conn.conectarBaseDatos().prepareStatement(consulta);
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
        for  (Cliente cliente : listaClientes) {
            addCliente(cliente);
        }
    }

    //TODO: Revisar y Test
    @Override
    public void addServicio(Servicio servicio) throws ValidacionException, ServiceAlreadyExistsException, IOException {
        String consulta = "INSERT INTO servicios (codigo, nombre, tipo, capacidad) VALUES (?,?,?,?)";

        try{
            PreparedStatement stm = conn.conectarBaseDatos().prepareStatement(consulta);
            stm.setString(1,servicio.getCodigo());
            stm.setString(2,servicio.getNombreServicio());
            stm.setString(3,servicio.getTipo().toString());
            stm.setInt(4,servicio.getCapacidadMaxima());

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

        String consulta = "INSERT INTO logs (dni,codigo,fecha,hora,concepto,cantidad_concepto) VALUES (?,?,?,?,?,?)";

        try {
            PreparedStatement stm = conn.conectarBaseDatos().prepareStatement(consulta);

            stm.setString(1,log.getCliente().getDni());
            stm.setString(2,log.getServicio().getCodigo());
            stm.setDate(3, Date.valueOf(log.getFecha()));
            stm.setTime(4, Time.valueOf(log.getHora()));
            stm.setString(5,log.getConcepto().toString());
            stm.setDouble(6,log.getCantidadConcepto());

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

        String query = "SELECT codigo, nombre, tipo, capacidad FROM servicios WHERE codigo = ?";
        Servicio s = new Servicio();
        try {
            PreparedStatement stm = conn.conectarBaseDatos().prepareStatement(query);
            stm.setString(1,codigo);
            try {
                ResultSet rs = stm.executeQuery();
                s.setCodigo(rs.getString("codigo"));
                s.setNombreServicio(rs.getString("nombre"));
                s.setTipo(TipoServicio.valueOf(rs.getString("tipo")));
                s.setCapacidadMaxima(rs.getInt("capacidad"));
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
        return List.of();
    }

    //TODO: Revisar excepciones y Test
    @Override
    public String consultaCliente(String dni) throws ValidacionException, ClientNotFoundException, IOException {
        if (dni.isEmpty()){
            throw new ValidacionException("El dni no es válido o está vacío");
        }

        String sql = "SELECT dni,nombre,apellidos FROM clientes WHERE dni = ?";
        Cliente c = new Cliente();
        try {
            PreparedStatement stm = conn.conectarBaseDatos().prepareStatement(sql);
            stm.setString(1,dni);

            try {
                ResultSet rs = stm.executeQuery();
                String dniQuery = rs.getString("dni");
                String nombreQuery = rs.getString("nombre");
                String apellidosQuery = rs.getString("apellidos");
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
        return List.of();
    }

    @Override
    public String consultaLog(String codigoServicio, String dni, LocalDate fecha) throws ValidacionException, LogNotFoundException, IOException {
        return "";
    }

    @Override
    public List<Log> leerListaLog() throws IOException {
        return List.of();
    }

    @Override
    public boolean actualizarServicio(String codigo, Servicio servicioActualizado) throws ValidacionException, ServiceNotFoundException, IOException {
        return false;
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
}
