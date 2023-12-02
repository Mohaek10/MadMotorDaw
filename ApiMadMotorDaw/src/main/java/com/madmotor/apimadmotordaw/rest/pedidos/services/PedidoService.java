package com.madmotor.apimadmotordaw.rest.pedidos.services;

import com.madmotor.apimadmotordaw.rest.pedidos.dto.CreatePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.ResponsePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.UpdatePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.models.Pedido;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PedidoService {

    Page<ResponsePedidoDto> findAll(Optional<Boolean> isDeleted , Pageable pageable);

    ResponsePedidoDto findById(ObjectId idPedido);

    Page<ResponsePedidoDto> findByIdUsuario(String idUsuario, Pageable pageable);

    ResponsePedidoDto save(CreatePedidoDto pedido);

    ResponsePedidoDto update(UpdatePedidoDto pedido, ObjectId idPedido);

    void delete(ObjectId idPedido);


}
