package ar.edu.utn.frc.tup.lciv.controllers;

import ar.edu.utn.frc.tup.lciv.models.dtos.requests.VentaFiltroRequest;
import ar.edu.utn.frc.tup.lciv.models.dtos.requests.VentaRequest;
import ar.edu.utn.frc.tup.lciv.models.dtos.responses.VentaResponse;
import ar.edu.utn.frc.tup.lciv.services.IVentaService;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    private final Tracer tracer;

    public VentaController() {
        this.tracer = GlobalOpenTelemetry.getTracer("ventas-controller-tracer");
    }

    @Autowired
    private IVentaService ventaService;

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> getById(@PathVariable Long id){
        Span span = tracer.spanBuilder("getVentaById").startSpan();
        try(Scope scope = span.makeCurrent()){
            span.setAttribute("venta.id", String.valueOf(id));
            return ResponseEntity.ok(ventaService.getById(id));
        } finally {
            span.end();
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<VentaResponse>> getByFiltros(
            @RequestParam(name = "id",required = false) Long id,
            @RequestParam(name="id_cliente",required = false) Long id_cliente,
            @RequestParam(name="id_vendedor",required = false) Long id_vendedor,
            @RequestParam(name = "fecha_min",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha_min,
            @RequestParam(name="fecha_max",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha_max,
            @RequestParam(name="forma_entrega",required = false) Integer forma_entrega,
            @RequestParam(name="tipo_venta",required = false) Integer tipo_venta,
            @RequestParam(name="monto_desde",required = false) BigDecimal monto_desde,
            @RequestParam(name="monto_hasta",required = false) BigDecimal monto_hasta,
            @RequestParam(name="estado",required = false) Integer estado){
        return ResponseEntity.ok(ventaService.getByFiltro(
                new VentaFiltroRequest(
                        id,
                        id_cliente,
                        id_vendedor,
                        fecha_min,
                        fecha_max,
                        forma_entrega,
                        tipo_venta,
                        monto_desde,
                        monto_hasta,
                        estado
                )
        ));
    }


    @GetMapping("all/")
    public ResponseEntity<List<VentaResponse>> getAll(){
        return ResponseEntity.ok(ventaService.getAll());
    }

    @PostMapping("/")
    public ResponseEntity<VentaResponse> save(@Valid @RequestBody VentaRequest request){
        return ResponseEntity.ok(ventaService.save(request));
    }
    @PutMapping("/{id}")
    public ResponseEntity<VentaResponse> putEstado (@PathVariable Long id, @RequestParam Integer estado){
        return ResponseEntity.ok(ventaService.putEstado(id, estado));
    }

    @DeleteMapping("/{id}") //baja lógica
    public ResponseEntity<VentaResponse> delete(@PathVariable Long id){
        return ResponseEntity.ok(ventaService.delete(id));
    }
}
