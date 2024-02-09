package ar.edu.utn.frc.tup.lciv.controllers;

import ar.edu.utn.frc.tup.lciv.VentasApi;
import ar.edu.utn.frc.tup.lciv.models.dtos.requests.VentaFiltroRequest;
import ar.edu.utn.frc.tup.lciv.models.dtos.requests.VentaRequest;
import ar.edu.utn.frc.tup.lciv.models.dtos.responses.VentaResponse;
import ar.edu.utn.frc.tup.lciv.services.IVentaService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = VentasApi.class)
public class VentaControllerTest {
    @Mock
    private IVentaService ventaService;

    @InjectMocks
    private VentaController ventaController;

    @Test
    public void testGetById() {
        long id = 1L;
        VentaResponse ventaResponse = new VentaResponse();
        ventaResponse.setId(id);

        when(ventaService.getById(id)).thenReturn(ventaResponse);

        ResponseEntity<VentaResponse> response = ventaController.getById(id);
        assertEquals(ventaResponse, response.getBody());
    }
    @Test
    public void testGetByFiltros() {
        VentaFiltroRequest filtroRequest = new VentaFiltroRequest(1L,null,null,null,
                null,null,null,null,null,null);

        when(ventaService.getByFiltro(filtroRequest)).thenReturn(Collections.emptyList());

        ResponseEntity<List<VentaResponse>> response = ventaController.getByFiltros(1L, null, null, null, null, null, null, null, null, null);
        assertEquals(Collections.emptyList(), response.getBody());
    }

    @Test
    public void testGetAll() {
        List<VentaResponse> ventaResponses = Collections.singletonList(new VentaResponse());

        when(ventaService.getAll()).thenReturn(ventaResponses);

        ResponseEntity<List<VentaResponse>> response = ventaController.getAll();
        assertEquals(ventaResponses, response.getBody());
    }


    @Test
    public void testSave(){
        VentaRequest ventaRequest = new VentaRequest();
        VentaResponse ventaResponse = new VentaResponse();
        when(ventaService.save(ventaRequest)).thenReturn(ventaResponse);

        ResponseEntity<VentaResponse> response = ventaController.save(ventaRequest);
        assertEquals(ventaResponse, response.getBody());
    }

    //DONE: Actualizar test, ahora este método recibe un entero en vez de una clase update estado
    @Test
    public void testPutEstado() {
        long id = 1L;
        int estado = 1;

        VentaResponse ventaResponse = new VentaResponse();
        ventaResponse.setId(id);
        ventaResponse.setEstado(estado);

        when(ventaService.putEstado(id, estado)).thenReturn(new VentaResponse());

        ResponseEntity<VentaResponse> response = ventaController.putEstado(id, estado);
        assertEquals(new VentaResponse(), response.getBody());
    }


    @Test
    public void testDelete() {

        long id = 1L;


        when(ventaService.delete(id)).thenReturn(new VentaResponse());


        ResponseEntity<VentaResponse> response = ventaController.delete(id);
        assertEquals(new VentaResponse(), response.getBody());
    }
}
