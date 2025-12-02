package casino.dao.impl;

import casino.model.*;
import exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CasinoDAOFileXMLTest {
    private CasinoDAOFileXML dao;

    @BeforeEach
    void setUp() {
        dao = new CasinoDAOFileXML();

        File testFileCliente = new File("build/test-data/xml/cliente_test.xml");
        File testFileServicio = new File("build/test-data/xml/servicio_test.xml");
        File testFileLog = new File("build/test-data/xml/log_test.xml");

        testFileCliente.getParentFile().mkdirs();

        // borrado de los archivos antes de cada test
        testFileCliente.delete();
        testFileServicio.delete();
        testFileLog.delete();

        dao.setFileCliente(testFileCliente);
        dao.setFileServicio(testFileServicio);
        dao.setFileLog(testFileLog);
    }


    // Lectura
    @Test
    void test01_LeerListaClientes_archivoVacio() throws IOException {
        List<Cliente> lista = dao.leerListaClientes();

        assertNotNull(lista);
        assertTrue(lista.isEmpty()); // Archivo vacío al inicio
    }

    @Test
    void test02_LeerListaServicios_archivoVacio() throws IOException{
        List<Servicio> lista = dao.leerListaServicios();

        assertNotNull(lista);
        assertTrue(lista.isEmpty()); // Archivo vacío al inicio
    }

    @Test
    void test03_LeerListaLogs_archivoVacio() throws IOException{
        List<Log> lista = dao.leerListaLog();

        assertNotNull(lista);
        assertTrue(lista.isEmpty()); // Archivo vacío al inicio
    }

    // Escritura
    @Test
    void test04_addCliente() throws IOException {
        Cliente c = new Cliente("06690442H", "Jojo", "Viyuela");
        dao.addCliente(c);

        List<Cliente> lista = dao.leerListaClientes();
        assertEquals(1, lista.size());
        assertEquals(c, lista.getFirst());

    }

    @Test
    void test05_addCliente_excepciones() throws IOException {
        Cliente c1 = new Cliente("06690442H", "Jojo", "Viyuela");
        dao.addCliente(c1);
        assertAll("Excepciones de Cliente",
                () -> assertThrows(ClientAlreadyExistsException.class, () -> dao.addCliente(c1)),
                () -> assertThrows(IllegalArgumentException.class, () -> dao.addCliente(null))
        );
    }

    @Test
    void test06_addServicio() throws IOException {
        Servicio s1 = new Servicio(TipoServicio.MESAPOKER, "Mesa Poker VIP");
        dao.addServicio(s1);

        List<Servicio> lista = dao.leerListaServicios();

        assertEquals(1, lista.size());

        assertEquals(TipoServicio.MESAPOKER, lista.getFirst().getTipo());
        assertEquals(s1, lista.getFirst());
    }

   @Test
   void test07_addServicio_excepciones() throws IOException {
       Servicio s1 = new Servicio(TipoServicio.MESAPOKER, "Mesa Poker VIP");
       dao.addServicio(s1);

       assertAll("Excepciones de Servicio",
               () -> assertThrows(IllegalArgumentException.class, () -> dao.addServicio(null)),
               () -> assertThrows(ServiceAlreadyExistsException.class, () -> dao.addServicio(s1))
       );
   }

//   @Test
//   void test08_addLog() throws IOException {
//       Cliente c1 = new Cliente("06690442H", "Jojo", "Viyuela");
//       Servicio s1 = new Servicio(TipoServicio.MESAPOKER, "Mesa Poker VIP");
//       dao.addCliente(c1);
//       dao.addServicio(s1);
//
//       Log log = new Log(c1, s1, TipoConcepto.APOSTAR, 100.0);
//       dao.addLog(log);
//
//       List<Log> lista = dao.leerListaLog();
//       assertEquals(1, lista.size());
//       assertEquals(log, lista.getFirst());
//   }

    @Test
    void test09_addLog_excepciones() {
        assertAll("Excepciones de Servicio",
                () -> assertThrows(IllegalArgumentException.class, () -> dao.addLog(null))
        );
    }

    // Consulta

    @Test
    void test10_consultaCliente() throws IOException {
        Cliente c1 = new Cliente("06690442H", "Jojo", "Viyuela");
        dao.addCliente(c1);

        String resultado = dao.consultaCliente("06690442H");

        assertNotNull(resultado, "La consulta no deberia devolver null");
        assertTrue(resultado.contains("06690442H"));
        assertTrue(resultado.contains("Jojo"));
        assertTrue(resultado.contains("Viyuela"));
    }

    @Test
    void test11_consultaCliente_excepciones() {
        assertAll( "Excepciones consultaCliente",
                () -> assertThrows(ClientNotFoundException.class, () -> dao.consultaCliente("06690442H")),
                () -> assertThrows(IllegalArgumentException.class, () -> dao.consultaCliente(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> dao.consultaCliente("06690442Z"))
                );
    }

    @Test
    void test12_consultaServicio() throws IOException {
        Servicio s1 = new Servicio(TipoServicio.MESAPOKER, "Mesa Poker VIP");
        dao.addServicio(s1);

        String resultado = dao.consultaServicio(s1.getCodigo());

        assertNotNull(resultado, "La consulta no deberia devolver null");
        assertTrue(resultado.contains(TipoServicio.MESAPOKER.toString()));
        assertTrue(resultado.contains("Mesa Poker VIP"));
    }

    @Test
    void test12_consultaServicio_excepciones() {
        assertAll( "Excepciones consultaServicio",
                () -> assertThrows(ServiceNotFoundException.class, () -> dao.consultaServicio("1312")),
                () -> assertThrows(IllegalArgumentException.class, () -> dao.consultaServicio(null))
        );
    }

    @Test
    void test13_consultarLog() throws IOException {
        Cliente c1 = new Cliente("06690442H", "Jojo", "Viyuela");
        Servicio s1 = new Servicio(TipoServicio.MESAPOKER, "Mesa Poker VIP");
        dao.addCliente(c1);
        dao.addServicio(s1);

        Log log = new Log(c1, s1, TipoConcepto.APOSTAR, 100.0);
        dao.addLog(log);

        String resultado = dao.consultaLog(log.getServicio().getCodigo(), log.getCliente().getDni(), log.getFecha());

        assertNotNull(resultado, "La consulta no deberia devolver null");
        assertTrue(resultado.contains(c1.toString()));
        assertTrue(resultado.contains(s1.getCodigo()));
        assertTrue(resultado.contains(s1.getNombreServicio()));
        assertTrue(resultado.contains(s1.getTipo().toString()));
        assertTrue(resultado.contains(TipoConcepto.APOSTAR.toString()));
        assertTrue(resultado.contains("100.0"));

    }

    @Test
    void test14_consultaLog_excepciones() {
        LocalDate fechaTest = LocalDate.of(1999, 12, 1);
        assertAll( "Excepciones consultaLog",
                () -> assertThrows(LogNotFoundException.class, () -> dao.consultaLog("1312", "06690442H", fechaTest)),
                () -> assertThrows(IllegalArgumentException.class, () -> dao.consultaLog(null, null, null)),
                () -> assertThrows(IllegalArgumentException.class, () -> dao.consultaLog("", "06690442H", fechaTest)),
                () -> assertThrows(IllegalArgumentException.class, () -> dao.consultaLog("S123", "   ", fechaTest)),
                () -> assertThrows(IllegalArgumentException.class, () -> dao.consultaLog("S123", "06690442H", null))
        );
    }

    @Test
    void test15_actualizarCliente() throws IOException{

    }
}