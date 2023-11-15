package com.madmotor.apimadmotordaw.piezas.services;
import com.madmotor.apimadmotordaw.piezas.dto.PiezaCreateDTO;
import com.madmotor.apimadmotordaw.piezas.dto.PiezaResponseDTO;
import com.madmotor.apimadmotordaw.piezas.dto.PiezaUpdateDTO;
import com.madmotor.apimadmotordaw.piezas.exceptions.PiezaNotFound;
import com.madmotor.apimadmotordaw.piezas.mappers.PiezaMapper;
import com.madmotor.apimadmotordaw.piezas.models.Pieza;
import com.madmotor.apimadmotordaw.piezas.repositories.PiezaRepository;
import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;
@Service
@CacheConfig(cacheNames = "piezas")
@Slf4j
public class PiezaServiceImpl implements PiezaService {
    private final PiezaRepository piezaRepository;
    private final PiezaMapper piezaMapper;

    @Autowired
    public PiezaServiceImpl(PiezaRepository piezaRepository, PiezaMapper piezaMapper) {
        this.piezaRepository = piezaRepository;
        this.piezaMapper = piezaMapper;
    }


    @Override
    public Page<PiezaResponseDTO> findAll(Optional<String> name, Optional<String> description, Optional<Double> price, Optional<Integer> stock, Pageable pageable) {
        Specification<Pieza> specNombrePieza = (root, query, criteriaBuilder) ->
                name.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Pieza> specDescripcionPieza = (root, query, criteriaBuilder) ->
                description.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Pieza> specMaxPrecioPieza = (root, query, criteriaBuilder) ->
                price.map(m -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Pieza> specStockPieza = (root, query, criteriaBuilder) ->
                price.map(m -> criteriaBuilder.greaterThanOrEqualTo(root.get("stock"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Pieza> criterio = Specification.where(specStockPieza)
                .and(specNombrePieza)
                .and(specMaxPrecioPieza)
                .and(specDescripcionPieza);

        return piezaRepository.findAll(criterio, pageable).map(piezaMapper::toPiezaResponse);




    }

    @Override
    @Cacheable(key = "#id")
    public PiezaResponseDTO findById(UUID id) {
        log.info("Buscando pieza por id: " + id);
        return piezaMapper.toPiezaResponse(piezaRepository.findById(id).orElseThrow(() -> new PiezaNotFound(id)));
    }

    @CachePut(key = "#result.id")
    @Override
    public PiezaResponseDTO save(PiezaCreateDTO pieza) {
        log.info("Guardando pieza: " + pieza);
        var piezaToSave = piezaRepository.save(piezaMapper.toPieza(pieza));
        return piezaMapper.toPiezaResponse(piezaToSave);
    }
    @CachePut(key = "#result.id")
    @Override
    public PiezaResponseDTO update(UUID id, PiezaUpdateDTO pieza) {
        log.info("Actualizando pieza: " + pieza);
        var piezaToUpdate = piezaRepository.findById(id).orElseThrow(() -> new PiezaNotFound(id));
        var piezaUpdated = piezaRepository.save(piezaMapper.toPieza(pieza, piezaToUpdate));
        return piezaMapper.toPiezaResponse(piezaUpdated);

    }

    @Override
    public void deleteById(UUID id) {
            log.info("Borrando pieza con id: " + id);
            piezaRepository.findById(id).orElseThrow(() -> new PiezaNotFound(id));
        piezaRepository.deleteById(id);

    }


}
