package com.madmotor.apimadmotordaw.rest.clientes.exceptions;

public abstract class ClienteException extends RuntimeException {
    public ClienteException(String message) {
        super(message);
    }
}
