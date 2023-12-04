package com.madmotor.apimadmotordaw.rest.piezas.services;
import com.madmotor.apimadmotordaw.rest.piezas.dto.PiezaCreateDTO;
import com.madmotor.apimadmotordaw.rest.piezas.dto.PiezaResponseDTO;
import com.madmotor.apimadmotordaw.rest.piezas.dto.PiezaUpdateDTO;
import com.madmotor.apimadmotordaw.rest.piezas.exceptions.PiezaNotFound;
import com.madmotor.apimadmotordaw.rest.piezas.mappers.PiezaMapper;
import com.madmotor.apimadmotordaw.rest.piezas.models.Pieza;
import com.madmotor.apimadmotordaw.rest.piezas.repositories.PiezaRepository;
import com.madmotor.apimadmotordaw.rest.storage.service.StorageService;
import com.madmotor.apimadmotordaw.rest.vehiculos.exceptions.VehiculoNotFound;
import com.madmotor.apimadmotordaw.rest.vehiculos.models.Vehiculo;
import com.madmotor.apimadmotordaw.websockets.notificaciones.models.Notificacion;
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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Clase PiezaServiceImpl
 *
 * En esta clase se definen los métodos de la interfaz PiezaService
 * @version 1.0
 * @Author Rubén Fernández
 */

@Service
@CacheConfig(cacheNames = "piezas")
@Slf4j
public class PiezaServiceImpl implements PiezaService {
    // Inyectamos el repositorio y el mapper
    private final PiezaRepository piezaRepository;
    private final PiezaMapper piezaMapper;
    private final StorageService storageService;


    @Autowired
    public PiezaServiceImpl(PiezaRepository piezaRepository, PiezaMapper piezaMapper, StorageService storageService) {
        this.piezaRepository = piezaRepository;
        this.piezaMapper = piezaMapper;
        this.storageService = storageService;
    }

    /**
     * Método para buscar todas las piezas
     *
     * @param name
     * @param description
     * @param price
     * @param stock
     * @param pageable
     * @return PiezaResponseDTO
     */


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

    /**
     * Método para buscar una pieza por su id
     *
     * @param id
     * @return PiezaResponseDTO
     * @throws PiezaNotFound (404)
     */

    @Override
    @Cacheable(key = "#id")
    public PiezaResponseDTO findById(UUID id) {
        log.info("Buscando pieza por id: " + id);
        return piezaMapper.toPiezaResponse(piezaRepository.findById(id).orElseThrow(() -> new PiezaNotFound(id)));
    }

    /**
     * Método para guardar una pieza
     *
     * @param pieza
     * @return PiezaResponseDTO
     * @throws PiezaNotFound (404)
     */

    @CachePut(key = "#result.id")
    @Override
    public PiezaResponseDTO save(PiezaCreateDTO pieza) {
        log.info("Guardando pieza: " + pieza);
        var piezaToSave = piezaRepository.save(piezaMapper.toPieza(pieza));
        return piezaMapper.toPiezaResponse(piezaToSave);
    }

    /**
     * Método para actualizar una pieza
     *
     * @param id
     * @param pieza
     * @return PiezaResponseDTO
     * @throws PiezaNotFound (404)
     */

    @CachePut(key = "#result.id")
    @Override
    public PiezaResponseDTO update(UUID id, PiezaUpdateDTO pieza) {
        log.info("Actualizando pieza: " + pieza);
        var piezaToUpdate = piezaRepository.findById(id).orElseThrow(() -> new PiezaNotFound(id));
        var piezaUpdated = piezaRepository.save(piezaMapper.toPieza(pieza, piezaToUpdate));
        return piezaMapper.toPiezaResponse(piezaUpdated);

    }

    /**
     * Método para actualizar la imagen de una pieza
     *
     * @param id
     * @param image
     * @param withUrl
     * @return Pieza
     * @throws PiezaNotFound (404)
     */

    public Pieza updateImage(String id, MultipartFile image, Boolean withUrl) {
        log.info("Actualizando imagen del vehiculo con id: " + id);
        var piezaActual = piezaRepository.findById(UUID.fromString(id)).orElseThrow(() -> new PiezaNotFound(UUID.fromString(id)));
        if (piezaActual.getImage() != null && !piezaActual.getImage().equals(Vehiculo.IMAGE_DEFAULT)) {
            storageService.delete(piezaActual.getImage());
        }
        var imagen = storageService.store(image);

        String imageUrl = !withUrl ? imagen : storageService.getUrl(imagen);

        var piezaActualizada = Pieza.builder()
                .id(piezaActual.getId())
                .name(piezaActual.getName())
                .description(piezaActual.getDescription())
                .price(piezaActual.getPrice())
                .stock(piezaActual.getStock())
                .image(imageUrl)
                .createdAt(piezaActual.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        var piezaGuardada = piezaRepository.save(piezaActualizada);
        return piezaGuardada;
    }

    /**
     * Método para borrar una pieza
     *
     * @param id
     * @throws PiezaNotFound (404)
     */
    
    @Override
    public void deleteById(UUID id) {
            log.info("Borrando pieza con id: " + id);
            piezaRepository.findById(id).orElseThrow(() -> new PiezaNotFound(id));
        piezaRepository.deleteById(id);

    }


}
