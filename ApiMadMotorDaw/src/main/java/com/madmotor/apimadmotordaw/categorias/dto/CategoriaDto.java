package com.madmotor.apimadmotordaw.categorias.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CategoriaDto {
    @Length
    private final String name;
    private final Boolean isDeleted;
}
