package com.madmotor.apimadmotordaw.rest.categorias.controllers;


import com.madmotor.apimadmotordaw.rest.categorias.dto.CategoriaDto;
import com.madmotor.apimadmotordaw.rest.categorias.models.Categoria;
import com.madmotor.apimadmotordaw.rest.categorias.services.CategoriaService;
import com.madmotor.apimadmotordaw.utils.pagination.PageResponse;
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


@RestController
@Slf4j
@RequestMapping("${api.version}/categorias")

public class CategoriaestController {
    private final CategoriaService categoriasService;

    @Autowired
    public CategoriaestController(CategoriaService categoriasService) {
        this.categoriasService = categoriasService;
    }

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
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoryById(@PathVariable Long id) {
        log.info("Buscando categoria por id: " + id);
        return ResponseEntity.ok(categoriasService.findById(id));
    }
    @PostMapping()
    public ResponseEntity<Categoria> createCategory(@Valid @RequestBody CategoriaDto categoriaCreateDto) {
        log.info("Creando categegoría: " + categoriaCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriasService.save(categoriaCreateDto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoriaDto categoriaUpdateDto) {
        log.info("Actualizando categoria por id: " + id + " con categoria: " + categoriaUpdateDto);
        return ResponseEntity.ok(categoriasService.update(id, categoriaUpdateDto));
    }
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
