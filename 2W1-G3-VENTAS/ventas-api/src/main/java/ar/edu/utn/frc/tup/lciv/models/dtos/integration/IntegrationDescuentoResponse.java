package ar.edu.utn.frc.tup.lciv.models.dtos.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntegrationDescuentoResponse {
    private BigDecimal monto;
    private String descripcion;
}
