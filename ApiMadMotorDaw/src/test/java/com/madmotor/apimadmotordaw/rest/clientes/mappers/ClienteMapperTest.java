package com.madmotor.apimadmotordaw.rest.clientes.mappers;

import com.madmotor.apimadmotordaw.rest.clientes.Model.Cliente;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteCreateRequest;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteUpdateRequest;
import com.madmotor.apimadmotordaw.rest.clientes.mappers.ClienteMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteMapperTest {
    private ClienteMapper clienteMapper = new ClienteMapper();

    @Test
    void testToClienteCreate() {
        ClienteCreateRequest clienteCreateRequest =ClienteCreateRequest.builder()
                .nombre("Juan")
                .apellido("Perez")
                .direccion("Calle 123")
                .codigoPostal(12345)
                .dni("12345678A")
                .imagen("paqui.png")
                .build();
        var cambio=clienteMapper.toCliente(clienteCreateRequest);

        assertAll(
                () -> assertEquals(clienteCreateRequest.getNombre(),cambio.getNombre()),
                () -> assertEquals(clienteCreateRequest.getApellido(),cambio.getApellido()),
                () -> assertEquals(clienteCreateRequest.getDireccion(),cambio.getDireccion()),
                () -> assertEquals(clienteCreateRequest.getCodigoPostal(),cambio.getCodigoPostal()),
                () -> assertEquals(clienteCreateRequest.getDni(),cambio.getDni()),
                () -> assertEquals(clienteCreateRequest.getImagen(),cambio.getImagen())
        );
    }
    @Test
    void testoToClienteUpdate() {
        String dni="12345678A";
        ClienteUpdateRequest clienteUpdateRequest =ClienteUpdateRequest.builder()
               .nombre("EL cambiazo")
               .apellido("Perez")
               .direccion("Calle 123")
               .codigoPostal(12345)
               .imagen("paqui.png")
               .build();
        Cliente cliente=Cliente.builder()
                .nombre("EL cambiazo")
                .apellido("Perez")
                .direccion("Calle 123")
                .codigoPostal(12345)
                .dni(dni)
                .imagen("").build();

        var resultado=clienteMapper.toCliente(clienteUpdateRequest,cliente);
        assertAll(
                () -> assertEquals(dni,resultado.getDni()),
                () -> assertEquals(clienteUpdateRequest.getNombre(),resultado.getNombre()),
                () -> assertEquals(clienteUpdateRequest.getApellido(),resultado.getApellido()),
                () -> assertEquals(clienteUpdateRequest.getDireccion(),resultado.getDireccion()),
                () -> assertEquals(clienteUpdateRequest.getCodigoPostal(),resultado.getCodigoPostal())
        );
    }

    @Test
    void toClienteReponse(){
        Cliente cliente=Cliente.builder()
              .nombre("EL cambiazo")
              .apellido("Perez")
              .direccion("Calle 123")
              .codigoPostal(12345)
                .dni("12345678A")
              .imagen("paqui.png")
              .build();
        var resultado=clienteMapper.toClienteReponse(cliente);
        assertAll(
                () -> assertEquals(cliente.getNombre(),resultado.getNombre()),
                () -> assertEquals(cliente.getApellido(),resultado.getApellido()),
                () -> assertEquals(cliente.getDireccion(),resultado.getDireccion()),
                () -> assertEquals(cliente.getCodigoPostal(),resultado.getCodigoPostal())
        );
    }
}