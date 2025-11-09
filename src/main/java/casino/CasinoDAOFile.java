package casino;

import java.time.LocalDate;
import java.util.List;

public class CasinoDAOFile implements CasinoDAO {

    @Override
    public void addCliente(Cliente cliente) {
        List<Cliente> lista = leerListaClientes();

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
        return "";
    }

    @Override
    public List<Cliente> leerListaClientes() {
        return List.of();
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
