package com.madmotor.apimadmotordaw.categorias.services;



import com.madmotor.apimadmotordaw.categorias.dto.CategoriaDto;
import com.madmotor.apimadmotordaw.categorias.models.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoriaService
{
    Page<Categoria> findAll(Optional<String> nombre, Optional<Boolean> isDeleted, Pageable pageable);

    Categoria findById(Long id);
    Categoria findByName(String name);
    Categoria save(CategoriaDto categoriaDto);
    Categoria update(Long id, CategoriaDto catDto);

    void delete(Long id);
}
