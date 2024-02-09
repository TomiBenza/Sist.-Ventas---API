package ar.edu.utn.frc.tup.lciv.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="detalle_presupuestos")
public class DetallePresupuestoEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "presupuesto_id")
    private PresupuestoEntity presupuesto;
    @Column
    private String cod_producto;
    @Column
    private String descripcion;
    @Column
    private Integer cantidad;
    @Column
    private BigDecimal precio_unitario;


}
