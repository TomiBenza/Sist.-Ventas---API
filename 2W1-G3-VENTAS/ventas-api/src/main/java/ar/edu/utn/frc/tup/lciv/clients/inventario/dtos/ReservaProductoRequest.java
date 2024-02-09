package ar.edu.utn.frc.tup.lciv.clients.inventario.dtos;

import java.util.List;

public record ReservaProductoRequest(List<StockReservado> stockReservado) {
}
