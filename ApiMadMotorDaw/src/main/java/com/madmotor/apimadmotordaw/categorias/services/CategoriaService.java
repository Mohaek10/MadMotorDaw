package com.madmotor.apimadmotordaw.categorias.services;



import com.madmotor.apimadmotordaw.categorias.dto.CategoriaDto;
import com.madmotor.apimadmotordaw.categorias.models.Categoria;

import java.util.List;

public interface CategoriaService
{
    List<Categoria> findAll(String name);
    Categoria findById(Long id);
    Categoria findByName(String name);
    Categoria save(Categoria categoria);
    Categoria update(Long id, CategoriaDto catDto);

    void delete(Long id);
}
