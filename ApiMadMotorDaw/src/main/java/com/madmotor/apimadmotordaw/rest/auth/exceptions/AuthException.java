package com.madmotor.apimadmotordaw.rest.auth.exceptions;

public abstract class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
