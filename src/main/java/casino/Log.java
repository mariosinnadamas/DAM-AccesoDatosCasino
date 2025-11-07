package casino;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.LocalDate;
import java.time.LocalTime;

public class Log implements Externalizable {
    private Cliente cliente;
    private Servicio servicio;
    private LocalDate fecha;
    private LocalTime hora;
    private TipoConcepto concepto;
    private double cantidadConcepto;

    //Constructor vacío para JSON (Serialización)
    public Log() {
    }

    //Constructor sin fecha y hora para crear log
    public Log(Cliente cliente, Servicio servicio, TipoConcepto concepto, double cantidadConcepto) {
        setCliente(cliente);
        setServicio(servicio);
        setConcepto(concepto);
        setCantidadConcepto(cantidadConcepto);
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
    }

    //Igual es necesario un constructor completo para la lectura del JSON/XML
    public Log(Cliente cliente, Servicio servicio, LocalDate fecha, LocalTime hora, TipoConcepto concepto, double cantidadConcepto) {
        setCliente(cliente);
        setServicio(servicio);
        setConcepto(concepto);
        setCantidadConcepto(cantidadConcepto);
        this.fecha = fecha;
        this.hora = hora;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        if (cliente == null){
            throw new IllegalArgumentException("Cliente no puede ser null");
        }
        this.cliente = cliente;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        if (servicio == null){
            throw new IllegalArgumentException("Servicio no puede ser null");
        }
        this.servicio = servicio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        if (fecha.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("La fecha no puede ser futura");
        }
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public TipoConcepto getConcepto() {
        return concepto;
    }

    public void setConcepto(TipoConcepto concepto) {
        if (concepto == null){
            throw new IllegalArgumentException("Concepto no puede ser null");
        }
        this.concepto = concepto;
    }

    public double getCantidadConcepto() {
        return cantidadConcepto;
    }

    public void setCantidadConcepto(double cantidadConcepto) {
        if (cantidadConcepto <= 0){
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        this.cantidadConcepto = cantidadConcepto;
    }

    @Override
    public String toString() {
        return "Log{" +
                "cliente=" + cliente +
                ", servicio=" + servicio +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", concepto=" + concepto +
                ", cantidadConcepto=" + cantidadConcepto +
                '}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(cliente);
        out.writeObject(servicio);
        out.writeObject(fecha);
        out.writeObject(hora);
        out.writeObject(concepto);
        out.writeDouble(cantidadConcepto);

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.cliente = (Cliente) in.readObject();
        this.servicio = (Servicio) in.readObject();
        this.fecha = (LocalDate) in.readObject();
        this.hora = (LocalTime) in.readObject();
        this.concepto = (TipoConcepto) in.readObject();
        this.cantidadConcepto = in.readDouble();

    }
}
