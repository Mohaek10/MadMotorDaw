package com.madmotor.apimadmotordaw.rest.pedidos.mappers;


import com.madmotor.apimadmotordaw.rest.clientes.Model.Cliente;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteInfoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.CreatePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.ResponsePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.UpdatePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.models.Pedido;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PedidoMapper {
    public Pedido toPedido(CreatePedidoDto createPedidoDto, ClienteInfoDto clientedto) {
        return Pedido.builder()
                .idUsuario(UUID.fromString(createPedidoDto.getIdUsuario()))
                .cliente(Cliente.builder()
                        .id(clientedto.getId())
                        .nombre(clientedto.getNombre())
                        .apellido(clientedto.getApellido())
                        .direccion(clientedto.getDireccion())
                        .codigoPostal(clientedto.getCodigoPostal())
                        .dni(clientedto.getDni())
                        .imagen(clientedto.getImagen())
                        .build())
                .lineasPedido(createPedidoDto.getLineasPedido())
                .build();
    }


    public Pedido toPedido(UpdatePedidoDto updatePedidoDto, Pedido pedido, ClienteInfoDto clientedto) {
        return Pedido.builder()
                .id(pedido.getId())
                .idUsuario(updatePedidoDto.getIdUsuario() != null ? UUID.fromString(updatePedidoDto.getIdUsuario()) : pedido.getIdUsuario())
                .cliente(clientedto != null ? Cliente.builder()
                        .id(clientedto.getId())
                        .nombre(clientedto.getNombre())
                        .apellido(clientedto.getApellido())
                        .direccion(clientedto.getDireccion())
                        .codigoPostal(clientedto.getCodigoPostal())
                        .dni(clientedto.getDni())
                        .imagen(clientedto.getImagen())
                        .build() : pedido.getCliente())
                .lineasPedido(updatePedidoDto.getLineasPedido() != null ? updatePedidoDto.getLineasPedido() : pedido.getLineasPedido())
                .build();
    }

    public ResponsePedidoDto toResponsePedidoDto(Pedido pedido) {
        return new ResponsePedidoDto(
                pedido.getId().toHexString(),
                pedido.getIdUsuario(),
                pedido.getCliente(),
                pedido.getLineasPedido(),
                pedido.getTotalItems(),
                pedido.getTotal(),
                pedido.getCreatedAt(),
                pedido.getUpdatedAt(),
                pedido.getIsDeleted()
        );
    }

}
