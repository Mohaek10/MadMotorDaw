package com.madmotor.apimadmotordaw.personal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class PersonalCreateDTO {

    @NotBlank
    @Length(min = 9, max = 9, message = "El DNI debe contener 8 numeros y una letra")
    private String dni;

    @NotEmpty
    @Length(min = 3, max = 20, message = "El nombre debe tener entre 2 y 20 caracteres")
    private String nombre;


    @NotEmpty
    @Length(min = 3, max = 50, message = "El/Los apellido/s deben contener entre 3 y 50 caracteres")
    private String apellidos;

    @NotNull
    private String fechaNacimiento;

    @NotEmpty
    @Length(min = 3, max = 150, message = "La dirección debe de tener entre 3 y 150 caracteres")
    private String direccion;

    @NotBlank
    @Length(min = 20, max = 20, message = "La cuenta de banco debe de contener 20 caracteres, los dos primeros son ES y los demás dígitos")
    private String iban;
}
