package com.madmotor.apimadmotordaw.rest.piezas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Clase PiezaNotFound
 *
 * Excepción de Pieza no encontrada
 * Status 404
 * @version 1.0
 * @author Rubén Fernández
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PiezaNotFound extends RuntimeException{
    public PiezaNotFound(UUID id) {
        super("Pieza con id" + id + "no encontrada");
    }
}
