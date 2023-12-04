package com.madmotor.apimadmotordaw.rest.piezas.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Clase PiezaCreateDTO
 *
 * En esta clase se definen los atributos de la clase PiezaCreateDTO
 * @version 1.0
 * @Author Rubén Férnandez
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Crea una nueva pieza")

public class PiezaCreateDTO {

    @NotBlank
    @Length(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    @Schema(description = "Nombre de la pieza")
    private String name;

    @NotBlank
    @Length(min = 3, max = 200, message = "La descripción debe tener entre 3 y 200 caracteres")
    @Schema(description = "Descripción de la pieza")
    private String description;

    @Positive
    @NotNull(message = "El precio no puede ser nulo")
    @Schema(description = "Precio de la pieza")
    private Double price;

    @PositiveOrZero
    @NotNull(message = "El stock no puede ser nulo")
    @Schema(description = "Stock de la pieza")
    private Integer stock;

}
