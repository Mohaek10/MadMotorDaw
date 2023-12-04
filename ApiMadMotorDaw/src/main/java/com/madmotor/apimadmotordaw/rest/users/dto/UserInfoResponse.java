package com.madmotor.apimadmotordaw.rest.users.dto;

import com.madmotor.apimadmotordaw.rest.users.models.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserInfoResponse", description = "Información del usuario")
public class UserInfoResponse {
    @Schema(description = "Identificador del usuario", example = "1")
    private String id;
    @Schema(description = "Nombre del usuario", example = "Paco")
    private String nombre;
    @Schema(description = "Apellidos del usuario", example = "Martínez")
    private String apellidos;
    @Schema(description = "Nombre de usuario", example = "pacopaco")
    private String username;
    @Schema(description = "Email del usuario", example = "prueba@gmail.com")
    private String email;
    @Schema(description = "Rol del usuario", example = "USER")
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);
    @Schema(description = "Si el usuario está eliminado", example = "true")
    @Builder.Default
    private Boolean isDeleted = false;
    @Schema(description = "Lista de pedidos del usuario", example = "1")
    @Builder.Default
    private List<String> pedidos = new ArrayList<>();
}
