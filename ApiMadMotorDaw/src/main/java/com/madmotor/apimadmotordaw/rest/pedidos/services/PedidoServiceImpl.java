package com.madmotor.apimadmotordaw.rest.pedidos.services;

import com.madmotor.apimadmotordaw.rest.clientes.Model.Cliente;
import com.madmotor.apimadmotordaw.rest.clientes.Repository.ClienteRepository;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteInfoDto;
import com.madmotor.apimadmotordaw.rest.clientes.exceptions.ClienteNotFound;
import com.madmotor.apimadmotordaw.rest.clientes.mappers.ClienteMapper;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.CreatePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.ResponsePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.UpdatePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.exceptions.*;
import com.madmotor.apimadmotordaw.rest.pedidos.mappers.PedidoMapper;
import com.madmotor.apimadmotordaw.rest.pedidos.models.ItemPedido;
import com.madmotor.apimadmotordaw.rest.pedidos.models.Pedido;
import com.madmotor.apimadmotordaw.rest.pedidos.repositories.PedidoRepository;
import com.madmotor.apimadmotordaw.rest.piezas.models.Pieza;
import com.madmotor.apimadmotordaw.rest.piezas.repositories.PiezaRepository;
import com.madmotor.apimadmotordaw.rest.vehiculos.models.Vehiculo;
import com.madmotor.apimadmotordaw.rest.vehiculos.repositories.VehiculoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = {"pedidos"})
@Slf4j
public class PedidoServiceImpl implements PedidoService {
    private final PedidoRepository pedidoRepository;
    private final VehiculoRepository vehiculoRepository;
    private final PiezaRepository piezaRepository;
    private final PedidoMapper pedidoMapper;
    private final ClienteMapper clienteMapper;

    private final ClienteRepository clienteRepository;


