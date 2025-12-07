package casino.util;

import casino.model.*;
import exceptions.ClientAlreadyExistsException;

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
        String[] namePool = {"Alberto", "Bea", "Carlos", "David", "Elena", "Federico", "Guillermo",
                "Hugo", "Jaime", "Kylian", "Miguel", "Nicolas", "Olivia", "Paula", "Rodrigo",
                "Sofia", "Teresa", "Ursula", "Vicente", "Yolanda"};
        int randomInt = (int) (Math.random() * (namePool.length));
        return namePool[randomInt];
    }


    public String randomSurname(){
        String[] surnamePool = {"Alvarez", "Blanco", "Cruz", "Diaz", "Espinoza", "Fernandez", "Garcia",
                "Herrera", "Ibaéz", "Jimenez", "Lopez", "Navarro", "Ortiz", "Perez", "Rodriguez", "Sanchez",
                "Torres", "Uribe", "Vargas", "Ximenez", "Zapata"};
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
        String[] conceptoPool = {"APOSTAR", "RETIRADA","APUESTACLIENTEGANA", "COMPRACOMIDA", "COMPRABEBIDA"};
        int randomConcepto = (int) (Math.random() * (conceptoPool.length));
        TipoConcepto tipoConcepto;
        if (randomConcepto == 0){
            tipoConcepto = TipoConcepto.APUESTACLIENTEGANA;
        } else if (randomConcepto == 1){
            tipoConcepto = TipoConcepto.APOSTAR;
        } else if (randomConcepto == 2){
            tipoConcepto = TipoConcepto.COMPRABEBIDA;
        } else if (randomConcepto == 3){
            tipoConcepto = TipoConcepto.COMPRACOMIDA;
        } else {
            tipoConcepto  = TipoConcepto.RETIRADA;
        }
        return tipoConcepto;
    }


    public List<Cliente> crearListaCliente(int longitudLista){
        List<Cliente> listaClientes = new ArrayList<>();
        for (int i = 0; i < longitudLista; i++) {
            while (true) {
                try {
                    Cliente cli = new Cliente(randomDNI(), randomName(), randomSurname());
                    if (listaClientes.contains(cli)) {
                        System.out.println();
                        throw new ClientAlreadyExistsException("Cliente Existente");
                    } else {
                        listaClientes.add(cli);
                        break;
                    }
                } catch (ClientAlreadyExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
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

    public List<Cliente> diluirListaCliente(int dilu, ArrayList<Cliente> listaClientes) {
        List<Cliente> listaDiluida = new ArrayList<>();
        while (listaDiluida.size() < dilu) {
            try {
                int randomInt = (int) (Math.random() * (listaClientes.size()));
                Cliente cli =  listaClientes.get(randomInt);
                if (listaDiluida.contains(cli)) {
                    throw new ClientAlreadyExistsException("Cliente Existente");
                } else {
                    listaDiluida.add(cli);
                }
            } catch (ClientAlreadyExistsException e) {
                System.out.println(e.getMessage());
            }
        }
        return listaDiluida;
    }

    public List<Servicio> crearListaServicioClientes(int longitudLista, ArrayList<Cliente> listaClientes) {
        List<Servicio> listaServicio = new ArrayList<>();
        for (int i = 0; i < longitudLista; i++) {
            Servicio ser = new Servicio(randomTipoServicio(), "Servicio"+(i+1));
            int randomNum = (int) (Math.random() * (5));
            ArrayList<Cliente> listaDiluida = (ArrayList<Cliente>) diluirListaCliente(randomNum, listaClientes);
            ser.setListaClientes(listaDiluida);
            listaServicio.add(ser);
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
                if (
                        (
                                randomServicio.getTipo().equals(TipoServicio.MESAPOKER) ||
                                        randomServicio.getTipo().equals(TipoServicio.MESABLACKJACK)
                        ) && (
                                concepto.equals(TipoConcepto.APUESTACLIENTEGANA) ||
                                        concepto.equals(TipoConcepto.APOSTAR) ||
                                        concepto.equals(TipoConcepto.RETIRADA)
                                )
                ) {
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
            if (concepto.equals(TipoConcepto.APUESTACLIENTEGANA)) {
                cantidad =  (Math.random() * 500);
            } else if (concepto.equals(TipoConcepto.APOSTAR)) {
                cantidad =  (Math.random() * 1000);
            } else if (concepto.equals(TipoConcepto.COMPRABEBIDA) || concepto.equals(TipoConcepto.COMPRACOMIDA)) {
                cantidad = Math.random() * 25;
            } else {
                cantidad = 0;
            }
            listaLogs.add(new Log (randomCliente, randomServicio, concepto, Math.floor(cantidad)+1));
        }
        return listaLogs;
    }
}
