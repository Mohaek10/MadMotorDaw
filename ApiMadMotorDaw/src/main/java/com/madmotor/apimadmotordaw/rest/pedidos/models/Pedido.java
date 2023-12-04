package com.madmotor.apimadmotordaw.rest.pedidos.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.madmotor.apimadmotordaw.rest.clientes.Model.Cliente;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("pedidos")
@TypeAlias("Pedido")
@EntityListeners(AuditingEntityListener.class)
public class Pedido {
    @Id
    @Builder.Default
    private ObjectId id = new ObjectId();

    @NotNull(message = "El id del usuario no puede ser nulo")
    private UUID idUsuario;

    @NotNull(message = "El cliente no puede ser nulo")
    private Cliente cliente;//CAMBIAR POR EL RESPONSE CUANDO SE ARREGLE

    @NotNull(message = "El pedido debe tener al menos un item de pedido")
    private List<ItemPedido> lineasPedido;

    @Builder.Default()
    private Integer totalItems = 0;

    @Builder.Default()
    private Double total = 0.0;

    @CreationTimestamp
    @Builder.Default()
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default()
    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder.Default()
    private Boolean isDeleted = false;

    @JsonProperty("id")
    public String get_id() {
        return id.toHexString();
    }

    public void setLineasPedido(List<ItemPedido> lineasPedido) {
        this.lineasPedido = lineasPedido;
        this.totalItems = lineasPedido != null ? lineasPedido.size() : 0;
        recalcularTotal();
    }

    private void recalcularTotal() {
        this.total = calcularTotal(lineasPedido);
    }

    private Double calcularTotal(List<ItemPedido> lineasPedido) {
        if (lineasPedido != null) {
            return lineasPedido.stream().mapToDouble(ItemPedido::getTotal).sum();
        } else {
            return 0.0;
        }
    }


}
