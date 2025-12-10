package casino.app;

import casino.db.CasinoDAODB;
import casino.model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class CanelaTest {
    public static void main(String[] args) {
        CasinoDAODB dao = new CasinoDAODB("casinotest");
        Cliente cli001 = new Cliente("91390590K", "Paquito", "Paquitez");
        Cliente cli002 = new Cliente("33683162F", "Alberto", "Albertez");
        Cliente cli003 = new Cliente("63454192K", "Josete", "Josetez");
        Cliente cli004 = new Cliente("60680053G", "Tobias", "Trembolona");

        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        clientes.add(cli001);
        clientes.add(cli002);
        clientes.add(cli003);
        clientes.add(cli004);

        Servicio ser001 = new Servicio("10908", TipoServicio.MESAPOKER,"Mesa Poker VIP", clientes, 10);
        Servicio ser002 = new Servicio("F4AC5",TipoServicio.MESABLACKJACK,"Mesa BlackJack Premium", clientes, 7);
        Servicio ser003 = new Servicio("64B52", TipoServicio.BAR, "Bar Casino" ,clientes ,20);
        Servicio ser004 = new Servicio("AAAAA", TipoServicio.BAR, "Bar Casino" ,clientes ,20);
        ArrayList<Servicio> servicios = new ArrayList<>();

        ser004.setListaClientes(clientes);

        ser004.setNombreServicio("Servicio actualizado");
        Log log001 = new Log(cli001, ser004, TipoConcepto.APOSTAR, 20.0);
        try {
            dao.actualizarServicio("AAAAA", ser004);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
