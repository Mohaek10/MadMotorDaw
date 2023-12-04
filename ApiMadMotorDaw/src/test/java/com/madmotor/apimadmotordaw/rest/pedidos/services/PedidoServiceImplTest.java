package com.madmotor.apimadmotordaw.rest.pedidos.services;

import com.madmotor.apimadmotordaw.rest.categorias.models.Categoria;
import com.madmotor.apimadmotordaw.rest.clientes.Model.Cliente;
import com.madmotor.apimadmotordaw.rest.clientes.Repository.ClienteRepository;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteCreateRequest;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteInfoDto;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteReponse;
import com.madmotor.apimadmotordaw.rest.clientes.mappers.ClienteMapper;
import com.madmotor.apimadmotordaw.rest.clientes.services.ClienteServiceImpl;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.CreatePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.ResponsePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.UpdatePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.mappers.PedidoMapper;
import com.madmotor.apimadmotordaw.rest.pedidos.models.ItemPedido;
import com.madmotor.apimadmotordaw.rest.pedidos.models.Pedido;
import com.madmotor.apimadmotordaw.rest.pedidos.repositories.PedidoRepository;
import com.madmotor.apimadmotordaw.rest.piezas.models.Pieza;
import com.madmotor.apimadmotordaw.rest.piezas.repositories.PiezaRepository;
import com.madmotor.apimadmotordaw.rest.vehiculos.models.Vehiculo;
import com.madmotor.apimadmotordaw.rest.vehiculos.repositories.VehiculoRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private ClienteMapper clienteMapper;


    @Mock
    private PedidoMapper pedidoMapper;
    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private VehiculoRepository vehiculoRepository;
    @Mock
    private PiezaRepository piezaRepository;

    @InjectMocks
    private PedidoServiceImpl pedidoService;
    @InjectMocks
    private ClienteServiceImpl clienteService;
    @Mock
    private ClienteRepository clienteRepository;
    private final Categoria categoria = new Categoria(1L, "Electrico", LocalDateTime.now(), LocalDateTime.now(), false);

    private final Vehiculo vehiculo1 = new Vehiculo(UUID.fromString("f5d2c8a0-0b7e-4b1a-8b1a-6b9b8b3b4b4b"), "Tesla", "Model 3", 2020, 0.0, 50000.0, 10, "https://www.tesla.com/sites/default/files/modelsx-new/social/model-x-social.jpg", "El mejor coche electrico", LocalDateTime.now(), LocalDateTime.now(), categoria, false);
    private final Pieza pieza1 = Pieza.builder()
            .id(UUID.randomUUID())
            .name("name1")
            .description("description1")
            .price(1.0)
            .stock(1)
            .image("image1")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    ClienteReponse clienteReponse = ClienteReponse.builder()
            .id(UUID.fromString("b3d4931d-c1c0-468b-a4b6-9814017a7339"))
            .nombre("nombre")
            .apellido("apellido")
            .direccion("direccion")
            .codigoPostal(12345)
            .dni("dni")
            .imagen("imagen")
            .build();
    Cliente cliente = Cliente.builder()
            .id(UUID.fromString("b3d4931d-c1c0-468b-a4b6-9814017a7339"))
            .nombre("nombre")
            .apellido("apellido")
            .direccion("direccion")
            .codigoPostal(12345)
            .dni("dni")
            .imagen("imagen")
            .build();
    private final ItemPedido itemPedido = ItemPedido.builder()
            .idVehiculo(UUID.fromString("f5d2c8a0-0b7e-4b1a-8b1a-6b9b8b3b4b4b"))
            .idPieza(UUID.fromString("b3d4931d-c1c0-468b-a4b6-9814017a7339"))
            .cantidadVehiculos(1)
            .cantidadPiezas(1)
            .precioVehiculo(1000.0)
            .precioPieza(1000.0)
            .build();
    private final List<ItemPedido> itemPedidoList = List.of(itemPedido);
    private final ClienteInfoDto clienteInfoDto = ClienteInfoDto.builder()
            .id(UUID.fromString("b3d4931d-c1c0-468b-a4b6-9814017a7339"))
            .nombre("nombre")
            .apellido("apellido")
            .direccion("direccion")
            .codigoPostal(12345)
            .dni("dni")
            .imagen("imagen")
            .build();
    private final UpdatePedidoDto updatePedidoDto = UpdatePedidoDto.builder()
            .idUsuario("b3d4931d-c1c0-468b-a4b6-9814017a7339")
            .lineasPedido(itemPedidoList)
            .build();
    Pedido pedido1 = Pedido.builder()
            .id(new ObjectId("656d1fe7b4c9d3a94f0d13c1"))
            .idUsuario(UUID.fromString("b3d4931d-c1c0-468b-a4b6-9814017a7339"))
            .cliente(cliente)
            .lineasPedido(itemPedidoList)
            .build();
    Pedido pedido2 = Pedido.builder()
            .id(new ObjectId("656d1fd7b4c9d3a94f0d13c0"))
            .idUsuario(UUID.fromString("b3d4931d-c1c0-468b-a4b6-9814017a7339"))
            .cliente(cliente)
            .lineasPedido(itemPedidoList)
            .build();
    CreatePedidoDto createPedidoDto = CreatePedidoDto.builder()
            .idUsuario("b3d4931d-c1c0-468b-a4b6-9814017a7339")
            .lineasPedido(itemPedidoList)
            .build();
    ClienteCreateRequest cl = ClienteCreateRequest.builder()
            .nombre("nombre")
            .apellido("apellido")
            .direccion("direccion")
            .codigoPostal(12345)
            .dni("dni")
            .imagen("imagen")
            .build();


    @Test
    void findAll() {


        List<Pedido> pedidos = List.of(pedido1, pedido2);
        Page<Pedido> expectedPage = new PageImpl<>(pedidos);
        Pageable pageable = PageRequest.of(0, 10);

        when(pedidoRepository.findAll(pageable)).thenReturn(expectedPage);
        when(pedidoMapper.toResponsePedidoDto(pedido1)).thenReturn(new ResponsePedidoDto(
                pedido1.getId().toHexString(),
                pedido1.getIdUsuario(),
                pedido1.getCliente(),
                pedido1.getLineasPedido(),
                pedido1.getTotalItems(),
                pedido1.getTotal(),
                pedido1.getCreatedAt(),
                pedido1.getUpdatedAt(),
                pedido1.getIsDeleted()
        ));
        when(pedidoMapper.toResponsePedidoDto(pedido2)).thenReturn(new ResponsePedidoDto(
                pedido2.getId().toHexString(),
                pedido2.getIdUsuario(),
                pedido2.getCliente(),
                pedido2.getLineasPedido(),
                pedido2.getTotalItems(),
                pedido2.getTotal(),
                pedido2.getCreatedAt(),
                pedido2.getUpdatedAt(),
                pedido2.getIsDeleted()
        ));

        Page<ResponsePedidoDto> res = pedidoService.findAll(pageable);

        assertAll(
                () -> assertEquals(expectedPage.getTotalElements(), res.getTotalElements()),
                () -> assertEquals(expectedPage.getTotalPages(), res.getTotalPages()),
                () -> assertEquals(expectedPage.getNumber(), res.getNumber()),
                () -> assertEquals(expectedPage.getContent().get(0).getIdUsuario(), res.getContent().get(0).getIdUsuario()),
                () -> assertEquals(expectedPage.getContent().get(0).getCliente(), res.getContent().get(0).getCliente()),
                () -> assertEquals(expectedPage.getContent().get(0).getLineasPedido(), res.getContent().get(0).getLineasPedido()),
                () -> assertEquals(expectedPage.getContent().get(0).getTotalItems(), res.getContent().get(0).getTotalItems()),
                () -> assertEquals(expectedPage.getContent().get(0).getTotal(), res.getContent().get(0).getTotal()),
                () -> assertEquals(expectedPage.getContent().get(0).getCreatedAt(), res.getContent().get(0).getCreatedAt()),
                () -> assertEquals(expectedPage.getContent().get(0).getUpdatedAt(), res.getContent().get(0).getUpdatedAt()),
                () -> assertEquals(expectedPage.getContent().get(0).getIsDeleted(), res.getContent().get(0).getIsDeleted()),
                () -> assertEquals(expectedPage.getContent().get(1).getIdUsuario(), res.getContent().get(1).getIdUsuario()),
                () -> assertEquals(expectedPage.getContent().get(1).getCliente(), res.getContent().get(1).getCliente()),
                () -> assertEquals(expectedPage.getContent().get(1).getLineasPedido(), res.getContent().get(1).getLineasPedido()),
                () -> assertEquals(expectedPage.getContent().get(1).getTotalItems(), res.getContent().get(1).getTotalItems()),
                () -> assertEquals(expectedPage.getContent().get(1).getTotal(), res.getContent().get(1).getTotal()),
                () -> assertEquals(expectedPage.getContent().get(1).getCreatedAt(), res.getContent().get(1).getCreatedAt()),
                () -> assertEquals(expectedPage.getContent().get(1).getUpdatedAt(), res.getContent().get(1).getUpdatedAt()),
                () -> assertEquals(expectedPage.getContent().get(1).getIsDeleted(), res.getContent().get(1).getIsDeleted())
        );

        verify(pedidoRepository, times(1)).findAll(pageable);


    }

    @Test
    void findById() {
        when(pedidoRepository.findById(new ObjectId("656d1fe7b4c9d3a94f0d13c1"))).thenReturn(Optional.of(pedido1));
        when(pedidoMapper.toResponsePedidoDto(pedido1)).thenReturn(new ResponsePedidoDto(
                pedido1.getId().toHexString(),
                pedido1.getIdUsuario(),
                pedido1.getCliente(),
                pedido1.getLineasPedido(),
                pedido1.getTotalItems(),
                pedido1.getTotal(),
                pedido1.getCreatedAt(),
                pedido1.getUpdatedAt(),
                pedido1.getIsDeleted()
        ));

        ResponsePedidoDto res = pedidoService.findById(new ObjectId("656d1fe7b4c9d3a94f0d13c1"));

        assertAll(
                () -> assertEquals(pedido1.getId().toHexString(), res.getIdPedido()),
                () -> assertEquals(pedido1.getIdUsuario(), res.getIdUsuario()),
                () -> assertEquals(pedido1.getCliente(), res.getCliente()),
                () -> assertEquals(pedido1.getLineasPedido(), res.getLineasPedido()),
                () -> assertEquals(pedido1.getTotalItems(), res.getTotalItems()),
                () -> assertEquals(pedido1.getTotal(), res.getTotal()),
                () -> assertEquals(pedido1.getCreatedAt(), res.getCreatedAt()),
                () -> assertEquals(pedido1.getUpdatedAt(), res.getUpdatedAt()),
                () -> assertEquals(pedido1.getIsDeleted(), res.getIsDeleted())
        );

        verify(pedidoRepository, times(1)).findById(new ObjectId("656d1fe7b4c9d3a94f0d13c1"));
    }

    @Test
    void findByIdUsuario() {
        List<Pedido> pedidos = List.of(pedido1, pedido2);
        Page<Pedido> expectedPage = new PageImpl<>(pedidos);
        Pageable pageable = PageRequest.of(0, 10);

        when(pedidoRepository.findByIdUsuario(UUID.fromString("b3d4931d-c1c0-468b-a4b6-9814017a7339"), pageable)).thenReturn(expectedPage);
        when(pedidoMapper.toResponsePedidoDto(pedido1)).thenReturn(new ResponsePedidoDto(
                pedido1.getId().toHexString(),
                pedido1.getIdUsuario(),
                pedido1.getCliente(),
                pedido1.getLineasPedido(),
                pedido1.getTotalItems(),
                pedido1.getTotal(),
                pedido1.getCreatedAt(),
                pedido1.getUpdatedAt(),
                pedido1.getIsDeleted()
        ));
        when(pedidoMapper.toResponsePedidoDto(pedido2)).thenReturn(new ResponsePedidoDto(
                pedido2.getId().toHexString(),
                pedido2.getIdUsuario(),
                pedido2.getCliente(),
                pedido2.getLineasPedido(),
                pedido2.getTotalItems(),
                pedido2.getTotal(),
                pedido2.getCreatedAt(),
                pedido2.getUpdatedAt(),
                pedido2.getIsDeleted()
        ));

        Page<ResponsePedidoDto> res = pedidoService.findByIdUsuario(("b3d4931d-c1c0-468b-a4b6-9814017a7339"), pageable);

        assertAll(
                () -> assertEquals(expectedPage.getTotalElements(), res.getTotalElements()),
                () -> assertEquals(expectedPage.getTotalPages(), res.getTotalPages()),
                () -> assertEquals(expectedPage.getNumber(), res.getNumber()),
                () -> assertEquals(expectedPage.getContent().get(0).getIdUsuario(), res.getContent().get(0).getIdUsuario()),
                () -> assertEquals(expectedPage.getContent().get(0).getCliente(), res.getContent().get(0).getCliente()),
                () -> assertEquals(expectedPage.getContent().get(0).getLineasPedido(), res.getContent().get(0).getLineasPedido()),
                () -> assertEquals(expectedPage.getContent().get(0).getTotalItems(), res.getContent().get(0).getTotalItems()),
                () -> assertEquals(expectedPage.getContent().get(0).getTotal(), res.getContent().get(0).getTotal()),
                () -> assertEquals(expectedPage.getContent().get(0).getCreatedAt(), res.getContent().get(0).getCreatedAt()),
                () -> assertEquals(expectedPage.getContent().get(0).getUpdatedAt(), res.getContent().get(0).getUpdatedAt()),
                () -> assertEquals(expectedPage.getContent().get(0).getIsDeleted(), res.getContent().get(0).getIsDeleted()),
                () -> assertEquals(expectedPage.getContent().get(1).getIdUsuario(), res.getContent().get(1).getIdUsuario()),
                () -> assertEquals(expectedPage.getContent().get(1).getCliente(), res.getContent().get(1).getCliente()),
                () -> assertEquals(expectedPage.getContent().get(1).getLineasPedido(), res.getContent().get(1).getLineasPedido()),
                () -> assertEquals(expectedPage.getContent().get(1).getTotalItems(), res.getContent().get(1).getTotalItems()),
                () -> assertEquals(expectedPage.getContent().get(1).getTotal(), res.getContent().get(1).getTotal()),
                () -> assertEquals(expectedPage.getContent().get(1).getCreatedAt(), res.getContent().get(1).getCreatedAt()),
                () -> assertEquals(expectedPage.getContent().get(1).getUpdatedAt(), res.getContent().get(1).getUpdatedAt()),
                () -> assertEquals(expectedPage.getContent().get(1).getIsDeleted(), res.getContent().get(1).getIsDeleted()));
        verify(pedidoRepository, times(1)).findByIdUsuario(UUID.fromString("b3d4931d-c1c0-468b-a4b6-9814017a7339"), pageable);
    }



