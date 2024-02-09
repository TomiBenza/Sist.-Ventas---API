package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.models.dtos.integration.IntegrationVentaResponse;
import org.springframework.stereotype.Service;

@Service
public interface IIntegrationService {
    IntegrationVentaResponse getById(Long id);

    Boolean actualizarEstado(Long id, Integer estado);
}
