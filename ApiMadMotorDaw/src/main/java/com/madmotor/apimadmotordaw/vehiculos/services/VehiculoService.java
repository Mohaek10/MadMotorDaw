package com.madmotor.apimadmotordaw.vehiculos.services;


import com.madmotor.apimadmotordaw.vehiculos.dto.VehiculoCreateDto;
import com.madmotor.apimadmotordaw.vehiculos.dto.VehiculoUpdateDto;
import com.madmotor.apimadmotordaw.vehiculos.models.Vehiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface VehiculoService {
    Page<Vehiculo> findAll(Optional<String> marca, Optional<String> categoria, Optional<Integer> minYear, Optional<Boolean> isDelete, Optional<Double>kmMax, Optional<Double> precioMax , Optional<Double> stockMin, Pageable pageable);

    Vehiculo findById(String id);

    Vehiculo save(VehiculoCreateDto vehiculoCreateDto);

    Vehiculo update(String id, VehiculoUpdateDto vehiculoUpdateDto);

    void deleteById(String id);


}
