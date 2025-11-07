package casino;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;

@XmlRootElement(name = "cliente")
@XmlType(propOrder = {"dni", "nombre", "apellidos"})
public class Cliente implements Externalizable {
    private String dni;
    private String nombre;
    private String apellidos;

    //Constructor vacío obligatorio para JAXB
    public Cliente() {
    }

    public Cliente(String dni, String nombre, String apellidos){
        setDni(dni);
        setNombre(nombre);
        setApellidos(apellidos);
    }
    @XmlElement
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        if (dni == null) {
            throw new IllegalArgumentException("ERROR: El DNI no puede estar vacio");
        }

        dni = dni.trim().toUpperCase();

        if(!validarDni(dni)){
            throw new IllegalArgumentException("ERROR: DNI no valido");
        }
        this.dni = dni;
    }

    @XmlElement
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre no puede estar vacío o ser nulo.");
        }
        this.nombre = nombre;
    }

    @XmlElement
    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        if (apellidos == null || apellidos.trim().isEmpty()) {
            throw new IllegalArgumentException("Apellidos no puede estar vacío o ser nulo.");
        }
        this.apellidos = apellidos;
    }

    private boolean validarDni(String dni){
        String[] letras = {"T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X","B",
                "N", "J", "Z", "S", "Q", "V", "H", "L", "C", "K", "E"};

        if (!dni.matches("^[0-9]{8}[A-Z]$")){
            return false;
        }

        String let = dni.substring(dni.length() -1);
        String numeros = dni.replaceAll("[^0-9]", "");
        int resto = Integer.parseInt(numeros) % 23;

        return let.equals(letras[resto]);
    }

    @Override
    public boolean equals(Object obj){

        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        Cliente cliente = (Cliente) obj;
        return Objects.equals(dni, cliente.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(dni);
        out.writeUTF(nombre);
        out.writeUTF(apellidos);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.dni = in.readUTF();
        this.nombre = in.readUTF();
        this.apellidos = in.readUTF();
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                '}';
    }
}
