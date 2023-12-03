package com.madmotor.apimadmotordaw.rest.vehiculos.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VehiculoBadRequest extends VehiculoException{
    public VehiculoBadRequest(String message) {
        super(message);
    }
}
