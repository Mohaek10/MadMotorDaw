package com.madmotor.apimadmotordaw.rest.categorias.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoriaNotFound extends CategoriaException{
    public CategoriaNotFound(String message) {
        super(message);
    }
}
