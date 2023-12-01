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

@Service
@Slf4j
@CacheConfig(cacheNames = "clientes")
public class ClienteServiceImpl implements ClienteService{
    private final ClienteRepository clienteRepository;

    private final ClienteMapper clienteMapper;
    private final StorageService storageService;

    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository, ClienteMapper clienteMapper, StorageService storageService) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
        this.storageService = storageService;
    }
    @CachePut
    @Override
    public ClienteReponse updateByDni(String dni, ClienteUpdateRequest clienteUpdateRequest) {
        log.info("Actualizando el cliente con el DNI número: " + dni);

        // Buscar el cliente existente por su DNI
        var clienteActualizar = clienteRepository.findByDniEqualsIgnoreCase(dni)
                .orElseThrow(() -> new ClienteNotFound(dni));
        var clienteActualizado = clienteRepository.save(clienteMapper.toCliente(clienteUpdateRequest,clienteActualizar));

        return clienteMapper.toClienteReponse(clienteActualizado);
    }
    @Cacheable
    @Override
    public ClienteReponse findByDni(String dni) {
        log.info("Buscando el cliente con el dni numero : " + dni );
        return clienteMapper.toClienteReponse(clienteRepository.findByDniEqualsIgnoreCase(dni).orElseThrow(()->new ClienteNotFound(dni)));
    }

    @Override
    public Page<ClienteReponse> findAll(Optional<String> nombre, Optional<String> apellido, Optional<String> direccion, Optional<Integer> codPostal, Pageable pageable) {
        Specification<Cliente> specNombre = (root, query, criteriaBuilder) ->
                nombre.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Cliente> specApellido = (root, query, criteriaBuilder) ->
                apellido.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("apellido")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Cliente> specDireccion = (root, query, criteriaBuilder) ->
                direccion.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("direccion")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Cliente> specCodPostal = (root, query, criteriaBuilder) ->
                codPostal.map(m -> criteriaBuilder.equal(root.get("codigoPostal"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Cliente> criterio = Specification.where(specNombre)
                .and(specApellido)
                .and(specDireccion)
                .and(specCodPostal);

        return clienteRepository.findAll(criterio, pageable).map(clienteMapper::toClienteReponse);
    }


    @CacheEvict
    @Override
    public void deleteByDni(String dni) {
        log.info("Eliminando el cliente con el dni numero : " + dni);
        var clienteAElminar = clienteRepository.findByDniEqualsIgnoreCase(dni).orElseThrow(() -> new ClienteNotFound(dni));
        if( clienteAElminar.getImagen()!= null &&!clienteAElminar.getImagen().equals(Cliente.IMAGE_DEFAULT)){
            storageService.delete(clienteAElminar.getImagen());
        }
        clienteRepository.delete(clienteAElminar);
    }
    @CachePut
    @Override
    public ClienteReponse savePost(ClienteCreateRequest clienteCreateRequest) {
        log.info("Guardando a un nuevo cliente");
        if( clienteRepository.findByDniEqualsIgnoreCase(clienteCreateRequest.getDni()).isEmpty()){
            var clienteGuardado= clienteRepository.save(clienteMapper.toCliente(clienteCreateRequest));
            return clienteMapper.toClienteReponse(clienteGuardado);
        }else{
            throw new ClienteFailSave("Ha habido un error al registrar al cliente");
        }
    }
    @CachePut
    @Override
    public ClienteReponse updateImage(String dni, MultipartFile imagen) {
        log.info("Actualizando la imagen del cliente con el dni numero: " + dni);


        var cliente = clienteRepository.findByDniEqualsIgnoreCase(dni).orElseThrow(() -> new ClienteNotFound(dni));
        if (cliente.getImagen() != null && !cliente.getImagen().equals(Cliente.IMAGE_DEFAULT)) {
            storageService.delete(cliente.getImagen());
        }
        String img = storageService.store(imagen);
        String url = storageService.getUrl(img);
        cliente.setImagen(url);
        return clienteMapper.toClienteReponse(clienteRepository.save(cliente));
    }
}
