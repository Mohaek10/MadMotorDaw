package com.madmotor.apimadmotordaw.clientes.exceptions;

public abstract class ClienteException extends RuntimeException {
    public ClienteException(String message) {
        super(message);
    }
}
