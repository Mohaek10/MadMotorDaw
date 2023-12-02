package com.madmotor.apimadmotordaw.rest.clientes.mappers;

import com.madmotor.apimadmotordaw.rest.clientes.Model.Cliente;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteCreateRequest;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteInfoDto;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteReponse;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public static final String IMAGE_DEFAULT = "https://via.placeholder.com/150";
    public Cliente toCliente(ClienteCreateRequest clienteRequest) {
        return Cliente.builder()
                .nombre(clienteRequest.getNombre())
                .apellido(clienteRequest.getApellido())
                .direccion(clienteRequest.getDireccion())
                .codigoPostal(clienteRequest.getCodigoPostal())
                .dni(clienteRequest.getDni())
                .imagen(clienteRequest.getImagen()!= null? clienteRequest.getImagen() : IMAGE_DEFAULT)
                .build();
    }

    public Cliente toCliente(ClienteUpdateRequest clienteUpdateRequest, Cliente cliente) {
        return Cliente.builder()
                .id(cliente.getId())
                .dni(cliente.getDni())
                .nombre(clienteUpdateRequest.getNombre()!=null && !clienteUpdateRequest.getNombre().isEmpty()? clienteUpdateRequest.getNombre() : cliente.getNombre())
                .apellido(clienteUpdateRequest.getApellido()!= null && !clienteUpdateRequest.getApellido().isEmpty()? clienteUpdateRequest.getApellido() : cliente.getApellido())
                .direccion(clienteUpdateRequest.getDireccion()!= null && !clienteUpdateRequest.getDireccion().isEmpty()? clienteUpdateRequest.getDireccion(): cliente.getDireccion())
                .codigoPostal(clienteUpdateRequest.getCodigoPostal()!= null? clienteUpdateRequest.getCodigoPostal(): cliente.getCodigoPostal())
                .imagen(clienteUpdateRequest.getImagen()!= null? clienteUpdateRequest.getImagen(): cliente.getImagen())
                .build();
    }
    public ClienteReponse toClienteReponse(Cliente cliente) {
        return ClienteReponse.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .direccion(cliente.getDireccion())
                .codigoPostal(cliente.getCodigoPostal())
                .dni(cliente.getDni())
                .imagen(cliente.getImagen())
                .build();
    }

    public ClienteInfoDto toClienteInfoDto(Cliente cliente) {
        return ClienteInfoDto.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .direccion(cliente.getDireccion())
                .codigoPostal(cliente.getCodigoPostal())
                .dni(cliente.getDni())
                .imagen(cliente.getImagen())
                .build();
    }

}