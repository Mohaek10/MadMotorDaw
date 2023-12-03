package com.madmotor.apimadmotordaw.rest.vehiculos.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madmotor.apimadmotordaw.rest.categorias.models.Categoria;
import com.madmotor.apimadmotordaw.rest.categorias.services.CategoriaService;
import com.madmotor.apimadmotordaw.config.websockets.WebSocketConfig;
import com.madmotor.apimadmotordaw.config.websockets.WebSocketHandler;
import com.madmotor.apimadmotordaw.rest.storage.service.StorageService;
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
import jakarta.transaction.Transactional;
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

    private final StorageService storageService;

    @Autowired
    public VehiculoServiceImpl(VehiculoRepository vehiculoRepository,
                               VehiculoMapper vehiculoMapper,
                               CategoriaService categoriaService,
                               WebSocketConfig webSocketConfig,
                               VehiculoNotificacionMapper vehiculoNotificacionMapper, StorageService storageService) {
        this.vehiculoRepository = vehiculoRepository;
        this.categoriaService = categoriaService;
        this.vehiculoMapper = vehiculoMapper;
        this.webSocketConfig = webSocketConfig;
        webSocketService = webSocketConfig.webSocketVehiculo();
        this.storageService = storageService;
        mapper = new ObjectMapper();
        this.vehiculoNotificacionMapper = vehiculoNotificacionMapper;
    }


    @Override
    public Page<Vehiculo> findAll(Optional<String> marca, Optional<String> categoria, Optional<String> modelo, Optional<Integer> minYear, Optional<Boolean> isDelete, Optional<Double> kmMax, Optional<Double> precioMax, Optional<Integer> stockMin, Pageable pageable) {

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
        Specification<Vehiculo> criterio = Specification.where(specMarcaVehiculo)
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
    @Cacheable(key = "#id")
    public Vehiculo findById(String id) {
        log.info("Buscando por UUID " + id);
        var myID = UUID.fromString(id);
        return vehiculoRepository.findById(myID).orElseThrow(() -> new VehiculoNotFound("No se encontro el vehiculo con el id " + id));

    }

    @CachePut(key = "#result.id")
    @Override
    public Vehiculo save(VehiculoCreateDto vehiculoCreateDto) {
        log.info("Guardando vehiculo " + vehiculoCreateDto);
        var categoria = categoriaService.findByName(vehiculoCreateDto.getCategoria());
        var vehiculoGuardado = vehiculoRepository.save(vehiculoMapper.toVevhiculo(vehiculoCreateDto, categoria));
        onChange(Notificacion.Tipo.CREATE, vehiculoGuardado);

        return vehiculoGuardado;

    }

    @Override
    @CachePut(key ="#result.id")
    @Transactional
    public Vehiculo update(String id, VehiculoUpdateDto vehiculoUpdateDto) {
        log.info("Actualizando vehiculo " + vehiculoUpdateDto);

        // Obtener la categoría actual del vehículo
        var vehiculoActual = findById(id);
        var categoriaActual = vehiculoActual.getCategoria();

        // Intentar obtener la categoría actualizada
        if (vehiculoUpdateDto.getCategoria() == null||vehiculoUpdateDto.getCategoria().equals("")) {
            vehiculoUpdateDto.setCategoria(categoriaActual.getName());
        }
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
    @CachePut(key = "#id")
    @Transactional
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

    @Override
    @CachePut(key = "#result.id")
    @Transactional
    public Vehiculo updateImage(String id, MultipartFile image, Boolean withUrl) {
        log.info("Actualizando imagen del vehiculo con id: " + id);
        var vehiculoActual = vehiculoRepository.findById(UUID.fromString(id)).orElseThrow(() -> new VehiculoNotFound(id));
        if (vehiculoActual.getImagen() != null && !vehiculoActual.getImagen().equals(Vehiculo.IMAGE_DEFAULT)) {
            storageService.delete(vehiculoActual.getImagen());
        }
        var imagen = storageService.store(image);

        String imageUrl = !withUrl ? imagen : storageService.getUrl(imagen);

        var vehiculoActualizado = Vehiculo.builder()
                .id(vehiculoActual.getId())
                .marca(vehiculoActual.getMarca())
                .modelo(vehiculoActual.getModelo())
                .year(vehiculoActual.getYear())
                .km(vehiculoActual.getKm())
                .precio(vehiculoActual.getPrecio())
                .stock(vehiculoActual.getStock())
                .imagen(imageUrl)
                .descripcion(vehiculoActual.getDescripcion())
                .createdAt(vehiculoActual.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .categoria(vehiculoActual.getCategoria())
                .isDeleted(vehiculoActual.getIsDeleted())
                .build();
        var vehiculoGuardado = vehiculoRepository.save(vehiculoActualizado);
        onChange(Notificacion.Tipo.UPDATE, vehiculoGuardado);
        return vehiculoGuardado;
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
                    webSocketService.sendMessage(json);
                } catch (Exception e) {
                }
            });
            senderThread.start();
        } catch (JsonProcessingException e) {
        }
    }


}
