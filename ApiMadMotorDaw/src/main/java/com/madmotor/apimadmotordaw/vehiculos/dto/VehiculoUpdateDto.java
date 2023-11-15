package com.madmotor.apimadmotordaw.vehiculos.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class VehiculoUpdateDto {
    @Length(min = 3, message = "La marca debe tener al menos 3 caracteres")
    private String marca;
    private String modelo;
    @Min(value = 1900, message = "El año no puede ser menor a 1900")
    private Integer year;
    @Min(value = 0, message = "Los kilometros no pueden ser negativos")
    private Double km;
    @Min(value = 0, message = "El precio no puede ser negativo")
    private Double precio;
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
    private String imagen;
    @Length(min = 3,message = "La descripcion debe tener al menos 3 caracteres")
    private String descripcion;
    private String categoria;
    private Boolean isDeleted;
}
