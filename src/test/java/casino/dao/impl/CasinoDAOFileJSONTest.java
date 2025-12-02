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
        assertThrows(IllegalArgumentException.class, () -> json.addCliente(cli001));
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

        assertThrows(IllegalArgumentException.class, () -> json.addServicio(ser001));
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
    void leerListaLog() {
        logs.add(log001);
        logs.add(log002);
        logs.add(log003);
        logs.add(log004);
        logs.add(log005);

        try {
            ArrayList<Log> listaLogs = (ArrayList<Log>) json.leerListaLog();
            assertEquals(logs, listaLogs);
        } catch (IOException e) {
            System.out.println("error");
        }
    }

    @Test
    void consultaCliente() {
        String clienteString = cli001.toString();
        try {
            assertEquals(clienteString, json.consultaCliente("12345678Z"));
        } catch (IOException e) {
            System.out.println("error en consultaCliente");
        }
    }

    @Test
    void consultaClienteThrowsClienteNotFound() {
        assertThrows(ClientNotFoundException.class,() -> json.consultaCliente("NOTVALIDDNI"));
    }

    @Test
    void consultaServicio() {
        String serviceString = ser001.toString();
        try {
            assertEquals(serviceString, json.consultaServicio("10908"));
        } catch (IOException e) {
            System.out.println("error en consultaServicio");
        }
    }

    @Test
    void consultaServicioThrowsServiceNotFound() {
        assertThrows(ServiceNotFoundException.class, () -> json.consultaServicio("NOTVALIDCODE"));
    }

    @Test
    void consultaLog() {
        String logString = log001.toString();
        try{
            assertEquals(logString, json.consultaLog("10908", "12345678Z", dateFecha));
        } catch (IOException e) {
            System.out.println("Error en consultaLog");
        }
    }

    @Test
    void consultaLogThrowsLogNotFound() {
        assertThrows(LogNotFoundException.class, () -> json.consultaLog("NOTVALIDCODE", "NOTVALIDDNI", dateFecha));
    }

    @Test
    void actualizarCliente() {
        cli001.setNombre("Alberto");
        String nombre = "Alberto";

        try {
            json.actualizarCliente("12345678Z",cli001);
        } catch (IOException e) {
            System.out.println("error en actualizarCliente");
        }
        assertEquals(nombre, cli001.getNombre());
    }

    @Test
    void actualizarClienteThrowsClientNotFound() {
        assertThrows(ClientNotFoundException.class, () -> json.actualizarCliente("NOTVALIDDNI", cli001));
    }

    @Test
    void actualizarServicio() {
        String nuevoNombre = "Mesa Actualizada";
        ser001.setNombreServicio(nuevoNombre);
        try {
            json.actualizarServicio("10908", ser001);
            assertEquals(nuevoNombre, ser001.getNombreServicio());
        } catch (Exception e) {
            System.out.println("error en actualizarServicio");
        }
    }

    @Test
    void actualizarServicioThrowsServiceNotFound() {
        assertThrows(ServiceNotFoundException.class, () -> json.actualizarServicio("NOTVALIDCODE", ser001));
    }

    @Test
    void borrarServicio() {
        try {
            json.borrarServicio(ser001);
            assertThrows(ServiceNotFoundException.class, () -> json.consultaServicio("10908"));
        } catch (IOException e) {
            System.out.println("error en borrarServicio");
        }
    }

    @Test
    void borrarServicioThrowsServiceNotFound(){
        Servicio ser004 = new Servicio(TipoServicio.BAR, "Bar De Pruebas");
        assertThrows(ServiceNotFoundException.class, () -> json.borrarServicio(ser004));
    }

    @Test
    void borrarCliente() {
        try {
            json.borrarCliente(cli001);
            assertThrows(ClientNotFoundException.class, () -> json.consultaCliente("12345678Z"));
        } catch (IOException e) {
            System.out.println("error en borrarCliente");
        }
    }

    @Test
    void borrarClienteThrowsClientNotFound() {
        Cliente cli003 = new Cliente("53720451H", "Prueba", "Pruebez");
        assertThrows(ClientNotFoundException.class, () -> json.borrarCliente(cli003));
    }

    @Test
    void dineroInvertidoClienteEnDia() {
        double totalInvertido = 35.0 + 15.0 + 50.0 - 200.0 + 100.0;
        try {
            assertEquals(totalInvertido, json.dineroInvertidoClienteEnDia("12345678Z", dateFecha));
        } catch (IOException e) {
            System.out.println("Error en dineroInvertidoClienteEnDia");
        }
    }

    @Test
    void dineroInvertidoClienteEnDiaThrowsClientNotFound(){
        assertThrows(ClientNotFoundException.class, () -> json.dineroInvertidoClienteEnDia("NOTVALIDDNI", dateFecha));
    }

    @Test
    void gananciasAlimentos() {
        double totalInvertido = 35.0 + 15.0;
        try {
            assertEquals(totalInvertido, json.gananciasAlimentos("12345678Z"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void gananciasAlimentosThrowsClientNotFound(){
        assertThrows(ClientNotFoundException.class, () -> json.gananciasAlimentos("NOTVALIDDNI"));
    }

    @Test
    void dineroGanadoClienteEnDia() {
        double totalInvertido = -100.0 - 50.0 + 200.0;
        try {
            assertEquals(totalInvertido, json.dineroGanadoClienteEnDia("12345678Z", dateFecha));
        } catch (IOException e) {
            System.out.println("Error en DineroGanadoClienteEnDia");
        }
    }

    @Test
    void dineroGanadoClienteEnDiaThrowsClientNotFound(){
        assertThrows(ClientNotFoundException.class, () -> json.dineroGanadoClienteEnDia("NOTVALIDDNI", dateFecha));
    }

    @Test
    void vecesClienteJuegaMesa() {
        double contador = 3.0;
        try {
            assertEquals(contador, json.vecesClienteJuegaMesa(cli001.getDni(), ser001.getCodigo()));
        } catch (IOException e) {
            System.out.println("Error en vecesClienteJuegaMesa");
        }
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
    void ganadoMesas() {
        double totalInvertido = 100.0 - 200.0 + 50.0;
        try {
            assertEquals(totalInvertido, json.ganadoMesas());
        } catch (IOException e) {
            System.out.println("Error en ganadoMesas");
        }
    }

    @Test
    void ganadoEstablecimientos() {
        double totalInvertido = 35.0 + 15.0;
        try {
            assertEquals(totalInvertido, json.ganadoEstablecimientos());
        } catch (IOException e) {
            System.out.println("Error en ganadoEstablecimientos");
        }
    }

    @Test
    void devolverServiciosTipo() {
        ArrayList<Servicio> mesaPokerLista = new ArrayList<>();
        mesaPokerLista.add(ser001);

        try {
            assertEquals(mesaPokerLista, json.devolverServiciosTipo(TipoServicio.MESAPOKER));
        } catch (IOException e) {
            System.out.println("Error en devolverServiciosTipo");
        }
    }
}