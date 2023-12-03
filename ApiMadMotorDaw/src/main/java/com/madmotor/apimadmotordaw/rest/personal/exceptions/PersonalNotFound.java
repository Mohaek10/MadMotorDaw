package com.madmotor.apimadmotordaw.rest.personal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)

public class PersonalNotFound  extends PersonalException{
    public PersonalNotFound(String message) {
        super("Trabajador@ no encontrado");
    }
}
