package casino;

import casino.recursos.DummyGenerator;
import exceptions.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class CasinoMain {
    public static void main(String[] args) {
        CasinoDAOFileXML xml = new CasinoDAOFileXML();
        DummyGenerator dg = new DummyGenerator();

        Cliente c = new Cliente("10155231H", "Prueba", "Pruebez Probatez");
        Cliente cActualizado = new Cliente("10155231H", "PruebaActualizada", "Pruebez Probatez");
        Cliente c2 = new Cliente("22041727E", "Prueba2", "Pruebez2 Probatez2");
        Servicio s = new Servicio(TipoServicio.BAR, "BaretoPruebas");
        Servicio s2 = new Servicio(TipoServicio.MESAPOKER, "Mesa1");
        Log l = new Log(c,s2, TipoConcepto.COMPRABEBIDA,20.0);
        Log l2 = new Log(c,s2, TipoConcepto.COMPRACOMIDA,10.0);
        Log l3 = new Log(c,s2, TipoConcepto.APUESTACLIENTEGANA,20.0);
        Log l4 = new Log(c,s2, TipoConcepto.RETIRADA,30.0);
        Log l5 = new Log(c,s, TipoConcepto.RETIRADA,10.0);

        ArrayList ls = new ArrayList();
        ls.add(s);
        try{
            //xml.addListaClientes(dg.crearListaCliente(50));
            //xml.addListaServicios(dg.crearListaServicio(50));
            //xml.addListaLogs(dg.crearLogs(xml.leerListaClientes(), xml.leerListaServicios(), 100));

            //PRUEBAS CLIENTE
            //Create, si ya existe no lo agrega, FUNCIONA
            //xml.addCliente(c);

            //Read, FUNCIONA
            System.out.println(xml.consultaCliente("10155231H"));
            //Update, FUNCIONA
            xml.actualizarCliente("10155231H", cActualizado);
            for (Cliente temp : xml.leerListaClientes()){
                System.out.println(temp);
            }
            //Delete, funciona
            xml.borrarCliente(c);
            for (Cliente temp : xml.leerListaClientes()){
                System.out.println(temp);
            }

            //PRUEBAS SERVICIO
            //Create
            //Queremos que la comprobación para crear un servicio sea solo por código? o incluyo el nombre tambien? porque al generar el codigo al crear el objeto nunca va a coincidir el código aunque pongas el mismo nombre
            xml.addServicio(s);
            for (Servicio temp : xml.leerListaServicios()){
                System.out.println(temp);
            }
            //Read
            System.out.println(xml.consultaServicio("01E34"));
            //Update, al actualizar cambia el código porque s2 al ser creado crea un objeto nuevo
            xml.actualizarServicio("01E34", s2);
            //Delete, funciona, pero lo del codigo es una movida jajaja
            xml.borrarServicio(s2);
            for (Servicio temp : xml.leerListaServicios()){
                System.out.println(temp);
            }

            //PRUEBAS LOG
            //Create
            xml.addLog(l);
            xml.addLog(l2);
            xml.addLog(l3);
            xml.addLog(l4);
            xml.addLog(l5);




            //xml.addListaLogs(dg.crearListaLogs(xml.leerListaClientes(),xml.leerListaServicios(),20));
            //Read
            //System.out.println(xml.consultaLog("17105", "10155231H",LocalDate.parse("2025-11-09")));

            //PRUEBAS NO CRUD XML
            System.out.println("TOTAL GANANCIAS ALIMENTOS: " + xml.gananciasAlimentos("10155231H"));
            System.out.println("DINERO INVERTIDO DE UN CLIENTE EN UN DÍA: " + xml.dineroInvertidoClienteEnDia("10155231H", LocalDate.parse("2025-11-09")));
            System.out.println("VECES JUGADA UNA MESA: " + xml.vecesClienteJuegaMesa("10155231H", "3B4E9"));
            System.out.println("TOTAL GANADO EN MESAS: " + xml.ganadoMesas());
            System.out.println("TOTAL GANADO EN ESTABLECIMIENTOS: " + xml.ganadoEstablecimientos());
            for (Servicio temp: xml.devolverServiciosTipo(TipoServicio.BAR)){
                System.out.println(temp);
            }
        } catch (ClientNotFoundException | ClientAlreadyExistsException | LogNotFoundException |
                 ServiceNotFoundException | ServiceAlreadyExistsException | TooManyPlayersException e) {
            System.err.println(e.getMessage());
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}
