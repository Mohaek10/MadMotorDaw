package com.madmotor.apimadmotordaw.rest.pedidos.controllers;


import com.madmotor.apimadmotordaw.rest.pedidos.dto.CreatePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.ResponsePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.UpdatePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.models.Pedido;
import com.madmotor.apimadmotordaw.rest.pedidos.services.PedidoService;
import com.madmotor.apimadmotordaw.utils.pagination.PageResponse;
import com.madmotor.apimadmotordaw.utils.pagination.PaginationLinksUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
/**
 * Controlador de la pieza del tipo RestController
 *
 * Con esta clase fijamos la ruta de acceso a este controlador con la anotación @RequestMapping
 *
 * @Autowired es una anotación que nos permite inyectar dependencias en las anotaciones @Controller, @Service, @Component, ...
 * Que se encuentren en nuestro contenedor Spring.
 *
 * @version 1.0
 * @autor Mohamed El Kasmi
 */

@RestController
@RequestMapping("${api.version}/pedidos")
@Slf4j
@PreAuthorize("hasRole('ADMIN')") //Este endpoint solo lo puede usar un usuario con rol ADMIN, el dueño o trabajador, desde aquí un cliente no deberá de crear pedidos
public class PedidosController {
    private final PedidoService pedidosService;
    private final PaginationLinksUtils paginationLinksUtils;

    @Autowired
    public PedidosController(PedidoService pedidosService, PaginationLinksUtils paginationLinksUtils) {
        this.pedidosService = pedidosService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    /**
     * Obtiene todos los pedidos
     *
     * @param page número de página
     * @param size tamaño de la página
     * @param sortBy campo por el que se ordena
     * @param direction dirección de la ordenación
     * @param request
     * @return una página de pedidos filtrada y paginada
     */

    @Operation(description = "Obtiene todos los pedidos mediante un listado paginado y filtrado")
    @Parameters({
            @io.swagger.v3.oas.annotations.Parameter(name = "page", description = "Número de página", example = "1"),
            @io.swagger.v3.oas.annotations.Parameter(name = "size", description = "Tamaño de la página", example = "10"),
            @io.swagger.v3.oas.annotations.Parameter(name = "sortBy", description = "Campo por el que se ordena", example = "id"),
            @io.swagger.v3.oas.annotations.Parameter(name = "direction", description = "Dirección de la ordenación", example = "asc")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
    })

    @GetMapping()
    public ResponseEntity<PageResponse<ResponsePedidoDto>> getAllPedidos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("Obteniendo todos los pedidos");
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<ResponsePedidoDto> pageResult = pedidosService.findAll(PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    /**
     * Obtiene un pedido por su id
     *
     * @param idPedido id del pedido
     * @return los pedidos encontrados por su id
     */

    @Operation(description = "Obtiene un pedido por su id")
    @Parameters({
            @io.swagger.v3.oas.annotations.Parameter(name = "idPedido", description = "Id del pedido", example = "60f0a9b9e3b9f83f7c7b0b1a")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found")
    })

    @GetMapping("/{idPedido}")
    public ResponseEntity<ResponsePedidoDto> getPedidoById(@RequestParam ObjectId idPedido) {
        log.info("Obteniendo pedido con id: " + idPedido.toHexString());
        return ResponseEntity.ok(pedidosService.findById(idPedido));
    }

    /**
     * Obtiene un pedido por su idUsuario
     *
     * @param idUsuario id del usuario
     * @return los pedidos encontrados por su idUsuario
     */

    @Operation(description = "Obtiene un pedido por su idUsuario")
    @Parameters({
            @io.swagger.v3.oas.annotations.Parameter(name = "idUsuario", description = "Id del usuario", example = "60f0a9b9e3b9f83f7c7b0b1a")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found")
    })

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<PageResponse<ResponsePedidoDto>> getPedidoByIdUsuario(
            @RequestParam String idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request) {
        log.info("Obteniendo pedidos del usuario con id: " + idUsuario);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<ResponsePedidoDto> pageResult = pedidosService.findByIdUsuario(idUsuario, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    /**
     * Crea un nuevo pedido
     *
     * @param pedido pedido a crear basado en el DTO
     * @return los parámetros del pedido creado
     */

    @Operation(description = "Crea un nuevo pedido")
    @Parameters({
            @io.swagger.v3.oas.annotations.Parameter(name = "pedido", description = "Pedido a crear basado en el DTO", example = "60f0a9b9e3b9f83f7c7b0b1a")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request"),

    })

    @PostMapping()
    public ResponseEntity<ResponsePedidoDto> createPedido(@RequestBody @Valid CreatePedidoDto pedido) {
        log.info("Creando pedido: {}", pedido);
        return ResponseEntity.ok(pedidosService.save(pedido));
    }

    /**
     * Actualiza un pedido
     *
     * @param pedido pedido a actualizar basado en el DTO
     * @param idPedido id del pedido
     * @return los parámetros del pedido actualizado
     */

    @Operation(description = "Actualiza un pedido")
    @Parameters({
            @io.swagger.v3.oas.annotations.Parameter(name = "pedido", description = "Pedido a actualizar basado en el DTO", example = "60f0a9b9e3b9f83f7c7b0b1a"),
            @io.swagger.v3.oas.annotations.Parameter(name = "idPedido", description = "Id del pedido", example = "60f0a9b9e3b9f83f7c7b0b1a")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found")
    })

    @PutMapping("/{idPedido}")
    public ResponseEntity<ResponsePedidoDto> updatePedido(@RequestBody @Valid UpdatePedidoDto pedido, @PathVariable ObjectId idPedido) {
        log.info("Actualizando pedido con id: {}", idPedido);
        return ResponseEntity.ok(pedidosService.update(pedido, idPedido));
    }

    /**
     * Borra un pedido
     *
     * @param idPedido id del pedido
     * @return 204 No Content
     */

    @Operation(description = "Borra un pedido")
    @Parameters({
            @io.swagger.v3.oas.annotations.Parameter(name = "idPedido", description = "Id del pedido", example = "60f0a9b9e3b9f83f7c7b0b1a")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No Content"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found")
    })

    @DeleteMapping("/{idPedido}")
    public ResponseEntity<Void> deletePedido(@PathVariable ObjectId idPedido) {
        log.info("Borrando pedido con id: {}", idPedido);
        pedidosService.delete(idPedido);
        return ResponseEntity.noContent().build();
    }

    /**
     * Manejador de excepciones de validación
     *
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
