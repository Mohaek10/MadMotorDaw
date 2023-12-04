package com.madmotor.apimadmotordaw.rest.clientes.Controller;

import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteCreateRequest;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteReponse;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteUpdateRequest;
import com.madmotor.apimadmotordaw.rest.clientes.exceptions.ClienteFailSave;
import com.madmotor.apimadmotordaw.rest.clientes.exceptions.ClienteNotFound;
import com.madmotor.apimadmotordaw.rest.clientes.services.ClienteService;
import com.madmotor.apimadmotordaw.utils.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
/**
 * Controlador de clientes del tipo RestController
 *
 * Fijamos la ruta de acceso a este controlador con la anotación @RequestMapping
 *
 * @Autowired es una anotación que nos permite inyectar dependencias basadas  en las anotaciones @Controller, @Service, @Component, etc.
 * y que se encuentren en nuestro contenedor de Spring.
 *
 * @version 1.0
 * @author Joe Brandon
 */

@RestController
@RequestMapping("${api.version}/clientes")
@PreAuthorize("hasRole('ADMIN')")
public class ClienteRestController {
    private final ClienteService clienteService;

    @Autowired
    public ClienteRestController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    /**
     * Obtiene todos los productos
     *
     * @param nombre nombre del cliente
     * @param apellido apellido del cliente
     * @param direccion direccion del cliente
     * @param codigoPostal codigo postal del cliente
     * @return Pagina encontradas con los filtros de Cliente
     */

