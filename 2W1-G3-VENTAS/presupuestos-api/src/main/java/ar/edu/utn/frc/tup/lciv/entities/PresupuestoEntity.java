package ar.edu.utn.frc.tup.lciv.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "presupuestos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PresupuestoEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long doc_cliente;
    @Column
    private LocalDateTime fecha_creacion;
    @Column
    private Integer tipo_venta;
    @OneToMany(mappedBy = "presupuesto")
    private List<DetallePresupuestoEntity> detalles;
    @Column
    private BigDecimal precio_total;



}
