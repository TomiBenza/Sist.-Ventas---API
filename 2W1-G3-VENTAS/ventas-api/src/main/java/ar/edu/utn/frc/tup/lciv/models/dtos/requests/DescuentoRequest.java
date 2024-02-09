package ar.edu.utn.frc.tup.lciv.models.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DescuentoRequest {
    @NotNull(message = "Ingresar una descripción para el descuento")
    private String descripcion;
    @NotNull(message = "Ingresar un monto para el descuento")
    private BigDecimal monto;
}
