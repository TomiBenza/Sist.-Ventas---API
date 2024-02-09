package ar.edu.utn.frc.tup.lciv.models.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data@NoArgsConstructor@AllArgsConstructor
public class VentaFiltroRequest {
    private Long id;
    private Long doc_cliente;
    private Long id_vendedor;
    private LocalDateTime fecha_desde;
    private LocalDateTime fecha_hasta;
    private Integer forma_entrega;
    private Integer tipo_venta;
    private BigDecimal monto_desde;
    private BigDecimal monto_hasta;
    private Integer estado;
}
