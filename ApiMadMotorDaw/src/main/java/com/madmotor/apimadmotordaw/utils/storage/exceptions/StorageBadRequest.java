package com.madmotor.apimadmotordaw.utils.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Devolvemos una excepción del tipo BadRequest 400
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StorageBadRequest extends StorageException {


    public StorageBadRequest(String mensaje) {
        super(mensaje);
    }
}