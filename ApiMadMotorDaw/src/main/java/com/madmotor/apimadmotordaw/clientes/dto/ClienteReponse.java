package com.madmotor.apimadmotordaw.clientes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Cliente a devolver")
public class ClienteReponse {

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

    @Schema(description = "Si el cliente esta interesado en piezas")
    private Boolean piezas;

    @Schema(description = "Si el cliente esta interesado en coches")
    private Boolean coches;
    @Schema(description = "Imagen del cliente")
    private String imagen;
}
