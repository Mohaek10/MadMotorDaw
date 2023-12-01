package com.madmotor.apimadmotordaw.rest.personal.controllers;

import com.madmotor.apimadmotordaw.rest.personal.controllers.PersonalRestController;
import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalCreateDTO;
import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalResponseDTO;
import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalUpdateDTO;
import com.madmotor.apimadmotordaw.rest.personal.services.PersonalService;
import com.madmotor.apimadmotordaw.utils.pagination.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonalRestControllerTest {

    private final String myEndpoint = "/v1/personal";

    @Mock
    private PersonalService personalService;

    @InjectMocks
    private PersonalRestController personalRestController;

    private PersonalCreateDTO createDto = new PersonalCreateDTO().builder()
            .dni("123456789")
            .nombre("Pedro")
            .apellidos("Lopez")
            .fechaNacimiento("04-04-1956")
            .direccion("Calle de la amapola 34")
            .iban("ES14785236978945")
            .build();

    private PersonalResponseDTO testResponseDTO = PersonalResponseDTO.builder()
            .id(1L)
            .dni("987654321")
            .nombre("Marta")
            .apellidos("Perez")
            .fechaNacimiento("07-08-2004")
            .direccion("Calle del Paraiso 12")
            .iban("ES54197561478564")
            .build();

    private PersonalResponseDTO testResponseDTO2 = PersonalResponseDTO.builder()
            .id(2L)
            .dni("965473214")
            .nombre("Ruben")
            .apellidos("Jazmin")
            .fechaNacimiento("18-04-1999")
            .direccion("Camino de la luna 45")
            .iban("ES96574167154618")
            .build();

    private PersonalUpdateDTO updateDTO = new PersonalUpdateDTO().builder()
            .direccion("direccion")
            .iban("iban")
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getPersonalById() {
        Long id = 1L;
        when(personalService.findById(id)).thenReturn(testResponseDTO);
        ResponseEntity<PersonalResponseDTO> response = personalRestController.getPersonalById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testResponseDTO, response.getBody());
        verify(personalService, times(1)).findById(id);

    }

    @Test
    void createPersonal() {
        when(personalService.save(createDto)).thenReturn(testResponseDTO);
        ResponseEntity<PersonalResponseDTO> response = personalRestController.createPersonal(createDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testResponseDTO, response.getBody());
        verify(personalService, times(1)).save(createDto);
    }

    @Test
    void getAllPersonal() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<PersonalResponseDTO> page = new PageImpl<>(List.of(testResponseDTO, testResponseDTO2));
        when(personalService.findAll(any(), any(), any(), any(), any(), any(), any())).thenReturn(page);

        ResponseEntity<PageResponse<PersonalResponseDTO>> response = personalRestController.getAllPersonal(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), 0, 10, "id", "asc");

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(2, response.getBody().totalElements()),
                () -> assertEquals(1, response.getBody().totalPages()),
                () -> assertEquals(2, response.getBody().content().size()),
                () -> verify(personalService, times(1)).findAll(any(), any(), any(), any(), any(), any(), any())
        );


    }

    @Test
    void updatePersonalById() {
        Long id = 1L;
        when(personalService.update(id, updateDTO)).thenReturn(testResponseDTO);
        ResponseEntity<PersonalResponseDTO> response = personalRestController.updatePersonalById(id, updateDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testResponseDTO, response.getBody());
        verify(personalService, times(1)).update(id, updateDTO);
    }

    @Test
    void deletePersonalById() {
        Long id = 1L;
        ResponseEntity<Void> response = personalRestController.deletePersonalById(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(personalService, times(1)).deleteById(id);
    }
}