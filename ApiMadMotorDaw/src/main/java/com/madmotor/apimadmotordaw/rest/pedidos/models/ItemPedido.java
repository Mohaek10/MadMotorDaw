package com.madmotor.apimadmotordaw.rest.pedidos.models;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedido {

    @Min(value = 0, message = "La cantidad de vehiculos no puede ser negativa ni 0")
    private Integer cantidadVehiculos = 0;

    @Min(value = 0, message = "La cantidad de piezas no puede ser negativa")
    private Integer cantidadPiezas = 0;

    @NotNull(message = "El id del vehiculo no puede ser nulo")
    private UUID idVehiculo;

    @NotNull(message = "El id de la pieza no puede ser nulo")
    private UUID idPieza;

    @Min(value = 0, message = "El precio del producto no puede ser negativo")
    @Builder.Default
    private Double precioVehiculo = 0.0;

    @Min(value = 0, message = "El precio del producto no puede ser negativo")
    @Builder.Default
    private Double precioPieza = 0.0;

    @Builder.Default
    private Double total = 0.0;

    public void setCantidadVehiculos(Integer cantidadVehiculos) {
        this.cantidadVehiculos = cantidadVehiculos;
        recalcularTotal();
    }

    public void setCantidadPiezas(Integer cantidadPiezas) {
        this.cantidadPiezas = cantidadPiezas;
        recalcularTotal();
    }

    public void setPrecioVehiculo(Double precioVehiculo) {
        this.precioVehiculo = precioVehiculo;
        recalcularTotal();
    }

    public void setPrecioPieza(Double precioPieza) {
        this.precioPieza = precioPieza;
        recalcularTotal();
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    private void recalcularTotal() {
        this.total = (this.cantidadVehiculos * this.precioVehiculo) + (this.cantidadPiezas * this.precioPieza);
    }

}
