package com.madmotor.apimadmotordaw.clientes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción de producto no encontrado
 * Status 404
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClienteNotFound extends ClienteException{

    public ClienteNotFound(String dni) {
        super("El cliente no existe con el dni numero : "+dni+" no fue encontrado");
    }
}
