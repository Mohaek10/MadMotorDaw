package com.madmotor.apimadmotordaw.categorias.services;

import com.madmotor.apimadmotordaw.categorias.dto.CategoriaDto;
import com.madmotor.apimadmotordaw.categorias.mapper.CategoriaMapper;
import com.madmotor.apimadmotordaw.categorias.models.Categoria;
import com.madmotor.apimadmotordaw.categorias.repositories.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {
    private final Categoria categoria = new Categoria(1L, "MOTOS", LocalDateTime.now(), LocalDateTime.now(), false);

    private final CategoriaDto categoriaDto = new CategoriaDto("MOTOS", false);
    @Mock
    private CategoriaMapper categoriaMapper;
    @Mock
    private CategoriaRepository categoriasRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriasService;


    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Categoria> expectedPage = new PageImpl<>(List.of(categoria));
        when(categoriasRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        var res = categoriasService.findAll(Optional.empty(), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(res),
                () -> assertFalse(res.isEmpty())
        );

        verify(categoriasRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));

    }

    @Test
    void findById() {
        when(categoriasRepository.findById(anyLong())).thenReturn(Optional.of(categoria));

        var res = categoriasService.findById(1L);

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(res, categoria)
        );

        verify(categoriasRepository, times(1)).findById(anyLong());
    }

    @Test
    void findByName() {
        when(categoriasRepository.findByNameEqualsIgnoreCase(any(String.class))).thenReturn(Optional.of(categoria));

        var res = categoriasService.findByName("MOTOS");

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(res, categoria)
        );

        verify(categoriasRepository, times(1)).findByNameEqualsIgnoreCase(any(String.class));
    }

    @Test
    void save() {
        when(categoriasRepository.save(any(Categoria.class))).thenReturn(categoria);
        when(categoriaMapper.map(any(CategoriaDto.class))).thenReturn(categoria);

        var res = categoriasService.save(categoriaDto);

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(res, categoria)
        );

        verify(categoriasRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void update() {
        when(categoriasRepository.findById(any(Long.class))).thenReturn(Optional.of(categoria));
        when(categoriasRepository.findByNameEqualsIgnoreCase(any(String.class))).thenReturn(Optional.of(categoria));
        when(categoriaMapper.map(any(CategoriaDto.class), any(Categoria.class))).thenReturn(categoria);
        when(categoriasRepository.save(any(Categoria.class))).thenReturn(categoria);

        categoriasService.update(1L, categoriaDto);

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals("MOTOS", categoria.getName())
        );

        verify(categoriasRepository, times(1)).findById(any(Long.class));
        verify(categoriasRepository, times(1)).findByNameEqualsIgnoreCase(any(String.class));
        verify(categoriasRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void delete() {
        when(categoriasRepository.findById(any(Long.class))).thenReturn(Optional.of(categoria));
        categoriasService.delete(1L);
        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals("MOTOS", categoria.getName())
        );

        verify(categoriasRepository, times(1)).findById(any(Long.class));

    }
}