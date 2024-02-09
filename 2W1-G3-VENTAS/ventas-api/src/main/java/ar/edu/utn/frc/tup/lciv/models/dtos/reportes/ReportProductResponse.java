package ar.edu.utn.frc.tup.lciv.models.dtos.reportes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ReportProductResponse {
    private String descripcion;
    private Long cantidad;
}
