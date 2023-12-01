package com.madmotor.apimadmotordaw.rest.personal.services;


import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalCreateDTO;
import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalResponseDTO;
import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PersonalService {
    Page<PersonalResponseDTO> findAll(Optional<String> dni, Optional<String> nombre, Optional<String> apellidos, Optional<String> fechaNacimiento, Optional<String> direccion, Optional<String> iban, Pageable pageable);
    PersonalResponseDTO findById(Long id);
    PersonalResponseDTO save(PersonalCreateDTO personalCreateDto);
    PersonalResponseDTO update(Long id, PersonalUpdateDTO personalCreateDto);
    void deleteById(Long id);

}
