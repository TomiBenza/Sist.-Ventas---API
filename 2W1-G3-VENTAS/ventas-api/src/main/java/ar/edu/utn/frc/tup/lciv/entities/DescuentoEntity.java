package ar.edu.utn.frc.tup.lciv.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Entity
@Table(name = "descuentos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DescuentoEntity {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String descripcion;
    @Column
    private BigDecimal monto;
    @ManyToOne
    @JoinColumn(name = "venta_id")
    private VentaEntity venta;
}
