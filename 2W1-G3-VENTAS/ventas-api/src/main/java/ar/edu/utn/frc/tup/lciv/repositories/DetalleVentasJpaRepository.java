package ar.edu.utn.frc.tup.lciv.repositories;

import ar.edu.utn.frc.tup.lciv.entities.DetalleVentaEntity;
import ar.edu.utn.frc.tup.lciv.entities.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface DetalleVentasJpaRepository extends JpaRepository<DetalleVentaEntity, Long> {
    void deleteByVenta(VentaEntity ventaEntity);
}
