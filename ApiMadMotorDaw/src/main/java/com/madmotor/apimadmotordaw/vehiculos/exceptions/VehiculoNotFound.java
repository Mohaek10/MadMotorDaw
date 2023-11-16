package com.madmotor.apimadmotordaw.vehiculos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VehiculoNotFound extends VehiculoException{
    public VehiculoNotFound(String message) {
        super("Vehiculo con id " + message + " no encontrado");
    }
}
