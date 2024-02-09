package ar.edu.utn.frc.tup.lciv.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Ventas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaEntity{

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private LocalDateTime fecha;
    @Column
    private Long doc_cliente;
    @Column
    private Integer tipo_venta;
    @Column
    private Integer forma_entrega;
    @Column
    private LocalDateTime fecha_entrega;
    @Column
    private Long id_vendedor;
    @OneToMany(mappedBy = "venta")
    private List<DescuentoEntity> descuentos;
    @Column
    private BigDecimal total;
    @Column
    private BigDecimal subtotal;
    @Column
    private Integer estado;
    @Column
    private Long id_reserva;

    @OneToMany(mappedBy = "venta")
    private List<DetalleVentaEntity> detalles;
}
