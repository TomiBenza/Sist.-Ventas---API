package ar.edu.utn.frc.tup.lciv.dtos.DetallePresupuesto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetallePresupuestoRequest {
    @NotNull
    private String cod_producto;
    private String descripcion;
    @NotNull
    private Integer cantidad;
    @NotNull@Positive
    private BigDecimal precio_unitario;
}
