package com.madmotor.apimadmotordaw.rest.personal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción de Personal no se ha podido guardar
 *
 * Status 400
 * @version 1.0
 * @author Miguel Vicario
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)


public class PersonalFailSave extends PersonalException{
    public PersonalFailSave(String message) {
        super("Error al guardar trabajador@");
    }
}
