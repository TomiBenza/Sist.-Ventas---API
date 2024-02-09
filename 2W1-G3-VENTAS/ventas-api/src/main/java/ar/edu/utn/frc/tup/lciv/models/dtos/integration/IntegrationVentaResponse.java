package ar.edu.utn.frc.tup.lciv.models.dtos.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data@AllArgsConstructor
public class IntegrationVentaResponse {
    private Long id;
    private Long id_reserva;
    private LocalDateTime fecha;
    private Long doc_cliente;
    private List<IntegrationDetalleVentaResponse> detalles;
    private List<IntegrationDescuentoResponse> descuentos;
    private BigDecimal subtotal;
    private BigDecimal total;

    public IntegrationVentaResponse(){
        detalles = new ArrayList<>();
        descuentos = new ArrayList<>();
    }
}
