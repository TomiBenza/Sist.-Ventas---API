package ar.edu.utn.frc.tup.lciv.clients.clientes;

import ar.edu.utn.frc.tup.lciv.clients.clientes.dtos.ClienteClientResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class ClienteRestClient {
    private static final String RESILIENCE4J_INSTANCE_NAME = "microClientes";
    private static final String FALLBACK_METHOD = "fallback";
    String baseResourceUrl = "https://my-json-server.typicode.com/113974-Olivera-Gustavo/api-clients-bd/clientes";
    @Autowired
    private RestTemplate restTemplate;

    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME, fallbackMethod = FALLBACK_METHOD)
    public ClienteClientResponse getCliente(Long cliente_id) {
        ResponseEntity<ClienteClientResponse[]> response = restTemplate.getForEntity
                (baseResourceUrl +"?id=" + cliente_id, ClienteClientResponse[].class);

        if (response.getStatusCode().is2xxSuccessful() && Objects.requireNonNull(response.getBody()).length>0)
            return response.getBody()[0];
        throw new EntityNotFoundException("El cliente con id "+cliente_id+" no se encontró");
    }

    public ClienteClientResponse fallback(Exception ex) {
        throw new ResponseStatusException(HttpStatusCode.valueOf(503),
                "El servicio de clientes se encuentra desactivado temporalmente, intente luego");
    }
}
