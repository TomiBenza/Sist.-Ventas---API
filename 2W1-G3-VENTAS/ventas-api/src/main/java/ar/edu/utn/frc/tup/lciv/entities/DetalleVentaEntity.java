package ar.edu.utn.frc.tup.lciv.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "Detalle_Ventas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleVentaEntity{
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "venta_id")
    private VentaEntity venta;
    @Column
    private String cod_producto;
    @Column
    private String descripcion;
    @Column
    private BigDecimal precio_unitario;
    @Column
    private Integer cantidad;

}
