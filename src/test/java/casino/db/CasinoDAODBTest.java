package casino.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class CasinoDAODBTest {
    public void init() throws SQLException {
        ConexionDB conexionDB =  new ConexionDB();
        String consulta =
                "DROP SCHEMA if EXISTS casinotest cascade; " +
                        "CREATE SCHEMA if not exists casinotest; " +
                        "set search_path to casinotest; " +
                        "CREATE TABLE if not exists clientes (" +
                        "dni varchar(9) primary key," +
                        "nombre varchar(50) not null," +
                        "apellido varchar (50) not null" +
                        ");" +
                        "create table if not exists servicios (" +
                        "codigo varchar(5) primary key," +
                        "nombre varchar (50) not null," +
                        "tipo varchar (50) not null," +
                        "capacidad int not null" +
                        "); " +
                        "create table if not exists logs (" +
                        "dni varchar(9)," +
                        "codigo varchar(5)," +
                        "fecha date not null," +
                        "hora time not null," +
                        "concepto varchar (50) not null, " +
                        "cantidad_concepto decimal(10,2) not null, " +
                        "foreign key (dni) references clientes(dni), " +
                        "foreign key (codigo) references servicios(codigo) " +
                        "); " +
                        "create table if not exists lista_clientes(" +
                        "id_lista serial primary key, " +
                        "dni varchar (9) not null, " +
                        "codigo varchar(5) not null, " +
                        "foreign key (dni) references clientes(dni), " +
                        "foreign key (codigo) references servicios(codigo) " +
                        "); " +
                        "INSERT INTO clientes(dni, nombre, apellido) VALUES" +
                        "('12345678Z', 'Juan Paco', 'García Pérez'), " +
                        "('87654321X', 'María', 'López Sánchez');" +
                        "INSERT INTO servicios(codigo, nombre, tipo, capacidad) VALUES" +
                        "('10908', 'Mesa Poker VIP', 'MESAPOKER', 10)," +
                        "('F4AC5', 'Mesa BlackJack', 'MESABLACKJACK', 7)," +
                        "('64B52', 'Bar Casino', 'BAR', 20);" +
                        "INSERT INTO logs(dni, codigo, fecha, hora, concepto, cantidad_concepto) VALUES" +
                        "('12345678Z', '10908', '2025-11-29', '13:14:44', 'APOSTAR', 100.0)," +
                        "('12345678Z', '10908', '2025-11-29', '13:14:44', 'APUESTACLIENTEGANA', 200.0)," +
                        "('12345678Z', '10908', '2025-11-29', '13:14:44', 'APOSTAR', 15.0)," +
                        "('12345678Z', '64B52', '2025-11-29', '13:14:44', 'COMPRABEBIDA', 15.0)," +
                        "('12345678Z', '64B52', '2025-11-29', '13:14:44', 'COMPRACOMIDA', 35.0);";
        Connection con = conexionDB.conectarBaseDatos();

        PreparedStatement stm = con.prepareStatement(consulta);

        stm.executeUpdate();
    }

    CasinoDAODB daodb = new CasinoDAODB("casinotest");

    @BeforeEach
    void setUp() {
        try {
            init();
        } catch (SQLException e) {
            System.out.println("Error en la conexión del test");
        }
    }

    @Test
    void addCliente() {
    }

    @Test
    void addServicio() {
    }

    @Test
    void addLog() {
    }

    @Test
    void consultaServicio() {
    }

    @Test
    void leerListaServicios() {
    }

    @Test
    void consultaCliente() {
    }

    @Test
    void leerListaClientes() {
    }

    @Test
    void consultaLog() {
    }

    @Test
    void leerListaLog() {
    }

    @Test
    void actualizarServicio() {
    }

    @Test
    void actualizarCliente() {
    }

    @Test
    void borrarServicio() {
    }

    @Test
    void borrarCliente() {
    }

    @Test
    void gananciasAlimentos() {
    }

    @Test
    void dineroInvertidoClienteEnDia() {
    }

    @Test
    void vecesClienteJuegaMesa() {
    }

    @Test
    void ganadoMesas() {
    }

    @Test
    void ganadoEstablecimientos() {
    }

    @Test
    void devolverServiciosTipo() {
    }
}