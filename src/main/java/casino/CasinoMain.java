package casino;

public class CasinoMain {
    public static void main(String[] args) {
        CasinoDAOFileXML xml = new CasinoDAOFileXML();
        Cliente c = new Cliente("10155231H", "Prueba", "Pruebez Probatez");
        Cliente c2 = new Cliente("22041727E", "Prueba2", "Pruebez2 Probatez2");
        //xml.addCliente(c);
        xml.addCliente(c2);
    }
}
