package ar.edu.utn.frc.tup.lciv.services.impl;

import ar.edu.utn.frc.tup.lciv.entities.VentaEntity;
import ar.edu.utn.frc.tup.lciv.models.dtos.reportes.ReportProductResponse;
import ar.edu.utn.frc.tup.lciv.models.dtos.reportes.ReportResponse;
import ar.edu.utn.frc.tup.lciv.models.dtos.requests.VentaFiltroRequest;
import ar.edu.utn.frc.tup.lciv.models.dtos.responses.VentaResponse;
import ar.edu.utn.frc.tup.lciv.models.dtos.shared.EstadoVenta;
import ar.edu.utn.frc.tup.lciv.models.dtos.shared.TipoVenta;
import ar.edu.utn.frc.tup.lciv.services.IReporteService;
import ar.edu.utn.frc.tup.lciv.services.IVentaService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReporteService implements IReporteService {
    @Autowired
    private IVentaService ventaService;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public ReportResponse getReporte(Integer mes, Integer anio, Integer tipoVenta) {
        LocalDateTime fechaInicio;
        LocalDateTime fechaFinal;
        LocalDateTime fechaInicioAnterior;
        LocalDateTime fechaFinalAnterior;

        if (mes == null) {
            fechaInicio = LocalDateTime.of(anio, 1, 1, 0, 0);
            fechaFinal = LocalDateTime.of(fechaInicio.getYear(), 12, 31, 23, 59);
            fechaInicioAnterior = fechaInicio.minusYears(1);
            fechaFinalAnterior = LocalDateTime.of(fechaInicioAnterior.getYear(), 12, 31, 23, 59);
        } else {
            fechaInicio = LocalDateTime.of(anio, Month.of(mes), 1, 0, 0);
            fechaFinal = fechaInicio.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59);
            fechaInicioAnterior = fechaInicio.minusMonths(1);
            fechaFinalAnterior = fechaInicioAnterior.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59);
        }

        VentaFiltroRequest filtro = crearFiltroVenta(fechaInicio, fechaFinal, tipoVenta);
        VentaFiltroRequest filtroAnterior = crearFiltroVenta(fechaInicioAnterior, fechaFinalAnterior, null);

        List<VentaResponse> ventaResponseList = ventaService.getByFiltro(filtro);
        Optional<List<VentaEntity>> ventaResponseListAnterior = ventaService.getByFiltroDB(filtroAnterior);

        ReportResponse response = new ReportResponse(BigDecimal.ZERO, 0, BigDecimal.ZERO, 0, 0, 0, 0, 0, null);

        for (VentaResponse v : ventaResponseList) {
            actualizarReporte(response, v);
        }

        // Mes anterior
        ventaResponseListAnterior.ifPresent(ventasAnteriores ->
                ventasAnteriores.forEach(v -> {
                    if(v.getEstado() == EstadoVenta.CANCELADO) {
                        return;
                    }
                    response.setMonto_mes_anterior(response.getMonto_mes_anterior().add(v.getTotal()));
                    response.setTotal_mes_anterior(response.getTotal_mes_anterior() + 1);
                })
        );

        // Productos:
        List<Object[]> topProductos = getTop5ProductosMasVendidosMesAnterior(fechaInicio, fechaFinal,tipoVenta);
        actualizarProductos(response, topProductos);

        return response;
    }

    public VentaFiltroRequest crearFiltroVenta(LocalDateTime fechaInicio, LocalDateTime fechaFinal, Integer tipoVenta) {
        VentaFiltroRequest filtro = new VentaFiltroRequest();
        filtro.setFecha_desde(fechaInicio);
        filtro.setFecha_hasta(fechaFinal);
        filtro.setTipo_venta(tipoVenta);
        return filtro;
    }

    public void actualizarReporte(ReportResponse response, VentaResponse v) {
        if(v.getEstado() == EstadoVenta.CANCELADO){
            return;
        }
        response.setMonto(response.getMonto().add(v.getTotal()));
        response.setTotal_ventas(response.getTotal_ventas() + 1);

        if (v.getTipo_venta() == TipoVenta.MAYORISTA) {
            response.setMayorista(response.getMayorista() + 1);
        } else {
            response.setMinorista(response.getMinorista() + 1);
        }

        if (v.getEstado() == EstadoVenta.ENTREGADO) {
            response.setEntregado(response.getEntregado() + 1);
        } else {
            response.setPendiente(response.getPendiente() + 1);
        }
    }

    public void actualizarProductos(ReportResponse response, List<Object[]> topProductos) {
        if (!topProductos.isEmpty()) {
            List<ReportProductResponse> productosResponse = new ArrayList<>();

            for (Object[] topProducto : topProductos) {
                if (topProducto[0] == null) {
                    continue;
                }

                ReportProductResponse reportProductResponse = new ReportProductResponse();
                reportProductResponse.setDescripcion((String) topProducto[0]);
                reportProductResponse.setCantidad((Long) topProducto[1]);
                productosResponse.add(reportProductResponse);
            }

            response.setProductos(productosResponse);
        }
    }

    public List<Object[]> getTop5ProductosMasVendidosMesAnterior(LocalDateTime fechaInicio, LocalDateTime fechaFinal, Integer tipoVenta) {
        String jpql = "SELECT d.descripcion, SUM(d.cantidad) as cantidadVendida " +
                "FROM DetalleVentaEntity d " +
                "WHERE (d.venta.fecha BETWEEN :fechaInicio AND :fechaFin) AND " +
                "(d.venta.tipo_venta = :tipoVenta OR :tipoVenta IS NULL) AND " +
                "d.venta.estado != 3 " +
                "GROUP BY d.descripcion " +
                "ORDER BY cantidadVendida DESC";

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class)
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("fechaFin", fechaFinal)
                .setParameter("tipoVenta", tipoVenta)
                .setMaxResults(5);

        return query.getResultList();
    }
}
