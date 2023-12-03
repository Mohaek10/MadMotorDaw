package com.madmotor.apimadmotordaw.rest.personal.services;


import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalCreateDTO;
import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalResponseDTO;
import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Interfaz PersonalService
 *
 * En esta interfaz se definen los métodos que se utilizarán en el PersonalServiceImpl
 * @version 1.0
 * @author Miguel Vicario
 */

public interface PersonalService {
    Page<PersonalResponseDTO> findAll(Optional<String> dni, Optional<String> nombre, Optional<String> apellidos, Optional<String> fechaNacimiento, Optional<String> direccion, Optional<String> iban, Optional<Double> sueldo, Optional<String> telefono, Pageable pageable);
    PersonalResponseDTO findById(Long id);
    PersonalResponseDTO save(PersonalCreateDTO personalCreateDto);
    PersonalResponseDTO update(Long id, PersonalUpdateDTO personalCreateDto);
    void deleteById(Long id);

}
