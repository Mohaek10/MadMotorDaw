package com.madmotor.apimadmotordaw.rest.vehiculos.repositories;


import com.madmotor.apimadmotordaw.rest.vehiculos.models.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio de la entidad Vehiculo
 * Utiliza JPARepository y JpaSpecificationExecutor
 * JPARepository: Proporciona métodos para realizar operaciones CRUD y de paginación
 * JpaSpecificationExecutor: Proporciona métodos para realizar consultas dinámicas
 */
@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, UUID>, JpaSpecificationExecutor<Vehiculo> {


    Optional<Vehiculo> findById(UUID id);

    List<Vehiculo> findByIsDeleted(Boolean isDeleted);
    List<Vehiculo> findAllByMarca(String marca);
    List<Vehiculo> findAllByKm(Double km);



}
