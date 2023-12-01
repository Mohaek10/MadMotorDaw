package com.madmotor.apimadmotordaw.rest.piezas.repositories;

import com.madmotor.apimadmotordaw.rest.piezas.models.Pieza;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.UUID;


@Repository
public interface PiezaRepository extends JpaRepository<Pieza, UUID>, JpaSpecificationExecutor<Pieza> {


}
