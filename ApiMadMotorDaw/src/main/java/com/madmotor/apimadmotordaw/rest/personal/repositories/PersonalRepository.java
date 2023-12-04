package com.madmotor.apimadmotordaw.rest.personal.repositories;


import com.madmotor.apimadmotordaw.rest.personal.models.Personal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de Personal
 *
 * Creamos el repositorio extendéndolo de JPARepository y pasándole el tipo de entidad y el tipo de la clave primaria
 * Con ello ya tenemos las operaciones básicas de CRUD y Paginación
 * extiende de JpaSpecificationExecutor para poder usar el metodo findAll con filtros de busqueda en el caso de que se necesite con uso de Criteria y Specification
 *
 * @version 1.0
 * @author Miguel Vicario
 */

public interface PersonalRepository  extends JpaRepository<Personal, Long>, JpaSpecificationExecutor<Personal> {
    Optional<Personal> findByDni(String dni);

    List<Personal> findAllByDni(String dni);
    List<Personal> findAllByNombre(String nombre);
    List<Personal> findAllByApellidos(String apellidos);
    void deleteByDni(String dni);
}
