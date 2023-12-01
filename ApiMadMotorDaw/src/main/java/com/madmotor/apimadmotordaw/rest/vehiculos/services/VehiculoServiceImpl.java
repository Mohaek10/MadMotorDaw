package com.madmotor.apimadmotordaw.rest.vehiculos.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madmotor.apimadmotordaw.rest.categorias.models.Categoria;
import com.madmotor.apimadmotordaw.rest.categorias.services.CategoriaService;
import com.madmotor.apimadmotordaw.config.websockets.WebSocketConfig;
import com.madmotor.apimadmotordaw.config.websockets.WebSocketHandler;
import com.madmotor.apimadmotordaw.websockets.notificaciones.dto.VehiculoNotificacionDto;
import com.madmotor.apimadmotordaw.websockets.notificaciones.mapper.VehiculoNotificacionMapper;
import com.madmotor.apimadmotordaw.websockets.notificaciones.models.Notificacion;
import com.madmotor.apimadmotordaw.rest.vehiculos.dto.VehiculoCreateDto;
import com.madmotor.apimadmotordaw.rest.vehiculos.dto.VehiculoUpdateDto;
import com.madmotor.apimadmotordaw.rest.vehiculos.exceptions.VehiculoNotFound;
import com.madmotor.apimadmotordaw.rest.vehiculos.repositories.VehiculoRepository;
import com.madmotor.apimadmotordaw.rest.vehiculos.mapper.VehiculoMapper;
import com.madmotor.apimadmotordaw.rest.vehiculos.models.Vehiculo;
import jakarta.persistence.criteria.Join;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@CacheConfig(cacheNames = {"vehiculos"})
@Slf4j
public class VehiculoServiceImpl implements VehiculoService {
    public void setWebSocketService(WebSocketHandler webSocketHandlerMock) {
        this.webSocketService = webSocketHandlerMock;
    }

    private final VehiculoRepository vehiculoRepository;
    private final CategoriaService categoriaService;
    private final WebSocketConfig webSocketConfig;
    private final VehiculoNotificacionMapper vehiculoNotificacionMapper;
    private WebSocketHandler webSocketService;
    private final VehiculoMapper vehiculoMapper;
    private final ObjectMapper mapper;

    @Autowired
    public VehiculoServiceImpl(VehiculoRepository vehiculoRepository,
                               VehiculoMapper vehiculoMapper,
                               CategoriaService categoriaService,
                               WebSocketConfig webSocketConfig,
                               VehiculoNotificacionMapper vehiculoNotificacionMapper) {
        this.vehiculoRepository = vehiculoRepository;
        this.categoriaService = categoriaService;
        this.vehiculoMapper = vehiculoMapper;
        this.webSocketConfig = webSocketConfig;
        webSocketService = webSocketConfig.webSocketVehiculo();
        mapper = new ObjectMapper();
        this.vehiculoNotificacionMapper = vehiculoNotificacionMapper;
    }


