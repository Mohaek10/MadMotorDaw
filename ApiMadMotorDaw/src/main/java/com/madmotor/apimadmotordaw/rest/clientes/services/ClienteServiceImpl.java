package com.madmotor.apimadmotordaw.rest.clientes.services;

import com.madmotor.apimadmotordaw.rest.clientes.Model.Cliente;
import com.madmotor.apimadmotordaw.rest.clientes.Repository.ClienteRepository;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteCreateRequest;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteReponse;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteUpdateRequest;
import com.madmotor.apimadmotordaw.rest.clientes.exceptions.ClienteFailSave;
import com.madmotor.apimadmotordaw.rest.clientes.exceptions.ClienteNotFound;
import com.madmotor.apimadmotordaw.rest.clientes.mappers.ClienteMapper;
import com.madmotor.apimadmotordaw.rest.storage.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;
/*
* Implementación de la interfaz ClienteService que contiene los métodos de la lógica de negocio
* Aplicacion de Cache
 */

@Service
@Slf4j
@CacheConfig(cacheNames = "clientes")
public class ClienteServiceImpl implements ClienteService{
    // Indicamos las dependencias que se van a inyectar
    private final ClienteRepository clienteRepository;

    private final ClienteMapper clienteMapper;
    private final StorageService storageService;

