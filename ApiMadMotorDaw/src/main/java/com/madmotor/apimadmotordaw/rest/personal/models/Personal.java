package com.madmotor.apimadmotordaw.rest.personal.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
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
 * Clase Personal
 *
 * En esta clase se definen los atributos de la clase Personal
 * @version 1.0
 * @author Miguel Vicario
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "PERSONAL")

public class Personal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador del personal")
    private Long id;

    @Column(name ="dni", nullable = false, length = 9)
    @NotBlank(message = "Debe de tener DNI")
    @Length(  min = 9, max = 9, message = "Debe tener 9 caracteres")
    @Schema(description = "DNI del personal")
    private String dni;

    @Column(name = "nombre", nullable = false)
    @NotEmpty(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre del personal")
    private String nombre;

    @Column(name = "apellidos", nullable = false)
    @NotEmpty(message = "El/los apellido(s) no puede(n) estar vacío(s)")
    @Schema(description = "Apellidos del personal")
    private String apellidos;

    @Column(name = "fechaNacimiento", nullable = false)
    @NotNull(message = "Debe de tener una fecha")
    @Schema(description = "Fecha de nacimiento del personal")
    private String fechaNacimiento;

    @Column(name = "direccion", nullable = false)
    @NotEmpty(message = "La dirección no puede estar vacía")
    @Schema(description = "Dirección del personal")
    private String direccion;

    @Column(name = "cuenta_bancaria", nullable = false)
    @NotBlank(message = "Debe tener una cuenta bancaria")
    @Length(min = 10, max = 30, message = "La cuenta bancaria debe de tener 20 caracteres")
    @Schema(description = "Cuenta bancaria del personal")
    private String iban;

    @Column(name = "sueldo", nullable = false)
    @NotBlank(message = "Debe tener un sueldo")
    @Positive(message = "El sueldo debe de ser positivo")
    @Schema(description = "Sueldo del personal")
    private Double sueldo;

    @Column(name = "telefono", nullable = false)
    @NotBlank(message = "Debe tener un teléfono")
    @Length(min = 9, max = 9, message = "El teléfono debe de tener 9 caracteres")
    @Schema(description = "Teléfono del personal")
    private String telefono;
}
