package ar.edu.utn.frc.tup.lciv.services.impl;

import ar.edu.utn.frc.tup.lciv.dtos.DetallePresupuesto.DetallePresupuestoRequest;
import ar.edu.utn.frc.tup.lciv.dtos.DetallePresupuesto.DetallePresupuestoResponse;
import ar.edu.utn.frc.tup.lciv.dtos.Presupuestos.PresupuestoRequest;
import ar.edu.utn.frc.tup.lciv.dtos.Presupuestos.PresupuestoResponse;
import ar.edu.utn.frc.tup.lciv.dtos.Presupuestos.PresupuestosParametros;
import ar.edu.utn.frc.tup.lciv.dtos.shared.TipoVenta;
import ar.edu.utn.frc.tup.lciv.entities.DetallePresupuestoEntity;
import ar.edu.utn.frc.tup.lciv.entities.PresupuestoEntity;
import ar.edu.utn.frc.tup.lciv.reporitories.IDetallePresupuestoRepository;
import ar.edu.utn.frc.tup.lciv.reporitories.IPresupuestoRepository;
import ar.edu.utn.frc.tup.lciv.services.IPresupuestoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Service
public class PresupuestoService implements IPresupuestoService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private IPresupuestoRepository presupuestoJpaRepository;
    @Autowired
    private IDetallePresupuestoRepository detallePresupuestoJpaRepository;

    @Override
    public List<PresupuestoResponse> getAll() {
        List<PresupuestoResponse> presupuestos = new ArrayList<>();
        List<PresupuestoEntity> presupuestoEntities = presupuestoJpaRepository.findAll();
        if(presupuestoEntities.isEmpty()){
            throw new EntityNotFoundException("No se encontraron presupuestos");
        }
        for (PresupuestoEntity p:presupuestoEntities) {
            presupuestos.add(modelMapper.map(p,PresupuestoResponse.class));
        }
        return presupuestos;
    }

    @Override
    public List<PresupuestoResponse> getPresupuestosByFiltros(PresupuestosParametros request){
        List<PresupuestoEntity> presupuestoEntities = presupuestoJpaRepository.findPresupuestosByFilters(
                request.getId(),
                request.getDoc_cliente(),
                request.getFecha_desde(),
                request.getFecha_hasta(),
                request.getMonto_desde(),
                request.getMonto_hasta(),
                request.getTipo_venta()

        ).orElseThrow( () -> new EntityNotFoundException("No se han encontrado presupuestos en base a los filtros especificados"));
        if(presupuestoEntities.isEmpty()) throw new EntityNotFoundException("No se han encontrado presupuestos en base a los filtros especificados");
        List<PresupuestoResponse> presupuestoResponses = new ArrayList<>();
        for (PresupuestoEntity v: presupuestoEntities) {
            PresupuestoResponse presupuestoResponse = modelMapper.map(v,PresupuestoResponse.class);
            presupuestoResponses.add(presupuestoResponse);
        }
        return presupuestoResponses;
    }

    @Override
    @Transactional
    public PresupuestoResponse save(PresupuestoRequest presupuesto) {
        validarRequest(presupuesto);

        PresupuestoEntity pE = new PresupuestoEntity();
        mapearPresupuestoRequest(presupuesto,pE);
        List<DetallePresupuestoEntity> detallesEntities = mapToDetalleEntityList(presupuesto,pE);
        pE.setDetalles(detallesEntities);
        PresupuestoEntity pESaved = presupuestoJpaRepository.save(pE);
        List<DetallePresupuestoEntity> detallesGuardados = detallePresupuestoJpaRepository.saveAll(detallesEntities);

        if (pESaved.getId() == null) {
            throw new EntityNotFoundException("Error al guardar el presupuesto");
        }
        return mapearRespuesta(pESaved,detallesGuardados);
    }

    private void mapearPresupuestoRequest(PresupuestoRequest presupuestoRequest,PresupuestoEntity presupuestoEntity){
        presupuestoEntity.setFecha_creacion(presupuestoRequest.getFecha_creacion());
        presupuestoEntity.setDoc_cliente(presupuestoRequest.getDoc_cliente());
        presupuestoEntity.setTipo_venta(presupuestoRequest.getTipo_venta());
        presupuestoEntity.setPrecio_total( calcularPrecio(presupuestoRequest) );
    }

    private BigDecimal calcularPrecio(PresupuestoRequest request) {
        BigDecimal precio = BigDecimal.valueOf(0D);
        for (DetallePresupuestoRequest detalle:request.getDetalles()) {
            precio = precio.add(detalle.getPrecio_unitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
        }
        return precio;
    }

    private List<DetallePresupuestoEntity> mapToDetalleEntityList(PresupuestoRequest presupuestoRequest,PresupuestoEntity presupuestoEntity){
        List<DetallePresupuestoEntity> detalleVentaEntities = new ArrayList<>();

        for (DetallePresupuestoRequest detalle : presupuestoRequest.getDetalles()) {
            DetallePresupuestoEntity detalleEntity = new DetallePresupuestoEntity();
            detalleEntity.setCod_producto(detalle.getCod_producto());
            detalleEntity.setDescripcion(detalle.getDescripcion());
            detalleEntity.setPrecio_unitario(detalle.getPrecio_unitario());
            detalleEntity.setCantidad(detalle.getCantidad());
            detalleEntity.setPresupuesto(presupuestoEntity);
            detalleVentaEntities.add(detalleEntity);
        }
        return detalleVentaEntities;
    }

    private PresupuestoResponse mapearRespuesta(PresupuestoEntity presupuestoEntitySaved, List<DetallePresupuestoEntity> detallesGuardados /*List<DescuentoEntity> descuentosGuardados*/) {
        PresupuestoResponse response = modelMapper.map(presupuestoEntitySaved,PresupuestoResponse.class);
        response.getDetalles().clear();
        List<DetallePresupuestoResponse> detalles = new ArrayList<>();

        for (DetallePresupuestoEntity detalle: detallesGuardados) {
            DetallePresupuestoResponse detResponse = new DetallePresupuestoResponse();
            detResponse.setCod_producto(detalle.getCod_producto());
            detResponse.setCantidad(detalle.getCantidad());
            detResponse.setPrecio_unitario(detalle.getPrecio_unitario());
            detResponse.setDescripcion(detalle.getDescripcion());
            detalles.add(detResponse);
        }
        response.setDetalles(detalles);
        return response;
    }


    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public PresupuestoResponse update(PresupuestoRequest presupuesto, Long presupuestoId) {
        PresupuestoEntity presupuestoEntity = presupuestoJpaRepository.findById(presupuestoId)
                .orElseThrow(() -> new EntityNotFoundException("El presupuesto con el ID " + presupuestoId + " no existe"));
        validarRequest(presupuesto);

        detallePresupuestoJpaRepository.deleteByPresupuesto(presupuestoEntity);
        
        mapearPresupuestoRequest(presupuesto,presupuestoEntity);
        List<DetallePresupuestoEntity> detallePresupuestoEntities = mapToDetalleEntityList(presupuesto,presupuestoEntity);
        List<DetallePresupuestoEntity> detallesSaved = detallePresupuestoJpaRepository.saveAll(detallePresupuestoEntities);
        presupuestoEntity.setDetalles(detallePresupuestoEntities);
        BigDecimal precioTotal = detallesSaved.stream()
                .map(detalle -> detalle.getPrecio_unitario().multiply(new BigDecimal(detalle.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        presupuestoEntity.setPrecio_total(precioTotal);
        PresupuestoEntity presupuestoEntitySaved = presupuestoJpaRepository.save(presupuestoEntity);

        return mapearRespuesta(presupuestoEntitySaved,detallesSaved);
    }
    private void validarRequest(PresupuestoRequest request) {

        if(!esTipoVentaValida(request.getTipo_venta())){
            throw new IllegalArgumentException("Tipo de venta no válido");
        }
    }


    private boolean esTipoVentaValida(int tipoVenta) {
        return tipoVenta == TipoVenta.MINORISTA || tipoVenta == TipoVenta.MAYORISTA;
    }

    @Override
    @Transactional
    public Boolean delete(Long id) {
        PresupuestoEntity presupuestoE = presupuestoJpaRepository.getReferenceById(id);
        if (presupuestoE.getDoc_cliente() == null) {
            throw new EntityNotFoundException("No se ha encontrado el presupuesto");
        } else {
            detallePresupuestoJpaRepository.deleteAllByPresupuesto(presupuestoE);
            presupuestoJpaRepository.delete(presupuestoE);

            return true;
        }
    }
}
