package ar.edu.utn.frc.tup.lciv.services;


import ar.edu.utn.frc.tup.lciv.dtos.Presupuestos.PresupuestoRequest;
import ar.edu.utn.frc.tup.lciv.dtos.Presupuestos.PresupuestoResponse;
import ar.edu.utn.frc.tup.lciv.dtos.Presupuestos.PresupuestosParametros;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface IPresupuestoService {
    List<PresupuestoResponse> getAll();
    PresupuestoResponse save(PresupuestoRequest presupuesto);
    PresupuestoResponse update(PresupuestoRequest presupuesto, Long Id);
    Boolean delete(Long id);
    List<PresupuestoResponse> getPresupuestosByFiltros(PresupuestosParametros request);
}