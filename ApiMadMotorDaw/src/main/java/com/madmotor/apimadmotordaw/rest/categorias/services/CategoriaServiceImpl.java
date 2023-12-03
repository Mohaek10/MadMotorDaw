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
/*
 * Implementación de la interfaz CategoriaService que contiene los métodos de la lógica de negocio
 * Aplicacion de Cache
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "categorias")
public class CategoriaServiceImpl implements CategoriaService {
    // Indicacion de las dependencias  que se van a usar
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;
    // Inyeccion de dependencias
    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }
    /*
     * Método que devuelve todas las categorias con paginacion
     * @param nombre el nombre de la categoria
     * @param isDeleted si la categoria esta borrada o no
     * @param pageable la paginacion
     * @return Page<Categoria>
     */


    @Override
    public Page<Categoria> findAll(Optional<String> nombre, Optional<Boolean> isDeleted, Pageable pageable) {
        log.info("Buscando todos las categorias con nombre: " + nombre + " y borrados: " + isDeleted);
        // Especificacion de los criterios de busqueda
        // Si no se pasa el nombre, se devuelve true
        //Criterio de busqueda por nombre
        Specification<Categoria> specNombreCategoria = (root, query, criteriaBuilder) ->
                nombre.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + m + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        //Criterio de busqueda por borrado
        // Si no se pasa el borrado, se devuelve true
        Specification<Categoria> specIsDeleted = (root, query, criteriaBuilder) ->
                isDeleted.map(m -> criteriaBuilder.equal(root.get("isDeleted"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        // Se concatenan los criterios
        Specification<Categoria> criterio = Specification.where(specNombreCategoria)
                .and(specIsDeleted);
        // Se devuelve la lista de categorias
        return categoriaRepository.findAll(criterio, pageable);
    }
    /*
     * Método que devuelve una categoria por su id tipo Long si no se encuentra devuelve una excepcion
     * @param id el id de la categoria
     * @return Categoria
     * @throws CategoriaNotFound si no se encuentra la categoria 404
     */

    @Override
    @Cacheable
    public Categoria findById(Long id) {
        // Se busca la categoria por su id y si no se encuentra se devuelve una excepcion
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNotFound("No se encontro la categoria con el id " + id))
                ;
    }
    /*
     * Método que devuelve una categoria por su nombre si no se encuentra devuelve una excepcion
     * @param name el nombre de la categoria
     * @return Categoria la categoria encontrada
     * @throws CategoriaNotFound si no se encuentra la categoria 404
     */
    @Override
    @Cacheable
    public Categoria findByName(String name) {
        // Se busca la categoria por su nombre y si no se encuentra se devuelve una excepcion
        return categoriaRepository.findByNameEqualsIgnoreCase(name)
                .orElseThrow(() -> new CategoriaNotFound("No se encontro la categoria con el nombre " + name));
    }
    /*
     * Método que guarda una categoria si ya existe una categoria con ese nombre devuelve una excepcion
     * @param categoria la categoria a guardar basado en los datos necesarios del DTO
     * @return Categoria la categoria guardada
     * @throws CategoriaExists si ya existe una categoria con ese nombre
     */
    @Override
    @CachePut
    public Categoria save(CategoriaDto categoria) {
        // Se comprueba si ya existe una categoria con ese nombre
        if (categoriaRepository.findByNameEqualsIgnoreCase(categoria.getName()).isEmpty()) {
            // Se mapea el DTO a la entidad
            Categoria cat = categoriaMapper.map(categoria);
            // Se guarda la categoria
            return categoriaRepository.save(cat);
        } else {
            // Si ya existe se devuelve una excepcion
            throw new CategoriaExists("Ya existe una categoria con el nombre " + categoria.getName());
        }

    }
    /*
     * Método que actualiza una categoria por su id si no se encuentra devuelve una excepcion
     * @param id el id de la categoria a actualizar
     * @param catDto la categoria a actualizar basado en los datos necesarios del DTO
     * @return Categoria la categoria actualizada
     * @throws CategoriaExists si ya existe una categoria con ese nombre 409
     * @throws CategoriaNotFound si no se encuentra la categoria 404
     */
    @Override
    @CachePut
    public Categoria update(Long id, CategoriaDto catDto) {
        // Se comprueba si ya existe una categoria con ese nombre y si no lanza una excepcion 404 not found
        Categoria categoriaActual = findById(id);
        // Se comprueba si ya existe una categoria con ese nombre y si no lanza una excepcion 409 conflict
        categoriaRepository.findByNameEqualsIgnoreCase(catDto.getName()).ifPresent(c -> {
            // Se comprueba si la categoria que se va a actualizar es la misma que la que ya existe
            if (!c.getId().equals(id)) {
                // Si ya existe se devuelve una excepcion
                throw new CategoriaExists("Ya existe una categoría con el nombre " + catDto.getName());
            }
        });
        // Actualizamos los datos
        return categoriaRepository.save(categoriaMapper.map(catDto, categoriaActual));
    }
    /*
     * Método que borra una categoria por su id si no se encuentra devuelve una excepcion
     * @param id el id de la categoria a borrar
     * @throws CategoriaExists si ya existe una categoria con ese nombre 409
     * @throws CategoriaNotFound si no se encuentra la categoria 404
     */
    @Override
    @CacheEvict
    @Transactional
    public void delete(Long id) {
        // Se comprueba si ya existe una categoria con ese nombre y si no lanza una excepcion 404 not found
        Categoria categoria = findById(id);
        // Se comprueba si la categoria tiene vehiculos asociados y si los tiene lanza una excepcion 409 conflict
        if (categoriaRepository.existeVehiculoByUd(id)) {
            log.warn("No se puede borrar la categoría con id: " + id + " porque tiene vehiculos asociados");
            throw new CategoriaExists("No se puede borrar la categoría con id " + id + " porque tiene vehiculos asociados");
        } else {
            // Se borra la categoria
            categoriaRepository.deleteById(id);
        }
    }

}
