package com.madmotor.apimadmotordaw.rest.pedidos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoStockForPiezas extends PedidosException{
public NoStockForPiezas(String piezaId, Integer cantidadPedida, Integer cantidadDisponible) {
        super(String.format("No hay suficiente stock para la pieza con id %s. Cantidad pedida: %d. Cantidad disponible: %d", piezaId, cantidadPedida, cantidadDisponible));
    }
}
