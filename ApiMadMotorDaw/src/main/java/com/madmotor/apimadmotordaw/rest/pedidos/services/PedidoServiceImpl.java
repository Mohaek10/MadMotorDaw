package com.madmotor.apimadmotordaw.rest.pedidos.services;

import com.madmotor.apimadmotordaw.rest.pedidos.dto.CreatePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.ResponsePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.UpdatePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.exceptions.PedidoDeUserNotFound;
import com.madmotor.apimadmotordaw.rest.pedidos.exceptions.PedidoNotFound;
import com.madmotor.apimadmotordaw.rest.pedidos.mappers.PedidoMapper;
import com.madmotor.apimadmotordaw.rest.pedidos.models.Pedido;
import com.madmotor.apimadmotordaw.rest.pedidos.repositories.PedidoRepository;
import com.madmotor.apimadmotordaw.rest.piezas.models.Pieza;
import com.madmotor.apimadmotordaw.rest.piezas.repositories.PiezaRepository;
import com.madmotor.apimadmotordaw.rest.piezas.services.PiezaService;
import com.madmotor.apimadmotordaw.rest.vehiculos.models.Vehiculo;
import com.madmotor.apimadmotordaw.rest.vehiculos.repositories.VehiculoRepository;
import com.madmotor.apimadmotordaw.rest.vehiculos.services.VehiculoService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@CacheConfig(cacheNames = {"pedidos"})
@Slf4j
public class PedidoServiceImpl implements PedidoService {
    private final PedidoRepository pedidoRepository;
    private final VehiculoRepository vehiculoRepository;
    private final PiezaRepository piezaRepository;
    private final PedidoMapper pedidoMapper;


    @Autowired
    public PedidoServiceImpl(PedidoRepository pedidoRepository, VehiculoRepository vehiculoRepository, PiezaRepository piezaRepository, PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.piezaRepository = piezaRepository;
        this.pedidoMapper = pedidoMapper;
    }

    @Override
    public Page<ResponsePedidoDto> findAll(Optional<Boolean> isDeleted, Pageable pageable) {
        log.info("Buscando todos los pedidos");
        return pedidoRepository.findAll(pageable).map(pedidoMapper::toResponsePedidoDto);
    }

    @Override
    public ResponsePedidoDto findById(ObjectId idPedido) {
        log.info("Buscando pedido con id: {}", idPedido);
        return pedidoRepository.findById(idPedido).map(pedidoMapper::toResponsePedidoDto)
                .orElseThrow(() -> new PedidoNotFound(idPedido.toHexString()));
    }

    @Override
    public Page<ResponsePedidoDto> findByIdUsuario(Long idUsuario, Pageable pageable) {
        return pedidoRepository.findByIdUsuario(idUsuario, pageable).map(pedidoMapper::toResponsePedidoDto);
    }

    @Override
    public ResponsePedidoDto save(CreatePedidoDto pedido) {
        return null;
    }

    @Override
    public ResponsePedidoDto update(UpdatePedidoDto pedido, ObjectId idPedido) {
        return null;
    }

    @Override
    public void delete(ObjectId idPedido) {

    }
}
