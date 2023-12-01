package com.madmotor.apimadmotordaw.rest.clientes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClienteFailSave extends ClienteException{
    public ClienteFailSave(String message) {
        super(message);
    }
}
