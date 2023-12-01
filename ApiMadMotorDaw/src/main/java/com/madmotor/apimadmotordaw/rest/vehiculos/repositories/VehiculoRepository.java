package com.madmotor.apimadmotordaw.rest.vehiculos.repositories;


import com.madmotor.apimadmotordaw.rest.vehiculos.models.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, UUID>, JpaSpecificationExecutor<Vehiculo> {


    Optional<Vehiculo> findById(UUID id);

    List<Vehiculo> findByIsDeleted(Boolean isDeleted);
    List<Vehiculo> findAllByMarca(String marca);
    List<Vehiculo> findAllByKm(Double km);



}
