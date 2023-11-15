package com.madmotor.apimadmotordaw.personal.repositories;


import com.madmotor.apimadmotordaw.personal.models.Personal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PersonalRepository  extends JpaRepository<Personal, Long>, JpaSpecificationExecutor<Personal> {
    Optional<Personal> findById(Long id);

    List<Personal> findAllByDni(String dni);
    List<Personal> findAllByNombre(String nombre);
    List<Personal> findAllByApellidos(String apellidos);
}
