package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.models.dtos.reportes.ReportResponse;
import org.springframework.stereotype.Service;

@Service
public interface IReporteService {
    ReportResponse getReporte(Integer mes, Integer anio, Integer tipoVenta);
}
