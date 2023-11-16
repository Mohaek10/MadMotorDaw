package com.madmotor.apimadmotordaw.piezas.controllers;

import com.madmotor.apimadmotordaw.piezas.dto.PiezaCreateDTO;
import com.madmotor.apimadmotordaw.piezas.dto.PiezaResponseDTO;
import com.madmotor.apimadmotordaw.piezas.dto.PiezaUpdateDTO;
import com.madmotor.apimadmotordaw.piezas.services.PiezaService;
import com.madmotor.apimadmotordaw.utils.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class PiezaControllerTest {
    private final String myEndpoint = "/v1/piezas";
    @Mock
    private PiezaService piezaService;

    @InjectMocks
    private PiezaController piezaController;

    private PiezaCreateDTO createDTO = new PiezaCreateDTO().builder()
            .name("createDTO")
            .description("description")
            .price(1.0)
            .stock(1)
            .build();
    private PiezaUpdateDTO updateDTO = new PiezaUpdateDTO().builder()
            .name("updateDTO")
            .description("description")
            .price(1.0)
            .stock(1)
            .image("image")
            .build();
    private PiezaResponseDTO testResponseDTO = PiezaResponseDTO.builder()
            .id(UUID.randomUUID())
            .name("name1")
            .description("description")
            .price(1.0)
            .stock(1)
            .image("image")
            .build();
    private PiezaResponseDTO testResponseDTO2 = PiezaResponseDTO.builder()
            .id(UUID.randomUUID())
            .name("name2")
            .description("description2")
            .price(2.0)
            .stock(2)
            .image("image2")
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void getAllPiezas() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<PiezaResponseDTO> page = new PageImpl<>(List.of(testResponseDTO, testResponseDTO2));
        when(piezaService.findAll(any(), any(), any(), any(), any())).thenReturn(page);
        ResponseEntity<PageResponse<PiezaResponseDTO>> response = piezaController.getAllPiezas(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), 0, 10, "id", "asc");
        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(2, response.getBody().totalElements()),
                () -> assertEquals(1, response.getBody().totalPages()),
                () -> assertEquals(2, response.getBody().content().size()),
                () -> assertEquals(testResponseDTO, response.getBody().content().get(0)),
                () -> assertEquals(testResponseDTO2, response.getBody().content().get(1)),
                () -> verify(piezaService, Mockito.times(1)).findAll(any(), any(), any(), any(), any())
        );


    }

    @Test
    void getPiezaById() {
        UUID id = UUID.randomUUID();
        when(piezaService.findById(id)).thenReturn(testResponseDTO);
        ResponseEntity<PiezaResponseDTO> response = piezaController.getPiezaById(id);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testResponseDTO, response.getBody());
        verify(piezaService, times(1)).findById(id);
    }

    @Test
    void createPieza() {
        when(piezaService.save(createDTO)).thenReturn(testResponseDTO);
        ResponseEntity<PiezaResponseDTO> response = piezaController.createPieza(createDTO);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(testResponseDTO, response.getBody());
        verify(piezaService, times(1)).save(createDTO);
    }

    @Test
    void updatePieza() {
        UUID id = UUID.randomUUID();
        when(piezaService.update(id, updateDTO)).thenReturn(testResponseDTO);
        ResponseEntity<PiezaResponseDTO> response = piezaController.updatePieza(id, updateDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testResponseDTO, response.getBody());
        verify(piezaService, times(1)).update(id, updateDTO);
    }

    @Test
    void updatePartialPieza() {
        UUID id = UUID.randomUUID();
        when(piezaService.update(id, updateDTO)).thenReturn(testResponseDTO);
        ResponseEntity<PiezaResponseDTO> response = piezaController.updatePartialPieza(id, updateDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testResponseDTO, response.getBody());
        verify(piezaService, times(1)).update(id, updateDTO);
    }

    @Test
    void deletePieza() {
        UUID id = UUID.randomUUID();
        ResponseEntity<Void> response = piezaController.deletePieza(id);
        assertEquals(204, response.getStatusCodeValue());
        verify(piezaService, times(1)).deleteById(id);
    }

}