package ar.edu.utn.frc.tup.lciv.reporitories;

import ar.edu.utn.frc.tup.lciv.entities.PresupuestoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IPresupuestoRepository extends JpaRepository<PresupuestoEntity,Long> {

    @Query("SELECT p FROM PresupuestoEntity p " +
            "WHERE (:id IS NULL OR p.id = :id) " +
            "AND (:doc_cliente IS NULL OR p.doc_cliente = :doc_cliente) " +
            "AND (:fecha_desde IS NULL AND :fecha_hasta IS NULL OR p.fecha_creacion BETWEEN :fecha_desde AND :fecha_hasta )"+
            "AND (:monto_desde IS NULL AND :monto_hasta IS NULL OR p.precio_total BETWEEN :monto_desde AND :monto_hasta)" +
            "AND (:tipo_venta IS NULL OR p.tipo_venta = :tipo_venta) "

    )
    Optional<List<PresupuestoEntity>> findPresupuestosByFilters(@Param("id") Long id,
                                                                @Param("doc_cliente") Long doc_cliente,
                                                                @Param("fecha_desde") LocalDateTime fecha_desde,
                                                                @Param("fecha_hasta") LocalDateTime fecha_hasta,
                                                                @Param("monto_desde") BigDecimal monto_desde,
                                                                @Param("monto_hasta") BigDecimal monto_hasta,
                                                                @Param("tipo_venta") Integer tipo_venta
    );

}
