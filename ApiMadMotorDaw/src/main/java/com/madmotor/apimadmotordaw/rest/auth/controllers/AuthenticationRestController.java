package com.madmotor.apimadmotordaw.rest.auth.controllers;


import com.madmotor.apimadmotordaw.rest.auth.dto.JwtAuthResponse;
import com.madmotor.apimadmotordaw.rest.auth.dto.UserSignInRequest;
import com.madmotor.apimadmotordaw.rest.auth.dto.UserSignUpRequest;
import com.madmotor.apimadmotordaw.rest.auth.services.authentication.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("${api.version}/auth") // Es la ruta del controlador
public class AuthenticationRestController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationRestController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Registra un usuario
     *
     * @param request datos del usuario
     * @return Token de autenticación
     * throws UserAuthNameOrEmailExisten si el nombre de usuario o el email ya existen
     * throws UserDiferentePasswords si las contraseñas no coinciden
     */
    @Operation(summary = "Registra un usuario")
    @Parameters(value = {
            @io.swagger.v3.oas.annotations.Parameter(name = "request",description = "Datos para registro", required = true)
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Clientes encontrados"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error de validación")
    })
    @PostMapping("/signup")
    public ResponseEntity<JwtAuthResponse> signUp(@Valid @RequestBody UserSignUpRequest request) {
        log.info("Registrando usuario: {}", request);
        return ResponseEntity.ok(authenticationService.signUp(request));
    }

    /**
     * Inicia sesión de un usuario
     *
     * @param request datos del usuario
     * @return Token de autenticación
     * throws AuthSingInInvalid si el usuario o la contraseña son incorrectos
     */
    @Operation(summary = "Inicia sesión de un usuario")
    @Parameters(value = {
            @Parameter(name = "request",description = "Datos para inicio de sesión", required = true)
    })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Clientes encontrados"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Error de validación")
    })
    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponse> signIn(@Valid @RequestBody UserSignInRequest request) {
        log.info("Iniciando sesión de usuario: {}", request);
        return ResponseEntity.ok(authenticationService.signIn(request));
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