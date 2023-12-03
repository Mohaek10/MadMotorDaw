package com.madmotor.apimadmotordaw.rest.categorias.services;

import com.madmotor.apimadmotordaw.rest.categorias.dto.CategoriaDto;
import com.madmotor.apimadmotordaw.rest.categorias.exceptions.CategoriaExists;
import com.madmotor.apimadmotordaw.rest.categorias.exceptions.CategoriaNotFound;
import com.madmotor.apimadmotordaw.rest.categorias.mapper.CategoriaMapper;
import com.madmotor.apimadmotordaw.rest.categorias.models.Categoria;
import com.madmotor.apimadmotordaw.rest.categorias.repositories.CategoriaRepository;
import jakarta.transaction.Transactional;
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
                nombre.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + m + "%"))
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
                .orElseThrow(() -> new CategoriaNotFound("No se encontro la categoria con el id " + id))
                ;
    }

    @Override
    @Cacheable
    public Categoria findByName(String name) {
        return categoriaRepository.findByNameEqualsIgnoreCase(name)
                .orElseThrow(() -> new CategoriaNotFound("No se encontro la categoria con el nombre " + name));
    }

    @Override
    @CachePut
    public Categoria save(CategoriaDto categoria) {
        if (categoriaRepository.findByNameEqualsIgnoreCase(categoria.getName()).isEmpty()) {
            Categoria cat = categoriaMapper.map(categoria);
            return categoriaRepository.save(cat);
        } else {
            throw new CategoriaExists("Ya existe una categoria con el nombre " + categoria.getName());
        }

    }

    @Override
    @CachePut
    public Categoria update(Long id, CategoriaDto catDto) {
        Categoria categoriaActual = findById(id);
        categoriaRepository.findByNameEqualsIgnoreCase(catDto.getName()).ifPresent(c -> {
            if (!c.getId().equals(id)) {
                throw new CategoriaExists("Ya existe una categoría con el nombre " + catDto.getName());
            }
        });
        // Actualizamos los datos
        return categoriaRepository.save(categoriaMapper.map(catDto, categoriaActual));
    }

    @Override
    @CacheEvict
    @Transactional
    public void delete(Long id) {
        Categoria categoria = findById(id);
        if (categoriaRepository.existeVehiculoByUd(id)) {
            log.warn("No se puede borrar la categoría con id: " + id + " porque tiene vehiculos asociados");
            throw new CategoriaExists("No se puede borrar la categoría con id " + id + " porque tiene vehiculos asociados");
        } else {
            categoriaRepository.deleteById(id);

        }
    }

}
