package ar.edu.utn.frc.tup.lciv.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DescuentoResponse {
    private String descripcion;
    private BigDecimal monto;
}
