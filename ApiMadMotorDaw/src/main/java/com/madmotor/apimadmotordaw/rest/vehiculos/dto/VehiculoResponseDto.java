package com.madmotor.apimadmotordaw.rest.vehiculos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(name = "VehiculoResponseDto", description = "Dto para devolver los datos de un vehiculo")
public class VehiculoResponseDto {
    @Schema(description = "Identificador del vehiculo", example = "123e4567-e89b-12d3-a456-426614174000")
    private final UUID id;
    @Schema(description = "Marca del vehiculo", example = "Seat")
    private final String marca;
    @Schema(description = "Modelo del vehiculo", example = "LEON")
    private final String modelo;
    @Schema(description = "Año del vehiculo", example = "2019")
    private final Integer year;
    @Schema(description = "Kilometraje del Vehiculo", example = "100000")
    private final Double km;
    @Schema(description = "Precio del vehiculo", example = "50000")
    private final Double precio;
    @Schema(description = "Stock del vehiculo", example = "5")
    private final Integer stock;
    @Schema(description = "Imagen del vehiculo", example = "https://loremflickr.com/150/150")
    private final String imagen;
    @Schema(description = "Descripcion del vehiculo", example = "Vehiculo en perfecto estado")
    private final String descripcion;
    @Schema(description = "Fecha de creacion del vehiculo", example = "2021-05-05T12:00:00")
    private final LocalDateTime createdAt;
    @Schema(description = "Fecha de actualizacion del vehiculo", example = "2021-05-05T12:00:00")
    private final LocalDateTime updatedAt;
    @Schema(description = "Categoria del vehiculo", example = "AUTOMATICO")
    private final String categoria;

}
