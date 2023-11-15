package com.madmotor.apimadmotordaw.vehiculos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "El año no puede estar vacio")
    @Length(min=4, max=4, message = "El año debe tener 4 caracteres")
    @Min(value = 1900, message = "El año no puede ser menor a 1900")
    private final Integer year;
    @NotBlank(message = "Los kilometros no pueden estar vacios")
    @Min(value = 0, message = "Los kilometros no pueden ser negativos")
    private final Double km;
    @NotBlank(message = "El precio no puede estar vacio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private final Double precio;
    @NotBlank(message = "El stock no puede estar vacio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private final Integer stock;
    private final String imagen;
    @NotBlank(message = "La descripcion no puede estar vacia")
    @Length(min=4, message = "La descripcion debe tener al menos 4 caracteres")
    private final String descripcion;
    @NotBlank(message = "La categoria no puede estar vacia")
    private final String categoria;

}
