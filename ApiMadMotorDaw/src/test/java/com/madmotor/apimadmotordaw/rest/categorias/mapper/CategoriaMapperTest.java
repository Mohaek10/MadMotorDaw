package com.madmotor.apimadmotordaw.rest.categorias.mapper;

import com.madmotor.apimadmotordaw.rest.categorias.dto.CategoriaDto;
import com.madmotor.apimadmotordaw.rest.categorias.mapper.CategoriaMapper;
import com.madmotor.apimadmotordaw.rest.categorias.models.Categoria;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaMapperTest {
    private final Categoria categoria= new Categoria(1L, "MOTOS", LocalDateTime.now(),LocalDateTime.now(), false);

    private final CategoriaMapper categoriaMapper = new CategoriaMapper();
    private final CategoriaDto categoriaDto = new CategoriaDto("MOTOS", false);

    @Test
    void map() {
        Categoria mappedCategoria = categoriaMapper.map(categoriaDto);

        assertAll(
                () -> assertEquals(categoriaDto.getName(), mappedCategoria.getName()),
                () -> assertEquals(categoriaDto.getIsDeleted(), mappedCategoria.getIsDeleted())
        );
    }

    @Test
    void testMap() {
        Categoria updatedCategoria = categoriaMapper.map(categoriaDto);

        assertAll("whenToCategoriaWithExistingCategoria_thenReturnUpdatedCategoria",
                () -> assertEquals(categoriaDto.getName(), updatedCategoria.getName()),
                () -> assertEquals(categoriaDto.getIsDeleted(), updatedCategoria.getIsDeleted())
        );
    }
}