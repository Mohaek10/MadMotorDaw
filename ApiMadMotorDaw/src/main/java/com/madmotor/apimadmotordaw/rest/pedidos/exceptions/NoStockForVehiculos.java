package com.madmotor.apimadmotordaw.rest.pedidos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoStockForVehiculos extends PedidosException{
    public NoStockForVehiculos(String vehiculoId, Integer cantidadPedida, Integer cantidadDisponible) {
        super(String.format("No hay suficiente stock para el vehiculo con id %s. Cantidad pedida: %d. Cantidad disponible: %d", vehiculoId, cantidadPedida, cantidadDisponible));
    }
}
