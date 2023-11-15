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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Page<Categoria> findAll(Optional<String> nombre, Optional<Boolean> isDeleted, Pageable pageable) {
        log.info("Buscando todos las categorias con nombre: " + nombre + " y borrados: " + isDeleted);
        Specification<Categoria> specNombreCategoria = (root, query, criteriaBuilder) ->
                nombre.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + m + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Categoria> specIsDeleted = (root, query, criteriaBuilder) ->
                isDeleted.map(m -> criteriaBuilder.equal(root.get("isDeleted"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Categoria> criterio = Specification.where(specNombreCategoria)
                .and(specIsDeleted);

        return categoriaRepository.findAll(criterio, pageable);
    }


    @Override
    @Cacheable
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(()->new CategoriaNotFound("No se encontro la categoria con el id "+id))
        ;
    }

    @Override
    @Cacheable
    public Categoria findByName(String name) {
        return categoriaRepository.findByNameEqualsIgnoreCase(name)
                .orElseThrow(()->new CategoriaNotFound("No se encontro la categoria con el nombre "+name));
    }

    @Override
    @CachePut
    public Categoria save(CategoriaDto categoria) {
       if (categoriaRepository.findByNameEqualsIgnoreCase(categoria.getName()).isEmpty()){
           Categoria cat =categoriaMapper.map(categoria);
           return categoriaRepository.save(cat);
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
        if (categoriaRepository.findById(id).isEmpty()) {
            throw new CategoriaNotFound("No se encontro la categoria con el id " + id);
        }
        categoriaRepository.updateIsDeletedToTrueById(id);

    }
}
