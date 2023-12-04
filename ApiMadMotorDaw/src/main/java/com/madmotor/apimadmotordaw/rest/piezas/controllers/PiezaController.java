package com.madmotor.apimadmotordaw.rest.piezas.controllers;

import com.madmotor.apimadmotordaw.rest.piezas.dto.PiezaCreateDTO;
import com.madmotor.apimadmotordaw.rest.piezas.dto.PiezaResponseDTO;
import com.madmotor.apimadmotordaw.rest.piezas.dto.PiezaUpdateDTO;
import com.madmotor.apimadmotordaw.rest.piezas.models.Pieza;
import com.madmotor.apimadmotordaw.rest.piezas.services.PiezaService;
import com.madmotor.apimadmotordaw.rest.vehiculos.models.Vehiculo;
import com.madmotor.apimadmotordaw.utils.pagination.PageResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
/**
 * Controlador de la pieza del tipo RestController
 *
 * Con esta clase fijamos la ruta de acceso a este controlador con la anotación @RequestMapping
 *
 * @Autowired es una anotación que nos permite inyectar dependencias en las anotaciones @Controller, @Service, @Component, ...
 * que se encuentren en nuestro contenedor Spring.
 *
 * @version 1.0
 * @author Rubén Fernández
 */

@RestController
@Slf4j
@RequestMapping("${api.version}/piezas")
public class PiezaController {
    private final PiezaService piezaService;


    @Autowired
    public PiezaController(PiezaService piezaService) {
        this.piezaService = piezaService;

    }

    /**
     * Obtiene todas las piezas
     *
     * @param name nombre de la pieza
     * @param description descripción de la pieza
     * @param price precio de la pieza
     * @param stock stock de la pieza
     * @param page número de página
     * @param size tamaño de la página
     * @param sortBy campo por el que se ordena
     * @param order orden ascendente o descendente
     * @return todos los parámetros de las piezas encontradas
     */
    @GetMapping()
    public ResponseEntity<PageResponse<PiezaResponseDTO>> getAllPiezas(
            @RequestParam (required = false) Optional<String> name,
            @RequestParam (required = false) Optional<String> description,
            @RequestParam (required = false) Optional<Double> price,
            @RequestParam (required = false) Optional<Integer> stock,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order
    ){
        log.info("Buscando piezas con los siguientes filtros:"+name+" "+description+" "+price+" "+stock);
        Sort sort =order.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page,size,sort);
        return ResponseEntity.ok(PageResponse.of(piezaService.findAll(name,description,price,stock, pageable), sortBy,order));

    }

    /**
     * Obtiene todas las piezas por su id
     *
     * @param id id de la pieza
     * @return ids de las piezas encontradas
     * @throws com.madmotor.apimadmotordaw.rest.piezas.exceptions.PiezaNotFound si no se encuentra la pieza con el id (404)
     */

    @GetMapping("/{id}")
    public ResponseEntity<PiezaResponseDTO> getPiezaById(@PathVariable @Valid UUID id) {
        log.info("Buscando pieza por id: " + id);
        return ResponseEntity.ok(piezaService.findById(id));
    }

    /**
     * Crea una nueva pieza
     *
     * @param piezaCreateDTO pieza a crear basado en el DTO
     * @return los parámetros de la pieza creada
     * @throws com.madmotor.apimadmotordaw.rest.piezas.exceptions.PiezaNotFound si la pieza no se ha podido crear (404)
     */
    @PostMapping()

    public ResponseEntity<PiezaResponseDTO> createPieza(@Valid @RequestBody PiezaCreateDTO piezaCreateDTO) {
        log.info("Creando pieza: " + piezaCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(piezaService.save(piezaCreateDTO));
    }

    /**
     * Actualiza una pieza
     *
     * @param id id de la pieza
     * @param piezaUpdateDTO pieza a actualizar basado en el DTO
     * @return los parámetros de la pieza actualizada
     * @throws com.madmotor.apimadmotordaw.rest.piezas.exceptions.PiezaNotFound si la pieza no se ha podido actualizar (404)
     */
    @PutMapping("/{id}")

    public ResponseEntity<PiezaResponseDTO> updatePieza(@PathVariable UUID id, @Valid @RequestBody PiezaUpdateDTO piezaUpdateDTO) {
        log.info("Actualizando pieza por id: " + id + " con pieza: " + piezaUpdateDTO);
        return ResponseEntity.ok(piezaService.update(id, piezaUpdateDTO));
    }

    /**
     * Actualiza parcialmente una pieza
     *
     * @param id id de la pieza
     * @param piezaUpdateDTO pieza a actualizar basado en el DTO
     * @return los parámetros de la pieza actualizada
     * @throws com.madmotor.apimadmotordaw.rest.piezas.exceptions.PiezaNotFound si la pieza no se ha podido actualizar (404)
     */

    @PatchMapping("/{id}")
    public ResponseEntity<PiezaResponseDTO> updatePartialPieza(@PathVariable UUID id, @Valid @RequestBody PiezaUpdateDTO piezaUpdateDTO) {
        log.info("Actualizando parcialmente pieza por id: " + id + " con pieza: " +piezaUpdateDTO );
        return ResponseEntity.ok(piezaService.update(id, piezaUpdateDTO));
    }

    /**
     * Borra una pieza
     *
     * @param id id de la pieza
     * @return 204 si la pieza se ha borrado correctamente
     * @throws com.madmotor.apimadmotordaw.rest.piezas.exceptions.PiezaNotFound si la pieza no se ha podido borrar (404)
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePieza(@PathVariable @Valid UUID id) {
        log.info("Borrando pieza por id: " + id);
        piezaService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Actualiza la imagen de una pieza
     *
     * @param id id de la pieza
     * @param image imagen de la pieza
     * @return los parámetros de la pieza actualizada
     * @throws com.madmotor.apimadmotordaw.rest.piezas.exceptions.PiezaNotFound si la pieza no se ha podido actualizar (404)
     */
    @PatchMapping(value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Pieza> patchVehiculoImage(
            @PathVariable UUID id, @RequestPart("image") MultipartFile image) {

        log.info("Actualizando imagen de vehiculo con id: " + id);
        List<String> permittedContentTypes = List.of("image/png", "image/jpg", "image/jpeg", "image/gif");
        try {
            String contentType = image.getContentType();
            log.info("Content type: " + contentType);
            if (!image.isEmpty() && contentType != null && permittedContentTypes.contains(contentType.toLowerCase())){
                return ResponseEntity.ok(piezaService.updateImage(id.toString(), image, true));
            } else {
                return ResponseEntity.badRequest().build();
            }
        }catch (Exception e){
            log.error("Error al actualizar la imagen del vehiculo con id: " + id);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Manejador de excepciones de validación
     * @param ex excepción
     * @return Mapa de errores de validación con el campo y el mensaje
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
