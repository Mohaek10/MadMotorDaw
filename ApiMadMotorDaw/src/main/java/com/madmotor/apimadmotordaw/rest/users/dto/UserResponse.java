package com.madmotor.apimadmotordaw.rest.users.dto;

import com.madmotor.apimadmotordaw.rest.users.models.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserResponse", description = "DTO que representa un usuario")
public class UserResponse {
    @Schema(description = "Identificador del usuario", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;
    @Schema(description = "Nombre del usuario", example = "John")
    private String nombre;
    @Schema(description = "Apellidos del usuario", example = "Doe")
    private String apellidos;
    @Schema(description = "Nombre de usuario", example = "johndoe")
    private String username;
    @Schema(description = "Email del usuario", example = "example@hotmail.com")
    private String email;
    @Schema(description = "Rol del usuario", example = "USER")
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);
    @Schema(description = "Si el usuario está borrado o no", example = "false")
    @Builder.Default
    private Boolean isDeleted = false;
}
