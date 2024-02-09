package ar.edu.utn.frc.tup.lciv.dtos.Presupuestos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data @AllArgsConstructor @NoArgsConstructor
public class PresupuestosParametros {
    private Long id;
    private Long doc_cliente;
    private LocalDateTime fecha_desde;
    private LocalDateTime fecha_hasta;
    private BigDecimal monto_desde;
    private BigDecimal monto_hasta;
    private Integer tipo_venta;
}
