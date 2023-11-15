package com.madmotor.apimadmotordaw.personal.services;


import com.madmotor.apimadmotordaw.personal.dto.PersonalCreateDTO;
import com.madmotor.apimadmotordaw.personal.dto.PersonalResponseDTO;
import com.madmotor.apimadmotordaw.personal.dto.PersonalUpdateDTO;
import com.madmotor.apimadmotordaw.personal.exceptions.PersonalNotFound;
import com.madmotor.apimadmotordaw.personal.mappers.PersonalMapper;
import com.madmotor.apimadmotordaw.personal.models.Personal;
import com.madmotor.apimadmotordaw.personal.repositories.PersonalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "personal")
@Slf4j

public class PersonalServiceImpl implements PersonalService {
    private final PersonalRepository personalRepository;
    private final PersonalMapper personalMapper;

    @Autowired


    public PersonalServiceImpl(PersonalRepository personalRepository, PersonalMapper personalMapper) {
        this.personalRepository = personalRepository;
        this.personalMapper = personalMapper;
    }


    @Override
    public PersonalResponseDTO findByDni(String dni) {
        log.info("Buscando trabajador@ por dni: " + dni);
        return personalMapper.toPersonalResponseDto(personalRepository.findByDni(dni).orElseThrow(() -> new PersonalNotFound("Trabajador@ no encontrado")));
    }


    @Override
    public PersonalResponseDTO save(PersonalCreateDTO personalCreateDto) {
        log.info("Creando trabajador@: " + personalCreateDto);
        return personalMapper.toPersonalResponseDto(personalRepository.save(personalMapper.toPersonal(personalCreateDto)));
    }

    @Override
    public PersonalResponseDTO update(String dni, PersonalUpdateDTO personalUpdateDto) {
        log.info("Actualizando trabajador@ con dni: " + dni);
        var personalActualizar = personalRepository.findByDni(dni).orElseThrow(() -> new PersonalNotFound("Trabajador@ no encontrado"));
        personalActualizar.setDireccion(personalUpdateDto.getDireccion());
        personalActualizar.setIban(personalUpdateDto.getIban());

        var personalActualizado = personalRepository.save(personalActualizar);
        return personalMapper.toPersonalResponseDto(personalActualizado);
    }


    @Override
    public void deleteByDni(String dni) {
        log.info("Borrando trabajador@ con dni: " + dni);
        personalRepository.findByDni(dni).orElseThrow(() -> new PersonalNotFound("Trabajador@ con dni: " + dni + " no encontrado"));
        personalRepository.deleteByDni(dni);
    }

    @Override
    public Page<PersonalResponseDTO> findAll(Optional<String> dni, Optional<String> nombre, Optional<String> apellidos, Optional<String> fechaNacimiento, Optional<String> direccion, Optional<String> iban, Pageable pageable) {
        Specification<Personal> specDni = (root, query, criteriaBuilder) ->
                dni.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("dni")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Personal> specNombre = (root, query, criteriaBuilder) ->
                nombre.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Personal> specApellidos = (root, query, criteriaBuilder) ->
                apellidos.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("apellidos")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Personal> specFechaNacimiento = (root, query, criteriaBuilder) ->
                fechaNacimiento.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("fechaNacimiento")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Personal> specDireccion = (root, query, criteriaBuilder) ->
                direccion.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("direccion")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Personal> specIban = (root, query, criteriaBuilder) ->
                iban.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("iban")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Personal> criterio = Specification.where(specDni)
                .and(specNombre)
                .and(specApellidos)
                .and(specFechaNacimiento)
                .and(specDireccion)
                .and(specIban);


        return personalRepository.findAll(criterio, pageable).map(personalMapper::toPersonalResponseDto);

    }
    @Override
    public PersonalResponseDTO findById(Long id) {
        log.info("Buscando trabajador@ por id: " + id);
        return personalRepository.findById(id).map(personalMapper::toPersonalResponseDto).orElseThrow(() -> new PersonalNotFound("Trabajador@ " + id + " no encontrado"));
    }

    @Override
    public void deleteById(Long id) {
        log.info("Borrando trabajador@ con id: " + id);
        personalRepository.findById(id).orElseThrow(() -> new PersonalNotFound("Trabajador@ con id: " + id + " no encontrado"));
        personalRepository.deleteById(id);
    }
}
