package com.madmotor.apimadmotordaw.rest.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "UserSignUpRequest", description = "Datos de usuario para registro")
public class UserSignUpRequest {

    @Schema(description = "Nombre de usuario", example = "user1", required = true)
    @NotBlank(message = "Nombre no puede estar vacío")
    private String nombre;

    @Schema(description = "Apellidos de usuario", example = "user1", required = true)
    @NotBlank(message = "Apellidos no puede estar vacío")
    private String apellidos;

    @Schema(description = "Nombre de usuario", example = "user1", required = true)
    @NotBlank(message = "Username no puede estar vacío")
    private String username;

    @Schema(description = "Email de usuario", example = "pepe@hotmail.com", required = true)
    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    @NotBlank(message = "Email no puede estar vacío")
    private String email;

    @Schema(description = "Password de usuario", example = "123456", required = true)
    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password debe tener al menos 5 caracteres")
    private String password;

    @Schema(description = "Password de comprobación de usuario", example = "123456", required = true)
    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password de comprobación debe tener al menos 5 caracteres")
    private String passwordComprobacion;

}