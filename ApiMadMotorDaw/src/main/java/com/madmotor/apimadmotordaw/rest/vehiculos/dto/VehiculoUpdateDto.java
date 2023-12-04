package com.madmotor.apimadmotordaw.rest.vehiculos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@Schema(name = "VehiculoUpdateDto", description = "Dto para actualizar un vehiculo")
public class VehiculoUpdateDto {
    @Schema(description = "Marca del vehiculo", example = "Seat")
    @Length(min = 3, message = "La marca debe tener al menos 3 caracteres")
    private String marca;
    @Schema(description = "Modelo del vehiculo", example = "Ibiza")
    private String modelo;
    @Schema(description = "Año del vehiculo", example = "2000")
    @Min(value = 1900, message = "El año no puede ser menor a 1900")
    private Integer year;
    @Schema(description = "Kilometraje del vehiculo", example = "100000")
    @Min(value = 0, message = "Los kilometros no pueden ser negativos")
    private Double km;
    @Schema(description = "Precio del vehiculo", example = "50000")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private Double precio;
    @Schema(description = "Stock del vehiculo", example = "5")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
    @Schema(description = "Imagen del vehiculo", example ="https://loremflickr.com/150/150")
    private String imagen;
    @Schema(description = "Descripcion del vehiculo", example = "Vehiculo en perfecto estado")
    @Length(min = 3,message = "La descripcion debe tener al menos 3 caracteres")
    private String descripcion;
    @Schema(description = "Categoria del vehiculo", example = "AUTOMATICO")
    private String categoria;
    @Schema(description = "Estado del vehiculo", example = "true")
    private Boolean isDeleted;
}
