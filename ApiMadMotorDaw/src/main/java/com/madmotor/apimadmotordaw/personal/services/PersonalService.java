package com.madmotor.apimadmotordaw.personal.services;


import com.madmotor.apimadmotordaw.personal.dto.PersonalCreateDTO;
import com.madmotor.apimadmotordaw.personal.dto.PersonalResponseDTO;
import com.madmotor.apimadmotordaw.personal.dto.PersonalUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface PersonalService {
    Page<PersonalResponseDTO> findAll(Optional<String> dni, Optional<String> nombre, Optional<String> apellidos, Optional<String> fechaNacimiento, Optional<String> direccion, Optional<String> iban, Pageable pageable);
    PersonalResponseDTO findByDni(String dni);
    PersonalResponseDTO findById(Long id);
    PersonalResponseDTO save(PersonalCreateDTO personalCreateDto);
    PersonalResponseDTO update(String dni, PersonalUpdateDTO personalCreateDto);
    void deleteById(Long id);
    void deleteByDni(String dni);

}
