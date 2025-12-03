package casino.app;

import casino.db.CasinoDAODB;
import casino.model.Cliente;

import java.io.IOException;
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

        try {
            dao.addClientes(clientes);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
