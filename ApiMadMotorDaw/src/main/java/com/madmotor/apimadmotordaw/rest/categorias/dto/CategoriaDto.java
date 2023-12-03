package com.madmotor.apimadmotordaw.rest.categorias.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "Crea una nueva categoria")
public class CategoriaDto {
    @Schema(description = "El nombre de la categoria")
    @NotBlank(message = "El campo name no puede estar vacío")
    @Length(min=3, message = "El nombre debe tener al menos 3 caracteres")
    private final String name;
    @Schema(description = "La valor booleano de la categoria")
    @NotNull(message = "El campo isDeleted no puede ser nulo")
    private final Boolean isDeleted;
}
