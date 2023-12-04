package com.madmotor.apimadmotordaw.rest.piezas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * Clase PiezaUpdateDTO
 *
 * En esta clase se definen los atributos de la clase PiezaUpdateDTO
 * @version 1.0
 * @Author Rubén Fernández
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Pieza a actualizar")

public class PiezaUpdateDTO {

    @Length(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    @Schema(description = "Nombre de la pieza")
    private String name;

    @Length(min = 3, max = 200, message = "La descripción debe tener entre 3 y 200 caracteres")
    @Schema(description = "Descripción de la pieza")
    private String description;

    @Positive(message = "El precio debe ser mayor que 0")
    @Schema(description = "Precio de la pieza")
    private Double price;

    @NotNull
    @Schema(description = "Imagen de la pieza")
    private String image;

    @PositiveOrZero
    @Schema(description = "Stock de la pieza")
    private Integer stock;

    @Schema(description = "Fecha de creación de la pieza")
    private LocalDateTime updatedAt;


}
