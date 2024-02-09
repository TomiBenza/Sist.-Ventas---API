package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.PresupuestoAPI;
import ar.edu.utn.frc.tup.lciv.dtos.DetallePresupuesto.DetallePresupuestoRequest;
import ar.edu.utn.frc.tup.lciv.dtos.DetallePresupuesto.DetallePresupuestoResponse;
import ar.edu.utn.frc.tup.lciv.dtos.Presupuestos.PresupuestoRequest;
import ar.edu.utn.frc.tup.lciv.dtos.Presupuestos.PresupuestoResponse;
import ar.edu.utn.frc.tup.lciv.entities.DetallePresupuestoEntity;
import ar.edu.utn.frc.tup.lciv.entities.PresupuestoEntity;
import ar.edu.utn.frc.tup.lciv.reporitories.IDetallePresupuestoRepository;
import ar.edu.utn.frc.tup.lciv.reporitories.IPresupuestoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = PresupuestoAPI.class)
public class PresupuestoServiceTest {

    @MockBean
    private IPresupuestoRepository presupuestoRepository;
    @MockBean
    IDetallePresupuestoRepository detallePresupuestoRepository;
    @Autowired
    private IPresupuestoService presupuestoService;
    private PresupuestoEntity presupuesto;
    List<DetallePresupuestoEntity> detalles;
    List<DetallePresupuestoResponse> detallesEsperados;
    PresupuestoResponse respuestaEsperada;


    @BeforeEach
    public void setUp() {
        presupuesto = new PresupuestoEntity(1L, 1L, LocalDateTime.of(2023, 11, 14, 2, 3),
                2, null, BigDecimal.valueOf(2500));
        detalles = List.of(
                new DetallePresupuestoEntity(1L, presupuesto, "cod-12", "Tornillo", 2, BigDecimal.valueOf(1500)),
                new DetallePresupuestoEntity(2L, presupuesto, "cod-13", "Alambre x kilo", 1, BigDecimal.valueOf(2000)));
        presupuesto.setDetalles(detalles);

        detallesEsperados = List.of(
                new DetallePresupuestoResponse("cod-12", "Tornillo", 2, BigDecimal.valueOf(1500)),
                new DetallePresupuestoResponse("cod-13", "Alambre x kilo", 1, BigDecimal.valueOf(2000)));

        respuestaEsperada = new PresupuestoResponse(1L, 1L, 2, LocalDateTime.of(2023, 11, 14, 2, 3), detallesEsperados, BigDecimal.valueOf(2500));
    }
    @Test
    public void getAllPresupuesto(){
        when(presupuestoRepository.findAll()).thenReturn(List.of(presupuesto));
        assertEquals(List.of(respuestaEsperada),presupuestoService.getAll());
    }
    @Test
    public void testUpdate_PresupuestoNotFound() {
        Long presupuestoId = 1L;
        PresupuestoRequest updatedPresupuesto = new PresupuestoRequest();

        when(presupuestoRepository.findById(presupuestoId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> presupuestoService.update(updatedPresupuesto, presupuestoId));

        assertEquals("El presupuesto con el ID " + presupuestoId + " no existe" , exception.getMessage());
    }

    @Test
    public void saveVenta(){
        List<DetallePresupuestoRequest> detalleRequest = List.of(new DetallePresupuestoRequest("1","Clavos", 2, BigDecimal.valueOf(100)));
        LocalDate localDate = LocalDate.of(2023, 10, 23);

        PresupuestoRequest presupuestoRequest = new PresupuestoRequest();
        presupuestoRequest.setTipo_venta(2);
        presupuestoRequest.setDoc_cliente(2L);
        presupuestoRequest.setFecha_creacion(localDate.atStartOfDay());
        presupuestoRequest.setDetalles(detalleRequest);
        when(presupuestoRepository.save(any())).thenReturn(presupuesto);
        when(detallePresupuestoRepository.saveAll(any())).thenReturn(this.detalles);

        PresupuestoResponse respuesta = presupuestoService.save(presupuestoRequest);
        assertEquals(respuestaEsperada,respuesta);
    }
//        @Test
//        void testUpdate() {
//            Long presupuestoId = 1L;
//            PresupuestoRequest presupuestoRequest = new PresupuestoRequest();
//            presupuestoRequest.setTipo_venta(2);
//            presupuestoRequest.setDoc_cliente(1L);
//            presupuestoRequest.setFecha_creacion(LocalDateTime.of(2023, 11, 20, 5, 2));
//            List<DetallePresupuestoRequest> detallePresupuestoRequest = new ArrayList<>();
//            detallePresupuestoRequest.add(new DetallePresupuestoRequest("25", "tornillo", 2, BigDecimal.valueOf(250)));
//            presupuestoRequest.setDetalles(detallePresupuestoRequest);
//            PresupuestoEntity presupuestoEntity = new PresupuestoEntity();
//            presupuestoEntity.setId(1L);
//            presupuestoEntity.setPrecio_total(BigDecimal.valueOf(100));
//            presupuestoEntity.setDoc_cliente(1L);
//            presupuestoEntity.setTipo_venta(2);
//            List<DetallePresupuestoEntity> detallePresupuestoEntities = new ArrayList<>();
//            DetallePresupuestoEntity detallePresupuestoEntity = new DetallePresupuestoEntity();
//            detallePresupuestoEntity.setPrecio_unitario(BigDecimal.valueOf(50));
//            detallePresupuestoEntity.setCantidad(2);
//            detallePresupuestoEntity.setPrecio_unitario(BigDecimal.valueOf(100));
//            detallePresupuestoEntities.add(detallePresupuestoEntity);
//
//            when(presupuestoRepository.findById(presupuestoId)).thenReturn(Optional.of(presupuestoEntity));
//            when(detallePresupuestoRepository.saveAll(detallePresupuestoEntities)).thenReturn(detallePresupuestoEntities);
//            when(presupuestoRepository.save(presupuestoEntity)).thenReturn(presupuestoEntity);
//
//            PresupuestoResponse result = presupuestoService.update(presupuestoRequest, presupuestoId);
//
//            assertNotNull(result);
//            assertEquals(presupuestoEntity.getId(), result.getId());
//            assertEquals(presupuestoEntity.getPrecio_total(), result.getPrecio_total());
//            //assertEquals(detallePresupuestoEntities.size(), result.getDetalles().size());
//            assertEquals(detallePresupuestoEntity.getPrecio_unitario(), result.getDetalles().get(0).getPrecio_unitario());
//            assertEquals(detallePresupuestoEntity.getCantidad(), result.getDetalles().get(0).getCantidad());
//
//            verify(presupuestoRepository, times(1)).findById(presupuestoId);
//            verify(detallePresupuestoRepository, times(1)).deleteByPresupuesto(presupuestoEntity);
//            verify(detallePresupuestoRepository, times(1)).saveAll(detallePresupuestoEntities);
//            verify(presupuestoRepository, times(1)).save(presupuestoEntity);
//        }
        @Test
        public void testDelete_PresupuestoFound() {
            Long presupuestoId = 1L;
            PresupuestoEntity presupuestoEntity = new PresupuestoEntity();
            presupuestoEntity.setId(presupuestoId);
            presupuestoEntity.setDoc_cliente(1L);

            when(presupuestoRepository.getReferenceById(presupuestoId)).thenReturn(presupuestoEntity);

            Boolean result = presupuestoService.delete(presupuestoId);

            assertTrue(result);
            verify(detallePresupuestoRepository, times(1)).deleteAllByPresupuesto(presupuestoEntity);
            verify(presupuestoRepository, times(1)).delete(presupuestoEntity);
        }

}
