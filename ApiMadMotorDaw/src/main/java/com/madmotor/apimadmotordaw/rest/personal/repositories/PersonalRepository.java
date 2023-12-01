package com.madmotor.apimadmotordaw.rest.personal.repositories;


import com.madmotor.apimadmotordaw.rest.personal.models.Personal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PersonalRepository  extends JpaRepository<Personal, Long>, JpaSpecificationExecutor<Personal> {
    Optional<Personal> findByDni(String dni);

    List<Personal> findAllByDni(String dni);
    List<Personal> findAllByNombre(String nombre);
    List<Personal> findAllByApellidos(String apellidos);
    void deleteByDni(String dni);
}
