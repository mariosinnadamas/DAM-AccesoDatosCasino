package casino;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class CasinoDAOFileXML implements CasinoDAO {
    Path pathCliente = Path.of("src", "proyecto", "casino", "recursos", "xml", "cliente.xml");
    File fileCliente = new File(pathCliente.toString());

    Path pathLog = Path.of("src", "proyecto", "casino", "recursos", "xml", "log.xml");
    File fileLog = new File(pathLog.toString());

    Path pathServicio = Path.of("src", "proyecto", "casino", "recursos", "xml", "servicio.xml");
    File fileServicio = new File(pathServicio.toString());


    @Override
    public void addCliente(Cliente cliente) {

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
    public List<Servicio> listaServicios() {
        return List.of();
    }

    @Override
    public String consultaCliente(String dni) {
        return "";
    }

    @Override
    public List<Cliente> listaClientes() {
        return List.of();
    }

    @Override
    public String consultaLog(String codigo, String dni, LocalDate fecha) {
        return "";
    }

    @Override
    public List<Log> listaLog() {
        return List.of();
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
}
