package com.madmotor.apimadmotordaw.rest.clientes.services;

import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteCreateRequest;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteReponse;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteUpdateRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Optional;

public interface ClienteService {
    ClienteReponse updateByDni(String dni, ClienteUpdateRequest clienteUpdateRequest);
    ClienteReponse findByDni(String dni);
    Page<ClienteReponse> findAll(Optional<String> nombre, Optional<String> apellido, Optional<String> direccion, Optional<Integer>codPostal,Pageable pageable);
    void deleteByDni(String dni);

    ClienteReponse savePost(ClienteCreateRequest clienteCreateRequest);
    ClienteReponse updateImage(String dni, MultipartFile image);
}
