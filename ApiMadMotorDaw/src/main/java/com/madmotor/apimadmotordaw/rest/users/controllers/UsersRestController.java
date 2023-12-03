package com.madmotor.apimadmotordaw.rest.users.controllers;


import com.madmotor.apimadmotordaw.rest.pedidos.dto.CreatePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.ResponsePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.UpdatePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.exceptions.NoItemsValid;
import com.madmotor.apimadmotordaw.rest.pedidos.exceptions.PedidoNotFound;
import com.madmotor.apimadmotordaw.rest.pedidos.models.Pedido;
import com.madmotor.apimadmotordaw.rest.pedidos.services.PedidoService;
import com.madmotor.apimadmotordaw.rest.users.dto.UserInfoResponse;
import com.madmotor.apimadmotordaw.rest.users.dto.UserRequest;
import com.madmotor.apimadmotordaw.rest.users.dto.UserResponse;
import com.madmotor.apimadmotordaw.rest.users.exceptions.UserNotFound;
import com.madmotor.apimadmotordaw.rest.users.models.User;
import com.madmotor.apimadmotordaw.rest.users.services.UsersService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
/**
 * Controlador de usuarios
 * Es necesario que tengan un ROL para acceder a los métodos
 */
@RestController
@Slf4j
@RequestMapping("${api.version}/users") // Es la ruta del controlador
@PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
public class UsersRestController {
    //Indicamamos las dependencias que vamos a usar
    private final UsersService usersService;
    private final PedidoService pedidosService;
    private final PaginationLinksUtils paginationLinksUtils;
    // Inyectamos las dependencias
    @Autowired
    public UsersRestController(UsersService usersService, PedidoService pedidosService, PaginationLinksUtils paginationLinksUtils) {
        this.usersService = usersService;
        this.pedidosService = pedidosService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    /**
     * Obtiene todos los usuarios
     *
     * @param username  username del usuario
     * @param email     email del usuario
     * @param isDeleted si está borrado o no
     * @param page      página
     * @param size      tamaño
     * @param sortBy    campo de ordenación
     * @param direction dirección de ordenación
     * @param request   petición
     * @return Respuesta con la página de usuarios con un Ok 200
     * throws Unauthorized si no está autenticado (401)
     * throws Forbidden si no tiene permisos (403)
     */
    @Operation(summary = "Obtiene todos los usuarios")
    @Parameters(value ={
                    @io.swagger.v3.oas.annotations.Parameter(name = "username", description = "username del usuario"),
                    @io.swagger.v3.oas.annotations.Parameter(name = "email", description = "email del usuario"),
                    @io.swagger.v3.oas.annotations.Parameter(name = "isDeleted", description = "si está borrado o no"),
                    @io.swagger.v3.oas.annotations.Parameter(name = "page", description = "página"),
                    @io.swagger.v3.oas.annotations.Parameter(name = "size", description = "tamaño"),
                    @io.swagger.v3.oas.annotations.Parameter(name = "sortBy", description = "campo de ordenación"),
                    @io.swagger.v3.oas.annotations.Parameter(name = "direction", description = "dirección de ordenación")
            })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Solo los admin pueden acceder
    public ResponseEntity<PageResponse<UserResponse>> findAll(
            @RequestParam(required = false) Optional<String> username,
            @RequestParam(required = false) Optional<String> email,
            @RequestParam(required = false) Optional<Boolean> isDeleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("findAll: username: {}, email: {}, isDeleted: {}, page: {}, size: {}, sortBy: {}, direction: {}",
                username, email, isDeleted, page, size, sortBy, direction);
        // Creamos el objeto de ordenación
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        // Creamos cómo va a ser la paginación
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<UserResponse> pageResult = usersService.findAll(username, email, isDeleted, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    /**
     * Obtiene un usuario por su id
     *
     * @param id del usuario, se pasa como parámetro de la URL /{id}
     * @return Usuario si existe
     * @throws UserNotFound si no existe el usuario (404)
     * throws Unauthorized si no está autenticado (401)
     * throws Forbidden si no tiene permisos (403)
     */
    @Operation(summary = "Obtiene un usuario por su id")
    @Parameters(value ={
            @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "id del usuario")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo los admin pueden acceder
    public ResponseEntity<UserInfoResponse> findById(@PathVariable String id) {
        log.info("findById: id: {}", id);
        return ResponseEntity.ok(usersService.findById(id));
    }
    /**
     * Crea un usuario
     *
     * @param userRequest usuario a crear
     * @return Usuario creado 201
     * @throws HttpClientErrorException.BadRequest si hay algún error de validación (400)
     * throws Unauthorized si no está autenticado (401)
     * throws BadRequest si hay error en los datos (400)
     * throws Forbidden si no tiene permisos (403)
     * throws UserNameOrEmailExists si ya existe un usuario con ese username o email (404)
     */
    @Operation(summary = "Crea un usuario")
    @Parameters(value ={
            @io.swagger.v3.oas.annotations.Parameter(name = "userRequest", description = "usuario a crear")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Creado "),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Hay algo incorrecto en los datos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No tiene autorización"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden prohiido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Ya hay un usuario con ese username o email")
    })

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Solo los admin pueden acceder
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("save: userRequest: {}", userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.save(userRequest));
    }
    /**
     * Actualiza un usuario
     *
     * @param id          id del usuario
     * @param userRequest usuario a actualizar
     * @return Usuario actualizado
     * @throws HttpClientErrorException.BadRequest si hay algún error de validación (400)
     * throws Unauthorized si no está autenticado (401)
     * throws BadRequest si hay error en los datos (400)
     * throws Forbidden si no tiene permisos (403)
     * throws UserNotFound si no existe el usuario (404)
     * throws UserNameOrEmailExists si ya existe un usuario con ese username o email (404)
     */
    @Operation(summary = "Actualiza un usuario")
    @Parameters(value ={
            @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "id del usuario"),
            @io.swagger.v3.oas.annotations.Parameter(name = "userRequest", description = "usuario a actualizar")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Hay algo incorrecto en los datos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No tiene autorización"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden no tienes permisos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Ya hay un usuario con ese username o email")
    })

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo los admin pueden acceder
    public ResponseEntity<UserResponse> updateUser(@PathVariable String id, @Valid @RequestBody UserRequest userRequest) {
        log.info("update: id: {}, userRequest: {}", id, userRequest);
        return ResponseEntity.ok(usersService.update(id, userRequest));
    }

    /**
     * Borra un usuario
     *
     * @param id id del usuario
     * @return Respuesta vacía
     * @throws UserNotFound si no existe el usuario (404)
     * throws Unauthorized si no está autenticado (401)
     * throws Forbidden si no tiene permisos (403)
     * throws BadRequest si hay error en los datos (400)
     */
    @Operation(summary = "Borra un usuario")
    @Parameters(value ={
            @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "id del usuario")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No Content"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No tiene autorización"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden no tienes permisos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No se ha podido encontrar el usuario")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo los admin pueden acceder
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        log.info("delete: id: {}", id);
        usersService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene el usuario actual
     *
     * @param user usuario autenticado
     * @return Datos del usuario
     * @throws UserNotFound si no existe el usuario (404)
     * throws Unauthorized si no está autenticado (401)
     * throws Forbidden si no tiene permisos (403)
     */
    @Operation(summary = "Obtiene el usuario actual")
    @Parameters(value ={
            @io.swagger.v3.oas.annotations.Parameter(name = "user", description = "usuario autenticado")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No tiene autorización"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden no tienes permisos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No se ha podido encontrar el usuario")
    })
    @GetMapping("/me/profile")
    @PreAuthorize("hasRole('ADMIN')") // Solo los admin pueden acceder
    public ResponseEntity<UserInfoResponse> me(@AuthenticationPrincipal User user) {
        log.info("Obteniendo usuario");
        // Esta autenticado, por lo que devolvemos sus datos ya sabemos su id
        return ResponseEntity.ok(usersService.findById(user.getId().toString()));
    }

    /**
     * Actualiza el usuario actual
     *
     * @param user        usuario autenticado
     * @param userRequest usuario a actualizar
     * @return Usuario actualizado
     * @throws UserNotFound si no existe el usuario (404)
     * throws Unauthorized si no está autenticado (401)
     *  throws Forbidden si no tiene permisos (403)
     *  throws UserNameOrEmailExists si ya existe un usuario con ese username o email (404)
     *  throws BadRequest si hay error en los datos (400)
     */
    @Operation(summary = "Actualiza el usuario actual")
    @Parameters(value ={
            @io.swagger.v3.oas.annotations.Parameter(name = "user", description = "usuario autenticado"),
            @io.swagger.v3.oas.annotations.Parameter(name = "userRequest", description = "usuario a actualizar")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Hay algo incorrecto en los datos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No tiene autorización"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden no tienes permisos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Ya hay un usuario con ese username o email")
    })
    @PutMapping("/me/profile")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<UserResponse> updateMe(@AuthenticationPrincipal User user, @Valid @RequestBody UserRequest userRequest) {
        log.info("updateMe: user: {}, userRequest: {}", user, userRequest);
        return ResponseEntity.ok(usersService.update(user.getId().toString(), userRequest));
    }

