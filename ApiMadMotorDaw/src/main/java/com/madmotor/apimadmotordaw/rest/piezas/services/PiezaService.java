package com.madmotor.apimadmotordaw.rest.piezas.services;


import com.madmotor.apimadmotordaw.rest.piezas.dto.PiezaCreateDTO;
import com.madmotor.apimadmotordaw.rest.piezas.dto.PiezaResponseDTO;
import com.madmotor.apimadmotordaw.rest.piezas.dto.PiezaUpdateDTO;
import com.madmotor.apimadmotordaw.rest.piezas.models.Pieza;
import com.madmotor.apimadmotordaw.rest.vehiculos.models.Vehiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

/**
 * Interfaz PiezaService
 *
 * En esta interfaz se definen los métodos que se utilizarán en el PiezaServiceImpl
 * @version 1.0
 * @Author Rubén Fernández
 */

public interface PiezaService {
    Page<PiezaResponseDTO> findAll(Optional<String> name, Optional<String> description, Optional<Double> price, Optional<Integer> stock, Pageable pageable);


    PiezaResponseDTO findById(UUID id);
    PiezaResponseDTO save(PiezaCreateDTO pieza);
    PiezaResponseDTO update(UUID id, PiezaUpdateDTO pieza);
    void deleteById(UUID id);
    Pieza updateImage(String id, MultipartFile image, Boolean withUrl);






}
