package com.madmotor.apimadmotordaw.notificaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class VehiculoNotificacionDto {
    UUID id;
    String marca;
    String modelo;
    Integer year;
    Double km;
    Double precio;
    Integer stock;
    String imagen;
    String descripcion;
    String createdAt;
    String updatedAt;
    String categoria;
    Boolean isDeleted;
}
