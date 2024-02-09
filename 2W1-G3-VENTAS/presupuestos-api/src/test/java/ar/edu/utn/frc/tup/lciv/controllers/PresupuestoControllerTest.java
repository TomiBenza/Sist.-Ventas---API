package ar.edu.utn.frc.tup.lciv.controllers;

import ar.edu.utn.frc.tup.lciv.PresupuestoAPI;
import ar.edu.utn.frc.tup.lciv.dtos.Presupuestos.PresupuestoRequest;
import ar.edu.utn.frc.tup.lciv.dtos.Presupuestos.PresupuestoResponse;
import ar.edu.utn.frc.tup.lciv.dtos.Presupuestos.PresupuestosParametros;
import ar.edu.utn.frc.tup.lciv.services.IPresupuestoService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PresupuestoAPI.class)
public class PresupuestoControllerTest {

    @Mock
    private IPresupuestoService presupuestoService;

    @InjectMocks
    private PresupuestoController presupuestoController;

    @Test
    public void testGetAll() {
        List<PresupuestoResponse> presupuestoResponse = Collections.singletonList(new PresupuestoResponse());

        when(presupuestoService.getAll()).thenReturn(presupuestoResponse);

        ResponseEntity<List<PresupuestoResponse>> response = presupuestoController.getAll();
        assertEquals(presupuestoResponse, response.getBody());
    }

    @Test
    public void testDelete() {
        long id = 1L;

        when(presupuestoService.delete(id)).thenReturn(true);
        ResponseEntity<Boolean> response = presupuestoController.deletePresupuesto(id);
        assertEquals(true, response.getBody());
    }

    @Test
    public void testSave() {
        PresupuestoRequest presupuestoRequest = new PresupuestoRequest();
        when(presupuestoService.save(presupuestoRequest)).thenReturn(new PresupuestoResponse());

        ResponseEntity<PresupuestoResponse> response = presupuestoController.save(presupuestoRequest);
        assertEquals(new PresupuestoResponse(), response.getBody());
    }

    @Test
    public void testUpdate() {
        long id = 1L;
        PresupuestoRequest presupuestoRequest = new PresupuestoRequest();
        when(presupuestoService.update(presupuestoRequest, id)).thenReturn(new PresupuestoResponse());
        ResponseEntity<PresupuestoResponse> response = presupuestoController.updatePresupuesto(presupuestoRequest, id);
        assertEquals(new PresupuestoResponse(), response.getBody());
    }

    @Test
    public void testGetByFiltros() {
        Long id = 1L;
        Long doc_cliente = 123456789L;
        LocalDateTime fecha_desde = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime fecha_hasta = LocalDateTime.of(2022, 12, 31, 23, 59);
        BigDecimal monto_desde = new BigDecimal("1000.00");
        BigDecimal monto_hasta = new BigDecimal("5000.00");
        Integer tipo_venta = 1;

        PresupuestosParametros request = new PresupuestosParametros(id, doc_cliente, fecha_desde, fecha_hasta, monto_desde, monto_hasta, tipo_venta);

        List<PresupuestoResponse> expectedResponse = new ArrayList<>();
        expectedResponse.add(new PresupuestoResponse());
        expectedResponse.add(new PresupuestoResponse());
        when(presupuestoService.getPresupuestosByFiltros(request)).thenReturn(expectedResponse);

        ResponseEntity<List<PresupuestoResponse>> responseEntity = presupuestoController.getByFiltros(id, doc_cliente, fecha_desde, fecha_hasta, monto_desde, monto_hasta, tipo_venta);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        List<PresupuestoResponse> actualResponse = responseEntity.getBody();
        Assert.assertEquals(expectedResponse, actualResponse);
    }
}
