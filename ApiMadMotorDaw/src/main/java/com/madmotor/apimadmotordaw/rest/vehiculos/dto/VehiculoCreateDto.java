package com.madmotor.apimadmotordaw.rest.vehiculos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(name = "VehiculoCreateDto", description = "Dto para crear un vehiculo")
public class VehiculoCreateDto {
    @Schema(description = "Marca del vehiculo", example = "Audi")
    @NotBlank(message = "Debe de tener una marca")
    @Length(min=2, message = "La marca debe tener al menos 2 caracteres")
    private final String marca;

    @Schema(description = "Modelo del vehiculo", example = "A4")
    @NotBlank(message = "El modelo no puede estar vacio")
    @Length(min=2, message = "El modelo debe tener al menos 2 caracteres")
    private final String modelo;

    @Schema(description = "Año del vehiculo", example = "2021")
    @NotNull(message = "El año no puede estar vacio")
    @Min(value = 1900, message = "El año no puede ser menor a 1900")
    private final Integer year;

    @Schema(description = "Kilometraje del Vehiculo", example = "100000")
    @NotNull(message = "Los kilometros no pueden estar vacios")
    @Min(value = 0, message = "Los kilometros no pueden ser negativos")
    private final Double km;

    @Schema(description = "Precio del vehiculo", example = "15000")
    @NotNull(message = "El precio no puede estar vacio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private final Double precio;

    @Schema(description = "Stock del vehiculo", example = "10")
    @NotNull(message = "El stock no puede estar vacio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private final Integer stock;

    @Schema(description = "Imagen del vehiculo", example = "https://loremflickr.com/150/150")
    private final String imagen;

    @Schema(description = "Descripcion del vehiculo", example = "Vehiculo en perfecto estado")
    @Length(min=4, message = "La descripcion debe tener al menos 4 caracteres")
    @NotNull(message = "La descripcion no puede estar vacia")
    private final String descripcion;

    @Schema(description = "Categoria del vehiculo", example = "Automatica")
    @NotBlank(message = "La categoria no puede estar vacia")
    private final String categoria;

}
