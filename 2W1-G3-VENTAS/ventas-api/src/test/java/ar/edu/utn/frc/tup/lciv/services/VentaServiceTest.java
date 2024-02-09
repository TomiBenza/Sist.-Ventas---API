package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.VentasApi;
import ar.edu.utn.frc.tup.lciv.entities.DescuentoEntity;
import ar.edu.utn.frc.tup.lciv.entities.DetalleVentaEntity;
import ar.edu.utn.frc.tup.lciv.entities.VentaEntity;
import ar.edu.utn.frc.tup.lciv.models.dtos.requests.*;
import ar.edu.utn.frc.tup.lciv.models.dtos.responses.DescuentoResponse;
import ar.edu.utn.frc.tup.lciv.models.dtos.responses.DetalleVentaResponse;
import ar.edu.utn.frc.tup.lciv.models.dtos.responses.VentaResponse;
import ar.edu.utn.frc.tup.lciv.models.dtos.shared.EstadoVenta;
import ar.edu.utn.frc.tup.lciv.repositories.DescuentoJpaRepository;
import ar.edu.utn.frc.tup.lciv.repositories.DetalleVentasJpaRepository;
import ar.edu.utn.frc.tup.lciv.repositories.VentasJpaRepository;
import ar.edu.utn.frc.tup.lciv.services.impl.VentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = VentasApi.class)
public class VentaServiceTest {
    @MockBean
    private VentasJpaRepository ventasJpaRepository;
    @MockBean
    DetalleVentasJpaRepository detalleVentasJpaRepository;
    @MockBean
    DescuentoJpaRepository descuentoJpaRepository;
    @Autowired
    private IVentaService ventaService;
    @Autowired
    private ModelMapper modelMapper;
    private VentaEntity venta;
    List<DetalleVentaEntity> detalles;
    List<DescuentoEntity> descuentos;
    List<DetalleVentaResponse> detallesEsperados;
    VentaResponse respuestaEsperada;

    @BeforeEach
    public void setUp(){
        venta = new VentaEntity(1L, LocalDateTime.of(2023,10,24,3,10),
                1L,1,1, LocalDateTime.of(2023,10,24,3,10),
                1L,null,BigDecimal.valueOf(3400),BigDecimal.valueOf(3400),2,1L,null);
        detalles = Arrays.asList(
                new DetalleVentaEntity(1L,venta,"1","Destornillador", BigDecimal.valueOf(3000),1),
                new DetalleVentaEntity(2L,venta,"2","Tornillo", BigDecimal.valueOf(40),10));
        venta.setDetalles(detalles);


        detallesEsperados = List.of(new DetalleVentaResponse("1", "Destornillador", BigDecimal.valueOf(3000), 1),
                new DetalleVentaResponse("2", "Tornillo", BigDecimal.valueOf(40), 10));
        respuestaEsperada = new VentaResponse(1L,LocalDateTime.of(2023,10,24,3,10),1L,1,
                    1,LocalDateTime.of(2023,10,24,3,10),1L,
                detallesEsperados,null,BigDecimal.valueOf(3400),BigDecimal.valueOf(3400),2,1L);

    }

    @Test
    public void getVentaById(){
        when(ventasJpaRepository.findById(1L)).thenReturn(Optional.of(venta));
        VentaResponse response = ventaService.getById(1L);
        assertEquals(respuestaEsperada,response);
    }

    @Test
    public void getAllVentas(){
        when(ventasJpaRepository.findAll()).thenReturn(List.of(venta));
        assertEquals(List.of(respuestaEsperada),ventaService.getAll());
    }
    @Test
    public void getByFiltro(){
        VentaFiltroRequest ventaFiltroRequest = new VentaFiltroRequest(null,null,null,null,
                null,null,null,null,null,1);

        when(ventasJpaRepository.findVentasByFilters(
                ventaFiltroRequest.getId(),
                ventaFiltroRequest.getDoc_cliente(),
                ventaFiltroRequest.getId_vendedor(),
                ventaFiltroRequest.getFecha_desde(),
                ventaFiltroRequest.getFecha_hasta(),
                ventaFiltroRequest.getForma_entrega(),
                ventaFiltroRequest.getTipo_venta(),
                ventaFiltroRequest.getMonto_desde(),
                ventaFiltroRequest.getMonto_hasta(),
                ventaFiltroRequest.getEstado()
        )).thenReturn(Optional.of(List.of(venta)));
        List<VentaResponse> respuesta = ventaService.getByFiltro(ventaFiltroRequest);

        assertEquals(respuesta,List.of(respuestaEsperada));
    }

