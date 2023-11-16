package com.madmotor.apimadmotordaw.vehiculos.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.madmotor.apimadmotordaw.categorias.models.Categoria;
import com.madmotor.apimadmotordaw.utils.PageResponse;
import com.madmotor.apimadmotordaw.vehiculos.dto.VehiculoCreateDto;
import com.madmotor.apimadmotordaw.vehiculos.dto.VehiculoUpdateDto;
import com.madmotor.apimadmotordaw.vehiculos.exceptions.VehiculoNotFound;
import com.madmotor.apimadmotordaw.vehiculos.models.Vehiculo;
import com.madmotor.apimadmotordaw.vehiculos.services.VehiculoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
class VehiculoRestControllerTest {
    private final String endPoint = "/v1/vehiculos";
    private final Categoria categoria = new Categoria(1L, "Camion", LocalDateTime.now(), LocalDateTime.now(), false);
    private final Vehiculo vehiculo1 = new Vehiculo(UUID.randomUUID(), "Mercedes", "Actros", 2019, 100000.0, 100000.0, 100, "https://loremflickr.com/150/150", "Camion de 3 ejes", LocalDateTime.now(), LocalDateTime.now(), categoria, false);
    private final Vehiculo vehiculo2 = new Vehiculo(UUID.randomUUID(), "TruckLeganes", "Track", 1999, 20.0, 300000.0, 2, "https://loremflickr.com/150/150", "Camion de 5 ejes", LocalDateTime.now(), LocalDateTime.now(), categoria, false);

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private VehiculoService vehiculoService;

    @Autowired
    private JacksonTester<VehiculoCreateDto> jsonVehiculoCreateDto;
    @Autowired
    private JacksonTester<VehiculoUpdateDto> jsonVehiculoUpdateDto;