    /**
     * Borra el usuario actual
     *
     * @param user usuario autenticado
     * @return Respuesta vacía
     * @throws UserNotFound si no existe el usuario (404)
     * throws Unauthorized si no está autenticado (401)
     * throws Forbidden si no tiene permisos (403)
     */
    @Operation(summary = "Borra el usuario actual")
    @Parameters(value ={
            @io.swagger.v3.oas.annotations.Parameter(name = "user", description = "usuario autenticado")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No Content  borrado con exito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No tiene autorización"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden no tienes permisos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No se ha podido encontrar el usuario")
    })
    @DeleteMapping("/me/profile")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<Void> deleteMe(@AuthenticationPrincipal User user) {
        log.info("deleteMe: user: {}", user);
        usersService.deleteById(user.getId().toString());
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene los pedidos del usuario actual
     *
     * @param user      usuario autenticado
     * @param page      página
     * @param size      tamaño
     * @param sortBy    campo de ordenación
     * @param direction dirección de ordenación
     * @return Respuesta con la página de pedidos
     */
    @Operation(summary = "Obtiene los pedidos del usuario actual")
    @Parameters(value ={
            @io.swagger.v3.oas.annotations.Parameter(name = "user", description = "usuario autenticado"),
            @io.swagger.v3.oas.annotations.Parameter(name = "page", description = "página"),
            @io.swagger.v3.oas.annotations.Parameter(name = "size", description = "tamaño"),
            @io.swagger.v3.oas.annotations.Parameter(name = "sortBy", description = "campo de ordenación"),
            @io.swagger.v3.oas.annotations.Parameter(name = "direction", description = "dirección de ordenación")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No tiene autorización"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden no tienes permisos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No se ha podido encontrar el usuario")
    })
    @GetMapping("/me/pedidos")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<PageResponse<ResponsePedidoDto>> getPedidosByUsuario(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        log.info("Obteniendo pedidos del usuario con id: " + user.getId());
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(pedidosService.findByIdUsuario(user.getId().toString(), pageable), sortBy, direction));
    }

    /**
     * Obtiene un pedido del usuario actual
     *
     * @param user     usuario autenticado
     * @param idPedido id del pedido
     * @return Pedido
     * @throws UserNotFound si no existe el pedido
     * throws Unauthorized si no está autenticado (401)
     * throws Forbidden si no tiene permisos (403)
     * throws PedidoNotFound si no existe el pedido (404)
     */
    @Operation(summary = "Obtiene un pedido del usuario actual")
    @Parameters(value ={
            @io.swagger.v3.oas.annotations.Parameter(name = "user", description = "usuario autenticado"),
            @io.swagger.v3.oas.annotations.Parameter(name = "idPedido", description = "id del pedido")
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No tiene autorización"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden no tienes permisos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No se ha podido encontrar el pedido")
    })
    @GetMapping("/me/pedidos/{id}")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<ResponsePedidoDto> getPedido(
            @AuthenticationPrincipal User user,
            @PathVariable("id") ObjectId idPedido
    ) {
        log.info("Obteniendo pedido con id: " + idPedido);
        return ResponseEntity.ok(pedidosService.findById(idPedido));
    }

    /**

    @PostMapping("/me/pedidos")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<ResponsePedidoDto> savePedido(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreatePedidoDto pedido
    ) {
        log.info("Creando pedido: " + pedido);
        pedido.setIdUsuario(user.getId().toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidosService.save(pedido));
    }


    @PutMapping("/me/pedidos/{id}")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<ResponsePedidoDto> updatePedido(
            @AuthenticationPrincipal User user,
            @PathVariable("id") ObjectId idPedido,
            @Valid @RequestBody UpdatePedidoDto pedido) {
        log.info("Actualizando pedido con id: " + idPedido);
        pedido.setIdUsuario(user.getId().toString());
        return ResponseEntity.ok(pedidosService.update(pedido,idPedido));
    }


    @DeleteMapping("/me/pedidos/{id}")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<Void> deletePedido(
            @AuthenticationPrincipal User user,
            @PathVariable("id") ObjectId idPedido
    ) {
        log.info("Borrando pedido con id: " + idPedido);
        pedidosService.delete(idPedido);
        return ResponseEntity.noContent().build();
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
