package com.madmotor.apimadmotordaw.personal.services;


import com.madmotor.apimadmotordaw.personal.dto.PersonalCreateDTO;
import com.madmotor.apimadmotordaw.personal.dto.PersonalResponseDTO;

public interface PersonalService {
    PersonalResponseDTO findById(Long id);
    PersonalResponseDTO save(PersonalCreateDTO personalCreateDto);
    PersonalCreateDTO update(Long id, PersonalCreateDTO personalCreateDto);
    void deleteById(Long id);

}
