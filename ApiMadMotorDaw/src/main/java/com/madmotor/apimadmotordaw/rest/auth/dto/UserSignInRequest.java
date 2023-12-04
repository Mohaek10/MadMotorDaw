package com.madmotor.apimadmotordaw.rest.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserSignInRequest", description = "Datos de usuario para inicio de Sesión")
public class UserSignInRequest {
    @Schema(description = "Nombre de usuario", example = "user1", required = true)
    @NotBlank(message = "Username no puede estar vacío")
    private String username;

    @Schema(description = "Password de usuario", example = "123456", required = true)
    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password debe tener al menos 5 caracteres")
    private String password;
}
