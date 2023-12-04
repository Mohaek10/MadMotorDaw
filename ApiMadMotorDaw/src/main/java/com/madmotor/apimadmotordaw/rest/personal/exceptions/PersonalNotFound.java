package com.madmotor.apimadmotordaw.rest.personal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Clase PersonalNotFound
 *
 * Excepción de Personal no encontrado
 * Status 404
 * @version 1.0
 * @author Miguel Vicario
 */

@ResponseStatus(HttpStatus.NOT_FOUND)

public class PersonalNotFound  extends PersonalException{
    public PersonalNotFound(String message) {
        super("Trabajador@ no encontrado");
    }
}
