package ar.edu.utn.frc.tup.lciv.models.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VentaRequest {
    @NotNull
    //@Future(message = "La fecha no puede ser menor a la actual")
    private LocalDateTime fecha;
    @NotNull
    private Long doc_cliente;
    @NotNull
    @Min(value = 1, message = "El valor de tipo de venta debe ser mayor o igual a 1")
    @Max(value = 2, message = "El valor de tipo de venta debe ser menor o igual a 2")
    private int tipo_venta;
    @NotNull
    @Min(value = 1, message = "El valor de forma de entrega debe ser mayor o igual a 1")
    @Max(value = 2, message = "El valor de forma de entrega debe ser menor o igual a 2")
    private int forma_entrega;
    @NotNull
    //@Future(message = "La fecha de entrega no puede ser menor a la actual")
    private LocalDateTime fecha_entrega;
    @NotNull
    private Long id_vendedor;
    @NotNull
    @NotEmpty(message = "La venta debe tener productos cargados")
    private List<DetalleVentaRequest> detalles;

    private List<DescuentoRequest> descuentos;
}
