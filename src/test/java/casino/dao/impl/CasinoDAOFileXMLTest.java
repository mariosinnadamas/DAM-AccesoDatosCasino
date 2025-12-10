package casino.dao.impl;

import casino.model.*;
import exceptions.*;
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
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CasinoDAOFileXMLTest {
    CasinoDAOFileXML xml = new CasinoDAOFileXML();

    //Clientes
    Cliente cli001 = new Cliente("12345678Z", "Juan Paco", "García Pérez");
    Cliente cli002 = new Cliente("87654321X", "María","López Sánchez");
    ArrayList<Cliente> clientes = new ArrayList<>();

    //Servicios
    Servicio ser001 = new Servicio("163C5", TipoServicio.MESAPOKER,"Mesa Poker VIP", clientes, 10);
    Servicio ser002 = new Servicio("59C45",TipoServicio.MESABLACKJACK,"Mesa BlackJack Premium", clientes, 7);
    Servicio ser003 = new Servicio("C0C45", TipoServicio.BAR, "Bar Casino" ,clientes ,20);
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
        Path dirBase = Path.of("src","test","resources","data" , "base", "xml");
        Path target = Path.of("src","test","resources" ,"data" , "xml");

        try {
            start(String.valueOf(dirBase), String.valueOf(target));
        } catch (IOException e) {
            System.out.println("Error en SETUP, XML");
        }

        Path testCliente = Path.of("src","test","resources","data","xml","cliente.xml");
        Path testServicio = Path.of("src","test","resources","data","xml","servicio.xml");
        Path testLog = Path.of("src","test","resources","data","xml","log.xml");

        xml.setFileCliente(new File(String.valueOf(testCliente)));
        xml.setFileServicio(new File(String.valueOf(testServicio)));
        xml.setFileLog(new File(String.valueOf(testLog)));
    }

    // Lectura
    @Test
    void test01_LeerListaClientes_archivoVacio() throws IOException {
        clientes.add(cli001);
        clientes.add(cli002);

        ArrayList<Cliente> clientesXml = (ArrayList<Cliente>) xml.leerListaClientes();
        assertEquals(clientes, clientesXml);
        assertNotNull(clientesXml);
    }

    @Test
    void test02_LeerListaServicios_archivoVacio() throws IOException{
        servicios.add(ser001);
        servicios.add(ser002);
        servicios.add(ser003);

        ArrayList<Servicio> serviciosXml = (ArrayList<Servicio>) xml.leerListaServicios();

        assertEquals(servicios, serviciosXml);
        assertNotNull(serviciosXml);
    }

    @Test
    void test03_LeerListaLogs_archivoVacio() throws IOException{
        logs.add(log001);
        logs.add(log002);
        logs.add(log003);
        logs.add(log004);
        logs.add(log005);

        ArrayList<Log> logsXml = (ArrayList<Log>) xml.leerListaLog();

        //Ordenamos para que el equals lo considere igual, al no estar usando equals esto podeis mandarlo a la mierda
        logs.sort(Comparator.comparing(l -> l.getCliente().getDni()));
        logsXml.sort(Comparator.comparing(l -> l.getCliente().getDni()));

        //He puesto esto porque me ha salido de los cojones, gracias
        assertEquals(5, logsXml.size());
        assertEquals(5,logs.size());
        assertEquals(logs, logsXml);
        assertNotNull(logsXml);
    }

    // Escritura
    @Test
    void test04_addCliente() throws IOException {
        Cliente c = new Cliente("53791037V", "Prueba", "Pruebez");
        xml.addCliente(c);

        List<Cliente> lista = xml.leerListaClientes();
        assertEquals(lista.getLast(),c);

    }

    @Test
    void test05_addCliente_excepciones() throws IOException {
        assertAll("Excepciones de Cliente",
                () -> assertThrows(ClientAlreadyExistsException.class, () -> xml.addCliente(cli001)),
                () -> assertThrows(ValidacionException.class, () -> xml.addCliente(null))
        );
    }

    @Test
    void test06_addServicio() throws IOException {
        Servicio s1 = new Servicio(TipoServicio.MESAPOKER, "Mesa Poker2 VIP");
        xml.addServicio(s1);

        List<Servicio> lista = xml.leerListaServicios();

        assertEquals(TipoServicio.MESAPOKER, lista.getLast().getTipo());
        assertEquals(s1, lista.getLast());
    }

   @Test
   void test07_addServicio_excepciones() {
       assertAll("Excepciones de Servicio",
               () -> assertThrows(ValidacionException.class, () -> xml.addServicio(null)),
               () -> assertThrows(ServiceAlreadyExistsException.class, () -> xml.addServicio(ser001))
       );
   }

   @Test
   void test08_addLog() throws IOException {
       Log log = new Log(cli002, ser001, TipoConcepto.APOSTAR, 100.0);
       xml.addLog(log);

       List<Log> lista = xml.leerListaLog();

       Log guardado = lista.getLast();
       assertEquals(cli002.getDni(), guardado.getCliente().getDni());
       assertEquals(ser001.getCodigo(), guardado.getServicio().getCodigo());
       assertEquals(TipoConcepto.APOSTAR, guardado.getConcepto());
       assertEquals(100.0, guardado.getCantidadConcepto());
   }

    @Test
    void test09_addLog_excepciones() {
        assertAll("Excepciones de Servicio",
                () -> assertThrows(ValidacionException.class, () -> xml.addLog(null))
        );
    }

    // Consulta
    @Test
    void test10_consultaCliente() throws IOException {

        String resultado = xml.consultaCliente("12345678Z");

        assertNotNull(resultado, "La consulta no deberia devolver null");
        assertTrue(resultado.contains("12345678Z"));
        assertTrue(resultado.contains("Juan"));
        assertTrue(resultado.contains("Pérez"));
    }

    @Test
    void test11_consultaCliente_excepciones() {
        assertAll( "Excepciones consultaCliente",
                () -> assertThrows(ClientNotFoundException.class, () -> xml.consultaCliente("06690442H")),
                () -> assertThrows(ValidacionException.class, () -> xml.consultaCliente(null)),
                () -> assertThrows(ValidacionException.class, () -> xml.consultaCliente("06690442Z"))
                );
    }

    @Test
    void test12_consultaServicio() throws IOException {
        String resultado = xml.consultaServicio(ser001.getCodigo());

        assertNotNull(resultado, "La consulta no deberia devolver null");
        assertTrue(resultado.contains(TipoServicio.MESAPOKER.toString()));
        assertTrue(resultado.contains("Mesa Poker VIP"));
    }

    @Test
    void test13_consultaServicio_excepciones() {
        assertAll( "Excepciones consultaServicio",
                () -> assertThrows(ServiceNotFoundException.class, () -> xml.consultaServicio("1312")),
                () -> assertThrows(ValidacionException.class, () -> xml.consultaServicio(null))
        );
    }

    @Test
    void test14_consultarLog() throws IOException {
        logs.add(log001);
        assertEquals(logs, xml.consultaLog("163C5", "12345678Z", dateFecha));
    }

    @Test
    void test15_consultaLog_excepciones() {
        LocalDate fechaTest = LocalDate.of(1999, 12, 1);
        assertAll( "Excepciones consultaLog",
                () -> assertThrows(LogNotFoundException.class, () -> xml.consultaLog("1312", "06690442H", fechaTest)),
                () -> assertThrows(ValidacionException.class, () -> xml.consultaLog(null, null, null)),
                () -> assertThrows(ValidacionException.class, () -> xml.consultaLog("", "06690442H", fechaTest)),
                () -> assertThrows(ValidacionException.class, () -> xml.consultaLog("S123", "   ", fechaTest)),
                () -> assertThrows(ValidacionException.class, () -> xml.consultaLog("S123", "06690442H", null))
        );
    }

    @Test
    void test16_actualizarCliente() throws IOException {
        assertTrue(xml.actualizarCliente("87654321X", cli002));

        List<Cliente> lista = xml.leerListaClientes();
        Cliente actualizado = lista.getLast();

        assertEquals("87654321X", actualizado.getDni());
        assertEquals("María", actualizado.getNombre());
        assertEquals("López Sánchez", actualizado.getApellidos());
    }

    @Test
    void test17_actualizarCliente_excepciones() throws IOException {
        Cliente c = new Cliente("06690442H", "Jose", "Cruz");
        xml.addCliente(c);
        assertAll("Validaciones de argumentos",
                () -> assertThrows(ValidacionException.class, () -> xml.actualizarCliente("",c)),
                () -> assertThrows(ValidacionException.class, () -> xml.actualizarCliente(null,c)),
                () -> assertThrows(ValidacionException.class, () -> xml.actualizarCliente("87654321X",null)),
                () -> assertThrows(ClientNotFoundException.class, () -> xml.actualizarCliente("87654321X",c)));
    }

    @Test
    void test18_actualizarServicio() throws IOException {

        assertTrue(xml.actualizarServicio(ser001.getCodigo(), ser002));

        List<Servicio> lista = xml.leerListaServicios();
        Servicio actualizado = lista.get(1);

        assertEquals(TipoServicio.MESABLACKJACK, actualizado.getTipo());
        assertEquals("Mesa BlackJack Premium", actualizado.getNombreServicio());

    }

    @Test
    void test19_actualizarServicio_excepciones() throws IOException {
        assertAll("Validaciones de argumentos",
                () -> assertThrows(ValidacionException.class, () -> xml.actualizarServicio(null,ser001)),
                () -> assertThrows(ValidacionException.class, () -> xml.actualizarServicio("",ser001)),
                () -> assertThrows(ValidacionException.class, () -> xml.actualizarServicio(ser001.getCodigo(),null)),
                () -> assertThrows(ServiceNotFoundException.class, () -> xml.actualizarServicio("8C7B",ser001)));
    }

    @Test
    void test20_borrarCliente() throws IOException {
        boolean borrado = xml.borrarCliente(cli001);

        assertTrue(borrado);
    }

    @Test
    void test21_borrarCliente_excepciones() throws IOException {
        Cliente c = new Cliente("06690442H", "jojo", "Cruz");

        assertThrows(ValidacionException.class, () -> xml.borrarCliente(null));
        assertThrows(ClientNotFoundException.class, () -> xml.borrarCliente(c));
    }

    @Test
    void test22_borrarServicio() throws IOException {

        boolean borrado = xml.borrarServicio(ser001);

        assertTrue(borrado);
    }

    @Test
    void test23_borrarServicio_excepciones() throws IOException {
        Servicio s = new Servicio(TipoServicio.RESTAURANTE,"Resutaurante VIP");
        xml.addServicio(s);

        Servicio s2 = new Servicio("0D1A",s.getTipo(),s.getNombreServicio(),s.getListaClientes(),s.getCapacidadMaxima());
        assertAll("Validaciones de argumentos",
                () -> assertThrows(ValidacionException.class, () -> xml.borrarServicio(null)),
                () -> assertThrows(ServiceNotFoundException.class, () -> xml.borrarServicio(s2)));
    }

    @Test
    void test24_gananciasAlimentos() throws IOException {
        double ganancias = xml.gananciasAlimentos("12345678Z");

        assertEquals(50, ganancias);
    }

    @Test
    void test25_gananciaAliementos_excepciones() {
        assertAll(
                () -> assertThrows(ValidacionException.class, () -> xml.gananciasAlimentos(" ")),
                () -> assertThrows(ValidacionException.class, () -> xml.gananciasAlimentos(null)),
                () -> assertThrows(ValidacionException.class, () -> xml.gananciasAlimentos("00000000A")), // DNI no valido
                () -> assertThrows(ValidacionException.class, () -> xml.gananciasAlimentos("06690442H")) // DNI no encontrado
        );
    }

    @Test
    void test26_dineroInvertidoDia() throws IOException {
        double ganancias = xml.dineroInvertidoClienteEnDia("12345678Z", dateFecha);

        assertEquals(0, ganancias);
    }

    @Test
    void test26_dineroInvertidoDia_excepciones() {
        LocalDate fechaTest = LocalDate.of(1999, 12, 1);
        assertAll(
                () -> assertThrows(ValidacionException.class, () -> xml.dineroInvertidoClienteEnDia("null", dateFecha)),
                () -> assertThrows(ValidacionException.class, () -> xml.dineroInvertidoClienteEnDia(" ", dateFecha)),
                () -> assertThrows(ValidacionException.class, () -> xml.dineroInvertidoClienteEnDia("12345678Z", null)),
                () -> assertThrows(LogNotFoundException.class, () -> xml.dineroInvertidoClienteEnDia("06690442H",  fechaTest))
        );
    }

    @Test
    void test27_vecesClienteJuega() throws IOException {
        int jugadas = xml.vecesClienteJuegaMesa("12345678Z", "163C5");

        assertEquals(3, jugadas);
    }

    @Test
    void test28_vecesClienteJuega_excepciones() {
        assertAll(
                () -> assertThrows(ValidacionException.class, () -> xml.vecesClienteJuegaMesa(" ", "163C5")),
                () -> assertThrows(ValidacionException.class, () -> xml.vecesClienteJuegaMesa("12345678Z", " ")),
                () -> assertThrows(ValidacionException.class, () -> xml.vecesClienteJuegaMesa(null, "163C5")),
                () -> assertThrows(ValidacionException.class, () -> xml.vecesClienteJuegaMesa("12345678Z", null))
        );
    }

    @Test
    void test29_ganadoMesas() throws IOException {
        double ganancias = xml.ganadoMesas();

        assertEquals(-50, ganancias);
    }

    @Test
    void test30_ganadoEstablecimiento() throws IOException {
        double ganancias = xml.ganadoEstablecimientos();

        assertEquals(50, ganancias);
    }

    @Test
    void test30_devolverServiciosTipo() throws IOException {
        List<Servicio> lista = xml.devolverServiciosTipo(TipoServicio.MESAPOKER);

        assertEquals(1, lista.size());
    }

    @Test
    void test31_devolverServiciosTipo_excepciones() {
        assertThrows(ValidacionException.class, () -> xml.devolverServiciosTipo(null));
    }
}