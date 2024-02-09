package ar.edu.utn.frc.tup.lciv.models.dtos.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data@NoArgsConstructor@AllArgsConstructor
public class IntegrationDetalleVentaResponse {
    private String cod_producto;
    private String descripcion;
    private BigDecimal precio_unitario;
    private Integer cantidad;
}
