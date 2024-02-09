package ar.edu.utn.frc.tup.lciv.controllers;

import ar.edu.utn.frc.tup.lciv.dtos.Presupuestos.PresupuestoRequest;
import ar.edu.utn.frc.tup.lciv.dtos.Presupuestos.PresupuestoResponse;
import ar.edu.utn.frc.tup.lciv.dtos.Presupuestos.PresupuestosParametros;
import ar.edu.utn.frc.tup.lciv.services.IPresupuestoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/presupuesto")
@CrossOrigin(origins = "*")
public class PresupuestoController {

    @Autowired
    private IPresupuestoService presupuestoService;

    @GetMapping("")
    public ResponseEntity<List<PresupuestoResponse>> getAll() {
        return ResponseEntity.ok(presupuestoService.getAll());
    }

    @GetMapping("/")
    public ResponseEntity<List<PresupuestoResponse>> getByFiltros(
            @RequestParam(name = "id",required = false) Long id,
            @RequestParam(name = "doc_cliente",required = false) Long doc_cliente,
            @RequestParam(name = "fecha_desde",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha_desde,
            @RequestParam(name="fecha_hasta",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha_hasta,
            @RequestParam(name="monto_desde",required = false) BigDecimal monto_desde,
            @RequestParam(name="monto_hasta",required = false) BigDecimal monto_hasta,
            @RequestParam(name="tipo_venta",required = false) Integer tipo_venta
    )
    {
        PresupuestosParametros request = new PresupuestosParametros(id,doc_cliente,fecha_desde,fecha_hasta,monto_desde,monto_hasta,tipo_venta);
        return ResponseEntity.ok(presupuestoService.getPresupuestosByFiltros(request));
    }
    @PostMapping("")
    public ResponseEntity<PresupuestoResponse> save(@RequestBody @Valid PresupuestoRequest presupuestoRequest) {
        return ResponseEntity.ok(presupuestoService.save(presupuestoRequest));
    }
    @PutMapping("/{id}")
    public ResponseEntity<PresupuestoResponse> updatePresupuesto(@RequestBody @Valid PresupuestoRequest presupuestoRequest , Long id) {
        return ResponseEntity.ok(presupuestoService.update(presupuestoRequest, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePresupuesto(@PathVariable Long id){
        return ResponseEntity.ok(presupuestoService.delete(id));
    }
}

