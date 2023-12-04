package com.madmotor.apimadmotordaw.rest.clientes.Controller;

import com.madmotor.apimadmotordaw.rest.clientes.Controller.ClienteRestController;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteCreateRequest;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteReponse;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteUpdateRequest;
import com.madmotor.apimadmotordaw.rest.clientes.services.ClienteService;
import com.madmotor.apimadmotordaw.utils.pagination.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.internal.verification.VerificationModeFactory.times;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ClienteRestControllerTest {
    private final String myEndpoint = "/v1/clientes";
    @Mock
    ClienteService clienteService;
    @InjectMocks
    ClienteRestController clienteRestController;

    private ClienteCreateRequest createDTO = ClienteCreateRequest.builder()
            .nombre("CreateDTO")
            .apellido("marta")
            .direccion("Avenida Central 456")
            .codigoPostal(67890)
            .dni("12345678L")
            .imagen("perro.png").build();
    private ClienteUpdateRequest updateDTO = ClienteUpdateRequest.builder()
            .nombre("UpdateDTO")
            .apellido("mart")
            .direccion("Avenida Central 456")
            .codigoPostal(67890)
            .imagen("perro.png").build();
    private ClienteReponse testResponseDTO = ClienteReponse.builder()
            .id(UUID.fromString("382ace01-23f4-4f68-adf4-4e6ff7f2679c"))
            .nombre("TestResponseDTO")
            .apellido("TestResponseDTO")
            .direccion("TestResponseD")
            .codigoPostal(67890)
            .dni("12345678L")
            .imagen("perro.png").build();

    private ClienteReponse testResponseDTO2 = ClienteReponse.builder()
            .nombre("TestResponseDTO2")
            .apellido("TestResponseDTO2")
            .direccion("TestResponseD")
            .codigoPostal(67890)
            .dni("12345678L")
            .imagen("perro.png").build();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllClientes() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<ClienteReponse> pageResponse = new PageImpl<>(List.of(testResponseDTO, testResponseDTO2));
        when(clienteService.findAll(any(), any(), any(), any(), any())).thenReturn(pageResponse);
        ResponseEntity<PageResponse<ClienteReponse>> response = clienteRestController.getAllClientes(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), 0, 10, "id", "asc");

        assertAll(
                () -> assertEquals(200, response.getStatusCodeValue()),
                () -> assertEquals(2, response.getBody().totalElements()),
                () -> assertEquals(1, response.getBody().totalPages()),
                () -> assertEquals(2, response.getBody().content().size()),
                () -> assertEquals(testResponseDTO, response.getBody().content().get(0)),
                () -> assertEquals(testResponseDTO2, response.getBody().content().get(1)),
                () -> verify(clienteService, Mockito.times(1)).findAll(any(), any(), any(), any(), any())
        );

    }

    @Test
    void getClienteById() {
        UUID id = UUID.fromString("382ace01-23f4-4f68-adf4-4e6ff7f2679c");;
        when(clienteService.findByID(id)).thenReturn(testResponseDTO);
        ResponseEntity<ClienteReponse> response = clienteRestController.getClienteByID(id);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testResponseDTO, response.getBody());
        verify(clienteService, times(1)).findByID(id);
    }
    @Test
    void createCliente() {
        when(clienteService.savePost(createDTO)).thenReturn(testResponseDTO);
        ResponseEntity<ClienteReponse> response = clienteRestController.createCliente(createDTO);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(testResponseDTO, response.getBody());
        verify(clienteService, times(1)).savePost(createDTO);
    }
    @Test
    void updateCliente() {
        UUID id = UUID.fromString("382ace01-23f4-4f68-adf4-4e6ff7f2679c");
        when(clienteService.updateByID(id, updateDTO)).thenReturn(testResponseDTO);
        ResponseEntity<ClienteReponse> response = clienteRestController.updateCliente(id, updateDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testResponseDTO, response.getBody());
        verify(clienteService, times(1)).updateByID(id, updateDTO);
    }
    @Test
    void updatePartial(){
        UUID id = UUID.fromString("382ace01-23f4-4f68-adf4-4e6ff7f2679c");
        when(clienteService.updateByID(id, updateDTO)).thenReturn(testResponseDTO);
        ResponseEntity<ClienteReponse> response = clienteRestController.patchClienteUpdate(id, updateDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testResponseDTO, response.getBody());
        verify(clienteService, times(1)).updateByID(id, updateDTO);
    }
    @Test
    void deleteCliente() {
        UUID id = UUID.fromString("382ace01-23f4-4f68-adf4-4e6ff7f2679c");
        ResponseEntity<Void> reponse=clienteRestController.deleteCliente(id);
        assertEquals(204, reponse.getStatusCodeValue());
        verify(clienteService, times(1)).deleteById(id);
    }
    @Test
    void updateImagen() throws IOException {
        UUID id = UUID.fromString("382ace01-23f4-4f68-adf4-4e6ff7f2679c");
        String DATA_FILE = "Cesped.png";
        String WORKING_DIRECTORY = System.getProperty("user.dir");
        Path path = Paths.get(WORKING_DIRECTORY + File.separator + "imagenTest" + File.separator + DATA_FILE);
        byte[] fileContent = Files.readAllBytes(path);

        MultipartFile imagen = new MockMultipartFile("imagen", DATA_FILE, "image/png", fileContent);


        when(clienteService.updateImage(id, imagen,true)).thenReturn(testResponseDTO);

        ResponseEntity<ClienteReponse> response = clienteRestController.updateImage(id, imagen);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testResponseDTO, response.getBody());
        verify(clienteService, times(1)).updateImage(id, imagen,true);
    }
}