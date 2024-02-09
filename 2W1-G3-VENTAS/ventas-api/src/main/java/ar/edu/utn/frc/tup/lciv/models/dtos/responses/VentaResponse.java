package ar.edu.utn.frc.tup.lciv.models.dtos.responses;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

import java.time.LocalDateTime;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaResponse {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
    private LocalDateTime fecha;
    private Long doc_cliente;
    private int tipo_venta;
    private int forma_entrega;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
    private LocalDateTime fecha_entrega;
    private Long id_vendedor;
    private List<DetalleVentaResponse> detalles;
    private List<DescuentoResponse> descuentos;
    private BigDecimal subtotal;
    private BigDecimal total;
    private int estado;
    private Long id_reserva;

}
