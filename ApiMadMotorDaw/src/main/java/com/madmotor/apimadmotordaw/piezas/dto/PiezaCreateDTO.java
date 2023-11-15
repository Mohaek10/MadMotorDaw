package com.madmotor.apimadmotordaw.piezas.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
@Data
public class PiezaCreateDTO {
    @NotBlank
    @Length(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String name;
    @NotBlank
    @Length(min = 3, max = 200, message = "La descripción debe tener entre 3 y 200 caracteres")
    private String description;
    @Positive
    private Double price;
    @PositiveOrZero
    private Integer stock;

}
