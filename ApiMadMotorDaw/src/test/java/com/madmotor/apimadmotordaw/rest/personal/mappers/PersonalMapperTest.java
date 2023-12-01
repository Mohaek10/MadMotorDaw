package com.madmotor.apimadmotordaw.rest.personal.mappers;

import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalCreateDTO;
import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalUpdateDTO;
import com.madmotor.apimadmotordaw.rest.personal.mappers.PersonalMapper;
import com.madmotor.apimadmotordaw.rest.personal.models.Personal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonalMapperTest {
    private final PersonalMapper personalMapper = new PersonalMapper();

    @Test
    void toPersonal() {
        PersonalCreateDTO personalCreateDTO = PersonalCreateDTO.builder()
                .dni("123456789")
                .nombre("Juan")
                .apellidos("Jimenez")
                .fechaNacimiento("20-05-2000")
                .direccion("Calle de la independencia")
                .iban("ES123456789123456789")
                .build();
        var res = personalMapper.toPersonal(personalCreateDTO);

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(personalCreateDTO.getDni(), res.getDni()),
                () -> assertEquals(personalCreateDTO.getNombre(), res.getNombre()),
                () -> assertEquals(personalCreateDTO.getApellidos(), res.getApellidos()),
                () -> assertEquals(personalCreateDTO.getFechaNacimiento(), res.getFechaNacimiento()),
                () -> assertEquals(personalCreateDTO.getDireccion(), res.getDireccion()),
                () -> assertEquals(personalCreateDTO.getIban(), res.getIban())
        );
    }

    @Test
    void toPersonalUpdate() {
        PersonalUpdateDTO personalUpdateDTO = PersonalUpdateDTO.builder()
                .direccion("Calle de la virgen")
                .iban("ES123456789123456745")
                .build();

        Personal personal = Personal.builder()
                .dni("123456789")
                .nombre("Juan")
                .apellidos("Jimenez")
                .fechaNacimiento("20-05-2000")
                .direccion(personalUpdateDTO.getDireccion())
                .iban(personalUpdateDTO.getIban())
                .build();

        var res = personalMapper.toPersonal(personalUpdateDTO, personal);
    }

    @Test
    void toPersonalResponseDto() {
        Personal personal = Personal.builder()
                .id(1L)
                .dni("123456789")
                .nombre("Juan")
                .apellidos("Jimenez")
                .fechaNacimiento("20-05-2000")
                .direccion("Calle de la independencia")
                .iban("ES123456789123456789")
                .build();

        var res = personalMapper.toPersonalResponseDto(personal);

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(personal.getId(), res.getId()),
                () -> assertEquals(personal.getDni(), res.getDni()),
                () -> assertEquals(personal.getNombre(), res.getNombre()),
                () -> assertEquals(personal.getApellidos(), res.getApellidos()),
                () -> assertEquals(personal.getFechaNacimiento(), res.getFechaNacimiento()),
                () -> assertEquals(personal.getDireccion(), res.getDireccion()),
                () -> assertEquals(personal.getIban(), res.getIban())
        );
    }
}