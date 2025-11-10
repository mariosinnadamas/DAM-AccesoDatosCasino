package casino.recursos;

import casino.*;

import java.util.ArrayList;
import java.util.List;

public class DummyGenerator {
    public String randomDNI(){
        int randomInt= (int) ((Math.random() * (99999999 - 10000000)) + 10000000);
        String[] letras = {"T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X","B",
                "N", "J", "Z", "S", "Q", "V", "H", "L", "C", "K", "E"};
        String letra = letras[randomInt % 23];
        return String.valueOf(randomInt) + letra;
    }

    public String randomName(){
        String[] namePool = {"Alberto", "Bea", "Carlos", "David", "Ernesto", "Federico", "Guillermo"};
        int randomInt = (int) (Math.random() * (namePool.length));
        return namePool[randomInt];
    }


    public String randomSurname(){
        String[] surnamePool = {"Albertez", "Beaz", "Carlez", "Davidez", "Ernestez", "Federiquez", "Guillermez"};
        int randomInt = (int) (Math.random() * (surnamePool.length));
        return surnamePool[randomInt];
    }


    public TipoServicio randomTipoServicio(){
        int randomInt = (int) (Math.random() * 9);
        TipoServicio tipoServicio;
        if (randomInt == 0){
            tipoServicio = TipoServicio.BAR;
        } else if (randomInt == 1){
            tipoServicio = TipoServicio.RESTAURANTE;
        } else if (randomInt > 1 &&  randomInt <= 5){
            tipoServicio = TipoServicio.MESAPOKER;
        } else {
            tipoServicio = TipoServicio.MESABLACKJACK;
        }
        return tipoServicio;
    }


    public TipoConcepto randomTipoConcepto(){
        String[] conceptoPool = {"APUESTACLIENTEGANA", "COMPRACOMIDA", "COMPRABEBIDA"};
        int randomConcepto = (int) (Math.random() * (conceptoPool.length));
        TipoConcepto tipoConcepto;
        if (randomConcepto == 0){
            tipoConcepto = TipoConcepto.APUESTACLIENTEGANA;
        } else if (randomConcepto == 1){
            tipoConcepto = TipoConcepto.COMPRACOMIDA;
        } else {
            tipoConcepto = TipoConcepto.COMPRABEBIDA;
        }

        return tipoConcepto;
    }


    public List<Cliente> crearListaCliente(int longitudLista){
        List<Cliente> listaClientes = new ArrayList<Cliente>();
        for (int i = 0; i < longitudLista; i++) {
            listaClientes.add(new Cliente(randomDNI(), randomName(), randomSurname() ) );
        }

        return listaClientes;
    }


    public List<Servicio> crearListaServicio(int longitudLista){
        List<Servicio> listaServicio = new ArrayList<>();
        for (int i = 0; i < longitudLista; i++) {
            listaServicio.add(new Servicio(randomTipoServicio(), "Servicio"+(i+1)));
        }
        return listaServicio;
    }


    public List<Log> crearLogs(List<Cliente> listaCliente, List<Servicio> listaServicio, int longitudLista){
        List<Log> listaLogs = new ArrayList<>();

        for (int i = 0; i < longitudLista; i++){
            int randomClienteIndex = (int) (Math.random() * (listaCliente.size()));
            int randomServicioIndex = (int) (Math.random() * (listaServicio.size()));

            Cliente randomCliente = listaCliente.get(randomClienteIndex);
            Servicio randomServicio = listaServicio.get(randomServicioIndex);

            boolean conceptoValido = false;
            TipoConcepto concepto = randomTipoConcepto();
            while (!conceptoValido) {
                concepto = randomTipoConcepto();
                if ((randomServicio.getTipo().equals(TipoServicio.MESAPOKER) ||
                randomServicio.getTipo().equals(TipoServicio.MESABLACKJACK)) &&
                concepto.equals(TipoConcepto.APUESTACLIENTEGANA)) {
                    conceptoValido = true;
                } else if ((randomServicio.getTipo().equals(TipoServicio.RESTAURANTE) ||
                randomServicio.getTipo().equals(TipoServicio.BAR)) && (
                concepto.equals(TipoConcepto.COMPRACOMIDA) ||
                concepto.equals(TipoConcepto.COMPRABEBIDA)
                )) {
                    conceptoValido = true;
                }
            }

            double cantidad;
            if (concepto.equals(TipoConcepto.APUESTACLIENTEGANA)){
                cantidad =  (Math.random() * 1000);
            } else if (concepto.equals(TipoConcepto.COMPRABEBIDA) || concepto.equals(TipoConcepto.COMPRACOMIDA)) {
                cantidad = Math.random() * 25;
            } else {
                cantidad = 0;
            }
            listaLogs.add(new Log (randomCliente, randomServicio, concepto, Math.floor(cantidad)));
        }
        return listaLogs;
    }
}
