package com.madmotor.apimadmotordaw.rest.pedidos.repositories;

import com.madmotor.apimadmotordaw.rest.pedidos.models.Pedido;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface PedidoRepository extends MongoRepository<Pedido, ObjectId> {



    Page<Pedido> findByIdUsuario(UUID idUsuario, Pageable pageable);

    List<Pedido> findPedidosIdsByIdUsuario(UUID idUsuario);

    boolean existsByIdUsuario(UUID idUsuario);



}
