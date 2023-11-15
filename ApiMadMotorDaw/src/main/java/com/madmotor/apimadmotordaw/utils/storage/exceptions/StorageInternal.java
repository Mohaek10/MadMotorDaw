package com.madmotor.apimadmotordaw.utils.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


//Devolvemos una excepción del tipo InternalServerError 500 esperemos que no salga
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class StorageInternal extends StorageException {

    public StorageInternal(String mensaje) {
        super(mensaje);
    }
}