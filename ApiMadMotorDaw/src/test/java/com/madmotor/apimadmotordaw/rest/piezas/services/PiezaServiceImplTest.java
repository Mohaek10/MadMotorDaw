package com.madmotor.apimadmotordaw.rest.piezas.services;

import com.madmotor.apimadmotordaw.rest.piezas.dto.PiezaCreateDTO;
import com.madmotor.apimadmotordaw.rest.piezas.dto.PiezaResponseDTO;
import com.madmotor.apimadmotordaw.rest.piezas.dto.PiezaUpdateDTO;
import com.madmotor.apimadmotordaw.rest.piezas.exceptions.PiezaNotFound;
import com.madmotor.apimadmotordaw.rest.piezas.mappers.PiezaMapper;
import com.madmotor.apimadmotordaw.rest.piezas.models.Pieza;
import com.madmotor.apimadmotordaw.rest.piezas.repositories.PiezaRepository;
import com.madmotor.apimadmotordaw.rest.piezas.services.PiezaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PiezaServiceImplTest {

    @Mock
    private PiezaRepository piezaRepository;

    @Mock
    private PiezaMapper piezaMapper;

    @InjectMocks
    private PiezaServiceImpl piezaService;

    private final Pieza pieza1 = Pieza.builder()
            .id(UUID.randomUUID())
            .name("name1")
            .description("description1")
            .price(1.0)
            .stock(1)
            .image("image1")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    private final PiezaResponseDTO piezaResponseDTO1 = PiezaResponseDTO.builder()
            .id(UUID.randomUUID())
            .name("name1")
            .description("description1")
            .price(1.0)
            .stock(1)
            .image("image1")
            .build();
    private final Pieza pieza2 = Pieza.builder()
            .id(UUID.randomUUID())
            .name("name2")
            .description("description2")
            .price(2.0)
            .stock(2)
            .image("image2")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    private final PiezaResponseDTO piezaResponseDTO2 = PiezaResponseDTO.builder()
            .id(UUID.randomUUID())
            .name("name2")
            .description("description2")
            .price(2.0)
            .stock(2)
            .image("image2")
            .build();

    @Test
    void findAll() {
        List<Pieza> expectedPiezas = Arrays.asList(pieza1, pieza2);
        List<PiezaResponseDTO> expectedResponsePiezas = Arrays.asList(piezaResponseDTO1, piezaResponseDTO2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Pieza> expectedPage = new PageImpl<>(expectedPiezas);
        when(piezaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(piezaMapper.toPiezaResponse(any(Pieza.class))).thenReturn(piezaResponseDTO1);
        Page<PiezaResponseDTO> actualPage = piezaService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        System.out.println(actualPage.getTotalElements());
        assertAll("findAll",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );

        verify(piezaRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(piezaMapper, times(2)).toPiezaResponse(any(Pieza.class));
    }

    @Test
    void findAllPiezasByName() {
        Optional<String> name = Optional.of("name2");
        List<Pieza> expectedPiezas = List.of(pieza2);
        List<PiezaResponseDTO> responsePiezasExpected = List.of(piezaResponseDTO2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Pieza> expectedPage = new PageImpl<>(expectedPiezas);
        when(piezaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(piezaMapper.toPiezaResponse(any(Pieza.class))).thenReturn(piezaResponseDTO2);
        Page<PiezaResponseDTO> actualPage = piezaService.findAll(name, Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        assertAll("findAllByName",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );
        verify(piezaRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(piezaMapper, times(1)).toPiezaResponse(any(Pieza.class));
    }


    @Test
    void FindById() {
        UUID id = UUID.randomUUID();
        Pieza pieza = new Pieza();
        when(piezaRepository.findById(id)).thenReturn(Optional.of(pieza));
        when(piezaMapper.toPiezaResponse(pieza)).thenReturn(new PiezaResponseDTO());

        PiezaResponseDTO result = piezaService.findById(id);

        assertEquals(result, new PiezaResponseDTO());
        verify(piezaRepository, times(1)).findById(id);
        verify(piezaMapper, times(1)).toPiezaResponse(pieza);
    }

    @Test
    void FindByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(piezaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PiezaNotFound.class, () -> piezaService.findById(id));

        verify(piezaRepository, times(1)).findById(id);
        verify(piezaMapper, never()).toPiezaResponse(any());
    }

    @Test
    void save() {
        PiezaCreateDTO piezaCreateDTO = new PiezaCreateDTO();
        Pieza pieza = new Pieza();
        when(piezaMapper.toPieza(piezaCreateDTO)).thenReturn(pieza);
        when(piezaRepository.save(pieza)).thenReturn(pieza);
        when(piezaMapper.toPiezaResponse(pieza)).thenReturn(new PiezaResponseDTO());

        PiezaResponseDTO result = piezaService.save(piezaCreateDTO);

        assertEquals(result, new PiezaResponseDTO());
        verify(piezaRepository, times(1)).save(pieza);
        verify(piezaMapper, times(1)).toPiezaResponse(pieza);
    }


    @Test
    void testDeleteById() {
        UUID id = UUID.randomUUID();
        when(piezaRepository.findById(id)).thenReturn(Optional.of(new Pieza()));

        piezaService.deleteById(id);

        verify(piezaRepository, times(1)).findById(id);
        verify(piezaRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteByIdWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(piezaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PiezaNotFound.class, () -> piezaService.deleteById(id));

        verify(piezaRepository, times(1)).findById(id);
        verify(piezaRepository, never()).deleteById(id);
    }

    @Test
    void testUpdate() {
        UUID id = UUID.randomUUID();
        Pieza pieza = new Pieza();
        PiezaUpdateDTO piezaUpdateDTO = new PiezaUpdateDTO();
        when(piezaRepository.findById(id)).thenReturn(Optional.of(pieza));
        when(piezaMapper.toPieza(piezaUpdateDTO, pieza)).thenReturn(pieza);
        when(piezaRepository.save(pieza)).thenReturn(pieza);
        when(piezaMapper.toPiezaResponse(pieza)).thenReturn(new PiezaResponseDTO());

        PiezaResponseDTO result = piezaService.update(id, piezaUpdateDTO);

        assertEquals(result, new PiezaResponseDTO());
        verify(piezaRepository, times(1)).findById(id);
        verify(piezaMapper, times(1)).toPieza(piezaUpdateDTO, pieza);
        verify(piezaRepository, times(1)).save(pieza);
        verify(piezaMapper, times(1)).toPiezaResponse(pieza);
    }

    @Test
    void testUpdateNotFound() {
        UUID id = UUID.randomUUID();
        PiezaUpdateDTO piezaUpdateDTO = new PiezaUpdateDTO();
        when(piezaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PiezaNotFound.class, () -> piezaService.update(id, piezaUpdateDTO));

        verify(piezaRepository, times(1)).findById(id);
        verify(piezaMapper, never()).toPieza(any(), any());
        verify(piezaRepository, never()).save(any());
        verify(piezaMapper, never()).toPiezaResponse(any());
    }
}
