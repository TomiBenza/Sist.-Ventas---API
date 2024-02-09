package ar.edu.utn.frc.tup.lciv.clients;

import ar.edu.utn.frc.tup.lciv.clients.clientes.dtos.ClienteClientResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ClienteClientResponseTest {

    @Test
    public void testClienteClientResponse() {
        ClienteClientResponse cliente = new ClienteClientResponse(1L, "John Doe");

        assertEquals(1L, cliente.getId());
        assertEquals("John Doe", cliente.getNombre());
    }

    @Test
    public void testClienteClientResponseSetter() {
        ClienteClientResponse cliente = new ClienteClientResponse();
        cliente.setId(2L);
        cliente.setNombre("Jane Smith");

        assertEquals(2L, cliente.getId());
        assertEquals("Jane Smith", cliente.getNombre());
    }

    @Test
    public void testClienteClientResponseJsonPropertyAnnotations() throws NoSuchFieldException {
        assertEquals("id", ClienteClientResponse.class.getDeclaredField("id").getAnnotation(JsonProperty.class).value());
        assertEquals("nombre", ClienteClientResponse.class.getDeclaredField("nombre").getAnnotation(JsonProperty.class).value());
    }
}