    @Test
    public void saveVentaConDescuentos(){
        VentaResponse respuestaEsperadaDescuentos = this.respuestaEsperada;
        respuestaEsperadaDescuentos.setDescuentos(List.of(new DescuentoResponse("Cliente recurrente",BigDecimal.valueOf(100))));

        List<DetalleVentaRequest> detallesTest = List.of(new DetalleVentaRequest("1","",BigDecimal.valueOf(3000), 1));
        List<DescuentoRequest> descuentoRequests = new ArrayList<>();
        descuentoRequests.add(new DescuentoRequest("Cliente recurrente", BigDecimal.valueOf(100)));

        VentaRequest ventaTest = new VentaRequest(LocalDateTime.of(2023,10,24,3,10),
                1L,1,1,LocalDateTime.of(2023,10,24,3,10),
                1L,detallesTest,descuentoRequests);

        this.descuentos = List.of(new DescuentoEntity(1L, "Cliente recurrente", BigDecimal.valueOf(100), venta));

        when(ventasJpaRepository.save(any())).thenReturn(venta);
        when(detalleVentasJpaRepository.saveAll(any())).thenReturn(this.detalles);
        when(descuentoJpaRepository.saveAll(any())).thenReturn(this.descuentos);

        VentaResponse respuesta = ventaService.save(ventaTest);
        assertEquals(respuestaEsperadaDescuentos,respuesta);
    }
    //DONE: Actualizar aquí tambien el estado
    @Test
    public void putEstadoVenta(){
        Long id = 1L;
        int estado = 2;
        VentaEntity ventaEntity = new VentaEntity();
        ventaEntity.setId(id);
        ventaEntity.setEstado(1);
        VentaEntity updatedVentaEntity = new VentaEntity();
        updatedVentaEntity.setId(id);
        updatedVentaEntity.setEstado(estado);
        VentaResponse expectedResponse = new VentaResponse();
        expectedResponse.setId(id);
        expectedResponse.setEstado(estado);

        when(ventasJpaRepository.findById(id)).thenReturn(Optional.of(ventaEntity));
        when(ventasJpaRepository.save(ventaEntity)).thenReturn(updatedVentaEntity);

        VentaResponse actualResponse = ventaService.putEstado(id, estado);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void deleteVenta_EstadoInvalido(){
        venta.setEstado(3);
        when(ventasJpaRepository.findById(1L)).thenReturn(Optional.ofNullable(venta));
        assertThrows(IllegalArgumentException.class,()-> ventaService.delete(1L));

    }

    @Test
    public void testCambioEstadoValido() {
        // Simulating different states for testing
        VentaService ventaService = new VentaService();

        Integer estadoPendiente = EstadoVenta.PENDIENTE_FACTURACION;
        Integer estadoEntregado = EstadoVenta.ENTREGADO;
        Integer estadoFacturado = EstadoVenta.FACTURADO;

        // Testing the change from PENDIENTE_FACTURACION to ENTREGADO
        try {
            ventaService.cambioEstadoValido(estadoEntregado, estadoPendiente);
        } catch (IllegalArgumentException e) {
            assertEquals("Para establecer el estado de ENTREGADO a una venta, la misma debe previamente estar facturada", e.getMessage());
        }

        // Testing the change to an invalid state
        try {
            ventaService.cambioEstadoValido(10, estadoPendiente); // 10 is an arbitrary invalid state
        } catch (IllegalArgumentException e) {
            assertEquals("El estado de venta ingresado es inválido", e.getMessage());
        }

        // Testing the attempt to set the state to CANCELADO
        try {
            ventaService.cambioEstadoValido(EstadoVenta.CANCELADO, estadoPendiente);
        } catch (IllegalArgumentException e) {
            assertEquals("Se está intentado establecer el estado de CANCELADO para la venta, la unica manera de cancelar una venta es mediante una baja lógica", e.getMessage());
        }
    }

}
