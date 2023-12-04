package com.madmotor.apimadmotordaw.rest.vehiculos.controller;


import com.madmotor.apimadmotordaw.rest.vehiculos.dto.VehiculoCreateDto;
import com.madmotor.apimadmotordaw.rest.vehiculos.dto.VehiculoUpdateDto;
import com.madmotor.apimadmotordaw.utils.pagination.PageResponse;
import com.madmotor.apimadmotordaw.rest.vehiculos.models.Vehiculo;
import com.madmotor.apimadmotordaw.rest.vehiculos.services.VehiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
/**
 * Controlador Rest de Vehiculos
 * Fija la ruta ${api.version}/vehiculos para todas las peticiones
 * @version 1.0
 */

@Slf4j
@RestController
@RequestMapping("${api.version}/vehiculos")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Vehiculos", description = "Vehiculos API")
public class VehiculoRestController {
    //Indicamos las depdenecia que vamos a usar
    private VehiculoService vehiculoService;
    //Inyectamos la dependencia
    @Autowired
    public VehiculoRestController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }
    /**
     * Metodo que devuelve todos los vehiculos de la base de datos de acuerdo a los criterios para filtrar
     * @Param marca De acuerdo a la marca
     * @Param categoria De acuerdo a la categoria
     * @Param modelo De acuerdo al modelo
     * @Param minYear De acuerdo al año minimo
     * @Param isDelete De acuerdo a si esta borrado o no
     * @Param kmMax De acuerdo al kilometraje maximo
     * @Param precioMax De acuerdo al precio maximo
     * @Param stockMin De acuerdo al stock minimo
     * @return List<Vehiculo> Lista de vehiculos paginada
     * Devuelve un 200 con la lista de vehiculos
     */
    @Operation(summary = "Obtener todos los vehiculos",description = "Obtiene todos los vehiculos de la base de datos de acuerdo a los criterios para filtrar")
    @Parameters(value = {
            @io.swagger.v3.oas.annotations.Parameter(name = "marca", description = "De acuerdo a la marca",example = "Audi"),
            @io.swagger.v3.oas.annotations.Parameter(name = "categoria", description = "De acuerdo a la categoria",example = "SUV"),
            @io.swagger.v3.oas.annotations.Parameter(name = "modelo", description = "De acuerdo al modelo",example = "A4"),
            @io.swagger.v3.oas.annotations.Parameter(name = "minYear", description = "De acuerdo al año minimo",example = "2010"),
            @io.swagger.v3.oas.annotations.Parameter(name = "isDelete", description = "De acuerdo a si esta borrado o no",example = "false"),
            @io.swagger.v3.oas.annotations.Parameter(name = "kmMax", description = "De acuerdo al kilometraje maximo",example = "100000"),
            @io.swagger.v3.oas.annotations.Parameter(name = "precioMax", description = "De acuerdo al precio maximo",example = "10000"),
            @io.swagger.v3.oas.annotations.Parameter(name = "stockMin", description = "De acuerdo al stock minimo",example = "10"),
            @io.swagger.v3.oas.annotations.Parameter(name = "page", description = "Pagina a mostrar",example = "0"),
            @io.swagger.v3.oas.annotations.Parameter(name = "size", description = "Numero de elementos por pagina",example = "10"),
            @io.swagger.v3.oas.annotations.Parameter(name = "sortBy", description = "Campo por el que ordenar",example = "id"),
            @io.swagger.v3.oas.annotations.Parameter(name = "direction", description = "Direccion de la ordenacion",example = "asc")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de vehiculos paginada"),
    })
    @GetMapping()
    public ResponseEntity<PageResponse<Vehiculo>> getAllVehiculos(
            @RequestParam(required = false) Optional<String> marca,
            @RequestParam(required = false) Optional<String> categoria,
            @RequestParam(required = false) Optional<String> modelo,
            @RequestParam(required = false) Optional<Integer> minYear,
            @RequestParam(required = false) Optional<Boolean> isDelete,
            @RequestParam(required = false) Optional<Double> kmMax,
            @RequestParam(required = false) Optional<Double> precioMax,
            @RequestParam(required = false) Optional<Integer> stockMin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction

    ) {
        log.info("Buscando todos los vehiculos por los parametros: marca: {}, categoria: {}, minYear: {}, isDelete: {}, kmMax: {}, precioMax: {}, stockMin: {}, page: {}, size: {}, sortBy: {}, direction: {}",
                marca, categoria, minYear, isDelete, kmMax, precioMax, stockMin, page, size, sortBy, direction);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(vehiculoService.findAll(marca, categoria, modelo, minYear, isDelete, kmMax, precioMax, stockMin, pageable), sortBy, direction));
    }
    /**
     * Metodo que devuelve un vehiculo de la base de datos de acuerdo a su id de tipo UUID
     * @Param id Identidicador unico del vehiculo de tipo UUID
     * @return Vehiculo Vehiculo
     * Devuelve un 200 con el vehiculo si lo encuentra
     * throws VehiculoNotFoundException si no encuentra el vehiculo 404
     */
    @Operation(summary = "Obtener un vehiculo por id",description = "Obtiene un vehiculo de la base de datos de acuerdo a su id de tipo UUID")
    @Parameters(value = {
            @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "Identidicador unico del vehiculo de tipo UUID",example = "123e4567-e89b-12d3-a456-426614174000",required = true)
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vehiculo"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vehiculo no encontrado")
    })

    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> getVehiculoById(@PathVariable UUID id) {
        log.info("Buscando vehiculo por id: " + id);
        return ResponseEntity.ok(vehiculoService.findById(id.toString()));
    }

    /**
     * Metodo que crea un vehiculo en la base de datos
     * @Param vehiculo VehiculoCreateDto con los datos necesarios para crear un vehiculo , en el caso de no pasarlos todos no dejara crearlo
     * @return Vehiculo Vehiculo
     * Devuelve un 200 con el vehiculo creado
     * throws CategoriaNotFoundException si no encuentra la categoria 404 indicada en el VehiculoCreateDto no deja crearlo
     */
    @Operation(summary = "Crear un vehiculo",description = "Crea un vehiculo en la base de datos")
    @Parameters(value = {
            @io.swagger.v3.oas.annotations.Parameter(name = "vehiculo", description = "VehiculoCreateDto con los datos necesarios para crear un vehiculo , en el caso de no pasarlos todos no dejara crearlo",required = true)
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vehiculo creado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Categoria no encontrada")
    })
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Vehiculo> createVehiculo(@Valid @RequestBody VehiculoCreateDto vehiculo) {
        log.info("Creando vehiculo: " + vehiculo);
        return ResponseEntity.ok(vehiculoService.save(vehiculo));
    }
    /**
     * Metodo que actualiza un vehiculo en la base de datos
     * @Param id Identidicador unico del vehiculo de tipo UUID
     * @Param vehiculo VehiculoUpdateDto con los datos necesarios para actualizar un vehiculo , en el caso de no pasarlos todos no dejara actualizarlo
     * @return Vehiculo Vehiculo
     * Devuelve un 200 con el vehiculo actualizado
     * throws VehiculoNotFoundException si no encuentra el vehiculo 404
     * throws CategoriaNotFoundException si no encuentra la categoria 404 indicada en el VehiculoUpdateDto usa la que ya tenía asignada
     */
    @Operation(summary = "Actualizar un vehiculo",description = "Actualiza un vehiculo en la base de datos")
    @Parameters(value = {
            @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "Identidicador unico del vehiculo de tipo UUID",example = "123e4567-e89b-12d3-a456-426614174000",required = true),
            @io.swagger.v3.oas.annotations.Parameter(name = "vehiculo", description = "VehiculoUpdateDto con los datos necesarios para actualizar un vehiculo , en el caso de no pasarlos todos no dejara actualizarlo",required = true)
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vehiculo actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vehiculo no encontrado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Vehiculo> updateVehiculo(@PathVariable UUID id, @Valid @RequestBody VehiculoUpdateDto vehiculoUpdateDto) {
        log.info("Actualizando vehiculo con id: " + id);
        return ResponseEntity.ok(vehiculoService.update(id.toString(), vehiculoUpdateDto));
    }
    /**
     * Metodo que actualiza parcialmente un vehiculo en la base de datos
     * @Param id Identidicador unico del vehiculo de tipo UUID
     * @Param vehiculo VehiculoUpdateDto con los datos necesarios para actualizar un vehiculo , en el caso de no pasarlos todos no dejara actualizarlo
     * @return Vehiculo Vehiculo
     * Devuelve un 200 con el vehiculo actualizado
     * throws VehiculoNotFoundException si no encuentra el vehiculo 404
     * throws CategoriaNotFoundException si no encuentra la categoria 404 indicada en el VehiculoUpdateDto usa la que ya tenía asignada
     */
    @Operation(summary = "Actualizar parcialmente un vehiculo",description = "Actualiza parcialmente un vehiculo en la base de datos")
    @Parameters(value = {
            @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "Identidicador unico del vehiculo de tipo UUID",example = "123e4567-e89b-12d3-a456-426614174000",required = true),
            @io.swagger.v3.oas.annotations.Parameter(name = "vehiculo", description = "VehiculoUpdateDto con los datos necesarios para actualizar un vehiculo , en el caso de no pasarlos todos no dejara actualizarlo",required = true)
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vehiculo actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vehiculo no encontrado")
    })

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Vehiculo> patchVehiculo(@PathVariable UUID id, @Valid @RequestBody VehiculoUpdateDto vehiculoUpdateDto) {
        log.info("Actualizando vehiculo de manera parcial, con id: " + id);
        return ResponseEntity.ok(vehiculoService.update(id.toString(), vehiculoUpdateDto));
    }
    /**
     * Metodo que borra un vehiculo en la base de datos
     * @Param id Identidicador unico del vehiculo de tipo UUID
     * Devuelve un 204
     * throws VehiculoNotFoundException si no encuentra el vehiculo 404
     */
    @Operation(summary = "Borrar un vehiculo",description = "Borra un vehiculo en la base de datos")
    @Parameters(value = {
            @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "Identidicador unico del vehiculo de tipo UUID",example = "123e4567-e89b-12d3-a456-426614174000",required = true)
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Vehiculo borrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vehiculo no encontrado")
    })

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVehiculo(@PathVariable UUID id) {
        log.info("Borrando vehiculo con id: " + id);
        vehiculoService.deleteById(id.toString());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    /**
     * Metodo que actualiza la imagen de un vehiculo en la base de datos
     * @Param id Identidicador unico del vehiculo de tipo UUID
     * @Param image MultipartFile con la imagen del vehiculo
     * @return Vehiculo Vehiculo
     * Devuelve un 200 con el vehiculo actualizado
     * throws VehiculoNotFoundException si no encuentra el vehiculo 404
     * throws StorageException si no se puede borrar la anterior  imagen 400 como error de servidor
     */
    @Operation(summary = "Actualizar la imagen de un vehiculo",description = "Actualiza la imagen de un vehiculo en la base de datos")
    @Parameters(value = {
            @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "Identidicador unico del vehiculo de tipo UUID",example = "123e4567-e89b-12d3-a456-426614174000",required = true),
            @io.swagger.v3.oas.annotations.Parameter(name = "image", description = "MultipartFile con la imagen del vehiculo",required = true)
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vehiculo actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vehiculo no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error de imagen en el Servidor")
    })
    @PatchMapping(value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Vehiculo> patchVehiculoImage(
            @PathVariable UUID id, @RequestPart("image") MultipartFile image) {

        log.info("Actualizando imagen de vehiculo con id: " + id);
        List<String> permittedContentTypes = List.of("image/png", "image/jpg", "image/jpeg", "image/gif");
        try {
            String contentType = image.getContentType();
            log.info("Content type: " + contentType);
            if (!image.isEmpty() && contentType != null && permittedContentTypes.contains(contentType.toLowerCase())){
                return ResponseEntity.ok(vehiculoService.updateImage(id.toString(), image, true));
            } else {
                return ResponseEntity.badRequest().build();
            }
        }catch (Exception e){
            log.error("Error al actualizar la imagen del vehiculo con id: " + id);
            return ResponseEntity.badRequest().build();
        }
    }


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
