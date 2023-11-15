package com.madmotor.apimadmotordaw.personal.mappers;

import com.madmotor.apimadmotordaw.personal.dto.PersonalCreateDTO;
import com.madmotor.apimadmotordaw.personal.dto.PersonalResponseDTO;
import com.madmotor.apimadmotordaw.personal.dto.PersonalUpdateDTO;
import com.madmotor.apimadmotordaw.personal.models.Personal;
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
                .build();
    }

    public Personal toPersonal(PersonalUpdateDTO dto) {
        return Personal.builder()
                .direccion(dto.getDireccion())
                .iban(dto.getIban())
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
                .build();
    }
}
