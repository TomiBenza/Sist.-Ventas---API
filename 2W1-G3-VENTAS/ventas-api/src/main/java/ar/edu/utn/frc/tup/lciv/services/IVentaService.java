package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.entities.VentaEntity;
import ar.edu.utn.frc.tup.lciv.models.dtos.requests.VentaFiltroRequest;
import ar.edu.utn.frc.tup.lciv.models.dtos.requests.VentaRequest;
import ar.edu.utn.frc.tup.lciv.models.dtos.responses.VentaResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IVentaService {
    VentaResponse getById(Long id);
    VentaResponse save(VentaRequest venta);
    List<VentaResponse> getAll();

    List<VentaResponse> getByFiltro(VentaFiltroRequest ventaFiltroRequest);
    Optional<List<VentaEntity>> getByFiltroDB(VentaFiltroRequest ventaFiltroRequest);

    VentaResponse putEstado(Long id, Integer estado);

    VentaResponse delete(Long id);
}
