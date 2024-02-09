package ar.edu.utn.frc.tup.lciv.dtos.Presupuestos;

import ar.edu.utn.frc.tup.lciv.dtos.DetallePresupuesto.DetallePresupuestoRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PresupuestoRequest {
    @NotNull
    private Long doc_cliente;
    @NotNull
    private Integer tipo_venta;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime fecha_creacion;
    @NotNull
    private List<DetallePresupuestoRequest> detalles;
}
