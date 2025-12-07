package casino.db;

import casino.model.*;
import exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CasinoDAODBTest {
    CasinoDAODB daodb = new CasinoDAODB("casinotest");

    //Clientes
    Cliente cli001 = new Cliente("12345678Z", "Juan Paco", "García Pérez");
    Cliente cli002 = new Cliente("87654321X", "María","López Sánchez");
    Cliente cli003 = new Cliente("75972453A", "Prueba", "Pruebez");
    ArrayList<Cliente> clientes = new ArrayList<>();

    //Servicios
    Servicio ser001 = new Servicio("10908", TipoServicio.MESAPOKER,"Mesa Poker VIP", clientes, 10);
    Servicio ser002 = new Servicio("F4AC5",TipoServicio.MESABLACKJACK,"Mesa BlackJack Premium", clientes, 7);
    Servicio ser003 = new Servicio("64B52", TipoServicio.BAR, "Bar Casino" ,clientes ,20);
    ArrayList<Servicio> servicios = new ArrayList<>();

    //Log
    LocalDate dateFecha = Date.valueOf("2025-11-29").toLocalDate();
    LocalTime dateHora = Time.valueOf("13:14:44").toLocalTime();
    Log log001 = new Log(cli001, ser001, dateFecha, dateHora, TipoConcepto.APOSTAR, 100.0);
    Log log002 = new Log(cli001, ser001, dateFecha, dateHora, TipoConcepto.APUESTACLIENTEGANA, 200.0);
    Log log003 = new Log(cli001, ser001, dateFecha, dateHora, TipoConcepto.APOSTAR, 15.0);
    Log log004 = new Log(cli001, ser003, dateFecha, dateHora, TipoConcepto.COMPRABEBIDA, 15.0);
    Log log005 = new Log(cli001, ser003, dateFecha, dateHora, TipoConcepto.COMPRACOMIDA, 35.0);
    ArrayList<Log> logs = new ArrayList<>();

    public void init() throws SQLException {
        ConexionDB conexionDB =  new ConexionDB();
        String consulta =
                "DROP SCHEMA if EXISTS casinotest cascade; " +
                        "CREATE SCHEMA if not exists casinotest; " +
                        "set search_path to casinotest; " +
                        "CREATE TABLE if not exists clientes (" +
                        "dni varchar(9) primary key," +
                        "nombre varchar(50) not null," +
                        "apellido varchar(50) not null" +
                        ");" +
                        "create table if not exists servicios (" +
                        "codigo varchar(5) primary key," +
                        "nombre varchar(50) not null," +
                        "tipo varchar(50) not null," +
                        "capacidad int not null," +
                        "lista_clientes JSON not null" +
                        "); " +
                        "create table if not exists logs (" +
                        "dni varchar(9)," +
                        "codigo varchar(5)," +
                        "fecha date not null," +
                        "hora time not null," +
                        "concepto varchar (50) not null, " +
                        "cantidad_concepto decimal(10,2) not null, " +
                        "lista_clientes JSON not null, " +
                        "foreign key (dni) references clientes(dni), " +
                        "foreign key (codigo) references servicios(codigo) " +
                        "); " +
                        "INSERT INTO clientes(dni, nombre, apellido) VALUES" +
                        "('12345678Z', 'Juan Paco', 'García Pérez'), " +
                        "('87654321X', 'María', 'López Sánchez');" +
                        "INSERT INTO servicios(codigo, nombre, tipo, capacidad, lista_clientes) VALUES" +
                        "('10908', 'Mesa Poker VIP', 'MESAPOKER', 10, '[]')," +
                        "('F4AC5', 'Mesa BlackJack', 'MESABLACKJACK', 7, '[]')," +
                        "('64B52', 'Bar Casino', 'BAR', 20, '[]');" +
                        "INSERT INTO logs(dni, codigo, fecha, hora, concepto, cantidad_concepto, lista_clientes) VALUES" +
                        "('12345678Z', '10908', '2025-11-29', '13:14:44', 'APOSTAR', 100.0, '[]')," +
                        "('12345678Z', '10908', '2025-11-29', '13:14:44', 'APUESTACLIENTEGANA', 200.0, '[]')," +
                        "('12345678Z', '10908', '2025-11-29', '13:14:44', 'APOSTAR', 15.0, '[]')," +
                        "('12345678Z', '64B52', '2025-11-29', '13:14:44', 'COMPRABEBIDA', 15.0, '[]')," +
                        "('12345678Z', '64B52', '2025-11-29', '13:14:44', 'COMPRACOMIDA', 35.0, '[]');";
        Connection con = conexionDB.conectarBaseDatos();

        PreparedStatement stm = con.prepareStatement(consulta);

        stm.executeUpdate();
    }

    @BeforeEach
    void setUp() {
        try {
            init();
        } catch (SQLException e) {
            System.out.println("Error en la conexión del test");
        }
    }

    //TODO: Agregar TEST addCliente()
    @Test
    void addCliente() throws IOException {
        daodb.addCliente(cli003);
        assertEquals(cli003.toString(),daodb.consultaCliente(cli003.getDni()));
    }

    @Test
    void addClienteNull(){
        Cliente cli = null;
        assertThrows(ValidacionException.class, () -> daodb.addCliente(cli));
    }

    @Test
    void addClienteThrowsClientAlreadyExistsException() {
        assertThrows(ClientAlreadyExistsException.class, () -> daodb.addCliente(cli001));
    }


    @Test
    void addClientesNull(){
        ArrayList<Cliente> clientes = new ArrayList<>();
        clientes.add(null);
        assertThrows(ValidacionException.class, () -> daodb.addClientes(null));
        assertThrows(ValidacionException.class, () -> daodb.addClientes(clientes));

    }

    //TODO: Agregar TEST addServicio()
    @Test
    void addServicio() {
    }

    @Test
    void addServicioNull() {
        Servicio serv = null;
        assertThrows(ValidacionException.class, () -> daodb.addServicio(serv));
    }

    @Test
    void addServicioThrowsServiceAlreadyExistsException() {
        assertThrows(ServiceAlreadyExistsException.class, () -> daodb.addServicio(ser001));
    }

    //TODO: Agregar TEST addLog()
    @Test
    void addLog() {
    }

    @Test
    void addLogNull() {
        Log log = null;
        assertThrows(ValidacionException.class, () -> daodb.addLog(log));
    }

    @Test
    void consultaServicio() throws IOException {
        String servicioString = ser001.toString();
        assertEquals(servicioString, daodb.consultaServicio(ser001.getCodigo()));
    }

    @Test
    void consultaServicioThrowsServiceNotFound(){
        assertThrows(ServiceNotFoundException.class, () -> daodb.consultaServicio("NOTVALIDCODE"));
    }

    @Test
    void leerListaServicios() throws IOException {
        servicios.add(ser001);
        servicios.add(ser002);
        servicios.add(ser003);
        ArrayList<Servicio> serviciosdb = (ArrayList<Servicio>) daodb.leerListaServicios();

        assertEquals(servicios, serviciosdb);
    }

    @Test
    void consultaCliente() throws IOException {
        String clienteString = cli001.toString();
        assertEquals(clienteString, daodb.consultaCliente(cli001.getDni()));
    }

    @Test
    void consultaClienteThrowsClientNotFound(){
        assertThrows(ClientNotFoundException.class, () -> daodb.consultaCliente("NOTVALIDDNI"));
    }

    @Test
    void leerListaClientes() throws IOException {
        clientes.add(cli001);
        clientes.add(cli002);

        ArrayList<Cliente> clientesdb = (ArrayList<Cliente>) daodb.leerListaClientes();

        assertEquals(clientes, clientesdb);
    }

    @Test
    void consultaLog() throws IOException {
        logs.add(log001);
        logs.add(log003);
        //List<Log> listaLogs = (ArrayList<Log>) daodb.leerListaLog();
        assertEquals(logs, daodb.consultaLog("10908", "12345678Z", dateFecha));
    }

    @Test
    void consultaLogThrowsLogNotFound() {
        assertThrows(LogNotFoundException.class, () -> daodb.consultaLog("NOTVALIDCODE", "NOTVALIDDNI", dateFecha));
    }

    @Test
    void actualizarCliente() throws IOException {
        cli001.setNombre("Alberto");
        String nombre = "Alberto";
        daodb.actualizarCliente("12345678Z",cli001);
        assertEquals(nombre, cli001.getNombre());
    }

    @Test
    void actualizarClienteThrowsClientNotFound() {
        assertThrows(ClientNotFoundException.class, () -> daodb.actualizarCliente("NOTVALIDDNI", cli001));
    }

    @Test
    void actualizarServicio() throws IOException {
        String nuevoNombre = "Mesa Actualizada";
        ser001.setNombreServicio(nuevoNombre);
        daodb.actualizarServicio("10908", ser001);
        assertEquals(nuevoNombre, ser001.getNombreServicio());
    }

    @Test
    void actualizarServicioThrowsServiceNotFound() {
        assertThrows(ServiceNotFoundException.class, () -> daodb.actualizarServicio("NOTVALIDCODE", ser001));
    }

    @Test
    void borrarServicio() throws IOException {
        daodb.borrarServicio(ser001);
        assertThrows(ServiceNotFoundException.class, () -> daodb.consultaServicio("10908"));
    }

    @Test
    void borrarServicioThrowsServiceNotFound(){
        Servicio ser004 = new Servicio(TipoServicio.BAR, "Bar De Pruebas");
        assertThrows(ServiceNotFoundException.class, () -> daodb.borrarServicio(ser004));
    }

    @Test
    void borrarCliente() throws IOException {
        daodb.borrarCliente(cli001);
        assertThrows(ClientNotFoundException.class, () -> daodb.consultaCliente("12345678Z"));

    }

    @Test
    void borrarClienteThrowsClientNotFound() {
        Cliente cli003 = new Cliente("53720451H", "Prueba", "Pruebez");
        assertThrows(ClientNotFoundException.class, () -> daodb.borrarCliente(cli003));
    }

    @Test
    void dineroInvertidoClienteEnDia() throws IOException {
        double totalInvertido = 35.0 + 15.0 + 50.0 - 200.0 + 100.0;
        assertEquals(totalInvertido, daodb.dineroInvertidoClienteEnDia("12345678Z", dateFecha));

    }

    @Test
    void dineroInvertidoClienteEnDiaThrowsClientNotFound(){
        assertThrows(ClientNotFoundException.class, () -> daodb.dineroInvertidoClienteEnDia("NOTVALIDDNI", dateFecha));
    }

    @Test
    void gananciasAlimentos() throws IOException {
        double totalInvertido = 35.0 + 15.0;
        assertEquals(totalInvertido, daodb.gananciasAlimentos("12345678Z"));
    }

    @Test
    void gananciasAlimentosThrowsClientNotFound(){
        assertThrows(ClientNotFoundException.class, () -> daodb.gananciasAlimentos("NOTVALIDDNI"));
    }

    @Test
    void dineroGanadoClienteEnDia() throws IOException {
        double totalInvertido = -100.0 - 50.0 + 200.0;
        assertEquals(totalInvertido, daodb.dineroGanadoClienteEnDia("12345678Z", dateFecha));
    }

    @Test
    void dineroGanadoClienteEnDiaThrowsClientNotFound(){
        assertThrows(ClientNotFoundException.class, () -> daodb.dineroGanadoClienteEnDia("NOTVALIDDNI", dateFecha));
    }

    @Test
    void vecesClienteJuegaMesa() throws IOException {
        double contador = 3.0;
        assertEquals(contador, daodb.vecesClienteJuegaMesa(cli001.getDni(), ser001.getCodigo()));
    }

    @Test
    void vecesClienteJuegaMesaThrowsClientNotFound(){
        assertThrows(ClientNotFoundException.class, () -> daodb.vecesClienteJuegaMesa("NOTVALIDDNI", ser001.getCodigo()));
    }

    @Test
    void vecesClienteJuegaMesaThrowsServiceNotFound(){
        assertThrows(ServiceNotFoundException.class, () -> daodb.vecesClienteJuegaMesa(cli001.getDni(), "NOTVALIDCODE"));
    }

    @Test
    void ganadoMesas() throws IOException {
        double totalInvertido = 100.0 - 200.0 + 50.0;
        assertEquals(totalInvertido, daodb.ganadoMesas());
    }

    @Test
    void ganadoEstablecimientos() throws IOException {
        double totalInvertido = 35.0 + 15.0;
        assertEquals(totalInvertido, daodb.ganadoEstablecimientos());
    }

    @Test
    void devolverServiciosTipo() throws IOException {
        ArrayList<Servicio> mesaPokerLista = new ArrayList<>();
        mesaPokerLista.add(ser001);
        assertEquals(mesaPokerLista, daodb.devolverServiciosTipo(TipoServicio.MESAPOKER));
    }

    @Test
    void listaClientesToJSON() {
        String validJsonFormat = "[\"12345678Z\", \"87654321X\"]";
        clientes.add(cli001);
        clientes.add(cli002);
        assertEquals(validJsonFormat, daodb.listaClientesToJSON(clientes));
    }

    @Test
    void listaClientesToJSONNull(){
        assertThrows(ValidacionException.class, () -> daodb.listaClientesToJSON(null));
    }

    @Test
    void jsonToClientes() throws SQLException, IOException {
        String validJsonFormat = "[\"12345678Z\",\"87654321X\"]";
        clientes.add(cli001);
        clientes.add(cli002);
        assertEquals(clientes, daodb.jsonToClientes(validJsonFormat));
    }
}