package com.madmotor.apimadmotordaw.clientes.repository;

import com.madmotor.apimadmotordaw.clientes.Model.Cliente;
import com.madmotor.apimadmotordaw.clientes.Repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClienteRepositoryTest {
    @Autowired
    private  ClienteRepository clienteRepository ;
    @Autowired
    private TestEntityManager entityManager;

    Cliente cliente1 = Cliente.builder().nombre("paco").apellido("paquito").direccion("federico").codigoPostal(12345).dni("12345678a").piezas(false).coches(false).build();
    Cliente cliente2 = Cliente.builder().nombre("marta").apellido("martita").direccion("garcia").codigoPostal(23451).dni("23456789a").piezas(false).coches(false).build();

    @BeforeEach
    void setUp() {
        entityManager.persist(cliente1);
        entityManager.persist(cliente2);
        entityManager.flush();
    }



    @Test
    void findByDniEqualsIgnoreCase_nonExistingDNI_returnsOptionalWithCliente(){
        String dni = "12345678B";
        Optional<Cliente> optionalCliente = clienteRepository.findByDniEqualsIgnoreCase(dni);
        assertAll("findByDniEqualsIgnoreCase_nonExistingDNI_returnsOptionalWithCliente",
                () ->assertNotNull(optionalCliente),
                () ->assertTrue(optionalCliente.isEmpty())
        );
    }
    @Test
    void saveButNotExist(){
        Cliente cliente3 = Cliente.builder()
                .id(UUID.randomUUID())
                .nombre("Juan")
                .apellido("López")
                .direccion("Avenida Central 456")
                .codigoPostal(67890)
                .dni("13579246c")
                .coches(false)
                .piezas(false)
                .build();

        Cliente clienteGuardado=clienteRepository.save(cliente3);
        var listado = clienteRepository.findAll();
        assertAll("saveButNotExist",
                () ->assertNotNull(clienteGuardado),
                () -> assertTrue(clienteRepository.findByDniEqualsIgnoreCase(clienteGuardado.getDni()).isPresent()),
                () ->assertEquals(7,listado.size())
        );
    }

    @Test
    void saveButExist(){
        Cliente cliente3 = Cliente.builder()
                .id(UUID.randomUUID())
                .nombre("Juan")
                .apellido("López")
                .direccion("Avenida Central 456")
                .codigoPostal(67890)
                .dni("13579246c")
                .coches(false)
                .piezas(false)
                .build();

        Cliente clienteGuardado=clienteRepository.save(cliente3);
        var listado = clienteRepository.findAll();
        assertAll("saveButExist",
                () ->assertNotNull(clienteGuardado),
                () -> assertTrue(clienteRepository.findByDniEqualsIgnoreCase(cliente3.getDni()).isPresent()),
                () ->assertEquals(7,listado.size())
        );
    }
}