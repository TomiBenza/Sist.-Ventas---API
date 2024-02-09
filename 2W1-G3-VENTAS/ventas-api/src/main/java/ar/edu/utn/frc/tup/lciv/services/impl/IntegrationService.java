package ar.edu.utn.frc.tup.lciv.services.impl;

import ar.edu.utn.frc.tup.lciv.models.dtos.integration.IntegrationDescuentoResponse;
import ar.edu.utn.frc.tup.lciv.models.dtos.integration.IntegrationDetalleVentaResponse;
import ar.edu.utn.frc.tup.lciv.models.dtos.integration.IntegrationVentaResponse;
import ar.edu.utn.frc.tup.lciv.models.dtos.responses.DescuentoResponse;
import ar.edu.utn.frc.tup.lciv.models.dtos.responses.DetalleVentaResponse;
import ar.edu.utn.frc.tup.lciv.models.dtos.responses.VentaResponse;
import ar.edu.utn.frc.tup.lciv.models.dtos.shared.EstadoVenta;
import ar.edu.utn.frc.tup.lciv.services.IIntegrationService;
import ar.edu.utn.frc.tup.lciv.services.IVentaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntegrationService implements IIntegrationService {
    @Autowired
    IVentaService ventaService;
    @Override
    public IntegrationVentaResponse getById(Long id) {
        VentaResponse response = ventaService.getById(id);
        if(response.getEstado() != EstadoVenta.PENDIENTE_FACTURACION){
            throw new EntityNotFoundException();
        }
        ///
        IntegrationVentaResponse integrationResponse = new IntegrationVentaResponse();
        integrationResponse.setId(response.getId());
        integrationResponse.setId_reserva(response.getId_reserva());
        integrationResponse.setFecha(response.getFecha());
        integrationResponse.setDoc_cliente(response.getDoc_cliente());
        integrationResponse.setSubtotal(response.getSubtotal());
        integrationResponse.setTotal(response.getTotal());
        for(DetalleVentaResponse detalle: response.getDetalles()){
            IntegrationDetalleVentaResponse integrationDetalle = new IntegrationDetalleVentaResponse();
            integrationDetalle.setCod_producto(detalle.getCod_producto());
            integrationDetalle.setDescripcion(detalle.getDescripcion());
            integrationDetalle.setPrecio_unitario(detalle.getPrecio_unitario());
            integrationDetalle.setCantidad(detalle.getCantidad());
            integrationResponse.getDetalles().add(integrationDetalle);
        }
        for(DescuentoResponse descuento: response.getDescuentos()){
            IntegrationDescuentoResponse integrationDescuento = new IntegrationDescuentoResponse();
            integrationDescuento.setMonto(descuento.getMonto());
            integrationDescuento.setDescripcion(descuento.getDescripcion());
            integrationResponse.getDescuentos().add(integrationDescuento);
        }

        return integrationResponse;
    }
    @Override
    public Boolean actualizarEstado(Long id, Integer estado) {
        VentaResponse response = ventaService.getById(id);
        if(response.getEstado() != EstadoVenta.PENDIENTE_FACTURACION){
            throw new EntityNotFoundException();
        }
        response = ventaService.putEstado(id, estado);
        if(estado == response.getEstado()){
            return true;
        }
        return false;
    }
}
