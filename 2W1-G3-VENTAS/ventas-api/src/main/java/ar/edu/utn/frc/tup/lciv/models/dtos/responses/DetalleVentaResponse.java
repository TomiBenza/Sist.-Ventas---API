package ar.edu.utn.frc.tup.lciv.models.dtos.responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DetalleVentaResponse {
    private String cod_producto;
    private String descripcion;
    private BigDecimal precio_unitario;
    private Integer cantidad;
}