    // Inyección de dependencias
    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository, ClienteMapper clienteMapper, StorageService storageService) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
        this.storageService = storageService;
    }
    /*
    * Método que actuallizar un cliente en la base de datos
    * @param id Identificador del cliente de tipo UUID
    * @param clienteCreateRequest Objeto con los datos del cliente
    * @return ClienteReponse Objeto con los datos del cliente actualizado
    * @throws ClienteFailSave Excepción que se lanza cuando no se ha podido actualizar el cliente
     */
    @CachePut
    @Override
    public ClienteReponse updateByID(UUID id, ClienteUpdateRequest clienteUpdateRequest) {
        log.info("Actualizando el cliente con el UUID número: " + id);

        // Buscar el cliente existente por su ID en el caso contrario lanza una excepción
        var clienteActualizar = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFound(id.toString()));
        // Actualizar los datos del cliente con los datos del objeto clienteUpdateRequest y los guardar en la base de datos
        var clienteActualizado = clienteRepository.save(clienteMapper.toCliente(clienteUpdateRequest,clienteActualizar));
        // Devolver el objeto cliente actualizado
        return clienteMapper.toClienteReponse(clienteActualizado);
    }
    /*
    * Método que busca un cliente por su ID
    * @param id Identificador del cliente de tipo UUID
    * @return ClienteReponse Objeto con los datos del cliente
    * @throws ClienteNotFound Excepción que se lanza cuando no se ha encontrado el cliente
     */
    @Cacheable
    @Override
    public ClienteReponse findByID(UUID id) {
        log.info("Buscando el cliente con el dni numero : " + id );
        // Buscar el cliente existente por su ID en el caso contrario lanza una excepción
        return clienteMapper.toClienteReponse(clienteRepository.findById(id).orElseThrow(()->new ClienteNotFound(id.toString())));
    }
    /*
    * Método que busca todos los clientes y los agrupa de acuerdo a los criterios de búsqueda
    * @param pageable Objeto con los datos de la paginación
    * @Param nombre Nombre del cliente
    * @Param apellido Apellido del cliente
    * @Param direccion Dirección del cliente
    * @Param codPostal Código postal del cliente
    * @return Page<ClienteReponse> Objeto con los datos de la paginación y los clientes
     */

    @Override
    public Page<ClienteReponse> findAll(Optional<String> nombre, Optional<String> apellido, Optional<String> direccion, Optional<Integer> codPostal, Pageable pageable) {
       // Especificación de los criterios de búsqueda
        // Si no se ha introducido ningún criterio de búsqueda se devuelven todos los clientes
        // Si se ha introducido algún criterio de búsqueda se devuelven los clientes que coincidan con los criterios de búsqueda
        // Se devuelven los clientes ordenados
        //Criterio de busqueda por nombre
        Specification<Cliente> specNombre = (root, query, criteriaBuilder) ->
                nombre.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        //Criterio de busqueda por apellido
        Specification<Cliente> specApellido = (root, query, criteriaBuilder) ->
                apellido.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("apellido")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        //Criterio de busqueda por direccion
        Specification<Cliente> specDireccion = (root, query, criteriaBuilder) ->
                direccion.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("direccion")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        //Criterio de busqueda por codigo postal
        Specification<Cliente> specCodPostal = (root, query, criteriaBuilder) ->
                codPostal.map(m -> criteriaBuilder.equal(root.get("codigoPostal"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        // Se combian los criterios de busqueda de las especificaciones
        Specification<Cliente> criterio = Specification.where(specNombre)
                .and(specApellido)
                .and(specDireccion)
                .and(specCodPostal);
        // Se devuelven los clientes basados en los criterios de busqueda
        return clienteRepository.findAll(criterio, pageable).map(clienteMapper::toClienteReponse);
    }
    /*
    * Método que elimina un cliente por su ID
    * @param id Identificador del cliente de tipo UUID
    * @throws ClienteNotFound Excepción que se lanza cuando no se ha encontrado el cliente
     */
    @CacheEvict
    @Override
    public void deleteById(UUID id) {
        log.info("Eliminando el cliente con el UUID numero : " + id);
        // Buscar el cliente existente por su ID en el caso contrario lanza una excepción
        var clienteAElminar = clienteRepository.findById(id).orElseThrow(() -> new ClienteNotFound(id.toString()));
        // Elimina la imagen del cliente si no es la imagen por defecto
        if( clienteAElminar.getImagen()!= null &&!clienteAElminar.getImagen().equals(Cliente.IMAGE_DEFAULT)){
            storageService.delete(clienteAElminar.getImagen());
        }
        // Elimina el cliente de la base de datos
        clienteRepository.delete(clienteAElminar);
    }
    /*
    * Método que guarda un cliente en la base de datos
    * @param clienteCreateRequest Objeto con los datos del cliente
    * @return ClienteReponse Objeto con los datos del cliente guardado
    * @throws ClienteFailSave Excepción que se lanza cuando no se ha podido guardar el cliente
     */
    @CachePut
    @Override
    public ClienteReponse savePost(ClienteCreateRequest clienteCreateRequest) {
        log.info("Guardando a un nuevo cliente");
        // Comprobar si hay ya alguien registrado con el mismo dni en la base de datos
        if( clienteRepository.findByDniEqualsIgnoreCase(clienteCreateRequest.getDni()).isEmpty()){
            // Guardar el cliente en la base de datos si no hay nadie mas con el mismo dni
            var clienteGuardado= clienteRepository.save(clienteMapper.toCliente(clienteCreateRequest));
            // Devuelve el ClienteReponse con los datos del cliente guardado
            return clienteMapper.toClienteReponse(clienteGuardado);
        }else{
            // Lanzar una excepción si ya hay alguien registrado con el mismo dni
            throw new ClienteFailSave("Ha habido un error al registrar al cliente");
        }
    }
    /*
    * Método que actualiza la imagen de un cliente
    * @param id Identificador del cliente de tipo UUID
    * @param image Imagen del cliente
    * @param withUrl Booleano que indica si se quiere la url de la imagen
    * @return ClienteReponse Objeto con los datos del cliente actualizado
    * @throws ClienteNotFound Excepción que se lanza cuando no se ha encontrado el cliente
     */
    @CachePut
    @Override
    public ClienteReponse updateImage(UUID id, MultipartFile image,  Boolean withUrl) {
        log.info("Actualizando la imagen del cliente con el UUID numero: " + id);
        // Buscar el cliente existente por su ID en el caso contrario lanza una excepción
        var cliente = clienteRepository.findById(id).orElseThrow(() -> new ClienteNotFound(id.toString()));
        // Elimina la imagen del cliente si no es la imagen por defecto
        if (cliente.getImagen() != null && !cliente.getImagen().equals(Cliente.IMAGE_DEFAULT)) {
            storageService.delete(cliente.getImagen());
        }
        // Guarda la imagen en el almacenamiento
        var imagen = storageService.store(image);
        // Actualiza la imagen del cliente
        String imageUrl = !withUrl ? imagen : storageService.getUrl(imagen);
        cliente.setImagen(imageUrl);
        // Guarda el cliente en la base de datos
        return clienteMapper.toClienteReponse(clienteRepository.save(cliente));
    }
}
