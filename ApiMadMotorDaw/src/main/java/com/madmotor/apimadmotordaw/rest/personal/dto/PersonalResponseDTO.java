package com.madmotor.apimadmotordaw.rest.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase PersonalResponseDTO
 *
 * En esta clase se definen los atributos de la clase PersonalResponseDTO
 * @version 1.0
 * @Author Miguel Vicario
 */



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Personal a devolver")

public class PersonalResponseDTO {

    @Schema(description = "Identificador del personal")
    private Long id;

    @Schema(description = "DNI del personal")
    private String dni;

    @Schema(description = "Nombre del personal")
    private String nombre;

    @Schema(description = "Apellidos del personal")
    private String apellidos;

    @Schema(description = "Fecha de nacimiento del personal")
    private String fechaNacimiento;

    @Schema(description = "Dirección del personal")
    private String direccion;

    @Schema(description = "Sueldo del personal")
    private Double sueldo;

    @Schema(description = "Teléfono del personal")
    private String telefono;

    @Schema(description = "IBAN del personal")
    private String iban;


}
