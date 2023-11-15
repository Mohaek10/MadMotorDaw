package com.madmotor.apimadmotordaw.utils.storage.exceptions;

//Permite devolver exceptions
public abstract class StorageException extends RuntimeException {
    public StorageException(String message) {
        super(message);
    }
}
