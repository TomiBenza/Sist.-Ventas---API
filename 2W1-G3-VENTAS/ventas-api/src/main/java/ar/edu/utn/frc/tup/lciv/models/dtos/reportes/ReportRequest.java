package ar.edu.utn.frc.tup.lciv.models.dtos.reportes;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ReportRequest {
    @Max(value = 12, message = "Ingresar mes válido")
    @Min(value = 1, message = "Ingresar mes válido")
    private Integer mes;

    @Min(value = 2010, message = "El año minimo es 2010")
    @NotNull(message = "Ingresar un año")
    private Integer anio;
    @Max(value = 2, message = "El tipo de venta debe ser entre 1 o 2")
    @Min(value = 1, message = "El tipo de venta debe ser entre 1 o 2")
    private Integer tipo_venta;
}
