package com.madmotor.apimadmotordaw.piezas.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madmotor.apimadmotordaw.piezas.dto.PiezaCreateDTO;
import com.madmotor.apimadmotordaw.piezas.dto.PiezaResponseDTO;
import com.madmotor.apimadmotordaw.piezas.dto.PiezaUpdateDTO;
import com.madmotor.apimadmotordaw.piezas.exceptions.PiezaNotFound;
import com.madmotor.apimadmotordaw.piezas.models.Pieza;
import com.madmotor.apimadmotordaw.piezas.services.PiezaService;
import com.madmotor.apimadmotordaw.utils.PageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class PiezaControllerTestMvc {
    private final String myEndpoint = "/v1/piezas";

    private final Pieza pieza = new Pieza().builder()
            .id(UUID.fromString("9f2220a7-cc32-487d-87d8-27981735b20b"))
            .name("name1")
            .description("description")
            .price(2.0)
            .stock(1)
            .image("image")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    private final Pieza pieza2 = new Pieza().builder()
            .id(UUID.fromString("5119c4aa-8339-4c0f-9cd3-6300063cd882"))
            .name("name2")
            .description("description")
            .price(1.0)
            .stock(1)
            .image("image")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    private final PiezaResponseDTO testResponseDTO = PiezaResponseDTO.builder()
            .id(UUID.fromString("9f2220a7-cc32-487d-87d8-27981735b20b"))
            .name("name1")
            .description("description")
            .price(2.0)
            .stock(1)
            .image("image")
            .build();
    private final PiezaResponseDTO testResponseDTO2 = PiezaResponseDTO.builder()
            .id(UUID.fromString("5119c4aa-8339-4c0f-9cd3-6300063cd882"))
            .name("name2")
            .description("description")
            .price(1.0)
            .stock(1)
            .image("image")
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private PiezaService piezaService;

    @Autowired
    public PiezaControllerTestMvc(PiezaService piezaService) {
        this.piezaService = piezaService;
    }

    @Test
    void getAllPiezas() throws Exception {
        var piezasList = List.of(testResponseDTO, testResponseDTO2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(piezasList);
        when(piezaService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);
        MockHttpServletResponse response = mockMvc.perform(get(myEndpoint).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        PageResponse<PiezaResponseDTO> piezasResponse = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, piezasResponse.totalElements()),
                () -> assertEquals(1, piezasResponse.totalPages()),
                () -> assertEquals(2, piezasResponse.content().size()));
        Mockito.verify(piezaService, Mockito.times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getPiezaById() throws Exception {
        var myEndpointId = myEndpoint + "/" + pieza.getId();
        when(piezaService.findById(pieza.getId())).thenReturn(testResponseDTO);
        MockHttpServletResponse response = mockMvc.perform(get(myEndpointId).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        PiezaResponseDTO res = objectMapper.readValue(response.getContentAsString(), PiezaResponseDTO.class);
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(testResponseDTO, res));
    }

    @Test
    void createPieza() throws Exception {
        var piezaDto = PiezaResponseDTO.builder()
                .name("name1")
                .description("description")
                .price(1.0)
                .stock(1)
                .image("image")
                .build();
        when(piezaService.save(any(PiezaCreateDTO.class))).thenReturn(piezaDto);
        MockHttpServletResponse response = mockMvc.perform(post(myEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(piezaDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse();
        PiezaResponseDTO res = objectMapper.readValue(response.getContentAsString(), PiezaResponseDTO.class);
        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(piezaDto, res));
        Mockito.verify(piezaService, Mockito.times(1)).save(any(PiezaCreateDTO.class));
    }





    @Test
    void deletePieza() throws Exception {
        var myEndpointId = myEndpoint + "/" + pieza.getId();
        Mockito.doNothing().when(piezaService).deleteById(pieza.getId());

        MockHttpServletResponse response = mockMvc.perform(delete(myEndpointId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(204, response.getStatus()));
        Mockito.verify(piezaService, Mockito.times(1)).deleteById(pieza.getId());
    }
    @Test
    void deletePiezaNotFound() throws Exception{
        var myEndpointId = myEndpoint + "/" + pieza.getId();
        Mockito.doThrow(new PiezaNotFound(pieza.getId())).when(piezaService).deleteById(pieza.getId());
        MockHttpServletResponse response = mockMvc.perform(delete(myEndpointId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(404, response.getStatus()));
        Mockito.verify(piezaService, Mockito.times(1)).deleteById(pieza.getId());
    }

    @Test
    void handleValidationExceptions() {
    }
}