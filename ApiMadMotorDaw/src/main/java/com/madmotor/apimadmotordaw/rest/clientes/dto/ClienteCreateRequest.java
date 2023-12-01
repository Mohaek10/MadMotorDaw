package com.madmotor.apimadmotordaw.rest.clientes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
@Data
@Builder
@Schema(description = "Crea un nuevo cliente")
public class ClienteCreateRequest {
    private static final String IMAGE_DEFAULT = "https://via.placeholder.com/150";

    @Length(min = 3, message = "El nombre debe tener al menos 3 caracteres")
    @NotBlank(message = "Necesitamos conocer su nombre")
    @Schema(description = "El nombre del cliente")
   private final String nombre;

    @Schema(description = "El apellido del cliente")
    @Length(min = 3, message = "El apellido debe tener al menos 3 caracteres")
    @NotBlank(message="Necesitamos conocer sus apellidos")
   private final String apellido;

    @Schema(description = "La direccion del cliente")
    @Length(min = 3,message = "La direccion debe tener al menos 3 caracteres")
    @NotBlank(message = "Es necesario conocer su direccion")
   private final String direccion;
    @NotNull(message = "Es necesario conocer su codigo postal")
    @Schema(description = "El codigo postal del cliente")
    @Min(value = 10000,message = "El codigo postal debe tener un minimo de 5 digitos")
   private final Integer codigoPostal;

    @Schema(description = "El dni del cliente")
    @NotBlank(message = "Es necesario conocer su dni")
    @Pattern(regexp = "^[0-9]{8}[A-Za-z]?$", message = "El DNI debe contener 8 dígitos seguidos de una letra opcional")
   private final String dni;

    @Schema(description = "Si el cliente esta interesado en piezas")
    @Builder.Default
    private final Boolean piezas=true;

    @Schema(description = "Si el cliente esta interesado en coches")
    @Builder.Default
    private final Boolean coches=true;

    @Schema(description = "Imagen del cliente")
    @Builder.Default
    private final String imagen = IMAGE_DEFAULT;
}
