package casino;

import casino.recursos.DummyGenerator;

import java.time.LocalDate;
import java.util.ArrayList;

public class JsonMain {
    public static void main(String[] args) {
        DummyGenerator dg = new DummyGenerator();
//        ArrayList<Cliente> testListaClientes = (ArrayList<Cliente>) dg.crearListaCliente(4);
//        ArrayList<Servicio> testListaServicio =  (ArrayList<Servicio>) dg.crearListaServicio(5);
//        ArrayList<Log> testListaLog = (ArrayList<Log>) dg.crearLogs(testListaClientes, testListaServicio, 50);

        CasinoDAOFileJSON Daojson = new CasinoDAOFileJSON();
//        Daojson.addCliente(testListaClientes);
//        Daojson.addServicio(testListaServicio);
//        Daojson.addLog(testListaLog);

        System.out.println(Daojson.dineroGanadoClienteEnDia("88832710V", LocalDate.of(2025, 11, 7)));

    }
}
