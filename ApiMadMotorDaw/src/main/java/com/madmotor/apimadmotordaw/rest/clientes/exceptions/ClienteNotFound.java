package com.madmotor.apimadmotordaw.rest.clientes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Excepción de Cliente no encontrado
 * Status 404
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClienteNotFound extends ClienteException{

    public ClienteNotFound(String id) {
        super("El cliente no existe con el UUID : "+id+" no fue encontrado");
    }
}
