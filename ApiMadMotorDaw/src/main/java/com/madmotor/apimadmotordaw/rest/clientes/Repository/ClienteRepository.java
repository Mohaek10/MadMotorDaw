package com.madmotor.apimadmotordaw.rest.clientes.Repository;

import com.madmotor.apimadmotordaw.rest.clientes.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio de Cliente
 *
 * Creamos el repositorio extendéndolo de JPARepository y pasándole el tipo de entidad y el tipo de la clave primaria
 * Con ello ya tenemos las operaciones básicas de CRUD y Paginación
 * extiende de JpaSpecificationExecutor para poder usar el metodo findAll con filtros de busqueda en el caso de que se necesite con uso de Criteria y Specification
 *
 * @version 1.0
 * @author Joe Brandon
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> , JpaSpecificationExecutor<Cliente> {


    //Metodo para guardar un cliente pasandolo un objeto de tipo Cliente
    Cliente save(Cliente cliente);
    //Metodo para encontrar un cliente mediante su ID

    Optional<Cliente>findByDniEqualsIgnoreCase(String dni);

}