    @Autowired
    public VehiculoRestControllerTest(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAllVehiculos() throws Exception {
        var vehiculosLista = List.of(vehiculo1, vehiculo2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(vehiculosLista);

        when(vehiculoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);
        MockHttpServletResponse response = mockMvc.perform(
                        get(endPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Vehiculo> res = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });
        assertAll(() -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size()));
        verify(vehiculoService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);


    }

    @Test
    void getAllVehiculosByMarca() throws Exception {
        var vehiculosLista = List.of(vehiculo2);
        var localEndpoint = endPoint + "?marca=TruckLeganes";
        Optional<String> marca = Optional.of("TruckLeganes");
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(vehiculosLista);
        when(vehiculoService.findAll(marca, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Vehiculo> res = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });
        assertAll(() -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size()));
        verify(vehiculoService, times(1)).findAll(marca, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllVehiculosByCategoria() throws Exception {
        var vehiculosLista = List.of(vehiculo2);
        var localEndpoint = endPoint + "?categoria=Camion";
        Optional<String> categoria = Optional.of("Camion");
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(vehiculosLista);
        when(vehiculoService.findAll(Optional.empty(), categoria, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Vehiculo> res = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });
        assertAll(() -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size()));
        verify(vehiculoService, times(1)).findAll(Optional.empty(), categoria, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllVehiculosByModelo() throws Exception {
        var vehiculosLista = List.of(vehiculo2);
        var localEndpoint = endPoint + "?modelo=Track";
        Optional<String> modelo = Optional.of("Track");
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(vehiculosLista);
        when(vehiculoService.findAll(Optional.empty(), Optional.empty(), modelo, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Vehiculo> res = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });
        assertAll(() -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size()));
        verify(vehiculoService, times(1)).findAll(Optional.empty(), Optional.empty(), modelo, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllVehiculosByMinYear() throws Exception {
        var vehiculosLista = List.of(vehiculo2);
        var localEndpoint = endPoint + "?minYear=1999";
        Optional<Integer> minYear = Optional.of(1999);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(vehiculosLista);
        when(vehiculoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), minYear, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Vehiculo> res = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });
        assertAll(() -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size()));
        verify(vehiculoService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), minYear, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllVehicleByIsDelete() throws Exception {
        var vehiculosLista = List.of(vehiculo2);
        var localEndpoint = endPoint + "?isDelete=true";
        Optional<Boolean> isDelete = Optional.of(true);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(vehiculosLista);
        when(vehiculoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), isDelete, Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Vehiculo> res = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });
        assertAll(() -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size()));
        verify(vehiculoService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), isDelete, Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllVehiculosByKmMax() throws Exception {
        var vehiculosLista = List.of(vehiculo2);
        var localEndpoint = endPoint + "?kmMax=20.0";
        Optional<Double> kmMax = Optional.of(20.0);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(vehiculosLista);
        when(vehiculoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), kmMax, Optional.empty(), Optional.empty(), pageable)).thenReturn(page);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Vehiculo> res = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });
        assertAll(() -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size()));
        verify(vehiculoService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), kmMax, Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllVehiculosByPrecioMax() throws Exception {
        var vehiculosLista = List.of(vehiculo2);
        var localEndpoint = endPoint + "?precioMax=300000.0";
        Optional<Double> precioMax = Optional.of(300000.0);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(vehiculosLista);
        when(vehiculoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), precioMax, Optional.empty(), pageable)).thenReturn(page);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Vehiculo> res = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });
        assertAll(() -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size()));
        verify(vehiculoService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), precioMax, Optional.empty(), pageable);
    }

    @Test
    void getAllVehicleByStockMin() throws Exception {
        var vehiculosLista = List.of(vehiculo2);
        var localEndpoint = endPoint + "?stockMin=2";
        Optional<Integer> stockMin = Optional.of(2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(vehiculosLista);
        when(vehiculoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), stockMin, pageable)).thenReturn(page);
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Vehiculo> res = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });
        assertAll(() -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size()));
        verify(vehiculoService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), stockMin, pageable);
    }


    @Test
    void getVehiculoById() throws Exception {
        var myEndPoint = endPoint + "/" + vehiculo1.getId();
        when(vehiculoService.findById(vehiculo1.getId().toString())).thenReturn(vehiculo1);
        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Vehiculo res = objectMapper.readValue(response.getContentAsString(), Vehiculo.class);
        assertAll(() -> assertEquals(200, response.getStatus()),
                () -> assertEquals(vehiculo1.getId(), res.getId()));
        verify(vehiculoService, times(1)).findById(vehiculo1.getId().toString());
    }

    @Test
    void getVehiculoById_NotFound() throws Exception {
        String id = UUID.randomUUID().toString();
        var myEndPoint = endPoint + "/" + id;
        when(vehiculoService.findById(id)).thenThrow(new VehiculoNotFound(id));
        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(() -> assertEquals(404, response.getStatus()));
        verify(vehiculoService, times(1)).findById(id);
    }

    @Test
    void createVehiculo() throws Exception {
        var myEndpoint = endPoint;
        var vehiculoDto = new VehiculoCreateDto("CocheNuevoHDFull", "LoUltimo", 2030, 0.0, 100000.0, 100, "https://loremflickr.com/150/150", "Camion de 3 ejes", "Camion");
        when(vehiculoService.save(any(VehiculoCreateDto.class))).thenReturn(vehiculo1);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonVehiculoCreateDto.write(vehiculoDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Vehiculo res = objectMapper.readValue(response.getContentAsString(), Vehiculo.class);
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(vehiculo1, res)
        );
        verify(vehiculoService, times(1)).save(any(VehiculoCreateDto.class));


    }

    @Test
    void createVehiculo_ConDatosIncorrectos() throws Exception {
        var vehiculoDto = new VehiculoCreateDto(
                "c", "1", -2090, -1.0, -1.0, -1, "", "", "");
        MockHttpServletResponse response = mockMvc.perform(
                        post(endPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonVehiculoCreateDto.write(vehiculoDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("La marca debe tener al menos 2 caracteres")),
                () -> assertTrue(response.getContentAsString().contains("El modelo debe tener al menos 2 caracteres")),
                () -> assertTrue(response.getContentAsString().contains("Los kilometros no pueden ser negativos")),
                () -> assertTrue(response.getContentAsString().contains("El precio no puede ser negativo")),
                () -> assertTrue(response.getContentAsString().contains("El stock no puede ser negativo")),
                () -> assertTrue(response.getContentAsString().contains("La descripcion debe tener al menos 4 caracteres")),
                () -> assertTrue(response.getContentAsString().contains("La categoria no puede estar vacia"))
        );

    }

    @Test
    void updateVehiculo() throws Exception {
        var myEndpoint = endPoint + "/" + vehiculo1.getId();
        var vehiculoDto = new VehiculoUpdateDto("NuevoMercedesActualizado", "Actros", 2019, 100000.0, 100000.0, 100, "https://loremflickr.com/150/150", "Camion de 3 ejes", "Camion", false);
        when(vehiculoService.update(vehiculo1.getId().toString(), vehiculoDto)).thenReturn(vehiculo1);
        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonVehiculoUpdateDto.write(vehiculoDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Vehiculo res = objectMapper.readValue(response.getContentAsString(), Vehiculo.class);
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(vehiculo1, res)
        );
        verify(vehiculoService, times(1)).update(vehiculo1.getId().toString(), vehiculoDto);
    }

    @Test
    void updateVehiculoNotFound() throws Exception {
        var myEndpoint = endPoint + "/" + vehiculo1.getId();
        var vehiculoDto = new VehiculoUpdateDto("NuevoMercedesActualizado", "Actros", 2019, 100000.0, 100000.0, 100, "https://loremflickr.com/150/150", "Camion de 3 ejes", "Camion", false);
        when(vehiculoService.update(vehiculo1.getId().toString(), vehiculoDto)).thenThrow(new VehiculoNotFound(vehiculo1.getId().toString()));
        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonVehiculoUpdateDto.write(vehiculoDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(vehiculoService, times(1)).update(vehiculo1.getId().toString(), vehiculoDto);

    }

    @Test
    void updateWithBadBody() throws Exception {
        var myEndpoint = endPoint + "/" + vehiculo1.getId();
        var vehiculoDto = new VehiculoUpdateDto("c", "1", -2090, -1.0, -1.0, -1, "", "", "", false);
        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonVehiculoUpdateDto.write(vehiculoDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("La marca debe tener al menos 3 caracteres")),
                () -> assertTrue(response.getContentAsString().contains("Los kilometros no pueden ser negativos")),
                () -> assertTrue(response.getContentAsString().contains("El precio no puede ser negativo")),
                () -> assertTrue(response.getContentAsString().contains("El stock no puede ser negativo")),
                () -> assertTrue(response.getContentAsString().contains("La descripcion debe tener al menos 3 caracteres"))
        );
    }

    @Test
    void patchVehiculo() throws Exception {
        var myEndpoint = endPoint + "/" + vehiculo1.getId();
        var vehiculoDto = new VehiculoUpdateDto("NuevoMercedesActualizado", "Actros", 2019, 100000.0, 100000.0, 100, "https://loremflickr.com/150/150", "Camion de 3 ejes", "Camion", false);
        when(vehiculoService.update(vehiculo1.getId().toString(), vehiculoDto)).thenReturn(vehiculo1);
        MockHttpServletResponse response = mockMvc.perform(
                        patch(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonVehiculoUpdateDto.write(vehiculoDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Vehiculo res = objectMapper.readValue(response.getContentAsString(), Vehiculo.class);
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(vehiculo1, res)
        );

    }

    @Test
    void deleteVehiculo() throws Exception {
        var myEndpoint = endPoint + "/" + vehiculo1.getId();
        doNothing().when(vehiculoService).deleteById(vehiculo1.getId().toString());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertEquals(204, response.getStatus());

        verify(vehiculoService, times(1)).deleteById(vehiculo1.getId().toString());
    }
    @Test
    void deleteVehiculo_DevuelveNotFound() throws Exception {
        var myEndpoint = endPoint + "/" + vehiculo1.getId();
        doThrow(new VehiculoNotFound(vehiculo1.getId().toString())).when(vehiculoService).deleteById(vehiculo1.getId().toString());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(404, response.getStatus());
        verify(vehiculoService, times(1)).deleteById(vehiculo1.getId().toString());

    }
}