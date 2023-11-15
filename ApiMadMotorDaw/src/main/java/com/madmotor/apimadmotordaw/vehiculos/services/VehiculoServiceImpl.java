package com.madmotor.apimadmotordaw.vehiculos.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madmotor.apimadmotordaw.categorias.services.CategoriaService;
import com.madmotor.apimadmotordaw.notificaciones.configurations.WebSocketConfig;
import com.madmotor.apimadmotordaw.notificaciones.configurations.WebSocketHandler;
import com.madmotor.apimadmotordaw.notificaciones.dto.VehiculoNotificacionDto;
import com.madmotor.apimadmotordaw.notificaciones.mapper.VehiculoNotificacionMapper;
import com.madmotor.apimadmotordaw.notificaciones.models.Notificacion;
import com.madmotor.apimadmotordaw.vehiculos.dto.VehiculoCreateDto;
import com.madmotor.apimadmotordaw.vehiculos.dto.VehiculoUpdateDto;
import com.madmotor.apimadmotordaw.vehiculos.exceptions.VehiculoNotFound;
import com.madmotor.apimadmotordaw.vehiculos.mapper.VehiculoMapper;
import com.madmotor.apimadmotordaw.vehiculos.models.Vehiculo;
import com.madmotor.apimadmotordaw.vehiculos.repositories.VehiculoRepository;
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
        this.webSocketHandler = webSocketHandlerMock;
    }

    private final VehiculoRepository vehiculoRepository;
    private final CategoriaService categoriaService;
    private final WebSocketConfig webSocketConfig;
    private final VehiculoNotificacionMapper vehiculoNotificacionMapper;
    private WebSocketHandler webSocketHandler;
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
        webSocketHandler = webSocketConfig.webSocketVehiHandler();
        mapper = new ObjectMapper();
        this.vehiculoNotificacionMapper = vehiculoNotificacionMapper;
    }

    @Override
    public Page<Vehiculo> findAll(Optional<String> marca,
                                  Optional<String> categoria,
                                  Optional<Integer> minYear,
                                  Optional<Boolean> isDelete,
                                  Optional<Double> kmMax,
                                  Optional<Double> precioMax,
                                  Optional<Double> stockMin,
                                  Pageable pageable) {

        Specification<Vehiculo> specMarcaVehiculo = (root, query, criteriaBuilder) ->
                marca.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("marca")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Vehiculo> specCategoriaVehiculo = (root, query, criteriaBuilder) ->
                categoria.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("categoria")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Vehiculo> specMinYearVehiculo = (root, query, criteriaBuilder) ->
                minYear.map(m -> criteriaBuilder.greaterThanOrEqualTo(root.get("year"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Vehiculo> specKmMaxVehiculo = (root, query, criteriaBuilder) ->
                kmMax.map(m -> criteriaBuilder.lessThanOrEqualTo(root.get("km"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Vehiculo> specPrecioMaxVehiculo = (root, query, criteriaBuilder) ->
                precioMax.map(m -> criteriaBuilder.lessThanOrEqualTo(root.get("precio"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Vehiculo> specStockMinVehiculo = (root, query, criteriaBuilder) ->
                stockMin.map(m -> criteriaBuilder.greaterThanOrEqualTo(root.get("stock"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Vehiculo> specIsDeleteVehiculo = (root, query, criteriaBuilder) ->
                isDelete.map(m -> criteriaBuilder.equal(root.get("isDelete"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(false)));
        Specification<Vehiculo> criterio = Specification.where(specMarcaVehiculo)
                .and(specCategoriaVehiculo)
                .and(specMinYearVehiculo)
                .and(specKmMaxVehiculo)
                .and(specPrecioMaxVehiculo)
                .and(specStockMinVehiculo)
                .and(specIsDeleteVehiculo);

        return vehiculoRepository.findAll(criterio, pageable);
    }

    @Override
    @Cacheable
    public Vehiculo findById(String id) {
        log.info("Buscando por UUID " + id);
        var myID = UUID.fromString(id);
        return vehiculoRepository.findById(myID).orElseThrow(() -> new RuntimeException("No se encontro el vehiculo con el id " + id));

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
        if (categoriaNueva == null) {
            categoriaNueva = categoriaActual;
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

    void onChange(Notificacion.Tipo tipo, Vehiculo data) {

        log.info("Enviando notificación a los clientes ws");
        if (webSocketHandler == null) {
            log.warn("No se ha podido enviar la notificación a los clientes ws, no se ha encontrado el servicio");
            webSocketHandler = this.webSocketConfig.webSocketVehiHandler();
        }

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
                    webSocketHandler.sendMessage(json);
                } catch (Exception e) {
                    log.error("Error al enviar el mensaje a través del servicio WebSocket", e);
                }
            });
            senderThread.start();
        } catch (JsonProcessingException e) {
            log.error("Error al convertir la notificación a JSON", e);
        }
    }


}
