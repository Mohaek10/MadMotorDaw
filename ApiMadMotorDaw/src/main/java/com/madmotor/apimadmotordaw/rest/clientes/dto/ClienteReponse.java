package com.madmotor.apimadmotordaw.rest.clientes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Cliente a devolver")
public class ClienteReponse {
    private UUID id;

    @Schema(description = "Nombre del cliente")
    private String nombre;

    @Schema(description = "Apellido del cliente")
    private String apellido;

    @Schema(description = "Direccion del cliente")
    private String direccion;

    @Schema(description = "Codigo postal del cliente")
    private Integer codigoPostal;

    @Schema(description = "Dni del cliente")
    private String dni;

    @Schema(description = "Imagen del cliente")
    private String imagen;
}
