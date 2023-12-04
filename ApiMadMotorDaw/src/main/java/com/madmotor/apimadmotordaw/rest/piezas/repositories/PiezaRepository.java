package com.madmotor.apimadmotordaw.rest.piezas.repositories;

import com.madmotor.apimadmotordaw.rest.piezas.models.Pieza;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.UUID;

/**
 * Repositorio de Pieza
 *
 * Creamos el repositorio extendéndolo de JPARepository y pasándole el tipo de entidad y el tipo de la clave primaria
 * Con ello ya tenemos las operaciones básicas de CRUD y Paginación
 * extiende de JpaSpecificationExecutor para poder usar el metodo findAll con filtros de busqueda en el caso de que se necesite con uso de Criteria y Specification
 *
 * @version 1.0
 * @Author Rubén Fernández
 */


@Repository
public interface PiezaRepository extends JpaRepository<Pieza, UUID>, JpaSpecificationExecutor<Pieza> {


}
