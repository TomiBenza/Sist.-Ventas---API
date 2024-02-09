package ar.edu.utn.frc.tup.lciv.controllers;

import ar.edu.utn.frc.tup.lciv.models.dtos.reportes.ReportResponse;
import ar.edu.utn.frc.tup.lciv.services.IReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("reportes")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ReporteController {

    @Autowired
    private IReporteService reporteService;
    @GetMapping("")
    public ResponseEntity<ReportResponse> generateReport(@RequestParam(name = "mes",required = false) Integer mes,
                                                         @RequestParam(name = "anio") Integer anio,
                                                         @RequestParam(name = "tipo_venta",required = false) Integer tipo_venta){
        return ResponseEntity.ok(reporteService.getReporte(mes,anio,tipo_venta));
    }
}
