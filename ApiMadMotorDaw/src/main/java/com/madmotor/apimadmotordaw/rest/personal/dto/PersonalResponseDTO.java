package com.madmotor.apimadmotordaw.rest.personal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonalResponseDTO {
    private Long id;
    private String dni;
    private String nombre;
    private String apellidos;
    private String fechaNacimiento;
    private String direccion;
    private Double sueldo;
    private String telefono;
    private String iban;


}
