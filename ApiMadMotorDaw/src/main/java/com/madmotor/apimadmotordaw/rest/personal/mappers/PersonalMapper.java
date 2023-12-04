package com.madmotor.apimadmotordaw.rest.personal.mappers;

import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalCreateDTO;
import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalResponseDTO;
import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalUpdateDTO;
import com.madmotor.apimadmotordaw.rest.personal.models.Personal;
import org.springframework.stereotype.Component;

/**
 * Clase PersonalMapper
 *
 * En esta clase se definen los métodos de la clase PersonalMapper
 * @version 1.0
 * @author  Miguel Vicario
 */


@Component

public class PersonalMapper {

    /**
     * Método toPersonal
     *
     * En este método se crean los atributos de la clase Personal
     * @param dto es del tipo PersonalCreateDTO
     * @return personal creado
     */
    public Personal toPersonal(PersonalCreateDTO dto) {
        return Personal.builder()
                .dni(dto.getDni())
                .nombre(dto.getNombre())
                .apellidos(dto.getApellidos())
                .fechaNacimiento(dto.getFechaNacimiento())
                .direccion(dto.getDireccion())
                .iban(dto.getIban())
                .sueldo(dto.getSueldo())
                .telefono(dto.getTelefono())
                .build();
    }

    /**
     * Método toPersonal
     *
     * En este método se actualizan los atributos de la clase Personal
     * @param dto es del tipo PersonalUpdateDTO
     * @param personal
     * @return personal actualizado
     */

    public Personal toPersonal(PersonalUpdateDTO dto, Personal personal) {
        return Personal.builder()
                .direccion(dto.getDireccion()!=null ? dto.getDireccion() : personal.getDireccion())
                .iban(dto.getIban()!=null ? dto.getIban() : personal.getIban())
                .sueldo(dto.getSueldo()!=null ? dto.getSueldo() : personal.getSueldo())
                .telefono(dto.getTelefono()!=null ? dto.getTelefono() : personal.getTelefono())
                .build();

    }

    /**
     * Método toPersonalResponseDto
     *
     * En este método se crean los atributos de la clase PersonalResponseDTO
     * @param dto es del tipo Personal
     * @return los datos que se muestran
     */

    public PersonalResponseDTO toPersonalResponseDto(Personal dto) {
        return PersonalResponseDTO.builder()
                .id(dto.getId())
                .dni(dto.getDni())
                .nombre(dto.getNombre())
                .apellidos(dto.getApellidos())
                .fechaNacimiento(dto.getFechaNacimiento())
                .direccion(dto.getDireccion())
                .iban(dto.getIban())
                .sueldo(dto.getSueldo())
                .telefono(dto.getTelefono())
                .build();
    }
}
