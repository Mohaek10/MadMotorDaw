package com.madmotor.apimadmotordaw.rest.categorias.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409 (Conflict)
public class CategoriaExists extends CategoriaException{
    public CategoriaExists(String message) {
        super(message);
    }
}
