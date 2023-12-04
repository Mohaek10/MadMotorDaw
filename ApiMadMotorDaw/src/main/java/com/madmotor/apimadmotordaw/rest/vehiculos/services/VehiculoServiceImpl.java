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
/**
 * Clase que implementa la interfaz VehiculoService
 *  Apliación de la anotación @CacheConfig para indicar el nombre de la cache
 *  Apliación de la anotación @Slf4j para la gestión de logs
 *  Apliación de la anotación @Service para indicar que es un servicio
 *  Apliación de la anotación @Autowired para la inyección de dependencias
 *  Apliación de la anotación @Transactional para indicar que es una transacción y no haya problemas de concurrencia
 */

@Service
@CacheConfig(cacheNames = {"vehiculos"})
@Slf4j
public class VehiculoServiceImpl implements VehiculoService {
    //En caso de que no se pueda inyectar el WebSocketHandler, se crea un mock
    public void setWebSocketService(WebSocketHandler webSocketHandlerMock) {
        this.webSocketService = webSocketHandlerMock;
    }
    //Indicamos las dependencias necesarias
    private final VehiculoRepository vehiculoRepository;
    private final CategoriaService categoriaService;
    private final WebSocketConfig webSocketConfig;
    private final VehiculoNotificacionMapper vehiculoNotificacionMapper;
    private WebSocketHandler webSocketService;
    private final VehiculoMapper vehiculoMapper;
    private final ObjectMapper mapper;

