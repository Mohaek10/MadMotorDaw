package com.madmotor.apimadmotordaw.personal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)


public class PersonalFailSave extends PersonalException{
    public PersonalFailSave(String message) {
        super("Error al guardar trabajador@");
    }
}
