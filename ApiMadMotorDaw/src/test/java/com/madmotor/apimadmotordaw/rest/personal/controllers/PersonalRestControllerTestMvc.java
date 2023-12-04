package com.madmotor.apimadmotordaw.rest.personal.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalCreateDTO;
import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalResponseDTO;
import com.madmotor.apimadmotordaw.rest.personal.models.Personal;
import com.madmotor.apimadmotordaw.rest.personal.services.PersonalService;
import com.madmotor.apimadmotordaw.utils.pagination.PageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"}) // Usuario de prueba (admin, tiene de rol usaurio y admin)

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class PersonalRestControllerTestMvc {

    private final String myEndpoint = "/v1/personal";

    private final Personal personal1 = new Personal().builder()
            .id(1L)
            .dni("741269458")
            .nombre("Juani")
            .apellidos("Torres")
            .fechaNacimiento("11-09-2002")
            .direccion("Av. de la Constitución 1")
            .iban("ES123456489123456789")
            .sueldo(1200.0)
            .telefono("123456789")
            .build();

    private final Personal personal2 = new Personal().builder()
            .id(1L)
            .dni("127649853")
            .nombre("Pepe")
            .apellidos("Torres")
            .fechaNacimiento("11-09-2002")
            .direccion("Av. de la Constitución 1")
            .iban("ES123456787123456789")
            .sueldo(1200.0)
            .telefono("123456789")
            .build();

    private final PersonalResponseDTO testResponseDTO1 = PersonalResponseDTO.builder()
            .id(1L)
            .dni("764197064")
            .nombre("Paco")
            .apellidos("Tomas")
            .fechaNacimiento("01-01-2002")
            .direccion("Camino de la Fuente 1")
            .iban("ES123456789123456789")
            .sueldo(1200.0)
            .telefono("123456789")
            .build();

    private final PersonalResponseDTO testResponseDTO2 = PersonalResponseDTO.builder()
            .id(1L)
            .dni("671005974")
            .nombre("Pepa")
            .apellidos("Vaquerizo")
            .fechaNacimiento("19-11-2006")
            .direccion("Callenueva 1")
            .iban("ES754145653945")
            .sueldo(1200.0)
            .telefono("123456789")
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private PersonalService personalService;


    @Autowired
    public PersonalRestControllerTestMvc(PersonalService personalService) {
        this.personalService = personalService;
    }

    @Test
    void getPersonalById() throws Exception {
        var endpointPruebaId = myEndpoint + "/" + personal1.getId();
        when(personalService.findById(personal1.getId())).thenReturn(testResponseDTO1);
        MockHttpServletResponse response = mockMvc.perform(get(endpointPruebaId).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        PersonalResponseDTO personalResponse = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(testResponseDTO1, personalResponse));
    }

    @Test
    void createPersonal() throws Exception {
        var personalDto = PersonalResponseDTO.builder()
                .dni("796417064")
                .nombre("Tomas")
                .apellidos("Martinez")
                .fechaNacimiento("21-01-2002")
                .direccion("Calle tercera 1")
                .iban("ES123456729123456789")
                .sueldo(1200.0)
                .telefono("123456789")
                .build();
        when(personalService.save(any(PersonalCreateDTO.class))).thenReturn(personalDto);
        MockHttpServletResponse response = mockMvc.perform(post(myEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personalDto)))
                        .andExpect(status().isCreated())
                        .andReturn().getResponse();
        PersonalResponseDTO res = objectMapper.readValue(response.getContentAsString(), PersonalResponseDTO.class);
        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), response.getStatus()),
                () -> assertEquals(personalDto, res));
        Mockito.verify(personalService, Mockito.times(1)).save(any(PersonalCreateDTO.class));
    }

    @Test
    void getAllPersonal() throws Exception {
        var personalList = List.of(testResponseDTO1, testResponseDTO2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(personalList);
        when(personalService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);
        MockHttpServletResponse response = mockMvc.perform(get(myEndpoint).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        PageResponse<PersonalResponseDTO> personalResponse = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, personalResponse.totalElements()),
                () -> assertEquals(1, personalResponse.totalPages()),
                () -> assertEquals(2, personalResponse.content().size()));
        Mockito.verify(personalService, Mockito.times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);

    }


    @Test
    void deletePersonalById() throws Exception {
        var endpointPruebaId = myEndpoint + "/" + personal1.getId();
        Mockito.doNothing().when(personalService).deleteById(personal1.getId());

        MockHttpServletResponse response = mockMvc.perform(delete(endpointPruebaId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent())
                        .andReturn().getResponse();
        assertAll(
                () -> assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus()));
        Mockito.verify(personalService, Mockito.times(1)).deleteById(personal1.getId());
    }
    }
