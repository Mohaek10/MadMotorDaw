package com.madmotor.apimadmotordaw.rest.pedidos.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)

public class NoItemsValid extends PedidosException{
    public NoItemsValid(String message) {
        super(message);
    }
}
