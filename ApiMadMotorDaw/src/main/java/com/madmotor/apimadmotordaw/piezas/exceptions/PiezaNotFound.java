package com.madmotor.apimadmotordaw.piezas.exceptions;

import java.util.UUID;

public class PiezaNotFound extends RuntimeException{
    public PiezaNotFound(UUID id) {
        super("Pieza con id" + id + "no encontrada");
    }
}
