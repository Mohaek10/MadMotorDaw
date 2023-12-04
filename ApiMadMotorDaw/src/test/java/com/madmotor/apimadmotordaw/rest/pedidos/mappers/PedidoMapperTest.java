package com.madmotor.apimadmotordaw.rest.pedidos.mappers;

import com.madmotor.apimadmotordaw.rest.clientes.Model.Cliente;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteInfoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.CreatePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.ResponsePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.UpdatePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.models.ItemPedido;
import com.madmotor.apimadmotordaw.rest.pedidos.models.Pedido;
import org.apache.coyote.Response;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PedidoMapperTest {
    private final PedidoMapper pedidoMapper = new PedidoMapper();
    private final ItemPedido itemPedido = ItemPedido.builder()
            .idVehiculo(UUID.fromString("b3d4931d-c1c0-468b-a4b6-9814017a7339"))
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
    Cliente cliente = Cliente.builder()
            .id(UUID.fromString("b3d4931d-c1c0-468b-a4b6-9814017a7339"))
            .nombre("nombre")
            .apellido("apellido")
            .direccion("direccion")
            .codigoPostal(12345)
            .dni("dni")
            .imagen("imagen")
            .build();
    private final Pedido pedido = Pedido.builder()
            .id(new ObjectId())
            .idUsuario(UUID.fromString("b3d4931d-c1c0-468b-a4b6-9814017a7339"))
            .cliente(cliente)
            .lineasPedido(itemPedidoList)
            .build();

    @Test
    void toPedido() {
        CreatePedidoDto createPedidoDto = CreatePedidoDto.builder()
                .idUsuario("b3d4931d-c1c0-468b-a4b6-9814017a7339")
                .lineasPedido(itemPedidoList)
                .build();
        var res = pedidoMapper.toPedido(createPedidoDto,clienteInfoDto);

        assertAll(
                () -> assertEquals(createPedidoDto.getIdUsuario(), res.getIdUsuario().toString()),
                () -> assertEquals(createPedidoDto.getLineasPedido(), res.getLineasPedido())
        );


    }

    @Test
    void testToPedido() {
        var res = pedidoMapper.toPedido(updatePedidoDto,pedido,clienteInfoDto);

        assertAll(
                () -> assertEquals(updatePedidoDto.getIdUsuario(), res.getIdUsuario().toString()),
                () -> assertEquals(updatePedidoDto.getLineasPedido(), res.getLineasPedido())
        );
    }

    @Test
    void toResponsePedidoDto() {
        var res = pedidoMapper.toResponsePedidoDto(pedido);

        assertAll(
                () -> assertEquals(pedido.getId().toHexString(), res.getIdPedido()),
                () -> assertEquals(pedido.getIdUsuario().toString(), res.getIdUsuario().toString()),
                () -> assertEquals(pedido.getCliente(), res.getCliente()),
                () -> assertEquals(pedido.getLineasPedido(), res.getLineasPedido()),
                () -> assertEquals(pedido.getTotalItems(), res.getTotalItems()),
                () -> assertEquals(pedido.getTotal(), res.getTotal()),
                () -> assertEquals(pedido.getCreatedAt(), res.getCreatedAt()),
                () -> assertEquals(pedido.getUpdatedAt(), res.getUpdatedAt()),
                () -> assertEquals(pedido.getIsDeleted(), res.getIsDeleted())
        );
    }
}