package casino.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@XmlRootElement(name = "Log")
@XmlType(propOrder = {"cliente", "servicio", "fechaStr", "horaStr", "concepto", "cantidadConcepto"})
public class Log implements Externalizable {
    private Cliente cliente;
    private Servicio servicio;
    @XmlTransient
    private LocalDate fecha;

    @XmlTransient
    private LocalTime hora;

    private TipoConcepto concepto;
    private double cantidadConcepto;

    //Constructor vacío para JSON (Serialización) y JAXB
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

    @XmlElement
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        if (cliente == null){
            throw new IllegalArgumentException("Cliente no puede ser null");
        }
        this.cliente = cliente;
    }

    @XmlElement
    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        if (servicio == null){
            throw new IllegalArgumentException("Servicio no puede ser null");
        }
        this.servicio = servicio;
    }

    @XmlTransient
    public LocalDate getFecha() {
        return fecha;
    }

    //Getter para no tener que adaptar el XML ya que no puede guardar LocalDate
    @XmlElement(name = "fecha")
    public String getFechaStr(){
        return (fecha !=null) ? fecha.toString(): "";
    }

    public void setFecha(LocalDate fecha) {
        if (fecha.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("La fecha no puede ser futura");
        }
        this.fecha = fecha;
    }

    //Setter para convertir de String a LocalDate
    public void setFechaStr(String fechaStr){
        if (fechaStr != null && !fechaStr.isBlank()){
            this.fecha = LocalDate.parse(fechaStr);
        }
    }

    @XmlTransient
    public LocalTime getHora() {
        return hora;
    }

    //Getter para no tener que adaptar el XML ya que no puede guardar LocalTime
    @XmlElement(name = "hora")
    public String getHoraStr(){
        if(hora != null){
            DateTimeFormatter formateo = DateTimeFormatter.ofPattern("HH:mm:ss");
            return hora.format(formateo);
        }
        return "";
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    //Setter para convertir de String a LocalTime
    public void setHoraStr(String horaStr){
        if (horaStr != null && !horaStr.isBlank()){
            this.hora = LocalTime.parse(horaStr);
        }
    }

    @XmlElement
    public TipoConcepto getConcepto() {
        return concepto;
    }

    public void setConcepto(TipoConcepto concepto) {
        if (concepto == null){
            throw new IllegalArgumentException("Concepto no puede ser null");
        }
        this.concepto = concepto;
    }

    @XmlElement
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Log log)) return false;
        return Double.compare(cantidadConcepto, log.cantidadConcepto) == 0 && Objects.equals(cliente, log.cliente) && Objects.equals(servicio, log.servicio) && Objects.equals(fecha, log.fecha) && Objects.equals(hora, log.hora) && concepto == log.concepto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cliente, servicio, fecha, hora, concepto, cantidadConcepto);
    }
}
