package com.madmotor.apimadmotordaw.websockets.notificaciones.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Notificacion<T> {
    private String entity;
    private Tipo type;
    private T data;
    private String createdAt;


    public enum Tipo {CREATE, UPDATE, DELETE}
}
