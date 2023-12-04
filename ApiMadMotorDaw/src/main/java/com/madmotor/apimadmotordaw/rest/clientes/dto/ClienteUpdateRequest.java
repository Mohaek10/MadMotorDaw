package com.madmotor.apimadmotordaw.rest.clientes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@Schema(description = "Cliente a actualizar")
public class ClienteUpdateRequest {

    @Schema(description = "Nombre nuevo para el cliente")
    @Length(min = 3, message = "EL nombre debe tener al menos 3 caracteres")
    private String nombre;

    @Schema(description = "Apellido del cliente")
    @Length(min = 3, message = "EL apellido debe tener al menos 3 caracteres")
    private String apellido;

    @Schema(description = "Nueva direccion del cliente")
    @Length(min = 5, message = "La direccion debe tener al menos 5 caracteres")
    private String direccion;

    @Schema(description = "Codigo postal del cliente")
    @Min(value = 10000, message = "El codigo postal no puede ser inferior a 10000")
    private Integer codigoPostal;

    @Schema(description = "Imagen del cliente")
    private final String imagen;

    @Schema(description = "Booleano del cliente")
    private final Boolean isDeleted;

    @Schema(description = "Fecha de creacion del cliente")
    private final String createdAt;

    @Schema(description = "Fecha de actualizacion del cliente")
    private final String updatedAt;
}
