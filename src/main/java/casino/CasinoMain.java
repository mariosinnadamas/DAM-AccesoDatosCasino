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

        //xml.addListaCliente(dg.crearListaCliente(50));
        //xml.addListaServicios(dg.crearListaServicio(50));
        //xml.addListaLogs(dg.crearListaLogs(xml.leerListaClientes(), xml.leerListaServicios(), 100));
        //xml.addLog(l);

        try{
            //System.out.println(xml.consultaCliente("10155231H"));
            //ESTA ROTO, ME QUIERO MORIIIIIIIIIIIIR (LO ESTOY ARREGLANDO LO JURO)
            xml.actualizarCliente("10155231H", cActualizado);
        } catch (ClientNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
