package com.madmotor.apimadmotordaw.rest.users.dto;

import com.madmotor.apimadmotordaw.rest.users.models.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserRequest", description = "DTO para crear un usuario")
public class UserRequest {
    @Schema(description = "Nombre del usuario", example = "Paco")
    @NotBlank(message = "Nombre no puede estar vacío")
    private String nombre;

    @Schema(description = "Apellidos del usuario", example = "Martínez")
    @NotBlank(message = "Apellidos no puede estar vacío")
    private String apellidos;

    @Schema(description = "Nombre de usuario", example = "pacopaco")
    @NotBlank(message = "Username no puede estar vacío")
    private String username;

    @Schema(description = "Email del usuario", example = "paco@hotmail.com")
    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    @NotBlank(message = "Email no puede estar vacío")
    private String email;

    @Schema(description = "Password del usuario", example = "12345")
    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password debe tener al menos 5 caracteres")
    @Size(min = 5, message = "Password debe tener al menos 5 caracteres")
    private String password;

    @Schema(description = "Rol del usuario", example = "USER")
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);

    @Schema(description = "Si el usuario está eliminado", example = "true")
    @Builder.Default
    private Boolean isDeleted = false;
}
