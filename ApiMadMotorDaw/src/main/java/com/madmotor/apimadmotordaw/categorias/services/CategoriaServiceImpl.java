package com.madmotor.apimadmotordaw.categorias.services;

import com.madmotor.apimadmotordaw.categorias.dto.CategoriaDto;
import com.madmotor.apimadmotordaw.categorias.exceptions.CategoriaExists;
import com.madmotor.apimadmotordaw.categorias.exceptions.CategoriaNotFound;
import com.madmotor.apimadmotordaw.categorias.mapper.CategoriaMapper;
import com.madmotor.apimadmotordaw.categorias.models.Categoria;
import com.madmotor.apimadmotordaw.categorias.repositories.CategoriaRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@CacheConfig(cacheNames = "categorias")
public class CategoriaServiceImpl implements CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }


    @Override
    public List<Categoria> findAll(String name) {
        if(name!=null){
            return categoriaRepository.findAllByNameContainsIgnoreCase(name);
        }else {
            return categoriaRepository.findAll();
        }
    }

    @Override
    @Cacheable
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(()->new CategoriaNotFound("No se encontro la categoria con el id "+id))
        ;
    }

    @Override
    public Categoria findByName(String name) {
        return categoriaRepository.findByNameEqualsIgnoreCase(name)
                .orElseThrow(()->new CategoriaNotFound("No se encontro la categoria con el nombre "+name));
    }

    @Override
    @CachePut
    public Categoria save(Categoria categoria) {
       if (categoriaRepository.findByNameEqualsIgnoreCase(categoria.getName()).isEmpty()){
           return categoriaRepository.save(categoria);
       }else{
           throw new CategoriaExists("Ya existe una categoria con el nombre "+categoria.getName());
       }

    }

    @Override
    @CachePut
    public Categoria update(Long id, CategoriaDto catDto) {
        Categoria categoriaActual=findById(id);
        if (categoriaRepository.findByNameEqualsIgnoreCase(catDto.getName()).isEmpty()){

            return categoriaRepository.save(categoriaMapper.map(catDto,categoriaActual));
    }else {
            throw new CategoriaExists("Ya existe una categoria con el nombre "+catDto.getName());
        }
    }

    @Override
    @CacheEvict
    public void delete(Long id) {
        Categoria categoria=findById(id);
        categoriaRepository.updateIsDeletedToTrueById(id);

    }
}
