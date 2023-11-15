package com.madmotor.apimadmotordaw.clientes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClienteFailList extends ClienteException{
    public ClienteFailList(String message) {
        super(message);
    }
}
