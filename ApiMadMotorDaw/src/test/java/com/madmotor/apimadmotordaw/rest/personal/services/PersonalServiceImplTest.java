package com.madmotor.apimadmotordaw.rest.personal.services;

import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalCreateDTO;
import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalResponseDTO;
import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalUpdateDTO;
import com.madmotor.apimadmotordaw.rest.personal.exceptions.PersonalNotFound;
import com.madmotor.apimadmotordaw.rest.personal.mappers.PersonalMapper;
import com.madmotor.apimadmotordaw.rest.personal.models.Personal;
import com.madmotor.apimadmotordaw.rest.personal.repositories.PersonalRepository;
import com.madmotor.apimadmotordaw.rest.personal.services.PersonalServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalServiceImplTest {

    @Mock
    private PersonalRepository personalRepository;

    @Mock
    private PersonalMapper personalMapper;

    @InjectMocks
    private PersonalServiceImpl personalService;

    private Personal personal1 = Personal.builder()
            .id(1L)
            .dni("123456789")
            .nombre("Benito")
            .apellidos("Sanchez")
            .fechaNacimiento("12/12/1999")
            .direccion("Calle de la piruleta")
            .iban("ES123456789123456")
            .build();

    private Personal personal2 = Personal.builder()
            .id(2L)
            .dni("123456788")
            .nombre("Maria")
            .apellidos("Sanemeterio")
            .fechaNacimiento("12/02/2002")
            .direccion("Calle de la Rioja")
            .iban("ES123456789123455")
            .build();

    private final PersonalResponseDTO personalResponseDTO1 = PersonalResponseDTO.builder()
            .id(1L)
            .dni("123456789")
            .nombre("Benito")
            .apellidos("Sanchez")
            .fechaNacimiento("12/12/1999")
            .direccion("Calle de la piruleta")
            .iban("ES123456789123456")
            .build();

    private final PersonalResponseDTO personalResponseDTO2 = PersonalResponseDTO.builder()
            .id(2L)
            .dni("123456788")
            .nombre("Maria")
            .apellidos("Sanemeterio")
            .fechaNacimiento("12/02/2002")
            .direccion("Calle de la Rioja")
            .iban("ES123456789123455")
            .build();



    @Test
    void save() {
        PersonalCreateDTO personalCreateDTO = new PersonalCreateDTO();
        Personal personal = new Personal();
        when(personalMapper.toPersonal(personalCreateDTO)).thenReturn(personal);
        when(personalRepository.save(personal)).thenReturn(personal);
        when(personalMapper.toPersonalResponseDto(personal)).thenReturn(personalResponseDTO1);

        PersonalResponseDTO result = personalService.save(personalCreateDTO);
        assertEquals(result, personalResponseDTO1);
        verify(personalMapper, times(1)).toPersonal(personalCreateDTO);
        verify(personalRepository, times(1)).save(personal);
    }

    @Test
    void update() {
        Long id = 1L;
        Personal personal = new Personal();
        PersonalUpdateDTO personalUpdateDTO = new PersonalUpdateDTO();
        when(personalRepository.findById(id)).thenReturn(java.util.Optional.of(personal));
        when(personalRepository.save(personal)).thenReturn(personal);
        when(personalMapper.toPersonalResponseDto(personal)).thenReturn(personalResponseDTO1);

        PersonalResponseDTO result = personalService.update(id, personalUpdateDTO);

        assertEquals(result, personalResponseDTO1);
        verify(personalRepository, times(1)).findById(id);
        verify(personalRepository, times(1)).save(personal);
        verify(personalMapper, times(1)).toPersonalResponseDto(personal);

    }

    @Test
    void updateNotFound() {
        Long id = 1L;
        PersonalUpdateDTO personalUpdateDTO = new PersonalUpdateDTO();
        when(personalRepository.findById(id)).thenReturn(java.util.Optional.empty());

        assertThrows(PersonalNotFound.class, () -> personalService.update(id, personalUpdateDTO));
        verify(personalRepository, times(1)).findById(id);
        verify(personalMapper, never()).toPersonal(any(), any());
        verify(personalRepository, never()).save(any());
        verify(personalMapper, never()).toPersonalResponseDto(any());
    }



    @Test
    void findAll() {
        List<Personal> expectedPersonal = Arrays.asList(personal1, personal2);
        List<PersonalResponseDTO> expectedResponsePersonal = Arrays.asList(personalResponseDTO1, personalResponseDTO2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dni").ascending());
        Page<Personal> expectedPage = new PageImpl<>(expectedPersonal);
        when(personalRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(personalMapper.toPersonalResponseDto(personal1)).thenReturn(personalResponseDTO1);
        when(personalMapper.toPersonalResponseDto(personal2)).thenReturn(personalResponseDTO2);
        Page<PersonalResponseDTO> actualPage = personalService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        assertAll("findAll",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );

        verify(personalRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(personalMapper, times(2)).toPersonalResponseDto((any(Personal.class)));

    }

    @Test
    void findAllParams(){
        Optional<String> dni = Optional.of("123456789");
        Optional<String> nombre = Optional.of("Benito");
        Optional<String> apellidos = Optional.of("Sanchez");
        Optional<String> fechaNacimiento = Optional.of("12/12/1999");
        Optional<String> direccion = Optional.of("Calle de la piruleta");
        Optional<String> iban =Optional.of("ES123456789123456");
        List<Personal> expectedPersonal = Arrays.asList(personal1, personal2);
        List<PersonalResponseDTO> expectedResponsePersonal = Arrays.asList(personalResponseDTO1, personalResponseDTO2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dni").ascending());
        Page<Personal> expectedPage = new PageImpl<>(expectedPersonal);
        when(personalRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(personalMapper.toPersonalResponseDto(personal1)).thenReturn(personalResponseDTO1);
        when(personalMapper.toPersonalResponseDto(personal2)).thenReturn(personalResponseDTO2);
        Page<PersonalResponseDTO> actualPage = personalService.findAll(dni, nombre, apellidos, fechaNacimiento, direccion, iban, pageable);
        assertAll("findAll",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0),
                () -> assertEquals(actualPage.getTotalElements(), 2),
                () -> assertFalse(dni.isEmpty())
        );


        verify(personalRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(personalMapper, times(2)).toPersonalResponseDto((any(Personal.class)));
    }



    @Test
    void findById() {
        Long id = 1L;
        Personal personal = new Personal();
        when(personalRepository.findById(id)).thenReturn(java.util.Optional.of(personal));
        when(personalMapper.toPersonalResponseDto(personal)).thenReturn(personalResponseDTO1);

        PersonalResponseDTO result = personalService.findById(id);

        assertEquals(result, personalResponseDTO1);
        verify(personalRepository, times(1)).findById(id);
        verify(personalMapper, times(1)).toPersonalResponseDto(personal);

    }

    @Test
    void findByIdNotFound() {
        Long id = 1L;
        when(personalRepository.findById(id)).thenReturn(java.util.Optional.empty());

        assertThrows(PersonalNotFound.class, () -> personalService.findById(id));
        verify(personalRepository, times(1)).findById(id);
        verify(personalMapper, never()).toPersonalResponseDto(any());

    }

    @Test
    void deleteById() {
        Long id = 1L;
        Personal personal = new Personal();
        when(personalRepository.findById(id)).thenReturn(java.util.Optional.of(personal));

        personalService.deleteById(id);
        verify(personalRepository, times(1)).findById(id);
        verify(personalRepository, times(1)).deleteById(id);

    }

    @Test
    void deleteByIdNotFound() {
        Long id = 1L;
        when(personalRepository.findById(id)).thenReturn(java.util.Optional.empty());

        assertThrows(PersonalNotFound.class, () -> personalService.deleteById(id));
        verify(personalRepository, times(1)).findById(id);
        verify(personalRepository, never()).deleteById(id);


    }
}