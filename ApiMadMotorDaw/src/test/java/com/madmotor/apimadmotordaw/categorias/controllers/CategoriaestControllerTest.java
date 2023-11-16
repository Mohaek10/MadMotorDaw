package com.madmotor.apimadmotordaw.categorias.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.madmotor.apimadmotordaw.categorias.dto.CategoriaDto;
import com.madmotor.apimadmotordaw.categorias.models.Categoria;
import com.madmotor.apimadmotordaw.categorias.services.CategoriaService;
import com.madmotor.apimadmotordaw.utils.PageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
class CategoriaestControllerTest {
    private final String myEndpoint = "/v1/categorias";

    private final Categoria categoria1 = new Categoria(1L, "MOTOS", LocalDateTime.now(), LocalDateTime.now(), false);
    private final Categoria categoria2 = new Categoria(2L, "DEPOSTIVOS", LocalDateTime.now(), LocalDateTime.now(), false);

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private CategoriaService categoriasService;
    @Autowired
    private JacksonTester<CategoriaDto> jsonCategoriaDto;

    @Autowired
    public CategoriaestControllerTest(CategoriaService categoriaService){
        this.categoriasService = categoriaService;
        mapper.registerModule(new JavaTimeModule());
    }


    @Test
    void getAllCategorias() throws Exception {
        var list = List.of(categoria1, categoria2);
        Page<Categoria> page = new PageImpl<>(list);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        when(categoriasService.findAll(Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Categoria> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );
        verify(categoriasService, times(1)).findAll(Optional.empty(), Optional.empty(), pageable);
    }
    @Test
    void getAllCategoriasByname() throws Exception {
        var list = List.of(categoria1, categoria2);
        Page<Categoria> page = new PageImpl<>(list);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        when(categoriasService.findAll(Optional.of("MOTOS"), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .param("nombre", "MOTOS")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Categoria> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );
        verify(categoriasService, times(1)).findAll(Optional.of("MOTOS"), Optional.empty(), pageable);
    }

    @Test
    void getCategoryById() throws Exception {
        var myLocalEndpoint = myEndpoint + "/1";

        when(categoriasService.findById(anyLong())).thenReturn(categoria1);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Categoria res = mapper.readValue(response.getContentAsString(), Categoria.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(categoria1, res)
        );
        verify(categoriasService, times(1)).findById(anyLong());
    }

    @Test
    void createCategory() throws Exception{
        var categoriaDto = new CategoriaDto("MOTOS", false);
        when(categoriasService.save(any(CategoriaDto.class))).thenReturn(categoria1);
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonCategoriaDto.write(categoriaDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Categoria res = mapper.readValue(response.getContentAsString(), Categoria.class);
        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(categoria1, res)
        );

        verify(categoriasService, times(1)).save(any(CategoriaDto.class));
    }

    @Test
    void updateCategory() throws Exception {
        var myLocalEndpoint = myEndpoint + "/1";
        var categoriaDto = new CategoriaDto("MOTOS", false);

        when(categoriasService.update(anyLong(), any(CategoriaDto.class))).thenReturn(categoria1);
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonCategoriaDto.write(categoriaDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Categoria res = mapper.readValue(response.getContentAsString(), Categoria.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(categoria1, res)
        );
        verify(categoriasService, times(1)).update(anyLong(), any(CategoriaDto.class));
    }

    @Test
    void deleteCategory() throws Exception {
        var myLocalEndpoint = myEndpoint + "/1";
        doNothing().when(categoriasService).delete(anyLong());

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(204, response.getStatus())
        );

        verify(categoriasService, times(1)).delete(anyLong());
    }

}