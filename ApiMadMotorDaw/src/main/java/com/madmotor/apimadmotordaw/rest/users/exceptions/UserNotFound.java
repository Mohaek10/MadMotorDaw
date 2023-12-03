package com.madmotor.apimadmotordaw.rest.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFound extends UserException {


    public UserNotFound(String id) {
        super("Usuario con id " + id + " no encontrado");
    }
}