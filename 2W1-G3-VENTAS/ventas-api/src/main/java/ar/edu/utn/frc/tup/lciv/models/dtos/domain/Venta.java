package ar.edu.utn.frc.tup.lciv.models.dtos.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Component
@Data@AllArgsConstructor@NoArgsConstructor
public class Venta {
    private Long id;
    private LocalDateTime fecha;
    private Long id_cliente;
    private int tipo_venta;
    private int forma_entrega;
    private LocalDateTime fecha_entrega;
    private Long id_vendedor;
    private List<DetalleVenta> detalles;
    private BigDecimal total;
    private int estado;
}
