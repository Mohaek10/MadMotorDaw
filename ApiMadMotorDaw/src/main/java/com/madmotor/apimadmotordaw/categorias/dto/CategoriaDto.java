package com.madmotor.apimadmotordaw.categorias.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CategoriaDto {
    @NotBlank
    @Length(min=3, message = "El nombre debe tener al menos 3 caracteres")
    private final String name;
    private final Boolean isDeleted;
}
