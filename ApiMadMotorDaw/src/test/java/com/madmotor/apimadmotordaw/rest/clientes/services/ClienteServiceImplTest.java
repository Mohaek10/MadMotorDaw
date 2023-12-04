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
import java.util.UUID;

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
    private final Cliente cliente1 = Cliente.builder().id(UUID.fromString("382ace01-23f4-4f68-adf4-4e6ff7f2679c")).nombre("paco").apellido("paquito").direccion("federico").codigoPostal(12345).dni("12345678a").imagen("perro.png").build();
    private final Cliente cliente2 = Cliente.builder().id(UUID.fromString("300ace01-23f4-4f68-adf4-4e6ff7f2679c")).nombre("marta").apellido("martita").direccion("garcia").codigoPostal(23451).dni("23456789a").imagen("cocodrilo.png").build();

    private final ClienteReponse clienteReponse1 = ClienteReponse.builder().id(UUID.fromString("382ace01-23f4-4f68-adf4-4e6ff7f2679c")).nombre("paco").apellido("paquito").direccion("federico").codigoPostal(12345).dni("12345678a").imagen("perro.png").build();
    private final ClienteReponse clienteReponse2 = ClienteReponse.builder().nombre("marta").apellido("martita").direccion("garcia").codigoPostal(23451).dni("23456789a").imagen("cocodrilo.png").build();


    @Test
    void updateByUUID_validateAndUpdate() {
        ClienteUpdateRequest clienteUpdateRequest = ClienteUpdateRequest.builder()
                .nombre("marcos")
                .apellido("mar")
                .direccion("Avenida Central 456")
                .codigoPostal(67890)
                .imagen("perro.png")
                .build();
        Cliente clientExist=cliente1;
        ClienteReponse excepCliente=clienteReponse1;

        when(clienteRepository.findById(cliente1.getId())).thenReturn(Optional.of(clientExist));
        when(clienteRepository.save(clientExist)).thenReturn(clientExist);
        when(clienteMapper.toCliente(clienteUpdateRequest,cliente1)).thenReturn(clientExist);
        when(clienteMapper.toClienteReponse(clientExist)).thenReturn(excepCliente);

        ClienteReponse clienteReponse =clienteService.updateByID(clientExist.getId(), clienteUpdateRequest);

        assertEquals(excepCliente,clienteReponse);

        verify(clienteRepository,times(1)).findById(cliente1.getId());
        verify(clienteRepository,times(1)).save(clientExist);
        verify(clienteMapper,times(1)).toCliente(clienteUpdateRequest,cliente1);
        verify(clienteMapper,times(1)).toClienteReponse(clientExist);
    }
    @Test
    void updateByDni_validateAndUpdate_ThrownDontFount() {
        UUID id=UUID.fromString("300ace01-23f4-4f68-adf4-4e6ff7f2679c");

        ClienteUpdateRequest clienteUpdateRequest = ClienteUpdateRequest.builder()
                .nombre("Nuevo ")
                .apellido("Nuevo ")
                .direccion("Nueva ")
                .codigoPostal(12345)
                .imagen("nueva_imagen.png")
                .build();

        when(clienteRepository.findById(id)).thenReturn(Optional.empty());


        assertThrows(ClienteNotFound.class, () -> clienteService.updateByID(id, clienteUpdateRequest));


        verify(clienteRepository, times(1)).findById(id);
        verify(clienteRepository, times(0)).save(any());
        verify(clienteMapper, times(0)).toCliente(any(), any());
        verify(clienteMapper, times(0)).toClienteReponse(any());
    }

    @Test
    void findByDni_returnCliente() {
        when(clienteRepository.findById(cliente1.getId())).thenReturn(Optional.of(cliente1));
        when(clienteMapper.toClienteReponse(cliente1)).thenReturn(clienteReponse1);

        ClienteReponse clienteReponse = clienteService.findByID(cliente1.getId());

        assertEquals(clienteReponse1, clienteReponse);

        verify(clienteRepository, times(1)).findById(cliente1.getId());
        verify(clienteMapper, times(1)).toClienteReponse(cliente1);
    }
    @Test
    void findByID_returnCliente_ThrownDontFount() {
        UUID id=UUID.fromString("366ace01-23f4-4f68-adf4-4e6ff7f2679c");


        when(clienteRepository.findById(id)).thenReturn(Optional.empty());


        assertThrows(ClienteNotFound.class, () -> clienteService.findByID(id));


        verify(clienteRepository, times(1)).findById(id);
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
    void deleteById_Validate() {
        UUID id =cliente1.getId();
        Cliente existingClient = cliente1;

        when(clienteRepository.findById(id)).thenReturn(Optional.of(existingClient));


        clienteService.deleteById(existingClient.getId());


        verify(clienteRepository, times(1)).delete(cliente1);
    }
    @Test
    void deleteByDni_Validate_ThrownDontFount() {
        UUID id=cliente1.getId();
        String dni = "dni_no_existente"; // DNI inválido o inexistente
//        when(clienteRepository.findByDniEqualsIgnoreCase(dni)).thenReturn(Optional.empty());
        //EL mock me indica que no es necesario lo anterior
        var res = assertThrows(ClienteNotFound.class, () -> clienteService.deleteById(id));
        assertEquals("El cliente no existe con el UUID : "+id+ " no fue encontrado", res.getMessage());



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