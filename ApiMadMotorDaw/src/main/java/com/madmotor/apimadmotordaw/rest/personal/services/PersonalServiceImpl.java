package com.madmotor.apimadmotordaw.rest.personal.services;


import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalCreateDTO;
import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalResponseDTO;
import com.madmotor.apimadmotordaw.rest.personal.dto.PersonalUpdateDTO;
import com.madmotor.apimadmotordaw.rest.personal.exceptions.PersonalNotFound;
import com.madmotor.apimadmotordaw.rest.personal.mappers.PersonalMapper;
import com.madmotor.apimadmotordaw.rest.personal.models.Personal;
import com.madmotor.apimadmotordaw.rest.personal.repositories.PersonalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


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
    @CachePut
    public PersonalResponseDTO save(PersonalCreateDTO personalCreateDto) {
        log.info("Creando trabajador@: " + personalCreateDto);
        return personalMapper.toPersonalResponseDto(personalRepository.save(personalMapper.toPersonal(personalCreateDto)));
    }

    @Override
    @CachePut
    public PersonalResponseDTO update(Long id, PersonalUpdateDTO personalUpdateDto) {
        log.info("Actualizando trabajador@ con id: " + id);
        var personalActualizar = personalRepository.findById(id).orElseThrow(() -> new PersonalNotFound("Trabajador@ no encontrado"));
        personalActualizar.setDireccion(personalUpdateDto.getDireccion());
        personalActualizar.setIban(personalUpdateDto.getIban());

        var personalActualizado = personalRepository.save(personalActualizar);
        return personalMapper.toPersonalResponseDto(personalActualizado);
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

        Specification<Personal> specIban = (root, query, criteriaBuilder) ->
                iban.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("iban")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Personal> criterio = Specification.where(specDni)
                .and(specNombre)
                .and(specApellidos)
                .and(specIban);


        return personalRepository.findAll(criterio, pageable).map(personalMapper::toPersonalResponseDto);

    }
    @Override
    public PersonalResponseDTO findById(Long id) {
        log.info("Buscando trabajador@ por id: " + id);
        return personalRepository.findById(id).map(personalMapper::toPersonalResponseDto).orElseThrow(() -> new PersonalNotFound("Trabajador@ " + id + " no encontrado"));
    }

    @Override
    @CacheEvict
    public void deleteById(Long id) {
        log.info("Borrando trabajador@ con id: " + id);
        personalRepository.findById(id).orElseThrow(() -> new PersonalNotFound("Trabajador@ con id: " + id + " no encontrado"));
        personalRepository.deleteById(id);
    }
}


