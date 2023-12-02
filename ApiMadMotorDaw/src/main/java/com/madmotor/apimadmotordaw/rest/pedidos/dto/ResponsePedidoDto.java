package com.madmotor.apimadmotordaw.rest.pedidos.dto;


import com.madmotor.apimadmotordaw.rest.clientes.Model.Cliente;
import com.madmotor.apimadmotordaw.rest.pedidos.models.ItemPedido;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ResponsePedidoDto {
    private String idPedido;
    private UUID idUsuario;
    private Cliente cliente;
    List<ItemPedido> lineasPedido;
    private Integer totalItems;
    private Double total;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    private Boolean isDeleted;
}
