package ar.edu.utn.frc.tup.lciv.repositories;

import ar.edu.utn.frc.tup.lciv.entities.VentaEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentasJpaRepository extends JpaRepository<VentaEntity, Long> {
    @Query("SELECT v FROM VentaEntity v " +
            "WHERE (:id IS NULL OR v.id = :id) " +
            "AND (:doc_cliente IS NULL OR v.doc_cliente = :doc_cliente) " +
            "AND (:id_vendedor IS NULL OR v.id_vendedor = :id_vendedor) " +
            "AND (:fecha_desde IS NULL AND :fecha_hasta IS NULL OR v.fecha BETWEEN :fecha_desde AND :fecha_hasta )" +
            "AND (:forma_entrega IS NULL OR v.forma_entrega = :forma_entrega) " +
            "AND (:tipo_venta IS NULL OR v.tipo_venta = :tipo_venta) " +
            "AND (:monto_inicial IS NULL OR v.total >= :monto_inicial) " +
            "AND (:monto_final IS NULL OR v.total <= :monto_final) " +
            "AND (:estado IS NULL OR v.estado = :estado)"
            )
    Optional<List<VentaEntity>> findVentasByFilters(@Param("id") Long id,
                                          @Param("doc_cliente") Long doc_cliente,
                                          @Param("id_vendedor") Long id_vendedor,
                                          @Param("fecha_desde") LocalDateTime fecha_min,
                                          @Param("fecha_hasta") LocalDateTime fecha_max,
                                          @Param("forma_entrega") Integer forma_entrega,
                                          @Param("tipo_venta") Integer tipo_venta,
                                          @Param("monto_inicial") BigDecimal monto_inicial,
                                          @Param("monto_final") BigDecimal monto_final,
                                          @Param("estado") Integer estado
                                        );
}
