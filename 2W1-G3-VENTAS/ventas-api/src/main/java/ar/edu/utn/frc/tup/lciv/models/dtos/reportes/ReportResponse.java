package ar.edu.utn.frc.tup.lciv.models.dtos.reportes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class ReportResponse {
    private BigDecimal monto;
    private Integer total_ventas;
    private BigDecimal monto_mes_anterior;
    private Integer total_mes_anterior;
    private Integer mayorista;
    private Integer minorista;
    private Integer pendiente;
    private Integer entregado;
    private List<ReportProductResponse> productos;

}
