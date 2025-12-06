package casino.dao.impl;

import casino.model.*;
import exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CasinoDAOFileJSONTest {
    CasinoDAOFileJSON json = new CasinoDAOFileJSON();

    //Clientes
    Cliente cli001 = new Cliente("12345678Z", "Juan Paco", "García Pérez");
    Cliente cli002 = new Cliente("87654321X", "María","López Sánchez");
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
    Log log003 = new Log(cli001, ser001, dateFecha, dateHora, TipoConcepto.APOSTAR, 50.0);
    Log log004 = new Log(cli001, ser003, dateFecha, dateHora, TipoConcepto.COMPRABEBIDA, 15.0);
    Log log005 = new Log(cli001, ser003, dateFecha, dateHora, TipoConcepto.COMPRACOMIDA, 35.0);
    ArrayList<Log> logs = new ArrayList<>();


    public void start(String sourcePath, String destinationPath) throws IOException {
        File dir =  new File(sourcePath);
        File[] archivos = dir.listFiles();
        for  (File file : archivos) {
            Files.copy(file.toPath(), Path.of(destinationPath, file.getName()), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @BeforeEach
    void setUp() {
        Path dirBase = Path.of("src","test","resources","data" , "base", "json");
        Path target = Path.of("src","test","resources" ,"data" , "json");

        try {
            start(String.valueOf(dirBase), String.valueOf(target));
        } catch (IOException e) {
            System.out.println("Error en SETUP, JSON");
        }



        Path testCliente = Path.of("src","test","resources","data","json","cliente.json");
        Path testServicio = Path.of("src","test","resources","data","json","servicio.json");
        Path testLog = Path.of("src","test","resources","data","json","log.json");

        json.setFileCliente(new File(String.valueOf(testCliente)));
        json.setFileServicio(new File(String.valueOf(testServicio)));
        json.setFileLog(new File(String.valueOf(testLog)));




    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void leerListaClientesEquals() throws IOException {
        clientes.add(cli001);
        clientes.add(cli002);
        ArrayList<Cliente> clientesJson = (ArrayList<Cliente>) json.leerListaClientes();
        assertEquals(clientes, clientesJson);
    }

    @Test
    void addClienteThrowsClientAlreadyExists() throws IOException {
        assertThrows(ClientAlreadyExistsException.class, () -> json.addCliente(cli001));
    }

    @Test
    void addClienteClientIsNull() throws IOException {
        Cliente cli001 = null;
        assertThrows(ValidacionException.class, () -> json.addCliente(cli001));
    }

    @Test
    void addListaClientesNull(){
        Cliente cli004 = null;
        clientes.add(cli004);
        assertThrows(ValidacionException.class, () -> json.addListaClientes(clientes));
    }

    @Test
    void addListaClientesThrowsClientAlreadyExists() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        clientes.add(cli001);
        assertThrows(ClientAlreadyExistsException.class, () -> json.addListaClientes(clientes));
    }

    @Test
    void leerListaServiciosEquals() throws IOException {
        servicios.add(ser001);
        servicios.add(ser002);
        servicios.add(ser003);

        ArrayList<Servicio> serviciosJson = (ArrayList<Servicio>) json.leerListaServicios();

        assertEquals(servicios, serviciosJson);
    }

    @Test
    void addServicioThrowsAlreadyExists() {
        assertThrows(ServiceAlreadyExistsException.class, () -> json.addServicio(ser001));
    }

    @Test
    void addServicioNull() {
        Servicio ser001 = null;
        assertThrows(ValidacionException.class, () -> json.addServicio(ser001));
    }

    @Test
    void addListaServiciosNull() {
        Servicio ser004 = null;
        servicios.add(ser004);
        assertThrows(ValidacionException.class, () -> json.addListaServicios(servicios));
    }

    @Test
    void addListaServiciosThrowsAlreadyExists() {
        Servicio ser001 = new Servicio(TipoServicio.MESAPOKER,"Nombre Actualizado");
        ser001.setCodigo("10908");
        ArrayList<Servicio> servicios = new ArrayList<>();
        servicios.add(ser001);
        assertThrows(ServiceAlreadyExistsException.class, () -> json.addListaServicios(servicios));
    }

    @Test
    void addLogNull(){
        Log log001 = null;
        assertThrows(ValidacionException.class, () -> json.addLog(log001));
    }

    @Test
    void addListaLogsNull(){
        Log log006 = null;
        logs.add(log006);
        assertThrows(ValidacionException.class, () -> json.addListaLogs(logs));
    }

    @Test
    void leerListaLog() throws IOException {
        logs.add(log001);
        logs.add(log002);
        logs.add(log003);
        logs.add(log004);
        logs.add(log005);

        ArrayList<Log> listaLogs = (ArrayList<Log>) json.leerListaLog();
        assertEquals(logs, listaLogs);

    }

    @Test
    void consultaCliente() throws IOException {
        String clienteString = cli001.toString();
        assertEquals(clienteString, json.consultaCliente("12345678Z"));

    }

    @Test
    void consultaClienteThrowsClienteNotFound() {
        assertThrows(ClientNotFoundException.class,() -> json.consultaCliente("NOTVALIDDNI"));
    }

    @Test
    void consultaServicio() throws IOException {
        String serviceString = ser001.toString();
        assertEquals(serviceString, json.consultaServicio("10908"));
    }

    @Test
    void consultaServicioThrowsServiceNotFound() {
        assertThrows(ServiceNotFoundException.class, () -> json.consultaServicio("NOTVALIDCODE"));
    }

    @Test
    void consultaLog() throws IOException {
        String logString = log001.toString();
        assertEquals(logString, json.consultaLog("10908", "12345678Z", dateFecha));

    }

    @Test
    void consultaLogThrowsLogNotFound() {
        assertThrows(LogNotFoundException.class, () -> json.consultaLog("NOTVALIDCODE", "NOTVALIDDNI", dateFecha));
    }

    @Test
    void actualizarCliente() throws IOException {
        cli001.setNombre("Alberto");
        String nombre = "Alberto";
        json.actualizarCliente("12345678Z",cli001);
        assertEquals(nombre, cli001.getNombre());
    }

    @Test
    void actualizarClienteThrowsClientNotFound() {
        assertThrows(ClientNotFoundException.class, () -> json.actualizarCliente("NOTVALIDDNI", cli001));
    }

    @Test
    void actualizarServicio() throws IOException {
        String nuevoNombre = "Mesa Actualizada";
        ser001.setNombreServicio(nuevoNombre);
        json.actualizarServicio("10908", ser001);
        assertEquals(nuevoNombre, ser001.getNombreServicio());
    }

    @Test
    void actualizarServicioThrowsServiceNotFound() {
        assertThrows(ServiceNotFoundException.class, () -> json.actualizarServicio("NOTVALIDCODE", ser001));
    }

    @Test
    void borrarServicio() throws IOException {
        json.borrarServicio(ser001);
        assertThrows(ServiceNotFoundException.class, () -> json.consultaServicio("10908"));
    }

    @Test
    void borrarServicioThrowsServiceNotFound(){
        Servicio ser004 = new Servicio(TipoServicio.BAR, "Bar De Pruebas");
        assertThrows(ServiceNotFoundException.class, () -> json.borrarServicio(ser004));
    }

    @Test
    void borrarCliente() throws IOException {
        json.borrarCliente(cli001);
        assertThrows(ClientNotFoundException.class, () -> json.consultaCliente("12345678Z"));

    }

    @Test
    void borrarClienteThrowsClientNotFound() {
        Cliente cli003 = new Cliente("53720451H", "Prueba", "Pruebez");
        assertThrows(ClientNotFoundException.class, () -> json.borrarCliente(cli003));
    }

    @Test
    void dineroInvertidoClienteEnDia() throws IOException {
        double totalInvertido = 35.0 + 15.0 + 50.0 - 200.0 + 100.0;
        assertEquals(totalInvertido, json.dineroInvertidoClienteEnDia("12345678Z", dateFecha));

    }

    @Test
    void dineroInvertidoClienteEnDiaThrowsClientNotFound(){
        assertThrows(ClientNotFoundException.class, () -> json.dineroInvertidoClienteEnDia("NOTVALIDDNI", dateFecha));
    }

    @Test
    void gananciasAlimentos() throws IOException {
        double totalInvertido = 35.0 + 15.0;
        assertEquals(totalInvertido, json.gananciasAlimentos("12345678Z"));
    }

    @Test
    void gananciasAlimentosThrowsClientNotFound(){
        assertThrows(ClientNotFoundException.class, () -> json.gananciasAlimentos("NOTVALIDDNI"));
    }

    @Test
    void dineroGanadoClienteEnDia() throws IOException {
        double totalInvertido = -100.0 - 50.0 + 200.0;
        assertEquals(totalInvertido, json.dineroGanadoClienteEnDia("12345678Z", dateFecha));
    }

    @Test
    void dineroGanadoClienteEnDiaThrowsClientNotFound(){
        assertThrows(ClientNotFoundException.class, () -> json.dineroGanadoClienteEnDia("NOTVALIDDNI", dateFecha));
    }

    @Test
    void vecesClienteJuegaMesa() throws IOException {
        double contador = 3.0;
        assertEquals(contador, json.vecesClienteJuegaMesa(cli001.getDni(), ser001.getCodigo()));
    }

    @Test
    void vecesClienteJuegaMesaThrowsClientNotFound(){
        assertThrows(ClientNotFoundException.class, () -> json.vecesClienteJuegaMesa("NOTVALIDDNI", ser001.getCodigo()));
    }

    @Test
    void vecesClienteJuegaMesaThrowsServiceNotFound(){
        assertThrows(ServiceNotFoundException.class, () -> json.vecesClienteJuegaMesa(cli001.getDni(), "NOTVALIDCODE"));
    }

    @Test
    void ganadoMesas() throws IOException {
        double totalInvertido = 100.0 - 200.0 + 50.0;
        assertEquals(totalInvertido, json.ganadoMesas());
    }

    @Test
    void ganadoEstablecimientos() throws IOException {
        double totalInvertido = 35.0 + 15.0;
        assertEquals(totalInvertido, json.ganadoEstablecimientos());
    }

    @Test
    void devolverServiciosTipo() throws IOException {
        ArrayList<Servicio> mesaPokerLista = new ArrayList<>();
        mesaPokerLista.add(ser001);
        assertEquals(mesaPokerLista, json.devolverServiciosTipo(TipoServicio.MESAPOKER));
    }
}