package ar.edu.utn.frc.tup.lciv.dtos.DetallePresupuesto;

import ar.edu.utn.frc.tup.lciv.entities.PresupuestoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data@AllArgsConstructor@NoArgsConstructor
public class DetallePresupuestoResponse {

    private String cod_producto;

    private String descripcion;

    private Integer cantidad;

    private BigDecimal precio_unitario;
}
