package com.madmotor.apimadmotordaw.rest.pedidos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)

public class PedidoDeUserNotFound extends PedidosException{
    public PedidoDeUserNotFound(String id) {
        super("Pedido de usuario con id " + id + " no encontrado");
    }
}
