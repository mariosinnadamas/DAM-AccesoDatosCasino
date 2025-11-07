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
        Cliente c = new Cliente("10155231H", "Prueba", "Pruebez Probatez");
        Cliente c2 = new Cliente("22041727E", "Prueba2", "Pruebez2 Probatez2");
        Servicio s = new Servicio(TipoServicio.BAR, "BaretoPruebas");
        Servicio s2 = new Servicio(TipoServicio.MESAPOKER, "Mesa1");
        Log l = new Log(c,s2, TipoConcepto.DARSEDEALTA,20.0);
        Log l2 = new Log(c2,s, TipoConcepto.DARSEDEALTA,30.0);


        try{
            System.out.println(xml.consultaCliente("06690442H"));
        } catch (ClientNotFoundException e) {
            System.err.println(e.getMessage());
        }



        //xml.addCliente(c);
        //xml.addCliente(c2);
        //xml.addServicio(s2);
        //xml.addLog(l2);

    }
}
