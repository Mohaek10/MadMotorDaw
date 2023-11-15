package com.madmotor.apimadmotordaw.piezas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PiezaNotFound extends RuntimeException{
    public PiezaNotFound(UUID id) {
        super("Pieza con id" + id + "no encontrada");
    }
}
