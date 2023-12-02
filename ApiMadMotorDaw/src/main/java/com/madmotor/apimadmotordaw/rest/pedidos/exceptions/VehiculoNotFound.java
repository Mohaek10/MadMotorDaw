package com.madmotor.apimadmotordaw.rest.pedidos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VehiculoNotFound extends PedidosException{
    public VehiculoNotFound(String message) {
        super("Vehiculo con id: " + message + " no encontrado");
    }
}
