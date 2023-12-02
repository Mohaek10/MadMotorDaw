package com.madmotor.apimadmotordaw.rest.pedidos.exceptions;

public abstract class PedidosException extends RuntimeException {

    public PedidosException(String message) {
        super(message);
    }

    public PedidosException(String message, Throwable cause) {
        super(message, cause);
    }
}
