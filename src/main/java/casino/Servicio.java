package casino;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Servicio implements Externalizable {
    private String codigo;
    private TipoServicio tipo;
    private String nombreServicio;
    private List<Cliente> listaClientes;
    private int capacidadMaxima;

    //Constructor serializable
    public Servicio() {

    }

    //Constructor para crear Servicio
    public Servicio(TipoServicio tipo, String nombreServicio) {
        this.codigo=generarCodigo();
        setTipo(tipo);
        setNombreServicio(nombreServicio);
        this.listaClientes = new ArrayList<>();
        this.capacidadMaxima = tipo.getCapacidadMaxima();
    }

    //Constructor para leer XML
    public Servicio(String codigo, TipoServicio tipo, String nombreServicio, List<Cliente> listaClientes, int capacidadMaxima) {
        setCodigo(codigo);
        setTipo(tipo);
        setNombreServicio(nombreServicio);
        this.listaClientes = listaClientes;
        this.capacidadMaxima = capacidadMaxima;
    }

    //Hacer comprobación de que el código no existiese ya en la clase CasinoDAOFileXML
    private String generarCodigo(){
        return UUID.randomUUID().toString().substring(0,5).toUpperCase();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == "" | codigo.isEmpty()){
            throw new IllegalArgumentException("El código no puede estar vacío");
        }
        this.codigo = codigo;
    }

    public TipoServicio getTipo() {
        return tipo;
    }

    public void setTipo(TipoServicio tipo) {
        this.tipo = Objects.requireNonNull(tipo, "TipoServicio no puede ser nulo.");
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        Objects.requireNonNull(nombreServicio, "Nombre no puede ser nulo.");
        if (nombreServicio.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre no puede estar vacío.");
        }

        this.nombreServicio = nombreServicio;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    @Override
    public String toString() {
        return "Servicio{" +
                "codigo='" + codigo + '\'' +
                ", tipo=" + tipo +
                ", nombreServicio='" + nombreServicio + '\'' +
                ", listaClientes=" + listaClientes +
                ", capacidadMaxima=" + capacidadMaxima +
                '}';
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    }
}
