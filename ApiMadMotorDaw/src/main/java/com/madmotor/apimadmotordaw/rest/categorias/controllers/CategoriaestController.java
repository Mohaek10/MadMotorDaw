package com.madmotor.apimadmotordaw.rest.categorias.controllers;


import com.madmotor.apimadmotordaw.rest.categorias.dto.CategoriaDto;
import com.madmotor.apimadmotordaw.rest.categorias.exceptions.CategoriaException;
import com.madmotor.apimadmotordaw.rest.categorias.exceptions.CategoriaNotFound;
import com.madmotor.apimadmotordaw.rest.categorias.models.Categoria;
import com.madmotor.apimadmotordaw.rest.categorias.services.CategoriaService;
import com.madmotor.apimadmotordaw.utils.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
/**
 * Controlador de categorias
 * Fijamos la ruta de acceso a este controlador con la anotación @RequestMapping
 *
 * @Autowired es una anotación que nos permite inyectar dependencias basadas  en las anotaciones @Controller, @Service, @Component, etc.
 * y que se encuentren en nuestro contenedor de Spring.
 */

@RestController
@Slf4j
@RequestMapping("${api.version}/categorias")

public class CategoriaestController {
    // Indicamos las dependencias que vamos a usar
    private final CategoriaService categoriasService;
    // Inyectamos las dependencias
    @Autowired
    public CategoriaestController(CategoriaService categoriasService) {
        this.categoriasService = categoriasService;
    }
    /**
     * Método que devuelve todas las categorias paginadas
     * @param nombre Nombre de la categoria
     * @param isDeleted Si la categoria esta borrada
     * @return Lista de categorias
     */

    @Operation(summary = "Obtiene todas las categorias",description = "Obtiene todas las categorias")
    @Parameters({
            @io.swagger.v3.oas.annotations.Parameter(name = "nombre", description = "Nombre de la categoria", example = "AUTOMATICO"),
            @io.swagger.v3.oas.annotations.Parameter(name = "isDeleted", description = "Validacion de la Categoria", example = "False"),
            @io.swagger.v3.oas.annotations.Parameter(name = "page", description = "Número de página", example = "0"),
            @io.swagger.v3.oas.annotations.Parameter(name = "size", description = "Número de elementos por página", example = "10"),
            @io.swagger.v3.oas.annotations.Parameter(name = "sort", description = "Campo por el que se ordena", example = "id"),
            @Parameter(name = "order", description = "Orden de la lista (asc o desc)", example = "asc")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de categorias"),
    })
    @GetMapping()
    public ResponseEntity<PageResponse<Categoria>> getAllCategorias(
            @RequestParam(required = false) Optional<String> nombre,
            @RequestParam(required = false) Optional<Boolean> isDeleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
            ) {
        log.info("Buscando todos los categorias por los parametros: nombre: {}, isDeleted: {}, page: {}, size: {}, sortBy: {}, direction: {}",
                nombre, isDeleted, page, size, sortBy, direction);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(categoriasService.findAll(nombre, isDeleted, pageable), sortBy, direction));

    }
    /**
     * Método que devuelve una categoria por su id
     * @param id Id de la categoria de tipo Long
     * @return Categoria
     * @throws CategoriaNotFound Excepcion que se lanza si no se encuentra la categoria
     */
    @Operation(summary = "Obtiene una categoria por su id",description = "Obtiene una categoria por su id")
    @Parameters({
            @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "Id de la categoria", example = "1")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Categoria encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Categoria no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoryById(@PathVariable Long id) {
        log.info("Buscando categoria por id: " + id);
        return ResponseEntity.ok(categoriasService.findById(id));
    }
    /**
     * Método que crea una categoria
     * @param categoriaCreateDto Categoria a crear
     * @Valid es una anotación que nos permite validar los campos de la categoria para evitar errores
     * @return Categoria creada
     * throws CategoriaExists Excepcion que se lanza si ya existe una categoria con ese nombre 409 (Conflict)
     */
    @Operation(summary = "Crea una categoria",description = "Crecion una categoria")
    @Parameters({
            @io.swagger.v3.oas.annotations.Parameter(name = "categoriaCreateDto", description = "Categoria a crear ", example = "AUTOMATICO")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Categoria creada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Categoria ya existe")
    })
    @PostMapping()
    public ResponseEntity<Categoria> createCategory(@Valid @RequestBody CategoriaDto categoriaCreateDto) {
        log.info("Creando categegoría: " + categoriaCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriasService.save(categoriaCreateDto));
    }
    /**
     * Método que actualiza una categoria
     * @param id Id de la categoria de tipo Long
     * @param categoriaUpdateDto Categoria a actualizar
     * @Valid es una anotación que nos permite validar los campos de la categoria para evitar errores
     * @return Categoria actualizada
     * @throws CategoriaNotFound Excepcion que se lanza si no se encuentra la categoria 404 (Not Found)
     */
    @Operation(summary = "Actualiza una categoria",description = "Actualiza una categoria")
    @Parameters({
            @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "Id de la categoria", example = "1"),
            @io.swagger.v3.oas.annotations.Parameter(name = "categoriaUpdateDto", description = "Categoria a actualizar", example = "AUTOMATICO")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Categoria actualizada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Categoria no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Categoria ya existente")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoriaDto categoriaUpdateDto) {
        log.info("Actualizando categoria por id: " + id + " con categoria: " + categoriaUpdateDto);
        return ResponseEntity.ok(categoriasService.update(id, categoriaUpdateDto));
    }
    /**
     * Método que borra una categoria
     * @param id Id de la categoria de tipo Long
     * @return Categoria borrada
     * @throws CategoriaNotFound Excepcion que se lanza si no se encuentra la categoria 404 (Not Found)
     * @throws CategoriaException Excepcion que se lanza si no se puede borrar la categoria 409 (Conflict)
     */
    @Operation(summary = "Borra una categoria",description = "Borra una categoria")
    @Parameters({
            @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "Id de la categoria", example = "1")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Categoria borrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Categoria no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Categoria no se puede borrar")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("Borrando categoria por id: " + id);
        categoriasService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
