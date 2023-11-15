package com.madmotor.apimadmotordaw.clientes.mappers;

import com.madmotor.apimadmotordaw.clientes.Model.Cliente;
import com.madmotor.apimadmotordaw.clientes.dto.ClienteCreateRequest;
import com.madmotor.apimadmotordaw.clientes.dto.ClienteReponse;
import com.madmotor.apimadmotordaw.clientes.dto.ClienteUpdateRequest;
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
              .piezas(clienteRequest.getPiezas())
              .coches(clienteRequest.getCoches())
                .imagen(clienteRequest.getImagen()!= null? clienteRequest.getImagen() : IMAGE_DEFAULT)
              .build();
    }

    public Cliente toCliente(ClienteUpdateRequest clienteUpdateRequest, Cliente cliente) {
        return Cliente.builder()
                .codCliente(cliente.getCodCliente())
                .dni(cliente.getDni())
                .nombre(clienteUpdateRequest.getNombre()!=null? clienteUpdateRequest.getNombre() : cliente.getNombre())
                .apellido(clienteUpdateRequest.getApellido()!= null? clienteUpdateRequest.getApellido() : cliente.getApellido())
                .direccion(clienteUpdateRequest.getDireccion()!= null? clienteUpdateRequest.getDireccion(): cliente.getDireccion())
                .codigoPostal(clienteUpdateRequest.getCodigoPostal()!= null? clienteUpdateRequest.getCodigoPostal(): cliente.getCodigoPostal())
                .piezas(clienteUpdateRequest.getPiezas()!= null? clienteUpdateRequest.getPiezas(): cliente.getPiezas())
                .coches(clienteUpdateRequest.getCoches()!= null? clienteUpdateRequest.getCoches(): cliente.getCoches())
                .imagen(clienteUpdateRequest.getImagen()!= null? clienteUpdateRequest.getImagen(): cliente.getImagen())
             .build();
    }
    public ClienteReponse toClienteReponse(Cliente cliente) {
        return ClienteReponse.builder()
              .nombre(cliente.getNombre())
              .apellido(cliente.getApellido())
              .direccion(cliente.getDireccion())
              .codigoPostal(cliente.getCodigoPostal())
              .piezas(cliente.getPiezas())
              .coches(cliente.getCoches())
                .dni(cliente.getDni())
                .imagen(cliente.getImagen())
              .build();
    }
}
