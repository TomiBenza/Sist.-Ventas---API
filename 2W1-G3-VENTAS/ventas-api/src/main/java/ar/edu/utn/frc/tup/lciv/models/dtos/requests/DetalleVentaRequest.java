package ar.edu.utn.frc.tup.lciv.models.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleVentaRequest {
    @NotNull
    private String cod_producto; //Si es descuento guardar "Desc"
    @NotNull
    private String descripcion; //Si es decuento guardar "Descuento por cliente categoría <tipoCategoría>"
    @NotNull
    private BigDecimal precio_unitario;
    @NotNull
    private Integer cantidad;
}
