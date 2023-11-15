package com.madmotor.apimadmotordaw.piezas.services;


import com.madmotor.apimadmotordaw.piezas.dto.PiezaCreateDTO;
import com.madmotor.apimadmotordaw.piezas.dto.PiezaResponseDTO;
import com.madmotor.apimadmotordaw.piezas.dto.PiezaUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

public interface PiezaService {
    Page<PiezaResponseDTO> findAll(Optional<String> name, Optional<String> description, Optional<Double> price, Optional<Integer> stock, Pageable pageable);

    PiezaResponseDTO findById(UUID id);
    PiezaResponseDTO save(PiezaCreateDTO pieza);
    PiezaResponseDTO update(UUID id, PiezaUpdateDTO pieza);
    void deleteById(UUID id);
    PiezaResponseDTO updateImage(UUID id, MultipartFile image, String url);




    }
