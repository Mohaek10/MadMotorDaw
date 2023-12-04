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

/**
 * Clase PersonalServiceImpl
 *
 * En esta clase se definen los métodos de la interfaz PersonalService
 * @version 1.0
 * @author Miguel Vicario
 */

@Service
@CacheConfig(cacheNames = "personal")
@Slf4j

public class PersonalServiceImpl implements PersonalService {
    // Inyectamos el repositorio y el mapper
    private final PersonalRepository personalRepository;
    private final PersonalMapper personalMapper;

    @Autowired


    public PersonalServiceImpl(PersonalRepository personalRepository, PersonalMapper personalMapper) {
        this.personalRepository = personalRepository;
        this.personalMapper = personalMapper;
    }

    /**
     * Método para guardar un personal creado
     *
     * @param personalCreateDto
     * @return PersonalResponseDTO
     * @throws com.madmotor.apimadmotordaw.rest.personal.exceptions.PersonalFailSave Excepción que se lanza cuando no puedes guardar un personal
     */

    @Override
    @CachePut
    public PersonalResponseDTO save(PersonalCreateDTO personalCreateDto) {
        log.info("Creando trabajador@: " + personalCreateDto);
        return personalMapper.toPersonalResponseDto(personalRepository.save(personalMapper.toPersonal(personalCreateDto)));
    }

    /**
     * Método para actualizar un personal
     *
     * @param id
     * @param personalUpdateDto
     * @return PersonalResponseDTO
     * @throws com.madmotor.apimadmotordaw.rest.personal.exceptions.PersonalNotFound Excepción que se lanza cuando no encuentras un personal con el id proporcionado
     */
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

    /**
     * Método para mostrar todos los parámetros del personal
     *
     * @param dni
     * @param nombre
     * @param apellidos
     * @param fechaNacimiento
     * @param direccion
     * @param iban
     * @param sueldo
     * @param telefono
     * @param pageable
     * @return PersonalResponseDTO
     */
    @Override
    public Page<PersonalResponseDTO> findAll(Optional<String> dni, Optional<String> nombre, Optional<String> apellidos, Optional<String> fechaNacimiento, Optional<String> direccion, Optional<String> iban, Optional<Double> sueldo, Optional<String> telefono, Pageable pageable) {
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

        Specification<Personal> specSueldo = (root, query, criteriaBuilder) ->
                sueldo.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("sueldo")), "%" + m.toString() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Personal> specFechaNacimiento = (root, query, criteriaBuilder) ->
                fechaNacimiento.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("fechaNacimiento")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Personal> specDireccion = (root, query, criteriaBuilder) ->
                direccion.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("direccion")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Personal> specTelefono = (root, query, criteriaBuilder) ->
                telefono.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("telefono")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Personal> criterio = Specification.where(specDni)
                .and(specNombre)
                .and(specApellidos)
                .and(specIban)
                .and(specFechaNacimiento)
                .and(specDireccion)
                .and(specSueldo)
                .and(specTelefono);


        return personalRepository.findAll(criterio, pageable).map(personalMapper::toPersonalResponseDto);

    }

    /**
     * Método para buscar un personal por su id
     *
     * @param id
     * @return PersonalResponseDTO
     * @throws com.madmotor.apimadmotordaw.rest.personal.exceptions.PersonalNotFound Excepción que se lanza cuando no encuentras un personal con el id proporcionado
     */
    @Override
    public PersonalResponseDTO findById(Long id) {
        log.info("Buscando trabajador@ por id: " + id);
        return personalRepository.findById(id).map(personalMapper::toPersonalResponseDto).orElseThrow(() -> new PersonalNotFound("Trabajador@ " + id + " no encontrado"));
    }

    /**
     * Método para borrar un personal por su id
     *
     * @param id
     * @throws com.madmotor.apimadmotordaw.rest.personal.exceptions.PersonalNotFound Excepción que se lanza cuando no encuentras un personal con el id proporcionado
     */
    @Override
    @CacheEvict
    public void deleteById(Long id) {
        log.info("Borrando trabajador@ con id: " + id);
        personalRepository.findById(id).orElseThrow(() -> new PersonalNotFound("Trabajador@ " + id + " no encontrado"));
        personalRepository.deleteById(id);
    }
}


