package com.madmotor.apimadmotordaw.clientes.services;

import com.madmotor.apimadmotordaw.clientes.dto.ClienteCreateRequest;
import com.madmotor.apimadmotordaw.clientes.dto.ClienteReponse;
import com.madmotor.apimadmotordaw.clientes.dto.ClienteUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ClienteService {
    ClienteReponse updateByDni(String dni, ClienteUpdateRequest clienteUpdateRequest);
    ClienteReponse findByDni(String dni);
    List<ClienteReponse> findAll();
    void deleteByDni(String dni);

    ClienteReponse savePost(ClienteCreateRequest clienteCreateRequest);
    ClienteReponse updateImage(String dni, MultipartFile image);
}
