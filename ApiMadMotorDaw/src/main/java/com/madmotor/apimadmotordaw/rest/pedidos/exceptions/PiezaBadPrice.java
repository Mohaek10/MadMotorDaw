package com.madmotor.apimadmotordaw.rest.pedidos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PiezaBadPrice extends PedidosException{
    public PiezaBadPrice(String pieza) {
        super("El precio de la pieza con id: " + pieza + " no es correcto");
    }
}
