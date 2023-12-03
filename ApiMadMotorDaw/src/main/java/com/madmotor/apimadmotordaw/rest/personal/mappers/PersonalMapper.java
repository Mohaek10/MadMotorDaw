package com.madmotor.apimadmotordaw.rest.personal.mappers;

import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalCreateDTO;
import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalResponseDTO;
import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalUpdateDTO;
import com.madmotor.apimadmotordaw.rest.personal.models.Personal;
import org.springframework.stereotype.Component;


@Component

public class PersonalMapper {
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

    public Personal toPersonal(PersonalUpdateDTO dto, Personal personal) {
        return Personal.builder()
                .direccion(dto.getDireccion()!=null ? dto.getDireccion() : personal.getDireccion())
                .iban(dto.getIban()!=null ? dto.getIban() : personal.getIban())
                .sueldo(dto.getSueldo()!=null ? dto.getSueldo() : personal.getSueldo())
                .telefono(dto.getTelefono()!=null ? dto.getTelefono() : personal.getTelefono())
                .build();

    }

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
