package com.madmotor.apimadmotordaw.vehiculos.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class VehiculoResponseDto {
    private final UUID id;
    private final String marca;
    private final String modelo;
    private final Integer year;
    private final Double km;
    private final Double precio;
    private final Integer stock;
    private final String imagen;
    private final String descripcion;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private String categoria;

}