    @Override
    public Page<Vehiculo> findAll(Optional<String> marca, Optional<String> categoria,Optional<String> modelo,Optional<Integer> minYear, Optional<Boolean> isDelete, Optional<Double> kmMax, Optional<Double> precioMax, Optional<Integer> stockMin, Pageable pageable) {

        Specification<Vehiculo> specMarcaVehiculo = (root, query, criteriaBuilder) ->
                marca.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("marca")), "%" + m + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Vehiculo> specCategoriaVehiculo = (root, query, criteriaBuilder) ->
                categoria.map(c -> {
                    Join<Vehiculo, Categoria> categoriaJoin = root.join("categoria");
                    return criteriaBuilder.like(criteriaBuilder.lower(categoriaJoin.get("name")), "%" + c + "%");
                }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Vehiculo> specModeloVehiculo = (root, query, criteriaBuilder) ->
                modelo.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("modelo")), "%" + m + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Vehiculo> specMinYearVehiculo = (root, query, criteriaBuilder) ->
                minYear.map(m -> criteriaBuilder.greaterThanOrEqualTo(root.get("year"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Vehiculo> specIsDeleteVehiculo = (root, query, criteriaBuilder) ->
                isDelete.map(d -> criteriaBuilder.equal(root.get("isDeleted"), d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Vehiculo> specKmMaxVehiculo = (root, query, criteriaBuilder) ->
                kmMax.map(k -> criteriaBuilder.lessThanOrEqualTo(root.get("km"), k))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Vehiculo> specPrecioMaxVehiculo = (root, query, criteriaBuilder) ->
                precioMax.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("precio"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Vehiculo> specStockMinVehiculo = (root, query, criteriaBuilder) ->
                stockMin.map(s -> criteriaBuilder.greaterThanOrEqualTo(root.get("stock"), s))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Vehiculo> criterio=Specification.where(specMarcaVehiculo)
                .and(specCategoriaVehiculo)
                .and(specModeloVehiculo)
                .and(specMinYearVehiculo)
                .and(specIsDeleteVehiculo)
                .and(specKmMaxVehiculo)
                .and(specPrecioMaxVehiculo)
                .and(specStockMinVehiculo);
        return vehiculoRepository.findAll(criterio, pageable);

    }

    @Override
    @Cacheable
    public Vehiculo findById(String id) {
        log.info("Buscando por UUID " + id);
        var myID = UUID.fromString(id);
        return vehiculoRepository.findById(myID).orElseThrow(() -> new VehiculoNotFound("No se encontro el vehiculo con el id " + id));

    }

    @CachePut
    @Override
    public Vehiculo save(VehiculoCreateDto vehiculoCreateDto) {
        log.info("Guardando vehiculo " + vehiculoCreateDto);
        var categoria = categoriaService.findByName(vehiculoCreateDto.getCategoria());
        var vehiculoGuardado = vehiculoRepository.save(vehiculoMapper.toVevhiculo(vehiculoCreateDto, categoria));
        onChange(Notificacion.Tipo.CREATE, vehiculoGuardado);

        return vehiculoGuardado;

    }

    @Override
    public Vehiculo update(String id, VehiculoUpdateDto vehiculoUpdateDto) {
        log.info("Actualizando vehiculo " + vehiculoUpdateDto);

        // Obtener la categoría actual del vehículo
        var vehiculoActual = findById(id);
        var categoriaActual = vehiculoActual.getCategoria();

        // Intentar obtener la categoría actualizada
        var categoriaNueva = categoriaService.findByName(vehiculoUpdateDto.getCategoria());

        // Si la categoría actualizada no existe, usar la categoría actual del vehículo
        if (categoriaNueva == null) {categoriaNueva = categoriaActual;
        }

        // Guardar el vehículo actualizado con la categoría correspondiente
        var vehiculoActualizado = vehiculoRepository.save(vehiculoMapper.toVehiculo(vehiculoUpdateDto, vehiculoActual, categoriaNueva));

        // Realizar las acciones necesarias al cambiar el vehículo
        onChange(Notificacion.Tipo.UPDATE, vehiculoActualizado);

        return vehiculoActualizado;
    }

    @Override
    public void deleteById(String id) {

        // Utilizamos el findById directamente del repositorio
        Optional<Vehiculo> optionalVehiculo = vehiculoRepository.findById(UUID.fromString(id));

        if (optionalVehiculo.isPresent()) {
            Vehiculo vehiculo = optionalVehiculo.get();

            // Borramos el vehículo del repositorio
            vehiculoRepository.deleteById(UUID.fromString(id));


            // Enviamos la notificación a los clientes ws
            onChange(Notificacion.Tipo.DELETE, vehiculo);
        } else {
            // Manejar la situación en la que el vehículo no existe
            log.warn("No se encontró el vehículo con id: " + id);
            throw new VehiculoNotFound("No se encontró el vehículo con id: " + id);
        }
    }

    public void onChange(Notificacion.Tipo tipo, Vehiculo data) {
        log.info("Servicio de vehiculo onChange con tipo: " + tipo + " y datos: " + data);
        try {
            Notificacion<VehiculoNotificacionDto> notificacion = new Notificacion<>(
                    "VEHICULO",
                    tipo,
                    vehiculoNotificacionMapper.toVehiculoNotificacionDto(data),
                    LocalDateTime.now().toString()
            );

            String json = mapper.writeValueAsString((notificacion));

            log.info("Enviando mensaje a los clientes ws");

            Thread senderThread = new Thread(() -> {
                try {
                    webSocketService.sendMessage(json); } catch (Exception e) {
                }
            });
            senderThread.start();} catch (JsonProcessingException e) {
        }
    }


}
