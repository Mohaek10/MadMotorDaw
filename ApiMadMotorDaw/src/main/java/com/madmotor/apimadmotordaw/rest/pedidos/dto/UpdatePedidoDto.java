package com.madmotor.apimadmotordaw.rest.pedidos.dto;

import com.madmotor.apimadmotordaw.rest.pedidos.models.ItemPedido;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UpdatePedidoDto {

    @NotNull(message = "El id del pedido no puede ser nulo")
    private String idPedido;

    @NotNull(message = "El id del usuario no puede ser nulo y tiene que ser un UUID")
    private UUID idUsuario;

    @NotNull(message = "Los items del pedido no pueden ser nulos")
    private List<ItemPedido> lineasPedido;



}
