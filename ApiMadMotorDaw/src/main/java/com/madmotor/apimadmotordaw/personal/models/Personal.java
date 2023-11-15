package com.madmotor.apimadmotordaw.personal.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

public class Personal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(name ="dni", nullable = false, length = 9)
    @NotBlank(message = "Debe de tener DNI")
    @Length(  min = 9, max = 9, message = "Debe tener 9 caracteres")
    private String dni;

    @Column(name = "nombre", nullable = false)
    @NotEmpty(message = "El nombre no puede estar vacío")
    private String nombre;

    @Column(name = "apellidos", nullable = false)
    @NotEmpty(message = "El/los apellido(s) no puede(n) estar vacío(s)")
    private String apellidos;

    @Column(name = "fechaNacimiento", nullable = false)
    @NotBlank(message = "Debe de tener una fecha")
    private LocalDate fechaNacimiento;

    @Column(name = "direccion", nullable = false)
    @NotEmpty(message = "La dirección no puede estar vacía")
    private String direccion;

    @Column(name = "cuenta_bancaria", nullable = false)
    @NotBlank(message = "Debe tener una cuenta bancaria")
    @Length(min = 20, max = 20, message = "La cuenta bancaria debe de tener 20 caracteres")
    private String iban;
}