     @Operation(summary = "Obtiene todos los clientes", description = "Obtiene todos los clientes mediante un listado paginado y filtrado")
     @Parameters({
                @Parameter(name = "nombre", description = "Nombre del Cliente", example = "Juan"),
                @Parameter(name = "apellido", description = "Apellido del Cliente", example = "Perez"),
                @Parameter(name = "direccion", description = "Direccion del Cliente", example = "Calle Principal "),
                @Parameter(name = "codigoPostal", description = "Codigo Postal del Cliente", example = "12345"),
                @Parameter(name = "page", description = "Número de página", example = "0"),
                @Parameter(name = "size", description = "Número de elementos por página", example = "10"),
                @Parameter(name = "sort", description = "Campo por el que se ordena", example = "id"),
                @Parameter(name = "order", description = "Orden de la lista (asc o desc)", example = "asc")
     })

    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Clientes encontrados")
    })

    @GetMapping()
    public ResponseEntity<PageResponse<ClienteReponse>> getAllClientes(
            @RequestParam (required = false) Optional<String> nombre,
            @RequestParam (required = false) Optional<String> apellido,
            @RequestParam (required = false) Optional<String> direccion,
            @RequestParam (required = false) Optional<Integer> codigoPostal,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order
    ){
        Sort sort = order.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page,size,sort);
        return ResponseEntity.ok(PageResponse.of(clienteService.findAll(nombre, apellido, direccion, codigoPostal, pageable),sortBy,order));
    }

    /**
     * Obtiene un cliente por su ID
     *
     * @param id UUID del cliente
     * @return Cliente encontrado
     * @throws ClienteNotFound si no existe el cliente con el UUID proporcionado 404
     */
    @Operation(summary = "Obtiene un cliente por su ID",description = "Obtiene un cliente mediante su ID")
    @Parameters({
            @Parameter(name = "id", description = "UUID del cliente", example = "123e4567-e89b-12d3-a456-426614174000")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Clientes encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Clientes no encontrado")

    })

    @GetMapping("/{id}")
    public ResponseEntity<ClienteReponse> getClienteByID(@PathVariable UUID id){
        return ResponseEntity.ok(clienteService.findByID(id));
    }
    /**
     * Crea un nuevo cliente
     *
     * @param clienteCreateRequest cliente a crear basado en el ClienteCreateRequest de cliente
     * @return Cliente creado
     * @throws ClienteFailSave si el cliente no es correcto 400
     */
    @Operation(summary = "Crea un nuevo cliente",description = "Crea un nuevo cliente mediante un CreateDto")
    @Parameters({
            @Parameter(name = "clienteCreateRequest", description = "Cliente a crear", example = "ClienteCreateRequest")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Cliente creado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Cliente no creado")
    })
    @PostMapping
    public ResponseEntity<ClienteReponse> createCliente(@Valid @RequestBody ClienteCreateRequest clienteCreateRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.savePost((clienteCreateRequest)));
    }
    /**
     * Actualiza un cliente
     *
     * @param id UUID del cliente
     * @param clienteUpdateRequest cliente a actualizar basado en el ClienteUpdateRequest de cliente
     * @return Cliente actualizado
     * @throws ClienteNotFound si no existe el cliente con el UUID proporcionado 404
     */
    @Operation(summary = "Actualiza un cliente",description = "Actualiza un cliente mediante un UpdateDto")
    @Parameters({
            @Parameter(name = "id", description = "UUID del cliente", example = "123e4567-e89b-12d3-a456-426614174000"),
            @Parameter(name = "clienteUpdateRequest", description = "Cliente a actualizar", example = "ClienteUpdateRequest")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cliente actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })

    @PutMapping("/{id}")
    public ResponseEntity<ClienteReponse> updateCliente(@PathVariable UUID id, @Valid@RequestBody ClienteUpdateRequest clienteUpdateRequest){
        return ResponseEntity.ok(clienteService.updateByID(id,clienteUpdateRequest));
    }
    /**
     * Actualiza un cliente
     *
     * @param id UUID del cliente
     * @param clienteUpdateRequest cliente a actualizar basado en el ClienteUpdateRequest de cliente de manera parcial
     * @return Cliente actualizado
     * @throws ClienteNotFound si no existe el cliente con el UUID proporcionado 404
     */
    @Operation(summary = "Actualiza un cliente",description = "Actualiza un cliente mediante un UpdateDto de manera parcial")
    @Parameters({
            @Parameter(name = "id", description = "UUID del cliente", example = "123e4567-e89b-12d3-a456-426614174000"),
            @Parameter(name = "clienteUpdateRequest", description = "Cliente a actualizar", example = "ClienteUpdateRequest")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cliente actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })


    @PatchMapping("/{id}")
    public ResponseEntity<ClienteReponse>patchClienteUpdate(@PathVariable UUID id, @RequestBody ClienteUpdateRequest clienteUpdateRequest){
        return ResponseEntity.ok(clienteService.updateByID(id,clienteUpdateRequest));
    }
    /**
     * Elimina un cliente
     *
     * @param id UUID del cliente
     * @return 204
     * @throws ClienteNotFound si no existe el cliente con el UUID proporcionado 404
     */
    @Operation(summary = "Elimina un cliente",description = "Elimina un cliente mediante su ID")
    @Parameters({
            @Parameter(name = "id", description = "UUID del cliente", example = "123e4567-e89b-12d3-a456-426614174000")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Cliente eliminado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteCliente(@PathVariable UUID id) {
        clienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    /**
     * Actualiza la imagen de un cliente
     *
     * @param id UUID del cliente
     * @param imagen imagen del cliente
     * @return Cliente actualizado
     * @throws ClienteNotFound si no existe el cliente con el UUID proporcionado 404
     */
    @Operation(summary = "Actualiza la imagen de un cliente",description = "Actualiza la imagen de un cliente mediante su ID")
    @Parameters({
            @Parameter(name = "id", description = "UUID del cliente", example = "123e4567-e89b-12d3-a456-426614174000"),
            @Parameter(name = "imagen", description = "Imagen del cliente", example = "imagen")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Imagen actualizada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "No se ha enviado una imagen para el cliente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @PatchMapping(value = "/imagen/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClienteReponse> updateImage(@PathVariable UUID id, @RequestPart("imagen") MultipartFile imagen) {
        List<String> datosPermitidos = List.of("image/png", "image/jpg", "image/jpeg", "image/gif");
        try {
            String contentType = imagen.getContentType();

            if (!imagen.isEmpty() && contentType != null && !contentType.isEmpty() && datosPermitidos.contains(contentType.toLowerCase())) {
                return ResponseEntity.ok(clienteService.updateImage(id, imagen,true));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha enviado una imagen para el cliente ");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede saber el tipo de la imagen");
        }
    }


    /**
     * Manejador de excepciones de Validación: 400 Bad Request
     *
     * @param ex excepción
     * @return Mapa de errores de validación con el campo y el mensaje
     */
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