package com.madmotor.apimadmotordaw.rest.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Clase PersonalCreateDTO
 *
 * En esta clase se definen los atributos de la clase PersonalCreateDTO
 * @version 1.0
 * @Author Miguel Vicario
 */


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Crea un nuevo personal")

public class PersonalCreateDTO {

    @NotBlank
    @Length(min = 9, max = 9, message = "El DNI debe contener 8 numeros y una letra")
    @Schema(description = "DNI del personal")
    private String dni;

    @NotEmpty
    @Length(min = 3, max = 20, message = "El nombre debe tener entre 2 y 20 caracteres")
    @Schema(description = "Nombre del personal")
    private String nombre;


    @NotEmpty
    @Length(min = 3, max = 50, message = "El/Los apellido/s deben contener entre 3 y 50 caracteres")
    @Schema(description = "Apellidos del personal")
    private String apellidos;

    @NotNull
    @Schema(description = "Fecha de nacimiento del personal")
    private String fechaNacimiento;

    @NotEmpty
    @Length(min = 3, max = 150, message = "La dirección debe de tener entre 3 y 150 caracteres")
    @Schema(description = "Dirección del personal")
    private String direccion;

    @NotBlank(message = "El IBAN no puede estar vacio")
    @Length(min = 20, max = 20, message = "La cuenta de banco debe de contener 20 caracteres, los dos primeros son ES y los demás dígitos")
    @Schema(description = "IBAN del personal")
    private String iban;

    @NotNull(message = "El sueldo no puede estar vacio")
    @Positive
    @Schema(description = "Sueldo del personal")
    private Double sueldo;

    @NotBlank(message = "El teléfono no puede estar vacio")
    @Length(min = 9, max = 9, message = "El teléfono debe de tener 9 caracteres")
    @Schema(description = "Teléfono del personal")
    private String telefono;
}
