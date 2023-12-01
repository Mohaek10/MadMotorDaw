package com.madmotor.apimadmotordaw.rest.clientes.services;

import com.madmotor.apimadmotordaw.rest.clientes.Model.Cliente;
import com.madmotor.apimadmotordaw.rest.clientes.Repository.ClienteRepository;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteCreateRequest;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteReponse;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteUpdateRequest;
import com.madmotor.apimadmotordaw.rest.clientes.exceptions.ClienteFailSave;
import com.madmotor.apimadmotordaw.rest.clientes.exceptions.ClienteNotFound;
import com.madmotor.apimadmotordaw.rest.clientes.mappers.ClienteMapper;
import com.madmotor.apimadmotordaw.rest.clientes.services.ClienteServiceImpl;
import com.madmotor.apimadmotordaw.rest.storage.service.StorageService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private StorageService storageService;
    @Mock
    private ClienteMapper clienteMapper;
    @InjectMocks
    private ClienteServiceImpl clienteService;
    private final Cliente cliente1 = Cliente.builder().nombre("paco").apellido("paquito").direccion("federico").codigoPostal(12345).dni("12345678a").piezas(false).coches(false).imagen("perro.png").build();
    private final Cliente cliente2 = Cliente.builder().nombre("marta").apellido("martita").direccion("garcia").codigoPostal(23451).dni("23456789a").piezas(false).coches(false).imagen("cocodrilo.png").build();

    private final ClienteReponse clienteReponse1 = ClienteReponse.builder().nombre("paco").apellido("paquito").direccion("federico").codigoPostal(12345).dni("12345678a").piezas(false).coches(false).imagen("perro.png").build();
    private final ClienteReponse clienteReponse2 = ClienteReponse.builder().nombre("marta").apellido("martita").direccion("garcia").codigoPostal(23451).dni("23456789a").piezas(false).coches(false).imagen("cocodrilo.png").build();


    @Test
    void updateByDni_validateAndUpdate() {
        ClienteUpdateRequest clienteUpdateRequest = ClienteUpdateRequest.builder()
                .nombre("marcos")
                .apellido("mar")
                .direccion("Avenida Central 456")
                .codigoPostal(67890)
                .piezas(true)
                .coches(true)
                .imagen("perro.png")
                .build();
        Cliente clientExist=cliente1;
        ClienteReponse excepCliente=clienteReponse1;

        when(clienteRepository.findByDniEqualsIgnoreCase(cliente1.getDni())).thenReturn(Optional.of(clientExist));
        when(clienteRepository.save(clientExist)).thenReturn(clientExist);
        when(clienteMapper.toCliente(clienteUpdateRequest,cliente1)).thenReturn(clientExist);
        when(clienteMapper.toClienteReponse(clientExist)).thenReturn(excepCliente);

        ClienteReponse clienteReponse =clienteService.updateByDni(cliente1.getDni(), clienteUpdateRequest);

        assertEquals(excepCliente,clienteReponse);

        verify(clienteRepository,times(1)).findByDniEqualsIgnoreCase(cliente1.getDni());
        verify(clienteRepository,times(1)).save(clientExist);
        verify(clienteMapper,times(1)).toCliente(clienteUpdateRequest,cliente1);
        verify(clienteMapper,times(1)).toClienteReponse(clientExist);
    }
    @Test
    void updateByDni_validateAndUpdate_ThrownDontFount() {
        String dni = "dni_no_existente"; // DNI erroneo
        ClienteUpdateRequest clienteUpdateRequest = ClienteUpdateRequest.builder()
                .nombre("Nuevo ")
                .apellido("Nuevo ")
                .direccion("Nueva ")
                .codigoPostal(12345)
                .piezas(true)
                .coches(true)
                .imagen("nueva_imagen.png")
                .build();

        when(clienteRepository.findByDniEqualsIgnoreCase(dni)).thenReturn(Optional.empty());


        assertThrows(ClienteNotFound.class, () -> clienteService.updateByDni(dni, clienteUpdateRequest));


        verify(clienteRepository, times(1)).findByDniEqualsIgnoreCase(dni);
        verify(clienteRepository, times(0)).save(any());
        verify(clienteMapper, times(0)).toCliente(any(), any());
        verify(clienteMapper, times(0)).toClienteReponse(any());
    }

    @Test
    void findByDni_returnCliente() {
        when(clienteRepository.findByDniEqualsIgnoreCase(cliente1.getDni())).thenReturn(Optional.of(cliente1));
        when(clienteMapper.toClienteReponse(cliente1)).thenReturn(clienteReponse1);

        ClienteReponse clienteReponse = clienteService.findByDni(cliente1.getDni());

        assertEquals(clienteReponse1, clienteReponse);

        verify(clienteRepository, times(1)).findByDniEqualsIgnoreCase(cliente1.getDni());
        verify(clienteMapper, times(1)).toClienteReponse(cliente1);
    }
    @Test
    void findByDni_returnCliente_ThrownDontFount() {
        String dni = "dni_no_existente"; // DNI erroneo

        when(clienteRepository.findByDniEqualsIgnoreCase(dni)).thenReturn(Optional.empty());


        assertThrows(ClienteNotFound.class, () -> clienteService.findByDni(dni));


        verify(clienteRepository, times(1)).findByDniEqualsIgnoreCase(dni);
        verify(clienteMapper, times(0)).toClienteReponse(any());
    }

    @Test
    void findAll() {

        List<Cliente> expectedClientes = Arrays.asList(cliente1, cliente2);
        List<ClienteReponse> expectedResponseClientes = Arrays.asList(clienteReponse1, clienteReponse2);


        Pageable pageable = PageRequest.of(0, 10, Sort.by("dni").ascending());
        Page<Cliente> expectedPage = new PageImpl<>(expectedClientes);
        when(clienteRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);


        when(clienteMapper.toClienteReponse(cliente1)).thenReturn(clienteReponse1);
        when(clienteMapper.toClienteReponse(cliente2)).thenReturn(clienteReponse2);


        Page<ClienteReponse> actualPage = clienteService.findAll(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                  pageable);


        assertAll("findAll",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );

        verify(clienteRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(clienteMapper, times(2)).toClienteReponse((any(Cliente.class)));
    }



    @Test
    void deleteByDni_Validate() {

        String dni =cliente1.getDni();
        Cliente existingClient = cliente1;

        when(clienteRepository.findByDniEqualsIgnoreCase(dni)).thenReturn(Optional.of(existingClient));


        clienteService.deleteByDni(dni);


        verify(clienteRepository, times(1)).delete(existingClient);
    }
    @Test
    void deleteByDni_Validate_ThrownDontFount() {

        String dni = "dni_no_existente"; // DNI inválido o inexistente
        when(clienteRepository.findByDniEqualsIgnoreCase(dni)).thenReturn(Optional.empty());


        var res = assertThrows(ClienteNotFound.class, () -> clienteService.deleteByDni(dni));
        assertEquals("El cliente no existe con el dni numero : "+dni+" no fue encontrado", res.getMessage());



        verify(clienteRepository, times(0)).delete((Cliente) any());
    }

    @Test
    void savePost_correct() {
        ClienteCreateRequest clienteCreateRequest = ClienteCreateRequest.builder()
                .nombre("marcos")
                .apellido("marta")
                .direccion("Avenida Central 456")
                .codigoPostal(67890)
                .dni("12345678a")
                .piezas(false)
                .coches(false)
                .build();
        Cliente cliente = new Cliente();
        when(clienteMapper.toCliente(clienteCreateRequest)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clienteMapper.toClienteReponse(cliente)).thenReturn(clienteReponse1);

        ClienteReponse clienteReponse = clienteService.savePost(clienteCreateRequest);

        assertEquals(clienteReponse, clienteReponse1);
        verify(clienteMapper, times(1)).toCliente(clienteCreateRequest);
        verify(clienteRepository, times(1)).save(cliente);
    }
    @Test
    void savePost_existingCliente_throwsClienteFailSave() {
        String existingDni = "12345678A";
        ClienteCreateRequest clienteCreateRequest = ClienteCreateRequest.builder()
                .nombre("paco")
                .dni(existingDni)
                .build();
        when(clienteRepository.findByDniEqualsIgnoreCase(existingDni)).thenReturn(Optional.of(new Cliente()));

        assertThrows(ClienteFailSave.class, () -> clienteService.savePost(clienteCreateRequest));
        verify(clienteRepository, times(1)).findByDniEqualsIgnoreCase(existingDni);
        verifyNoMoreInteractions(clienteRepository);
        verifyNoInteractions(clienteMapper);
    }


}