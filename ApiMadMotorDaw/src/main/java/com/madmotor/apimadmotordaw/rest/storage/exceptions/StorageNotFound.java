package com.madmotor.apimadmotordaw.rest.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
//Devolver una excepción del tipo 404
@ResponseStatus(HttpStatus.NOT_FOUND)
public class StorageNotFound extends StorageException {

    public StorageNotFound(String mensaje) {
        super(mensaje);
    }
}