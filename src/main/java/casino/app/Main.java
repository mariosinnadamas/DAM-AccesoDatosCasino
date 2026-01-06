package casino.app;

import casino.dao.CasinoDAO;
import casino.dao.impl.CasinoDAOFileJSON;
import casino.dao.impl.CasinoDAOFileXML;
import casino.model.*;
import exceptions.*;


import java.io.IOException;
import java.time.LocalDate;
import java. time.LocalTime;
import java. util.ArrayList;
import java. util.List;

public class Main {
    public static void main(String[] args) {

        // Puedes cambiar entre JSON y XML aquí
        CasinoDAO dao = new CasinoDAOFileJSON(); // O usar:  new CasinoDAOFileXML()

        try {
            // 1. CREAR CLIENTES
            System.out.println("--- 1. Añadiendo clientes ---");
            Cliente cliente1 = new Cliente("12345678Z", "Juan", "Pérez García");
            Cliente cliente2 = new Cliente("06690442H", "María", "López Martín");

            dao.addCliente(cliente1);
            dao.addCliente(cliente2);
            System.out.println("✓ Clientes añadidos correctamente\n");

            // 2. CONSULTAR CLIENTE
            System.out.println("--- 2. Consultando cliente ---");
            String infoCliente = dao.consultaCliente("12345678Z");
            System. out.println(infoCliente + "\n");

            // 3. CREAR SERVICIOSa
            System.out.println("--- 3. Añadiendo servicios ---");
            List<Cliente> clientesMesa = new ArrayList<>();
            clientesMesa.add(cliente1);

            Servicio mesaPoker = new Servicio( TipoServicio.MESAPOKER,
                    "Mesa Poker VIP");
            Servicio bar = new Servicio(TipoServicio.BAR,
                    "Bar Principal");

            dao.addServicio(mesaPoker);
            dao.addServicio(bar);
            System.out. println("✓ Servicios añadidos correctamente\n");

            // 4. CONSULTAR SERVICIO
            System.out.println("--- 4. Consultando servicio ---");
            String infoServicio = dao.consultaServicio(mesaPoker.getCodigo());
            System.out.println(infoServicio + "\n");

            // 5. CREAR LOGS
            System.out. println("--- 5. Añadiendo logs ---");
            Log log1 = new Log(cliente1, mesaPoker, LocalDate.now(), LocalTime.now(),
                    TipoConcepto.APOSTAR, 50.0);
            Log log2 = new Log(cliente1, bar, LocalDate.now(), LocalTime.now(),
                    TipoConcepto.COMPRABEBIDA, 5.5);
            Log log3 = new Log(cliente1, mesaPoker, LocalDate.now(), LocalTime.now(),
                    TipoConcepto.APUESTACLIENTEGANA, 100.0);

            dao.addLog(log1);
            dao.addLog(log2);
            dao.addLog(log3);
            System.out.println("✓ Logs añadidos correctamente\n");

            // 6. CONSULTAS Y ESTADÍSTICAS
            System.out. println("--- 6. Consultando estadísticas ---");

            double dineroInvertido = dao.dineroInvertidoClienteEnDia("12345678Z", LocalDate.now());
            System.out.println("Dinero invertido hoy por Juan:  " + dineroInvertido + "€");

            double gananciasAlimentos = dao.gananciasAlimentos("12345678Z");
            System.out. println("Gastado en alimentos por Juan: " + gananciasAlimentos + "€");

            int vecesJugado = dao.vecesClienteJuegaMesa("12345678Z", mesaPoker.getCodigo());
            System.out.println("Veces que Juan ha jugado en Mesa Poker: " + vecesJugado);

            double ganadoMesas = dao.ganadoMesas();
            System.out.println("Total ganado por las mesas: " + ganadoMesas + "€");

            double ganadoEstablecimientos = dao.ganadoEstablecimientos();
            System.out.println("Total ganado por establecimientos: " + ganadoEstablecimientos + "€\n");

            // 7. ACTUALIZAR CLIENTE
            System.out.println("--- 7. Actualizando cliente ---");
            Cliente clienteActualizado = new Cliente("12345678Z", "Juan Carlos", "Pérez García");
            dao.actualizarCliente("12345678Z", clienteActualizado);
            System.out.println("✓ Cliente actualizado:  " + dao.consultaCliente("12345678Z") + "\n");

            // 8. LISTAR SERVICIOS POR TIPO
            System.out.println("--- 8. Listando servicios tipo MESAPOKER ---");
            List<Servicio> mesasPoker = dao.devolverServiciosTipo(TipoServicio.MESAPOKER);
            System.out.println("Mesas de poker encontradas: " + mesasPoker.size());
            mesasPoker.forEach(s -> System.out.println("  - " + s.getNombreServicio()));
            System.out.println();

            // 9. LISTAR TODOS LOS CLIENTES
            System.out.println("--- 9. Listando todos los clientes ---");
            List<Cliente> todosClientes = dao.leerListaClientes();
            System.out.println("Total de clientes: " + todosClientes.size());
            todosClientes.forEach(c -> System.out.println("  - " + c.getNombre() + " " + c.getApellidos()));
            System.out.println();


        } catch (ClientAlreadyExistsException e) {
            System.err.println("Error: El cliente ya existe - " + e.getMessage());
        } catch (ServiceAlreadyExistsException e) {
            System.err.println("Error: El servicio ya existe - " + e.getMessage());
        } catch (ClientNotFoundException e) {
            System. err.println("Error: Cliente no encontrado - " + e. getMessage());
        } catch (ServiceNotFoundException e) {
            System. err.println("Error: Servicio no encontrado - " + e.getMessage());
        } catch (LogNotFoundException e) {
            System.err.println("Error: Log no encontrado - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error de entrada/salida - " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Error: Argumento inválido - " + e.getMessage());
        } catch (Exception e){
            System.err.println("Error: Excepción - " + e.getMessage());
        }
    }
}