package casino;

import casino.model.Cliente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    Cliente c;

    @Order(1)
    @Test
    void validarDni() {
        Assertions.assertTrue(Cliente.validarDni("12345678Z"));
        Assertions.assertFalse(Cliente.validarDni("12345678"));
    }
}