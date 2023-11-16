package com.madmotor.apimadmotordaw.vehiculos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class VehiculoCreateDto {
    @NotBlank(message = "Debe de tener una marca")
    @Length(min=2, message = "La marca debe tener al menos 2 caracteres")
    private final String marca;
    @NotBlank(message = "El modelo no puede estar vacio")
    @Length(min=2, message = "El modelo debe tener al menos 2 caracteres")
    private final String modelo;
    @NotNull(message = "El año no puede estar vacio")
    @Min(value = 1900, message = "El año no puede ser menor a 1900")
    private final Integer year;
    @NotNull(message = "Los kilometros no pueden estar vacios")
    @Min(value = 0, message = "Los kilometros no pueden ser negativos")
    private final Double km;
    @NotNull(message = "El precio no puede estar vacio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private final Double precio;
    @NotNull(message = "El stock no puede estar vacio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private final Integer stock;
    private final String imagen;
    @Length(min=4, message = "La descripcion debe tener al menos 4 caracteres")
    @NotNull(message = "La descripcion no puede estar vacia")
    private final String descripcion;
    @NotBlank(message = "La categoria no puede estar vacia")
    private final String categoria;

}