    @Autowired
    public PedidoServiceImpl(PedidoRepository pedidoRepository, VehiculoRepository vehiculoRepository, PiezaRepository piezaRepository, PedidoMapper pedidoMapper, ClienteMapper clienteMapper, ClienteRepository clienteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.piezaRepository = piezaRepository;
        this.pedidoMapper = pedidoMapper;
        this.clienteMapper = clienteMapper;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Page<ResponsePedidoDto> findAll(Pageable pageable) {
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
    public Page<ResponsePedidoDto> findByIdUsuario(String idUsuario, Pageable pageable) {
        try {
            var miUuid = UUID.fromString(idUsuario);
            return pedidoRepository.findByIdUsuario(miUuid, pageable).map(pedidoMapper::toResponsePedidoDto);
        } catch (IllegalArgumentException e) {
            throw new PedidoDeUserNotFound(idUsuario);
        }

    }

    @Override
    @Transactional
    public ResponsePedidoDto save(CreatePedidoDto pedido) {
        log.info("Creando pedido");
        Optional<Cliente> cl = Optional.ofNullable(clienteRepository.findById(UUID.fromString(pedido.getIdUsuario())).orElseThrow(() -> new ClienteNotFound(pedido.getIdUsuario())));
        ClienteInfoDto clienteInfoDto = clienteMapper.toClienteInfoDto(cl.get());

        Pedido pedidoToSave = pedidoMapper.toPedido(pedido, clienteInfoDto);
        checkPedido(pedidoToSave);
        pedidoToSave = reservarStock(pedidoToSave);
        pedidoToSave.setCreatedAt(LocalDateTime.now());
        pedidoToSave.setUpdatedAt(LocalDateTime.now());


        return pedidoMapper.toResponsePedidoDto(pedidoRepository.save(pedidoToSave));
    }

    @Override
    @Transactional
    public ResponsePedidoDto update(UpdatePedidoDto updatePedidoDto, ObjectId idPedido) {

        Pedido pedidoToUpdate = pedidoRepository.findById(idPedido).orElseThrow(() -> new PedidoNotFound(idPedido.toHexString()));

        returnPedidoItems(pedidoToUpdate);

        Optional<Cliente> cl = Optional.ofNullable(clienteRepository.findById(UUID.fromString(updatePedidoDto.getIdUsuario())).orElseThrow(() -> new ClienteNotFound(updatePedidoDto.getIdUsuario())));
        ClienteInfoDto clienteInfoDto = clienteMapper.toClienteInfoDto(cl.get());

        Pedido pedidoToSave = pedidoMapper.toPedido(updatePedidoDto, pedidoToUpdate, clienteInfoDto);

        returnPedidoItems(pedidoToSave);
        checkPedido(pedidoToSave);
        pedidoToSave = reservarStock(pedidoToSave);
        pedidoToSave.setUpdatedAt(LocalDateTime.now());
        return pedidoMapper.toResponsePedidoDto(pedidoRepository.save(pedidoToSave));
    }

    @Override
    public void delete(ObjectId idPedido) {
        log.info("Borrando pedido con id: {}", idPedido);
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(() -> new PedidoNotFound(idPedido.toHexString()));
        returnPedidoItems(pedido);
        pedidoRepository.deleteById(idPedido);
    }

    void checkPedido(Pedido pedido) {
        for (ItemPedido itemPedido : pedido.getLineasPedido()) {

            if (itemPedido.getIdVehiculo() != null) {
                validateExistenceAndStockVehiculo(itemPedido.getIdVehiculo(), itemPedido.getCantidadVehiculos(), itemPedido.getPrecioVehiculo());
            } else if (itemPedido.getIdPieza() != null) {
                validateExistenceAndStockPieza(itemPedido.getIdPieza(), itemPedido.getCantidadPiezas(), itemPedido.getPrecioPieza());
            } else {
                throw new NoItemsValid("El pedido no tiene ni piezas ni vehículos o no son validos");
            }
        }

    }

    // Métodos de validación específicos para vehículos y piezas
    private void validateExistenceAndStockVehiculo(UUID idVehiculo, Integer cantidad, Double precio) {
        Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo).orElseThrow(() -> new VehiculoNotFound(idVehiculo.toString()));
        if (vehiculo.getStock() < cantidad) {
            throw new NoStockForVehiculos(idVehiculo.toString(), cantidad, vehiculo.getStock());
        }
    }

    private void validateExistenceAndStockPieza(UUID idPieza, Integer cantidad, Double precio) {
        Pieza pieza = piezaRepository.findById(idPieza).orElseThrow(() -> new PiezaNotFound(idPieza.toString()));
        if (pieza.getStock() < cantidad) {
            throw new NoStockForPiezas(idPieza.toString(), cantidad, pieza.getStock());
        }
    }


    Pedido reservarStock(Pedido pedido) {
        log.info("Reservando stock del pedido: {}", pedido);

        if (pedido.getLineasPedido() == null || pedido.getLineasPedido().isEmpty()) {
            throw new NoItemsValid("El pedido no tiene items de pedido");
        }

        for (ItemPedido lineaPedido : pedido.getLineasPedido()) {
            if (lineaPedido.getIdVehiculo() != null) {
                reservarStockVehiculo(lineaPedido.getIdVehiculo(), lineaPedido.getCantidadVehiculos());
            } else if (lineaPedido.getIdPieza() != null) {
                reservarStockPieza(lineaPedido.getIdPieza(), lineaPedido.getCantidadPiezas());
            } else {
                throw new NoItemsValid("El pedido no tiene ni piezas ni vehículos o no son válidos");
            }
        }

        return pedido;
    }

    private void reservarStockVehiculo(UUID idVehiculo, Integer cantidad) {
        Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo).orElseThrow(() -> new VehiculoNotFound(idVehiculo.toString()));
        if (vehiculo.getStock() < cantidad) {
            throw new NoStockForVehiculos(idVehiculo.toString(), cantidad, vehiculo.getStock());
        }
        vehiculo.setStock(vehiculo.getStock() - cantidad);
        vehiculoRepository.save(vehiculo);
    }

    private void reservarStockPieza(UUID idPieza, Integer cantidad) {
        Pieza pieza = piezaRepository.findById(idPieza).orElseThrow(() -> new PiezaNotFound(idPieza.toString()));
        if (pieza.getStock() < cantidad) {
            throw new NoStockForPiezas(idPieza.toString(), cantidad, pieza.getStock());
        }
        pieza.setStock(pieza.getStock() - cantidad);
        piezaRepository.save(pieza);
    }

    Pedido returnPedidoItems(Pedido originalPedido) {
        log.info("Devolviendo items al stock del pedido original: {}", originalPedido);



        for (ItemPedido lineaPedido : originalPedido.getLineasPedido()) {
            if (lineaPedido.getIdVehiculo() != null) {
                returnStockVehiculo(lineaPedido.getIdVehiculo(), lineaPedido.getCantidadVehiculos());
            } else if (lineaPedido.getIdPieza() != null) {
                returnStockPieza(lineaPedido.getIdPieza(), lineaPedido.getCantidadPiezas());
            }
        }

        return originalPedido;
    }

    private void returnStockVehiculo(UUID idVehiculo, Integer cantidad) {
        Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo).get();
        vehiculo.setStock(vehiculo.getStock() + cantidad);
        vehiculoRepository.save(vehiculo);
    }

    private void returnStockPieza(UUID idPieza, Integer cantidad) {
        Pieza pieza = piezaRepository.findById(idPieza).orElseThrow(() -> new PiezaNotFound(idPieza.toString()));
        pieza.setStock(pieza.getStock() + cantidad);
        piezaRepository.save(pieza);
    }
}


