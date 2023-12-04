package com.madmotor.apimadmotordaw.rest.pedidos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)

public class VehiculoBadPrice extends PedidosException{
    public VehiculoBadPrice(String message) {
        super("El precio del vehiculo con id: " + message + " no es correcto");
    }
    public VehiculoBadPrice(String id, Double precio,Double precioVehiculo) {
        super("El precio del vehiculo con id: " + id + " no es correcto, el precio del vehiculo es: " + precioVehiculo + " y el precio del pedido es: " + precio);
    }
}