//    @Test
//    void update() {
//        when(pedidoRepository.findById(new ObjectId("656d1fe7b4c9d3a94f0d13c1"))).thenReturn(Optional.of(pedido1));
//        when(pedidoMapper.toPedido(updatePedidoDto, pedido1, clienteInfoDto)).thenReturn(pedido1);
//        when(pedidoRepository.save(pedido1)).thenReturn(pedido1);
//        when(pedidoMapper.toResponsePedidoDto(pedido1)).thenReturn(new ResponsePedidoDto(
//                pedido1.getId().toHexString(),
//                pedido1.getIdUsuario(),
//                pedido1.getCliente(),
//                pedido1.getLineasPedido(),
//                pedido1.getTotalItems(),
//                pedido1.getTotal(),
//                pedido1.getCreatedAt(),
//                pedido1.getUpdatedAt(),
//                pedido1.getIsDeleted()
//        ));
//        try{
//            ResponsePedidoDto res = pedidoService.update(updatePedidoDto, new ObjectId("656d1fe7b4c9d3a94f0d13c1"));
//
//        }catch (Exception e){
//
//        }
//
//
//        assertAll(
//                () -> assertEquals(pedido1.getId().toHexString(), res.getIdPedido()),
//                () -> assertEquals(pedido1.getIdUsuario(), res.getIdUsuario()),
//                () -> assertEquals(pedido1.getCliente(), res.getCliente()),
//                () -> assertEquals(pedido1.getLineasPedido(), res.getLineasPedido()),
//                () -> assertEquals(pedido1.getTotalItems(), res.getTotalItems()),
//                () -> assertEquals(pedido1.getTotal(), res.getTotal()),
//                () -> assertEquals(pedido1.getCreatedAt(), res.getCreatedAt()),
//                () -> assertEquals(pedido1.getUpdatedAt(), res.getUpdatedAt()),
//                () -> assertEquals(pedido1.getIsDeleted(), res.getIsDeleted())
//        );
//
//        verify(pedidoRepository, times(1)).save(pedido1);
//    }

//    @Test
//    void delete() {
//        ObjectId idPedido = new ObjectId();
//        Pedido pedidoToDelete = new Pedido();
//        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(pedidoToDelete));
//
//
//        // Act
//        pedidoService.delete(idPedido);
//
//
//        // Verify
//        verify(pedidoRepository).findById(idPedido);
//        verify(pedidoRepository).deleteById(idPedido);
//
//
//    }

    @Test
    void checkPedido() {


    }

    @Test
    void reservarStock() {
    }

    @Test
    void returnPedidoItems() {
    }
}