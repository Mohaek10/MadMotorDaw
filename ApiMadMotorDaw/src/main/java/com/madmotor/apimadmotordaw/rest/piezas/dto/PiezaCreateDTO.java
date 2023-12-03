package com.madmotor.apimadmotordaw.rest.piezas.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PiezaCreateDTO {
    @NotBlank
    @Length(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String name;
    @NotBlank
    @Length(min = 3, max = 200, message = "La descripción debe tener entre 3 y 200 caracteres")
    private String description;
    @Positive
    @NotNull(message = "El precio no puede ser nulo")
    private Double price;
    @PositiveOrZero
    @NotNull(message = "El stock no puede ser nulo")
    private Integer stock;

}
