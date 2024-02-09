package ar.edu.utn.frc.tup.lciv.controllers;

import ar.edu.utn.frc.tup.lciv.models.dtos.integration.IntegrationVentaResponse;
import ar.edu.utn.frc.tup.lciv.services.IIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("ventas/facturacion")
public class VentaIntegrationController {
    @Autowired
    IIntegrationService integrationService;
    @GetMapping("/{id}")
    public ResponseEntity<IntegrationVentaResponse> getVentaById(@PathVariable Long id){
        return ResponseEntity.ok(integrationService.getById(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> actualizarEstado(@PathVariable Long id, @RequestParam Integer state){
        return ResponseEntity.ok(integrationService.actualizarEstado(id, state));
    }
}
