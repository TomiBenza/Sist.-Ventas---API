package ar.edu.utn.frc.tup.lciv.clients.inventario;

import ar.edu.utn.frc.tup.lciv.clients.clientes.dtos.ClienteClientResponse;
import ar.edu.utn.frc.tup.lciv.clients.inventario.dtos.ReservaProductoRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class InventarioRestClient {
    private static final String RESILIENCE4J_INSTANCE_NAME = "microInventario";
    private static final String FALLBACK_METHOD = "fallback";
    String baseResourceUrl = "";
    @Autowired
    private RestTemplate restTemplate;


    private Integer counter = 0;
    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME, fallbackMethod = FALLBACK_METHOD)
    public Long postReservaProducto(ReservaProductoRequest reserva) {
        counter += 1;
        return counter.longValue();
        /*ResponseEntity<Long> response = restTemplate.postForEntity(baseResourceUrl,reserva,Long.class);

        if (response.getStatusCode().is2xxSuccessful() )
            return response.getBody();
        throw new IllegalArgumentException("Error al reservar los productos");*/
    }

    public Long fallback(Exception ex) {
        throw new ResponseStatusException(HttpStatusCode.valueOf(503),
                "El servicio de inventario no se encuentra disponible, intente en otro momento");
    }
}
