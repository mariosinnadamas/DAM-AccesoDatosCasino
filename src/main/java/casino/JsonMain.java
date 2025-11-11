package casino;

import casino.recursos.DummyGenerator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class JsonMain {
    public static ArrayList<ArrayList> initListas(){
        DummyGenerator dg = new DummyGenerator();
        ArrayList<Cliente> testListaClientes = (ArrayList<Cliente>) dg.crearListaCliente(20);
        ArrayList<Servicio> testListaServicio =  (ArrayList<Servicio>) dg.crearListaServicio(20);
        ArrayList<Log> testListaLog = (ArrayList<Log>) dg.crearLogs(testListaClientes, testListaServicio, 400);

        ArrayList<ArrayList> listaListas = new ArrayList<>();
        listaListas.add(testListaClientes);
        listaListas.add(testListaServicio);
        listaListas.add(testListaLog);

        return listaListas;

    }

    public static void initDaojson(CasinoDAOFileJSON daojson, ArrayList<ArrayList> lista) throws IOException {
        daojson.addCliente(lista.get(0));
        daojson.addServicio(lista.get(1));
        daojson.addLog(lista.get(2));
    }

    public static void main(String[] args) throws IOException {

        CasinoDAOFileJSON daojson = new CasinoDAOFileJSON();
        ArrayList<ArrayList> listas = initListas();
        initDaojson(daojson, listas);

        System.out.println(daojson.ganadoMesas());
        System.out.println(daojson.ganadoEstablecimientos());

    }
}
