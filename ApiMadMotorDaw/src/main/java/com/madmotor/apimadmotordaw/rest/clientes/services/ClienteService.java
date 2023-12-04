package com.madmotor.apimadmotordaw.rest.clientes.services;

import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteCreateRequest;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteReponse;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteUpdateRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Optional;
import java.util.UUID;

/**
 * Interfaz ClienteService
 *
 * En esta interfaz se definen los métodos que se utilizarán en el ClienteServiceImpl
 * @version 1.0
 * @author Joe Brandon
 */

public interface ClienteService {
    ClienteReponse updateByID(UUID id, ClienteUpdateRequest clienteUpdateRequest);
    ClienteReponse findByID(UUID id);
    Page<ClienteReponse> findAll(Optional<String> nombre, Optional<String> apellido, Optional<String> direccion, Optional<Integer>codPostal,Pageable pageable);
    void deleteById(UUID id);

    ClienteReponse savePost(ClienteCreateRequest clienteCreateRequest);
    ClienteReponse updateImage(UUID id, MultipartFile image, Boolean withUrl);
}
