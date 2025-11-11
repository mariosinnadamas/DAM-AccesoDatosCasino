package casino;

import casino.recursos.DummyGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

public class JsonMain {
    public static ArrayList<ArrayList> initListas(){
        DummyGenerator dg = new DummyGenerator();
        ArrayList<Cliente> testListaClientes = (ArrayList<Cliente>) dg.crearListaCliente(10);
        ArrayList<Servicio> testListaServicio =  (ArrayList<Servicio>) dg.crearListaServicio(10);
        ArrayList<Log> testListaLog = (ArrayList<Log>) dg.crearLogs(testListaClientes, testListaServicio, 5000);

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

    public static void borrarArchvios(CasinoDAOFileJSON daojson){
        try (FileWriter fwc = new FileWriter(daojson.fileCliente)) {
            fwc.write("");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        try (FileWriter fws = new FileWriter(daojson.fileServicio)){
            fws.write("");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        try (FileWriter fwl = new FileWriter(daojson.fileLog)){
            fwl.write("");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        CasinoDAOFile daofile = new CasinoDAOFile();
        Paths prueba = Path.of("src", "main", "java", "casino", "prueba");

    }
}
