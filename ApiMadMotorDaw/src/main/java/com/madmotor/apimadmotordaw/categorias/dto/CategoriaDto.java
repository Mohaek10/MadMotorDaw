package com.madmotor.apimadmotordaw.categorias.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CategoriaDto {
    @NotBlank(message = "El campo name no puede estar vacío")
    @Length(min=3, message = "El nombre debe tener al menos 3 caracteres")
    private final String name;
    @NotNull(message = "El campo isDeleted no puede ser nulo")
    private final Boolean isDeleted;
}
