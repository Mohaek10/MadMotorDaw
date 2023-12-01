package com.madmotor.apimadmotordaw.rest.storage.exceptions;

//Permite devolver exceptions
public abstract class StorageException extends RuntimeException {
    public StorageException(String message) {
        super(message);
    }
}
