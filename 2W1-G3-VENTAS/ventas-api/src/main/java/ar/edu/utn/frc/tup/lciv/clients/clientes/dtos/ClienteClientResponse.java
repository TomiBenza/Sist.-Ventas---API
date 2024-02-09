package ar.edu.utn.frc.tup.lciv.clients.clientes.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor@NoArgsConstructor
public class ClienteClientResponse{
    @JsonProperty("id")
    private Long id;
    @JsonProperty("nombre")
    private String nombre;
}
