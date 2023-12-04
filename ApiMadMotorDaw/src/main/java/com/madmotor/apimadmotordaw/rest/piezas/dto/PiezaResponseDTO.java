package com.madmotor.apimadmotordaw.rest.piezas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Clase PiezaResponseDTO
 *
 * En esta clase se definen los atributos de la clase PiezaResponseDTO
 * @version 1.0
 * @Author Rubén Fernández
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Pieza a devolver")

public class PiezaResponseDTO {

    @Schema(description = "Identificador de la pieza")
    private UUID id;

    @Schema(description = "Nombre de la pieza")
    private String name;

    @Schema(description = "Descripción de la pieza")
    private String description;

    @Schema(description = "Precio de la pieza")
    private Double price;

    @Schema(description = "Stock de la pieza")
    private Integer stock;

    @Schema(description = "Imagen de la pieza")
    private String image;


}
