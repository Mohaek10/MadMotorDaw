package com.madmotor.apimadmotordaw.rest.vehiculos.services;


import com.madmotor.apimadmotordaw.rest.vehiculos.dto.VehiculoCreateDto;
import com.madmotor.apimadmotordaw.rest.vehiculos.dto.VehiculoUpdateDto;
import com.madmotor.apimadmotordaw.rest.vehiculos.models.Vehiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Servicio de la entidad Vehiculo
 * Proporciona métodos para realizar operaciones CRUD y de paginación
 */

public interface VehiculoService {
    Page<Vehiculo> findAll(Optional<String> marca, Optional<String> categoria,Optional<String> modelo, Optional<Integer> minYear, Optional<Boolean> isDelete, Optional<Double>kmMax, Optional<Double> precioMax , Optional<Integer> stockMin, Pageable pageable);

    Vehiculo findById(String id);

    Vehiculo save(VehiculoCreateDto vehiculoCreateDto);

    Vehiculo update(String id, VehiculoUpdateDto vehiculoUpdateDto);

    void deleteById(String id);

    Vehiculo updateImage(String id, MultipartFile image, Boolean withUrl);


}
