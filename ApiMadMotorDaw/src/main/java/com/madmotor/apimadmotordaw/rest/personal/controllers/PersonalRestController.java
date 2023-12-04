package com.madmotor.apimadmotordaw.rest.personal.controllers;

import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalCreateDTO;
import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalResponseDTO;
import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalUpdateDTO;
import com.madmotor.apimadmotordaw.rest.personal.exceptions.PersonalNotFound;
import com.madmotor.apimadmotordaw.rest.personal.services.PersonalService;

import com.madmotor.apimadmotordaw.utils.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
/**
 * Controlador del personal del tipo RestController
 *
 * Con esta clase fijamos la ruta de acceso a este controlador con la anotación @RequestMapping
 *
 * @Autowired es una anotación que nos permite inyectar dependencias en las anotaciones @Controller, @Service, @Component, ...
 * que se encuentren en nuestro contenedor Spring.
 *
 * @version 1.0
 * @author Miguel Vicario
*/
@RestController
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("${api.version}/personal")

public class PersonalRestController {
    private final PersonalService personalService;

    @Autowired
    public PersonalRestController(PersonalService personalService) {
        this.personalService = personalService;
    }

    /**
     * Obtiene todo el personal por su id
     *
     * @param id id del personal
     * @return ids del personal encontrado
     * @throws PersonalNotFound si no se encuentra el personal con el id (404)
     */

    @Operation(summary = "Obtiene todo el personal por su id")
    @Parameters(value = {
            @Parameter(name = "id", description = "Id del personal", example = "1L")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Personal encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Personal no encontrado")
    })

    @GetMapping("/{id}")
    public ResponseEntity<PersonalResponseDTO> getPersonalById(@PathVariable @Valid Long id) {
        log.info("Buscando personal por id: " + id);
        return ResponseEntity.ok(personalService.findById(id));

    }

    /**
     * Crea un nuevo personal
     *
     * @param personalCreateDTO personal a crear basado en el DTO de creación del personal
     * @return los parámetros del personal creado
     * @throws com.madmotor.apimadmotordaw.rest.personal.exceptions.PersonalFailSave si el personal no se ha podido crear
     */

    @Operation(summary = "Crea un nuevo personal")
    @Parameters(value = {
            @Parameter(name = "personalCreateDTO", description = "Personal a crear", example = "PersonalCreateDTO")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Personal creado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error de validación")
    })

    @PostMapping()

    public ResponseEntity<PersonalResponseDTO> createPersonal(@Valid @RequestBody PersonalCreateDTO personalCreateDTO) {
        log.info("Creando personal: " + personalCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(personalService.save(personalCreateDTO));
    }

    /**
     * Obtiene todo el personal
     *
     * @param dni dni del personal
     * @param nombre nombre del personal
     * @param apellidos apellidos del personal
     * @param fechaNacimiento fecha de nacimiento del personal
     * @param direccion direccion del personal
     * @param iban iban del personal
     * @param sueldo sueldo del personal
     * @param telefono telefono del personal
     * @param page numero de pagina
     * @param size tamaño de la pagina
     * @param sortBy ordenar por
     * @param order orden ascendente o descendente
     * @return todos los parámetros del personal encontrado
     */

    @Operation(summary = "Obtiene todos los parámetros del personal")
    @Parameters(value = {
            @Parameter(name = "dni", description = "Dni del personal", example = "12345678A"),
            @Parameter(name = "nombre", description = "Nombre del personal", example = "Miguel"),
            @Parameter(name = "apellidos", description = "Apellidos del personal", example = "Fernández López"),
            @Parameter(name = "fechaNacimiento", description = "Fecha de nacimiento del personal", example = "1999-12-12"),
            @Parameter(name = "direccion", description = "Direccion del personal", example = "Calle Falsa 123"),
            @Parameter(name = "iban", description = "Iban del personal", example = "ES1234567891234567891234"),
            @Parameter(name = "sueldo", description = "Sueldo del personal", example = "1000.0"),
            @Parameter(name = "telefono", description = "Telefono del personal", example = "123456789"),
            @Parameter(name = "page", description = "Número de página", example = "1"),
            @Parameter(name = "size", description = "Tamaño de la página", example = "10"),
            @Parameter(name = "sortBy", description = "Ordenar por", example = "id"),
            @Parameter(name = "order", description = "Orden ascendente o descendente", example = "asc")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Personal encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Personal no encontrado")
    })

    @GetMapping()
    public ResponseEntity<PageResponse<PersonalResponseDTO>> getAllPersonal(
            @RequestParam (required = false) Optional<String> dni,
            @RequestParam (required = false) Optional<String> nombre,
            @RequestParam (required = false) Optional<String> apellidos,
            @RequestParam (required = false) Optional<String> fechaNacimiento,
            @RequestParam (required = false) Optional<String> direccion,
            @RequestParam (required = false) Optional<String> iban,
            @RequestParam (required = false) Optional<Double> sueldo,
            @RequestParam (required = false) Optional<String> telefono,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order
    ){
        log.info("Buscando personal con los siguientes filtros:");
        Sort sort =order.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page,size,sort);
        return ResponseEntity.ok(PageResponse.of(personalService.findAll(dni, nombre, apellidos, fechaNacimiento, direccion, iban, sueldo, telefono, pageable), sortBy, order));

    }

    /**
     * Actualiza un personal
     * @param id id del personal
     * @param personalUpdateDTO
     * @return los parámetros del personal actualizado
     * @throws PersonalNotFound si no se encuentra el personal con el id proporcionado (404)
     */

    @Operation(summary = "Actualiza un personal")
    @Parameters(value = {
            @Parameter(name = "id", description = "Id del personal", example = "1L"),
            @Parameter(name = "personalUpdateDTO", description = "Personal a actualizar", example = "PersonalUpdateDTO")
    })

    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Personal actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Personal no encontrado")
    })

    @PutMapping("/{id}")
    public ResponseEntity<PersonalResponseDTO> updatePersonalById(@PathVariable @Valid Long id, @RequestBody @Valid PersonalUpdateDTO personalUpdateDTO) {
        log.info("Actualizando personal con id: " + id);
        return ResponseEntity.ok(personalService.update(id, personalUpdateDTO));
    }

    /**
     * Elimina a un personal
     * @param id id del personal
     * @return el personal borrado
     * @throws PersonalNotFound si no se encuentra el personal con el id proporcionado (404)
     */

    @Operation(summary = "Elimina a un personal")
    @Parameters(value = {
            @Parameter(name = "id", description = "Id del personal", example = "1L")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Personal eliminado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Personal no encontrado")
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonalById(@PathVariable @Valid Long id) {
        log.info("Borrando personal con id: " + id);
        try{
            personalService.findById(id);
        } catch (Exception e) {
            PersonalNotFound personalNotFound = new PersonalNotFound("Personal no encontrado");
            return ResponseEntity.notFound().build();
        }
        personalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Manejador de excepciones
     * @param ex excepcion
     * @return  Mapa de errores de validación con el campo y el mensaje
     */

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}