package casino.db;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConexionDBTest {
    ConexionDB conexionDB = new ConexionDB("casinotest");

    @Test
    void conectarBaseDatos() {
        assertDoesNotThrow(() -> conexionDB.conectarBaseDatos () );
    }
}