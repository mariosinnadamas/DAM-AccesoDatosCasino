package casino;

import casino.recursos.DummyGenerator;
import exceptions.ClientNotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CasinoMain {
    public static void main(String[] args) {
        CasinoDAOFileXML xml = new CasinoDAOFileXML();
        DummyGenerator dg = new DummyGenerator();

        Cliente c = new Cliente("10155231H", "Prueba", "Pruebez Probatez");
        Cliente cActualizado = new Cliente("10155231H", "PruebaActualizada", "Pruebez Probatez");
        Cliente c2 = new Cliente("22041727E", "Prueba2", "Pruebez2 Probatez2");
        Servicio s = new Servicio(TipoServicio.BAR, "BaretoPruebas");
        Servicio s2 = new Servicio(TipoServicio.MESAPOKER, "Mesa1");
        Log l = new Log(c,s2, TipoConcepto.DARSEDEALTA,20.0);
        Log l2 = new Log(c2,s, TipoConcepto.DARSEDEALTA,30.0);

        //xml.addListaClientes(dg.crearListaCliente(10));
        //xml.addListaServicios(dg.crearListaServicio(10));
        //xml.addListaLogs(dg.crearListaLogs(xml.leerListaClientes(), xml.leerListaServicios(), 100));

        try{
            //PRUEBAS CLIENTE
            //Create, si ya existe no lo agrega, FUNCIONA
            xml.addCliente(c);

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
            //TODO: Queremos que la comprobación para crear un servicio sea solo por código? o incluyo el nombre tambien? porque al generar el codigo al crear el objeto nunca va a coincidir el código aunque pongas el mismo nombre
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
        } catch (ClientNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
