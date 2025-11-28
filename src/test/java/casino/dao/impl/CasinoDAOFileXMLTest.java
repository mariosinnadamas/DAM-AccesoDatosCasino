package casino.dao.impl;

import casino.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CasinoDAOFileXMLTest {
    private CasinoDAOFileXML dao;
    private File testFileCliente;
    private File testFileServicio;
    private File testFileLog;

    @BeforeEach
    void setUp() {
        dao = new CasinoDAOFileXML();

        testFileCliente = new File("build/test-data/xml/cliente_test.xml");
        testFileServicio = new File("build/test-data/xml/servicio_test.xml");
        testFileLog = new File("build/test-data/xml/log_test.xml");

        testFileCliente.getParentFile().mkdirs();

        // borrado de los archivos antes de cada test
        testFileCliente.delete();
        testFileServicio.delete();
        testFileLog.delete();

        dao.setFileCliente(testFileCliente);
        dao.setFileServicio(testFileServicio);
        dao.setFileLog(testFileLog);
    }

    @Test
    void testLeerListaClientes() throws IOException {
        List<Cliente> lista = dao.leerListaClientes();
        assertNotNull(lista);
        assertTrue(lista.isEmpty()); // Archivo vacío al inicio
    }

}