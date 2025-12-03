package casino.db;

import casino.dao.CasinoDAO;
import casino.model.Cliente;
import casino.model.Log;
import casino.model.Servicio;
import casino.model.TipoServicio;
import exceptions.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    public void addCliente(Cliente cliente) throws ValidacionException, ClientAlreadyExistsException, IOException {
        String consulta = "INSERT INTO clientes(dni, nombre, apellido) VALUES(?, ?, ?)";

        try {
            PreparedStatement stm = conn.conectarBaseDatos().prepareStatement(consulta);
            stm.setString(1, cliente.getDni());
            stm.setString(2, cliente.getNombre());
            stm.setString(3, cliente.getApellidos());

            stm.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO: Revisar este método por las excepciones
    public void addClientes(ArrayList<Cliente> listaClientes) throws IOException {
        for  (Cliente cliente : listaClientes) {
            addCliente(cliente);
        }
    }


    @Override
    public void addServicio(Servicio servicio) throws ValidacionException, ServiceAlreadyExistsException, IOException {

    }

    @Override
    public void addLog(Log log) throws ValidacionException, IOException {

    }

    @Override
    public String consultaServicio(String codigo) throws ValidacionException, IOException {
        return "";
    }

    @Override
    public List<Servicio> leerListaServicios() throws IOException {
        return List.of();
    }

    @Override
    public String consultaCliente(String dni) throws ValidacionException, ClientNotFoundException, IOException {
        return "";
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
}
