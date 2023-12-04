package com.madmotor.apimadmotordaw.rest.auth.dto;


import com.madmotor.apimadmotordaw.rest.users.models.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserResponse", description = "UserResponse")
public class UserResponse {
    @Schema(description = "Id", example = "1")
    private String id;
    @Schema(description = "Nombre", example = "Pepe")
    private String nombre;
    @Schema(description = "Apellidos", example = "Perez")
    private String apellidos;
    @Schema(description = "Username", example = "pepeperez")
    private String username;
    @Schema(description = "Email", example = "Pepe@pepe.com")
    private String email;
    @Schema(description = "Roles", example = "USER")
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);
}
