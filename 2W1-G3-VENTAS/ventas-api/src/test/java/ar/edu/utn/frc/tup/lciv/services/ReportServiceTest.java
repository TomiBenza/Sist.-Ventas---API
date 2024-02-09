package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.VentasApi;
import ar.edu.utn.frc.tup.lciv.entities.VentaEntity;
import ar.edu.utn.frc.tup.lciv.models.dtos.reportes.ReportProductResponse;
import ar.edu.utn.frc.tup.lciv.models.dtos.reportes.ReportResponse;
import ar.edu.utn.frc.tup.lciv.models.dtos.responses.VentaResponse;
import ar.edu.utn.frc.tup.lciv.services.impl.ReporteService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = VentasApi.class)
public class ReportServiceTest {
    @MockBean
    private IVentaService ventaService;
    @MockBean
    private EntityManager entityManager;
    @Autowired
    private ReporteService reporteService;

    @Test
    void getReporteFull_Test(){
        ReportResponse expectedResponse = new ReportResponse(BigDecimal.valueOf(300),1,BigDecimal.valueOf(300),
                2,0,1,1,0,null/*
                List.of(
                        new ReportProductResponse("PROD-01", 3L)
                )*/);
        List<VentaResponse> ventaResponseList = List.of(
                new VentaResponse(1L, null, null, 1, 1,
                        null, null, null, null,
                        BigDecimal.valueOf(300), BigDecimal.valueOf(300), 1, null)
        );
        Optional<List<VentaEntity>> ventaResponseListAnterior = Optional.of(Arrays.asList(
                new VentaEntity(1L,null,null,1,1,
                        null,null,null, BigDecimal.valueOf(150),
                       BigDecimal.valueOf(150),1,null,null),
                new VentaEntity(2L,null,null,1,1,
                        null,null,null, BigDecimal.valueOf(150),
                        BigDecimal.valueOf(150),1,null,null)
        ));


        TypedQuery<Object[]> query = mock(TypedQuery.class);



        when(ventaService.getByFiltro(any())).thenReturn(ventaResponseList);
        when(ventaService.getByFiltroDB(any())).thenReturn(ventaResponseListAnterior);
        when(entityManager.createQuery(anyString(),eq(Object[].class))).thenReturn(query);
        when(query.setParameter(eq("fechaInicio"), any(LocalDateTime.class))).thenReturn(query);
        when(query.setParameter(eq("fechaFin"), any(LocalDateTime.class))).thenReturn(query);
        when(query.setMaxResults(anyInt())).thenReturn(query);
        ReportResponse response = reporteService.getReporte(2,2023,1);
        assertEquals(expectedResponse,response);
    }
}