    private final StorageService storageService;
    //Inyección de dependencias en el constructor
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
        //Instanciamos el ObjectMapper con el que se mapeará el objeto a JSON
        mapper = new ObjectMapper();
        //Instanciamos el mapper de la notificación
        this.vehiculoNotificacionMapper = vehiculoNotificacionMapper;
    }

    /**
     * Método que devuelve todos los vehículos paginados y filtrados por los parámetros
     * @param marca Marca del vehículo
     * @param categoria Categoría del vehículo
     * @param modelo Modelo del vehículo
     * @param minYear Año mínimo del vehículo
     * @param isDelete Si el vehículo está borrado
     * @param kmMax Kilómetros máximos del vehículo
     * @param precioMax Precio máximo del vehículo
     * @param stockMin Stock mínimo del vehículo
     * @param pageable Paginación
     * @return Lista de vehículos paginada
     */
    @Override
    public Page<Vehiculo> findAll(Optional<String> marca, Optional<String> categoria, Optional<String> modelo, Optional<Integer> minYear, Optional<Boolean> isDelete, Optional<Double> kmMax, Optional<Double> precioMax, Optional<Integer> stockMin, Pageable pageable) {
        //Creamos las especificaciones para filtrar los vehículos
        //Filtrado por marca
        Specification<Vehiculo> specMarcaVehiculo = (root, query, criteriaBuilder) ->
                marca.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("marca")), "%" + m + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        //Filtrado por categoría
        Specification<Vehiculo> specCategoriaVehiculo = (root, query, criteriaBuilder) ->
                categoria.map(c -> {
                    Join<Vehiculo, Categoria> categoriaJoin = root.join("categoria");
                    return criteriaBuilder.like(criteriaBuilder.lower(categoriaJoin.get("name")), "%" + c + "%");
                }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        //Filtrado por modelo
        Specification<Vehiculo> specModeloVehiculo = (root, query, criteriaBuilder) ->
                modelo.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("modelo")), "%" + m + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        //Filtrado por año mínimo
        Specification<Vehiculo> specMinYearVehiculo = (root, query, criteriaBuilder) ->
                minYear.map(m -> criteriaBuilder.greaterThanOrEqualTo(root.get("year"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        //Filtrado por borrado
        Specification<Vehiculo> specIsDeleteVehiculo = (root, query, criteriaBuilder) ->
                isDelete.map(d -> criteriaBuilder.equal(root.get("isDeleted"), d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        //Filtrado por kilómetros máximos
        Specification<Vehiculo> specKmMaxVehiculo = (root, query, criteriaBuilder) ->
                kmMax.map(k -> criteriaBuilder.lessThanOrEqualTo(root.get("km"), k))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        //Filtrado por precio máximo
        Specification<Vehiculo> specPrecioMaxVehiculo = (root, query, criteriaBuilder) ->
                precioMax.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("precio"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        //Filtrado por stock mínimo
        Specification<Vehiculo> specStockMinVehiculo = (root, query, criteriaBuilder) ->
                stockMin.map(s -> criteriaBuilder.greaterThanOrEqualTo(root.get("stock"), s))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        //Creamos la especificación final con todos los filtros aplicados
        Specification<Vehiculo> criterio = Specification.where(specMarcaVehiculo)
                .and(specCategoriaVehiculo)
                .and(specModeloVehiculo)
                .and(specMinYearVehiculo)
                .and(specIsDeleteVehiculo)
                .and(specKmMaxVehiculo)
                .and(specPrecioMaxVehiculo)
                .and(specStockMinVehiculo);
        //Devolvemos los vehículos paginados y filtrados
        return vehiculoRepository.findAll(criterio, pageable);

    }
    /**
     * Método que devuelve todos los vehículos paginados
     * @param  id Id del vehículo a buscar
     * @return Lista de vehículos paginada
     * @throws VehiculoNotFound Excepción que se lanza si no se encuentra el vehículo
     */
    @Override
    @Cacheable(key = "#id")
    public Vehiculo findById(String id) {
        log.info("Buscando por UUID " + id);
        //Creamos el UUID a partir del id pasado por parámetro
        var myID = UUID.fromString(id);
        //Devolvemos el vehículo con el id pasado por parámetro o lanzamos una excepción si no se encuentra
        return vehiculoRepository.findById(myID).orElseThrow(() -> new VehiculoNotFound("No se encontro el vehiculo con el id " + id));

    }
    /**
    * Metodo que guarda un vehiculo en la base de datos
    * @param vehiculoCreateDto Objeto con los datos del vehiculo a guardar
    * @return Vehiculo guardado
    * throws CategoriaNotFound Excepción que se lanza si no se encuentra la categoría
     */
    @CachePut(key = "#result.id")
    @Override
    public Vehiculo save(VehiculoCreateDto vehiculoCreateDto) {
        log.info("Guardando vehiculo " + vehiculoCreateDto);
        //Obtenemos la categoría del vehículo y en el caso de que no exista, lanzamos una excepción
        var categoria = categoriaService.findByName(vehiculoCreateDto.getCategoria());
        //Se guarda el vehículo en la base de datos
        var vehiculoGuardado = vehiculoRepository.save(vehiculoMapper.toVevhiculo(vehiculoCreateDto, categoria));
        //Enviamos la notificación a los clientes ws de que se ha creado un vehículo
        onChange(Notificacion.Tipo.CREATE, vehiculoGuardado);
        //Devolvemos el vehículo guardado
        return vehiculoGuardado;

    }
    /**
     *   Método que actualiza un vehículo
     * @param id Id del vehículo a actualizar
     * @param vehiculoUpdateDto Objeto con los datos del vehículo a actualizar
     * @return Vehiculo actualizado
     */

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
    /**
    * Método que borra un vehículo
    * @param id Id del vehículo a borrar
    * @throws VehiculoNotFound Excepción que se lanza si no se encuentra el vehículo
     */

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
    /**
     * Método que actualiza la imagen de un vehículo
     * @param id Id del vehículo a actualizar
     * @param image Imagen del vehículo
     * @param withUrl Si se quiere guardar la url de la imagen
     * @return Vehículo actualizado
     * @throws VehiculoNotFound Excepción que se lanza si no se encuentra el vehículo
     */

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
    /**
     * Método que envía una notificación a los clientes ws
     * @param tipo Tipo de notificación (CREATE, UPDATE, DELETE)
     * @param data Datos de la notificación (vehículo)
     * @throws JsonProcessingException Excepción que se lanza si no se puede convertir el objeto a JSON
     */

    public void onChange(Notificacion.Tipo tipo, Vehiculo data) {
        log.info("Servicio de vehiculo onChange con tipo: " + tipo + " y datos: " + data);
        //Creamos la notificación con los datos del vehículo
        try {
            Notificacion<VehiculoNotificacionDto> notificacion = new Notificacion<>(
                    "VEHICULO",
                    tipo,
                    vehiculoNotificacionMapper.toVehiculoNotificacionDto(data),
                    LocalDateTime.now().toString()
            );
            //Convertimos la notificación a JSON
            String json = mapper.writeValueAsString((notificacion));
            //Enviamos la notificación a los clientes ws
            log.info("Enviando mensaje a los clientes ws");
            //Creamos un hilo para enviar el mensaje
            Thread senderThread = new Thread(() -> {
                //Enviamos el mensaje a los clientes ws
                try {
                    webSocketService.sendMessage(json);
                } catch (Exception e) {
                }
            });
            //Iniciamos el hilo
            senderThread.start();
        } catch (JsonProcessingException e) {
        }
    }


}
