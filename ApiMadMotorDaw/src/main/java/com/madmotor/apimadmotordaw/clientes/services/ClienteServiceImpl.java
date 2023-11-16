package com.madmotor.apimadmotordaw.clientes.services;

import com.madmotor.apimadmotordaw.clientes.Model.Cliente;
import com.madmotor.apimadmotordaw.clientes.Repository.ClienteRepository;
import com.madmotor.apimadmotordaw.clientes.dto.ClienteCreateRequest;
import com.madmotor.apimadmotordaw.clientes.dto.ClienteReponse;
import com.madmotor.apimadmotordaw.clientes.dto.ClienteUpdateRequest;
import com.madmotor.apimadmotordaw.clientes.exceptions.ClienteFailList;
import com.madmotor.apimadmotordaw.clientes.exceptions.ClienteFailSave;
import com.madmotor.apimadmotordaw.clientes.exceptions.ClienteNotFound;
import com.madmotor.apimadmotordaw.clientes.mappers.ClienteMapper;
import com.madmotor.apimadmotordaw.utils.storage.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
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

    @Override
    public ClienteReponse updateByDni(String dni, ClienteUpdateRequest clienteUpdateRequest) {
        log.info("Actualizando el cliente con el DNI número: " + dni);

        // Buscar el cliente existente por su DNI
        var clienteActualizar = clienteRepository.findByDniEqualsIgnoreCase(dni)
                .orElseThrow(() -> new ClienteNotFound(dni));
        var clienteActualizado = clienteRepository.save(clienteMapper.toCliente(clienteUpdateRequest,clienteActualizar));

        return clienteMapper.toClienteReponse(clienteActualizado);
    }

    @Override
    public ClienteReponse findByDni(String dni) {
        log.info("Buscando el cliente con el dni numero : " + dni );
        return clienteMapper.toClienteReponse(clienteRepository.findByDniEqualsIgnoreCase(dni).orElseThrow(()->new ClienteNotFound(dni)));
    }

    @Override
    public List<ClienteReponse> findAll() {
        log.info("Dando un listado de todos los clientes");
        try {
         return clienteRepository.findAll().stream().map(clienteMapper::toClienteReponse).toList();
         } catch (Exception e) {
        log.error("Error al obtener la lista de clientes", e);
        throw new ClienteFailList("Error al obtener la lista de clientes");
    }
    }

    @Override
    public void deleteByDni(String dni) {
        log.info("Eliminando el cliente con el dni numero : " + dni);
        var clienteAElminar = clienteRepository.findByDniEqualsIgnoreCase(dni).orElseThrow(() -> new ClienteNotFound(dni));
        if( clienteAElminar.getImagen()!= null &&!clienteAElminar.getImagen().equals(Cliente.IMAGE_DEFAULT)){
            storageService.delete(clienteAElminar.getImagen());
        }
        clienteRepository.delete(clienteAElminar);
    }

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

    @Override
    public ClienteReponse updateImage(String dni, MultipartFile imagen) {
        log.info("Actualizando la imagen del cliente con el dni numero: " + dni);


        var cliente = clienteRepository.findByDniEqualsIgnoreCase(dni).orElseThrow(() -> new ClienteNotFound(dni));
        if( cliente.getImagen()!= null && !cliente.getImagen().equals(Cliente.IMAGE_DEFAULT)){
            storageService.delete(cliente.getImagen());
        }
        String img = storageService.store(imagen);
        String url = storageService.getUrl(img);
        cliente.setImagen(url);
        return clienteMapper.toClienteReponse(clienteRepository.save(cliente));
    }
}
