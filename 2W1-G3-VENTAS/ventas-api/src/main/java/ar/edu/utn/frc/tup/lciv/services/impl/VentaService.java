package ar.edu.utn.frc.tup.lciv.services.impl;


import ar.edu.utn.frc.tup.lciv.clients.inventario.InventarioRestClient;
import ar.edu.utn.frc.tup.lciv.clients.inventario.dtos.ReservaProductoRequest;
import ar.edu.utn.frc.tup.lciv.clients.inventario.dtos.StockReservado;
import ar.edu.utn.frc.tup.lciv.entities.DescuentoEntity;
import ar.edu.utn.frc.tup.lciv.entities.DetalleVentaEntity;
import ar.edu.utn.frc.tup.lciv.entities.VentaEntity;
import ar.edu.utn.frc.tup.lciv.models.dtos.requests.*;
import ar.edu.utn.frc.tup.lciv.models.dtos.responses.DescuentoResponse;
import ar.edu.utn.frc.tup.lciv.models.dtos.responses.DetalleVentaResponse;
import ar.edu.utn.frc.tup.lciv.models.dtos.responses.VentaResponse;
import ar.edu.utn.frc.tup.lciv.models.dtos.shared.EstadoVenta;
import ar.edu.utn.frc.tup.lciv.models.dtos.shared.FormaEntrega;
import ar.edu.utn.frc.tup.lciv.models.dtos.shared.TipoVenta;
import ar.edu.utn.frc.tup.lciv.repositories.DescuentoJpaRepository;
import ar.edu.utn.frc.tup.lciv.repositories.DetalleVentasJpaRepository;
import ar.edu.utn.frc.tup.lciv.repositories.VentasJpaRepository;
import ar.edu.utn.frc.tup.lciv.services.IVentaService;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.transaction.TransactionalException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService implements IVentaService {
    @Autowired
    private VentasJpaRepository ventasJpaRepository;
    @Autowired
    private DetalleVentasJpaRepository detalleVentasJpaRepository;
    @Autowired
    private DescuentoJpaRepository descuentoJpaRepository;
    @Autowired
    private InventarioRestClient inventarioRestClient;
    @Autowired
    private ModelMapper modelMapper;

    private final Tracer tracer;

    public VentaService() {
        this.tracer = GlobalOpenTelemetry.getTracer("venta-service-tracer");
    }

    @Override
    public VentaResponse getById(Long id) {
        VentaEntity ventaEntity = ventasJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la venta con el id "+id));
        return modelMapper.map(ventaEntity,VentaResponse.class);
    }
    @Override
    public List<VentaResponse> getAll() {
        List<VentaResponse> ventas = new ArrayList<>();
        List<VentaEntity> ventaEntities = ventasJpaRepository.findAll();
        if(ventaEntities.isEmpty()){
            throw new EntityNotFoundException("No se encontraron ventas");
        }
        for (VentaEntity v:ventaEntities) {
            ventas.add(modelMapper.map(v,VentaResponse.class));
        }
        return ventas;
    }
    @Override
    public Optional<List<VentaEntity>> getByFiltroDB(VentaFiltroRequest ventaFiltroRequest){
        Optional<List<VentaEntity>> ventaEntities = ventasJpaRepository.findVentasByFilters(
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

        );
        return ventaEntities;
    }

    @Override
    public List<VentaResponse> getByFiltro(VentaFiltroRequest ventaFiltroRequest) {
        List<VentaEntity> ventaEntities = getByFiltroDB(ventaFiltroRequest)
            .orElseThrow( () -> new EntityNotFoundException("No se han encontrado ventas en base a los filtros especificados"));

        if(ventaEntities.isEmpty()) throw new EntityNotFoundException("No se han encontrado ventas en base a los filtros especificados");
        List<VentaResponse> ventaResponses = new ArrayList<>();
        for (VentaEntity v: ventaEntities) {
            VentaResponse ventaResponse = modelMapper.map(v,VentaResponse.class);
            ventaResponses.add(ventaResponse);
        }
        return ventaResponses;
    }


    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public VentaResponse save(VentaRequest request) {
        validarRequest(request);

        // Construcción de ventaEntity y su detalle
        VentaEntity ventaEntity = new VentaEntity();
        ventaEntity.setEstado(EstadoVenta.PENDIENTE_FACTURACION);

        mapearVentaRequest(request,ventaEntity);
        List<DetalleVentaEntity> detalleVentaEntities = mapToDetalleEntityList(request,ventaEntity);
        ventaEntity.setDetalles(detalleVentaEntities);
        List<DescuentoEntity> descuentoEntities = mapToDescuentoEntityList(request,ventaEntity);
        generarReservaProductos(ventaEntity);

        return saveTransaction(ventaEntity,detalleVentaEntities,descuentoEntities);
    }

    private void generarReservaProductos(VentaEntity ventaEntity) {
        List<StockReservado> stocks = new ArrayList<>();
        for (DetalleVentaEntity d:ventaEntity.getDetalles()) {
            stocks.add(new StockReservado(d.getCod_producto(),d.getCantidad()));
        }
        ReservaProductoRequest reservaProductoRequest = new ReservaProductoRequest(stocks);
        Long id_reserva = inventarioRestClient.postReservaProducto(reservaProductoRequest);
        ventaEntity.setId_reserva(id_reserva);
    }

    private List<DescuentoEntity> mapToDescuentoEntityList(VentaRequest ventaRequest,VentaEntity ventaEntity){
        List<DescuentoEntity> descuentoEntities = new ArrayList<>();

        for (DescuentoRequest descuento : ventaRequest.getDescuentos()) {
            DescuentoEntity descuentoEntity = new DescuentoEntity();
            descuentoEntity.setDescripcion(descuento.getDescripcion());
            descuentoEntity.setMonto(descuento.getMonto());
            descuentoEntity.setVenta(ventaEntity);
            descuentoEntities.add(descuentoEntity);
        }
        return descuentoEntities;
    }

    private List<DetalleVentaEntity> mapToDetalleEntityList(VentaRequest ventaRequest,VentaEntity ventaEntity){
        List<DetalleVentaEntity> detalleVentaEntities = new ArrayList<>();

        for (DetalleVentaRequest detalle : ventaRequest.getDetalles()) {
            DetalleVentaEntity detalleEntity = new DetalleVentaEntity();
            detalleEntity.setCod_producto(detalle.getCod_producto());
            detalleEntity.setDescripcion(detalle.getDescripcion());
            detalleEntity.setPrecio_unitario(detalle.getPrecio_unitario());
            detalleEntity.setCantidad(detalle.getCantidad());
            detalleEntity.setVenta(ventaEntity);
            detalleVentaEntities.add(detalleEntity);
        }
        return detalleVentaEntities;
    }

    @Transactional
    public VentaResponse saveTransaction(VentaEntity ventaEntity, List<DetalleVentaEntity> detalleVentaEntities,List<DescuentoEntity> descuentoEntities) {
        VentaEntity ventaEntitySaved = ventasJpaRepository.save(ventaEntity);
        List<DetalleVentaEntity> detallesGuardados = detalleVentasJpaRepository.saveAll(detalleVentaEntities);
        List<DescuentoEntity> descuentosGuardados = descuentoJpaRepository.saveAll(descuentoEntities);
        if(ventaEntitySaved.getId()==null) throw new TransactionalException("Ocurrió un error en la transacción",new Exception());

        return mapearRespuesta(ventaEntitySaved, detallesGuardados,descuentosGuardados);
    }


    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public VentaResponse putEstado(Long id, Integer estado) {
        VentaEntity ventaEntity = ventasJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("La venta con el ID " + id + " no existe"));

        cambioEstadoValido(estado ,ventaEntity.getEstado());

        ventaEntity.setEstado(estado);
        VentaEntity ventaSaved = ventasJpaRepository.save(ventaEntity);
        return modelMapper.map(ventaSaved, VentaResponse.class);

    }

    @Override
    public VentaResponse delete(Long id) {
        VentaEntity ventaEntity = ventasJpaRepository.findById(id)
                .orElseThrow( () -> new EntityNotFoundException("La venta con el ID " + id + " no existe"));

        if (ventaEntity.getEstado() == EstadoVenta.CANCELADO)
            throw new IllegalArgumentException("La venta con ID "+ id +" ya se encuentra en estado cancelado");

        ventaEntity.setEstado(EstadoVenta.CANCELADO); // Cancelar venta, baja lógica (?)
        return modelMapper.map(ventasJpaRepository.save(ventaEntity),VentaResponse.class);
    }

    private BigDecimal calcularSubtotal(VentaRequest request) {
        BigDecimal precio = BigDecimal.valueOf(0D);
        for (DetalleVentaRequest detalleVenta:request.getDetalles()) {
            precio = precio.add(detalleVenta.getPrecio_unitario().multiply(BigDecimal.valueOf(detalleVenta.getCantidad())));
        }
        return precio;
    }
    private BigDecimal calcularDescuentoTotal(VentaRequest request) {
        BigDecimal precio = BigDecimal.valueOf(0D);
        for (DescuentoRequest descuentoRequest:request.getDescuentos()) {
            precio = precio.add(descuentoRequest.getMonto());
        }
        return precio;
    }
    private void mapearVentaRequest(VentaRequest ventaRequest,VentaEntity ventaEntity){
        ventaEntity.setFecha(ventaRequest.getFecha());
        ventaEntity.setDoc_cliente(ventaRequest.getDoc_cliente());
        ventaEntity.setId_vendedor(ventaRequest.getId_vendedor());
        ventaEntity.setFecha_entrega(ventaRequest.getFecha_entrega());

        ventaEntity.setForma_entrega(ventaRequest.getForma_entrega());
        ventaEntity.setTipo_venta(ventaRequest.getTipo_venta());

        if(ventaRequest.getDetalles().size() > 0){
            ventaEntity.setSubtotal( calcularSubtotal(ventaRequest) );

            // No existen descuentos
            if(ventaRequest.getDescuentos().isEmpty()){
                ventaEntity.setTotal( ventaEntity.getSubtotal() );
            }
            // Existen descuentos
            else ventaEntity.setTotal( ventaEntity.getSubtotal().subtract(calcularDescuentoTotal(ventaRequest)) );

        }


    }
    private void validarRequest(VentaRequest request) {
        if (!esFormaEntregaValida(request.getForma_entrega())){
            throw new IllegalArgumentException("Forma de entrega no válida");
        }
        if(!esTipoVentaValida(request.getTipo_venta())){
            throw new IllegalArgumentException("Tipo de venta no válido");
        }
    }

    private boolean esFormaEntregaValida(int formaEntrega) {
        return formaEntrega == FormaEntrega.CAJA || formaEntrega == FormaEntrega.DEPOSITO;
    }
    private boolean esTipoVentaValida(int tipoVenta) {
        return tipoVenta == TipoVenta.MINORISTA || tipoVenta == TipoVenta.MAYORISTA;
    }
    public void cambioEstadoValido(Integer nuevoEstado, Integer estadoActual) {
        // Se supone que volver el estado de la venta hacia atrás siempre será válido.
        if(nuevoEstado != EstadoVenta.ENTREGADO && nuevoEstado != EstadoVenta.CANCELADO
        && nuevoEstado != EstadoVenta.FACTURADO && nuevoEstado != EstadoVenta.PENDIENTE_FACTURACION){
            throw new IllegalArgumentException("El estado de venta ingresado es inválido");
        }
        if(nuevoEstado == EstadoVenta.CANCELADO) {
            // Para cancelar una venta se supone que se usa el endpoint de baja.
            throw new IllegalArgumentException("Se está intentado establecer el estado de CANCELADO para la venta," +
                    " la unica manera de cancelar una venta es mediante una baja lógica");
        }

        if(estadoActual == EstadoVenta.PENDIENTE_FACTURACION && nuevoEstado == EstadoVenta.ENTREGADO){
            throw new IllegalArgumentException("Para establecer el estado de ENTREGADO a una venta," +
                    " la misma debe previamente estar facturada");
        }

    }
    private VentaResponse mapearRespuesta(VentaEntity ventaEntitySaved, List<DetalleVentaEntity> detallesGuardados, List<DescuentoEntity> descuentosGuardados) {
        VentaResponse response = modelMapper.map(ventaEntitySaved,VentaResponse.class);
        response.getDetalles().clear();
        List<DetalleVentaResponse> detalles = new ArrayList<>();
        List<DescuentoResponse> descuentos = new ArrayList<>();

        for (DetalleVentaEntity detalle: detallesGuardados) {
            DetalleVentaResponse detResponse = new DetalleVentaResponse();
            detResponse.setCod_producto(detalle.getCod_producto());
            detResponse.setCantidad(detalle.getCantidad());
            detResponse.setPrecio_unitario(detalle.getPrecio_unitario());
            detResponse.setDescripcion(detalle.getDescripcion());
            detalles.add(detResponse);
        }
        for (DescuentoEntity descuento: descuentosGuardados) {
            DescuentoResponse descuentoResponse = new DescuentoResponse();
            descuentoResponse.setMonto(descuento.getMonto());
            descuentoResponse.setDescripcion(descuento.getDescripcion());
            descuentos.add(descuentoResponse);
        }

        response.setDetalles(detalles);
        response.setDescuentos(descuentos);
        return response;
    }
}
