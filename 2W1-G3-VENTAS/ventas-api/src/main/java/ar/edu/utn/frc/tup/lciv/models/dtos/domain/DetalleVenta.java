package ar.edu.utn.frc.tup.lciv.models.dtos.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
@Data@NoArgsConstructor@AllArgsConstructor
public class DetalleVenta {
    @NotNull
    private Long id;
    @NotNull
    private Long id_producto;
    @NotNull
    @NotEmpty
    private String descripcion;
    @NotNull
    private BigDecimal precio_unitario;
    @NotNull
    private Integer cantidad;
}
