package com.madmotor.apimadmotordaw.rest.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Clase PersonalUpdateDTO
 *
 * En esta clase se definen los atributos de la clase PersonalUpdateDTO
 * @version 1.0
 * @Author Miguel Vicario
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Personal a actualizar")

public class PersonalUpdateDTO {

    @Length(min = 3, max = 150, message = "La dirección debe de tener entre 3 y 150 caracteres")
    @Schema(description = "Dirección del personal")
    private String direccion;

    @Length(min = 20, max = 20, message = "La cuenta de banco debe de contener 20 caracteres, los dos primeros son ES y los demás dígitos")
    @Schema(description = "IBAN del personal")
    private String iban;

    @Positive(message = "El sueldo debe de ser positivo")
    @Schema(description = "Sueldo del personal")
    private Double sueldo;

    @Length(min = 9, max = 9, message = "El telefono debe de tener 9 caracteres")
    @Schema(description = "Telefono del personal")
    private String telefono;

}
