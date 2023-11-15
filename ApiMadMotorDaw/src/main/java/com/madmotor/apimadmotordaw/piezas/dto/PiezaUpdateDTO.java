package com.madmotor.apimadmotordaw.piezas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class PiezaUpdateDTO {
    @Length(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String name;
    @Length(min = 3, max = 200, message = "La descripción debe tener entre 3 y 200 caracteres")
    private String description;
    @Positive(message = "El precio debe ser mayor que 0")
    private Double price;
    @NotNull
    private String image;
    @PositiveOrZero
    private Integer stock;


}
