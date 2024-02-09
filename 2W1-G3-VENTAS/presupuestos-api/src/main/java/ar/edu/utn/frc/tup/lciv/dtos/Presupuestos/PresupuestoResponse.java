package ar.edu.utn.frc.tup.lciv.dtos.Presupuestos;

import ar.edu.utn.frc.tup.lciv.dtos.DetallePresupuesto.DetallePresupuestoRequest;
import ar.edu.utn.frc.tup.lciv.dtos.DetallePresupuesto.DetallePresupuestoResponse;
import ar.edu.utn.frc.tup.lciv.entities.DetallePresupuestoEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data@AllArgsConstructor@NoArgsConstructor
public class PresupuestoResponse {
    private Long id;
    private Long doc_cliente;
    private Integer tipo_venta;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime fecha_creacion;
    private List<DetallePresupuestoResponse> detalles;
    private BigDecimal precio_total;
}
