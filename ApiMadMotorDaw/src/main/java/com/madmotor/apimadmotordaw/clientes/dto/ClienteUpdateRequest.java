package com.madmotor.apimadmotordaw.clientes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Cliente a actualizar")
public class ClienteUpdateRequest {

    @Schema(description = "Nombre nuevo para el cliente")
    private String nombre;

    @Schema(description = "Apellido del cliente")
    private String apellido;

    @Schema(description = "Nueva direccion del cliente")
    private String direccion;

    @Schema(description = "Codigo postal del cliente")
    private Integer codigoPostal;

    @Schema(description = "Si el cliente ha cambiado de opinion sobre piezas")
    private Boolean piezas;

    @Schema(description = "Si el cliente ha cambiado de opinion sobre coches")
    private Boolean coches;
    @Schema(description = "Imagen del cliente")
    private final String imagen;
}
