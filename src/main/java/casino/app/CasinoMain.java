package casino.app;

import casino.dao.impl.CasinoDAOFileJSON;
import casino.dao.impl.CasinoDAOFileXML;
import casino.dao.helper.CasinoGestorArchivos;
import casino.model.*;
import casino.util.DummyGenerator;
import exceptions.*;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CasinoMain {
    public static ArrayList<ArrayList> initListas(){
        DummyGenerator dg = new DummyGenerator();
        ArrayList<Cliente> testListaClientes = (ArrayList<Cliente>) dg.crearListaCliente(10);
        ArrayList<Servicio> testListaServicio =  (ArrayList<Servicio>) dg.crearListaServicio(10);
        ArrayList<Log> testListaLog = (ArrayList<Log>) dg.crearLogs(testListaClientes, testListaServicio, 500);

        ArrayList<ArrayList> listaListas = new ArrayList<>();
        listaListas.add(testListaClientes);
        listaListas.add(testListaServicio);
        listaListas.add(testListaLog);

        return listaListas;
    }

    public static void initJson(CasinoDAOFileJSON daoJson){
        ArrayList<ArrayList> listaListas = initListas();
        try {
            daoJson.addListaClientes(listaListas.get(0));
            daoJson.addListaServicios(listaListas.get(1));
            daoJson.addListaLogs(listaListas.get(2));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        CasinoDAOFileXML dao = new CasinoDAOFileXML();
        CasinoDAOFileJSON daoJson = new CasinoDAOFileJSON();

        System.out.println("=== INICIO DE PRUEBAS ===\n");

        // Test 1: Añadir clientes válidos
        testAddCliente(dao);
        testAddCliente(daoJson);

        // Test 2: Añadir cliente duplicado (debe lanzar excepción)
        testAddClienteDuplicado(dao);
        testAddClienteDuplicado(daoJson);

        // Test 3: Añadir cliente nulo (debe lanzar excepción)
        testAddClienteNulo(dao);
        testAddClienteNulo(daoJson);

        // Test 4: Consultar cliente existente
        testConsultaCliente(dao);
        testConsultaCliente(daoJson);

        // Test 5: Consultar cliente inexistente (debe lanzar excepción)
        testConsultaClienteInexistente(dao);
        testConsultaClienteInexistente(daoJson);

        // Test 6: Añadir servicios
        testAddServicio(dao);
        testAddServicio(daoJson);

        // Test 7: Añadir servicio duplicado
        testAddServicioDuplicado(dao);
        testAddServicioDuplicado(daoJson);

        // Test 8: Consultar servicio
        testConsultaServicio(dao);
        testConsultaServicio(daoJson);

        // Test 9: Añadir logs
        testAddLog(dao);
        testAddLog(daoJson);

        // Test 10: Consultar log
        //testConsultaLog(dao);
        //testConsultaLog(daoJson);

        // Test 11: Actualizar cliente
        testActualizarCliente(dao);
        testActualizarCliente(daoJson);

        // Test 12: Actualizar servicio
        testActualizarServicio(dao);
        testActualizarServicio(daoJson);

        // Test 13: Ganancias de alimentos
        testGananciasAlimentos(dao);
        testGananciasAlimentos(daoJson);

        // Test 14: Dinero invertido por cliente en un día
        testDineroInvertidoClienteEnDia(dao);
        testDineroInvertidoClienteEnDia(daoJson);

        // Test 15: Veces que un cliente juega en una mesa
        testVecesClienteJuegaMesa(dao);
        testVecesClienteJuegaMesa(daoJson);

        // Test 16: Ganado en mesas
        testGanadoMesas(dao);
        testGanadoMesas(daoJson);

        // Test 17: Ganado en establecimientos
        testGanadoEstablecimientos(dao);
        testGanadoEstablecimientos(daoJson);

        // Test 18: Devolver servicios por tipo
        testDevolverServiciosTipo(dao);
        testDevolverServiciosTipo(daoJson);

        // Test 19: Borrar cliente
        testBorrarCliente(dao);
        testBorrarCliente(daoJson);

        // Test 20: Borrar servicio
        testBorrarServicio(dao);
        testBorrarServicio(daoJson);

        // Test 21: Leer listas completas
        testLeerListas(dao);
        testLeerListas(daoJson);

        // Test 22: DNI inválido
        testDNIInvalido(dao);
        testDNIInvalido(daoJson);

        // Test 23: Crear Copia Seguridad
        testCarpetaSeguridad();

        System.out.println("\n=== FIN DE PRUEBAS ===");
    }

    // ===== MÉTODOS DE PRUEBA XML =====

    private static void testAddCliente(CasinoDAOFileXML dao) {
        System.out.println("--- Test 1: Añadir clientes válidos ---");
        try {
            Cliente c1 = new Cliente("12345678Z", "Juan Paco", "García Pérez");
            Cliente c2 = new Cliente("87654321X", "María", "López Sánchez");
            Cliente c3 = new Cliente("11111111H", "Pedro", "Martínez Ruiz");

            dao.addCliente(c1);
            dao.addCliente(c2);
            dao.addCliente(c3);

            System.out.println("Clientes añadidos correctamente\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testAddClienteDuplicado(CasinoDAOFileXML dao) {
        System.out.println("--- Test 2: Añadir cliente duplicado ---");
        try {
            Cliente c1 = new Cliente("12345678Z", "Juan Paco", "García Pérez");
            dao.addCliente(c1);
            System.err.println("ERROR: Debería haber lanzado ClientAlreadyExistsException\n");
        } catch (ClientAlreadyExistsException e) {
            System.out.println("Excepción capturada correctamente: " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.err.println("Excepción incorrecta: " + e.getMessage() + "\n");
        }
    }

    private static void testAddClienteNulo(CasinoDAOFileXML dao) {
        System.out.println("--- Test 3: Añadir cliente nulo ---");
        try {
            dao.addCliente(null);
            System.err.println("ERROR: Debería haber lanzado IllegalArgumentException\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Excepción capturada correctamente: " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.err.println("Excepción incorrecta: " + e.getMessage() + "\n");
        }
    }

    private static void testConsultaCliente(CasinoDAOFileXML dao) {
        System.out.println("--- Test 4: Consultar cliente existente ---");
        try {
            String resultado = dao.consultaCliente("12345678Z");
            System.out.println("Cliente encontrado: " + resultado + "\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testConsultaClienteInexistente(CasinoDAOFileXML dao) {
        System.out.println("--- Test 5: Consultar cliente inexistente ---");
        try {
            dao.consultaCliente("99999999R");
            System.err.println("ERROR: Debería haber lanzado ClientNotFoundException\n");
        } catch (ClientNotFoundException e) {
            System.out.println("Excepción capturada correctamente: " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.err.println("Excepción incorrecta: " + e.getMessage() + "\n");
        }
    }

    private static void testAddServicio(CasinoDAOFileXML dao) {
        System.out.println("--- Test 6: Añadir servicios ---");
        try {
            Servicio s1 = new Servicio(TipoServicio.MESAPOKER, "Mesa Poker VIP");
            Servicio s2 = new Servicio(TipoServicio.MESABLACKJACK, "Mesa BlackJack Premium");
            Servicio s3 = new Servicio(TipoServicio.BAR, "Bar Casino");
            Servicio s4 = new Servicio(TipoServicio.RESTAURANTE, "Restaurante Gourmet");

            dao.addServicio(s1);
            dao.addServicio(s2);
            dao.addServicio(s3);
            dao.addServicio(s4);

            System.out.println("Servicios añadidos correctamente\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    private static void testAddServicioDuplicado(CasinoDAOFileXML dao) {
        System.out.println("--- Test 7: Añadir servicio duplicado ---");
        try {
            List<Servicio> servicios = dao.leerListaServicios();
            if (!servicios.isEmpty()) {
                Servicio servicioDuplicado = servicios.get(0);
                dao.addServicio(servicioDuplicado);
                System.err.println("✗ ERROR: Debería haber lanzado ServiceAlreadyExistsException\n");
            }
        } catch (ServiceAlreadyExistsException e) {
            System.out.println("Excepción capturada correctamente: " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.err.println("Excepción incorrecta: " + e.getMessage() + "\n");
        }
    }

    private static void testConsultaServicio(CasinoDAOFileXML dao) {
        System.out.println("--- Test 8: Consultar servicio ---");
        try {
            List<Servicio> servicios = dao.leerListaServicios();
            if (!servicios.isEmpty()) {
                String codigo = servicios.get(0).getCodigo();
                String resultado = dao.consultaServicio(codigo);
                System.out.println("Servicio encontrado: " + resultado + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testAddLog(CasinoDAOFileXML dao) {
        System.out.println("--- Test 9: Añadir logs ---");
        try {
            List<Cliente> clientes = dao.leerListaClientes();
            List<Servicio> servicios = dao.leerListaServicios();

            if (!clientes.isEmpty() && !servicios.isEmpty()) {
                Cliente cliente = clientes.get(0);
                Servicio mesaPoker = servicios.stream()
                        .filter(s -> s.getTipo() == TipoServicio.MESAPOKER)
                        .findFirst()
                        .orElse(servicios.get(0));

                Servicio bar = servicios.stream()
                        .filter(s -> s.getTipo() == TipoServicio.BAR)
                        .findFirst()
                        .orElse(servicios.get(0));

                // Logs de apuestas
                Log log1 = new Log(cliente, mesaPoker, TipoConcepto.APOSTAR, 100.0);
                Log log2 = new Log(cliente, mesaPoker, TipoConcepto.APUESTACLIENTEGANA, 200.0);
                Log log3 = new Log(cliente, mesaPoker, TipoConcepto.APOSTAR, 50.0);

                // Logs de consumo
                Log log4 = new Log(cliente, bar, TipoConcepto.COMPRABEBIDA, 15.0);
                Log log5 = new Log(cliente, bar, TipoConcepto.COMPRACOMIDA, 35.0);

                dao.addLog(log1);
                dao.addLog(log2);
                dao.addLog(log3);
                dao.addLog(log4);
                dao.addLog(log5);

                System.out.println("Logs añadidos correctamente\n");
            } else {
                System.out.println("No hay clientes o servicios para crear logs\n");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }
/*
    private static void testConsultaLog(CasinoDAOFileXML dao) {
        System.out.println("--- Test 10: Consultar log ---");
        try {
            List<Log> logs = dao.leerListaLog();
            if (!logs.isEmpty()) {
                Log log = logs.get(0);
                String resultado = dao.consultaLog(
                        log.getServicio().getCodigo(),
                        log.getCliente().getDni(),
                        log.getFecha()
                );
                System.out.println("Log encontrado: " + resultado + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

 */

    private static void testActualizarCliente(CasinoDAOFileXML dao) {
        System.out.println("--- Test 11: Actualizar cliente ---");
        try {
            Cliente clienteActualizado = new Cliente("12345678Z", "Juan Carlos", "García Pérez");
            boolean resultado = dao.actualizarCliente("12345678Z", clienteActualizado);
            System.out.println("Cliente actualizado: " + resultado + "\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testActualizarServicio(CasinoDAOFileXML dao) {
        System.out.println("--- Test 12: Actualizar servicio ---");
        try {
            List<Servicio> servicios = dao.leerListaServicios();
            if (!servicios.isEmpty()) {
                Servicio servicio = servicios.get(0);
                Servicio servicioActualizado = new Servicio(
                        servicio.getCodigo(),
                        servicio.getTipo(),
                        "Nombre Actualizado",
                        new ArrayList<>(),
                        servicio.getCapacidadMaxima()
                );
                boolean resultado = dao.actualizarServicio(servicio.getCodigo(), servicioActualizado);
                System.out.println("Servicio actualizado: " + resultado + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testGananciasAlimentos(CasinoDAOFileXML dao) {
        System.out.println("--- Test 13: Ganancias de alimentos ---");
        try {
            double ganancias = dao.gananciasAlimentos("12345678Z");
            System.out.println("Ganancias de alimentos: €" + ganancias + "\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testDineroInvertidoClienteEnDia(CasinoDAOFileXML dao) {
        System.out.println("--- Test 14: Dinero invertido por cliente en un día ---");
        try {
            LocalDate hoy = LocalDate.now();
            double dinero = dao.dineroInvertidoClienteEnDia("12345678Z", hoy);
            System.out.println("Dinero invertido hoy: €" + dinero + "\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testVecesClienteJuegaMesa(CasinoDAOFileXML dao) {
        System.out.println("--- Test 15: Veces que cliente juega en mesa ---");
        try {
            List<Servicio> servicios = dao.leerListaServicios();
            Servicio mesa = servicios.stream()
                    .filter(s -> s.getTipo() == TipoServicio.MESAPOKER || s.getTipo() == TipoServicio.MESABLACKJACK)
                    .findFirst()
                    .orElse(null);

            if (mesa != null) {
                int veces = dao.vecesClienteJuegaMesa("12345678Z", mesa.getCodigo());
                System.out.println("Veces jugadas: " + veces + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testGanadoMesas(CasinoDAOFileXML dao) {
        System.out.println("--- Test 16: Ganado en mesas ---");
        try {
            double ganado = dao.ganadoMesas();
            System.out.println("Total ganado en mesas: €" + ganado + "\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testGanadoEstablecimientos(CasinoDAOFileXML dao) {
        System.out.println("--- Test 17: Ganado en establecimientos ---");
        try {
            double ganado = dao.ganadoEstablecimientos();
            System.out.println("Total ganado en establecimientos: €" + ganado + "\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testDevolverServiciosTipo(CasinoDAOFileXML dao) {
        System.out.println("--- Test 18: Devolver servicios por tipo ---");
        try {
            List<Servicio> mesas = dao.devolverServiciosTipo(TipoServicio.MESAPOKER);
            System.out.println("Servicios de tipo MESAPOKER encontrados: " + mesas.size() + "\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testBorrarCliente(CasinoDAOFileXML dao) {
        System.out.println("--- Test 19: Borrar cliente ---");
        try {
            Cliente cliente = new Cliente("11111111H", "Pedro", "Martínez Ruiz");
            boolean resultado = dao.borrarCliente(cliente);
            System.out.println("Cliente borrado: " + resultado + "\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testBorrarServicio(CasinoDAOFileXML dao) {
        System.out.println("--- Test 20: Borrar servicio ---");
        try {
            List<Servicio> servicios = dao.leerListaServicios();
            if (servicios.size() > 1) {
                Servicio servicio = servicios.get(servicios.size() - 1);
                boolean resultado = dao.borrarServicio(servicio);
                System.out.println("Servicio borrado: " + resultado + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testLeerListas(CasinoDAOFileXML dao) {
        System.out.println("--- Test 21: Leer listas completas ---");
        try {
            List<Cliente> clientes = dao.leerListaClientes();
            List<Servicio> servicios = dao.leerListaServicios();
            List<Log> logs = dao.leerListaLog();

            System.out.println("Total clientes: " + clientes.size());
            System.out.println("Total servicios: " + servicios.size());
            System.out.println("Total logs: " + logs.size() + "\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testDNIInvalido(CasinoDAOFileXML dao) {
        System.out.println("--- Test 22: DNI inválido ---");
        try {
            Cliente c = new Cliente("12345678A", "Test", "Test");
            System.err.println("ERROR: Debería haber lanzado IllegalArgumentException\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Excepción capturada correctamente: " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.err.println("Excepción incorrecta: " + e.getMessage() + "\n");
        }
    }

    // ===== MÉTODOS DE PRUEBA CasinoGestorArchivos =====
    private static void testCarpetaSeguridad(){
        CasinoGestorArchivos cga = new CasinoGestorArchivos();
        Path rutaArchivo = Path.of("src", "main", "resources", "backup");
        try {
            cga.crearCopiaSeguridad(String.valueOf(rutaArchivo));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // ===== MÉTODOS DE PRUEBA JSON =====

    private static void testAddCliente(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 1: Añadir clientes válidos ---");
        try {
            Cliente c1 = new Cliente("12345678Z", "Juan Paco", "García Pérez");
            Cliente c2 = new Cliente("87654321X", "María", "López Sánchez");
            Cliente c3 = new Cliente("11111111H", "Pedro", "Martínez Ruiz");

            dao.addCliente(c1);
            dao.addCliente(c2);
            dao.addCliente(c3);

            System.out.println("Clientes añadidos correctamente\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testAddClienteDuplicado(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 2: Añadir cliente duplicado ---");
        try {
            Cliente c1 = new Cliente("12345678Z", "Juan Paco", "García Pérez");
            dao.addCliente(c1);
            System.err.println("ERROR: Debería haber lanzado ClientAlreadyExistsException\n");
        } catch (ClientAlreadyExistsException e) {
            System.out.println("Excepción capturada correctamente: " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.err.println("Excepción incorrecta: " + e.getMessage() + "\n");
        }
    }

    private static void testAddClienteNulo(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 3: Añadir cliente nulo ---");
        try {
            dao.addCliente(null);
            System.err.println("ERROR: Debería haber lanzado IllegalArgumentException\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Excepción capturada correctamente: " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.err.println("Excepción incorrecta: " + e.getMessage() + "\n");
        }
    }

    private static void testConsultaCliente(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 4: Consultar cliente existente ---");
        try {
            String resultado = dao.consultaCliente("12345678Z");
            System.out.println("Cliente encontrado: " + resultado + "\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testConsultaClienteInexistente(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 5: Consultar cliente inexistente ---");
        try {
            dao.consultaCliente("99999999R");
            System.err.println("ERROR: Debería haber lanzado ClientNotFoundException\n");
        } catch (ClientNotFoundException e) {
            System.out.println("Excepción capturada correctamente: " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.err.println("Excepción incorrecta: " + e.getMessage() + "\n");
        }
    }

    private static void testAddServicio(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 6: Añadir servicios ---");
        try {
            Servicio s1 = new Servicio(TipoServicio.MESAPOKER, "Mesa Poker VIP");
            Servicio s2 = new Servicio(TipoServicio.MESABLACKJACK, "Mesa BlackJack Premium");
            Servicio s3 = new Servicio(TipoServicio.BAR, "Bar Casino");
            Servicio s4 = new Servicio(TipoServicio.RESTAURANTE, "Restaurante Gourmet");

            dao.addServicio(s1);
            dao.addServicio(s2);
            dao.addServicio(s3);
            dao.addServicio(s4);

            System.out.println("Servicios añadidos correctamente\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    private static void testAddServicioDuplicado(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 7: Añadir servicio duplicado ---");
        try {
            List<Servicio> servicios = dao.leerListaServicios();
            if (!servicios.isEmpty()) {
                Servicio servicioDuplicado = servicios.get(0);
                dao.addServicio(servicioDuplicado);
                System.err.println("✗ ERROR: Debería haber lanzado ServiceAlreadyExistsException\n");
            }
        } catch (ServiceAlreadyExistsException e) {
            System.out.println("Excepción capturada correctamente: " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.err.println("Excepción incorrecta: " + e.getMessage() + "\n");
        }
    }

    private static void testConsultaServicio(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 8: Consultar servicio ---");
        try {
            List<Servicio> servicios = dao.leerListaServicios();
            if (!servicios.isEmpty()) {
                String codigo = servicios.get(0).getCodigo();
                String resultado = dao.consultaServicio(codigo);
                System.out.println("Servicio encontrado: " + resultado + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testAddLog(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 9: Añadir logs ---");
        try {
            List<Cliente> clientes = dao.leerListaClientes();
            List<Servicio> servicios = dao.leerListaServicios();

            if (!clientes.isEmpty() && !servicios.isEmpty()) {
                Cliente cliente = clientes.get(0);
                Servicio mesaPoker = servicios.stream()
                        .filter(s -> s.getTipo() == TipoServicio.MESAPOKER)
                        .findFirst()
                        .orElse(servicios.get(0));

                Servicio bar = servicios.stream()
                        .filter(s -> s.getTipo() == TipoServicio.BAR)
                        .findFirst()
                        .orElse(servicios.get(0));

                // Logs de apuestas
                Log log1 = new Log(cliente, mesaPoker, TipoConcepto.APOSTAR, 100.0);
                Log log2 = new Log(cliente, mesaPoker, TipoConcepto.APUESTACLIENTEGANA, 200.0);
                Log log3 = new Log(cliente, mesaPoker, TipoConcepto.APOSTAR, 50.0);

                // Logs de consumo
                Log log4 = new Log(cliente, bar, TipoConcepto.COMPRABEBIDA, 15.0);
                Log log5 = new Log(cliente, bar, TipoConcepto.COMPRACOMIDA, 35.0);

                dao.addLog(log1);
                dao.addLog(log2);
                dao.addLog(log3);
                dao.addLog(log4);
                dao.addLog(log5);

                System.out.println("Logs añadidos correctamente\n");
            } else {
                System.out.println("No hay clientes o servicios para crear logs\n");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }
    /*
    private static void testConsultaLog(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 10: Consultar log ---");
        try {
            List<Log> logs = dao.leerListaLog();
            List<Log> logsCoincidencias = new ArrayList<>();
            if (!logs.isEmpty()) {
                Log log = logs.get(0);
                String resultado = dao.consultaLog(
                        log.getServicio().getCodigo(),
                        log.getCliente().getDni(),
                        log.getFecha()
                );
                System.out.println("Log encontrado: " + resultado + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

     */

    private static void testActualizarCliente(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 11: Actualizar cliente ---");
        try {
            Cliente clienteActualizado = new Cliente("12345678Z", "Juan Carlos", "García Pérez");
            boolean resultado = dao.actualizarCliente("12345678Z", clienteActualizado);
            System.out.println("Cliente actualizado: " + resultado + "\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testActualizarServicio(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 12: Actualizar servicio ---");
        try {
            List<Servicio> servicios = dao.leerListaServicios();
            if (!servicios.isEmpty()) {
                Servicio servicio = servicios.get(0);
                Servicio servicioActualizado = new Servicio(
                        servicio.getCodigo(),
                        servicio.getTipo(),
                        "Nombre Actualizado",
                        new ArrayList<>(),
                        servicio.getCapacidadMaxima()
                );
                boolean resultado = dao.actualizarServicio(servicio.getCodigo(), servicioActualizado);
                System.out.println("Servicio actualizado: " + resultado + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testGananciasAlimentos(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 13: Ganancias de alimentos ---");
        try {
            double ganancias = dao.gananciasAlimentos("12345678Z");
            System.out.println("Ganancias de alimentos: €" + ganancias + "\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testDineroInvertidoClienteEnDia(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 14: Dinero invertido por cliente en un día ---");
        try {
            LocalDate hoy = LocalDate.now();
            double dinero = dao.dineroInvertidoClienteEnDia("12345678Z", hoy);
            System.out.println("Dinero invertido hoy: €" + dinero + "\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testVecesClienteJuegaMesa(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 15: Veces que cliente juega en mesa ---");
        try {
            List<Servicio> servicios = dao.leerListaServicios();
            Servicio mesa = servicios.stream()
                    .filter(s -> s.getTipo() == TipoServicio.MESAPOKER || s.getTipo() == TipoServicio.MESABLACKJACK)
                    .findFirst()
                    .orElse(null);

            if (mesa != null) {
                int veces = dao.vecesClienteJuegaMesa("12345678Z", mesa.getCodigo());
                System.out.println("Veces jugadas: " + veces + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testGanadoMesas(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 16: Ganado en mesas ---");
        try {
            double ganado = dao.ganadoMesas();
            System.out.println("Total ganado en mesas: €" + ganado + "\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testGanadoEstablecimientos(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 17: Ganado en establecimientos ---");
        try {
            double ganado = dao.ganadoEstablecimientos();
            System.out.println("Total ganado en establecimientos: €" + ganado + "\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testDevolverServiciosTipo(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 18: Devolver servicios por tipo ---");
        try {
            List<Servicio> mesas = dao.devolverServiciosTipo(TipoServicio.MESAPOKER);
            System.out.println("Servicios de tipo MESAPOKER encontrados: " + mesas.size() + "\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testBorrarCliente(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 19: Borrar cliente ---");
        try {
            Cliente cliente = new Cliente("11111111H", "Pedro", "Martínez Ruiz");
            boolean resultado = dao.borrarCliente(cliente);
            System.out.println("Cliente borrado: " + resultado + "\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testBorrarServicio(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 20: Borrar servicio ---");
        try {
            List<Servicio> servicios = dao.leerListaServicios();
            if (servicios.size() > 1) {
                Servicio servicio = servicios.get(servicios.size() - 1);
                boolean resultado = dao.borrarServicio(servicio);
                System.out.println("Servicio borrado: " + resultado + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testLeerListas(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 21: Leer listas completas ---");
        try {
            List<Cliente> clientes = dao.leerListaClientes();
            List<Servicio> servicios = dao.leerListaServicios();
            List<Log> logs = dao.leerListaLog();

            System.out.println("Total clientes: " + clientes.size());
            System.out.println("Total servicios: " + servicios.size());
            System.out.println("Total logs: " + logs.size() + "\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void testDNIInvalido(CasinoDAOFileJSON dao) {
        System.out.println("--- Test 22: DNI inválido ---");
        try {
            Cliente c = new Cliente("12345678A", "Test", "Test");
            System.err.println("ERROR: Debería haber lanzado IllegalArgumentException\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Excepción capturada correctamente: " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.err.println("Excepción incorrecta: " + e.getMessage() + "\n");
        }
    }
}