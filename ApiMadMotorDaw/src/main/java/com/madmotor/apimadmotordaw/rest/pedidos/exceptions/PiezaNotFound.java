package com.madmotor.apimadmotordaw.rest.pedidos.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PiezaNotFound extends PedidosException{
    public PiezaNotFound(String message) {
        super("Pieza con id: " + message + " no encontrada");
    }
}
