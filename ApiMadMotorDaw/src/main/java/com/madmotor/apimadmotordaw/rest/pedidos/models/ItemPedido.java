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
    @Builder.Default
    private Integer cantidadVehiculos = 0;

    @Min(value = 0, message = "La cantidad de piezas no puede ser negativa")
    @Builder.Default
    private Integer cantidadPiezas = 0;

    private UUID idVehiculo;

    private UUID idPieza;

    @Min(value = 0, message = "El precio del producto no puede ser negativo")
    @Builder.Default
    private Double precioVehiculo=0.0;

    @Builder.Default
    private Double precioTotalVehiculo=0.0;

    @Min(value = 0, message = "El precio del producto no puede ser negativo")
    @Builder.Default
    private Double precioPieza = 0.0;

    @Builder.Default
    private Double precioTotalPieza=0.0;

    @Builder.Default
    private Double total = 0.0;




    public void setPrecioVehiculo(Double precioVehiculo) {
        this.precioTotalVehiculo = precioVehiculo;
        recalcularTotal();
    }

    public void setPrecioPieza(Double precioPieza) {
        this.precioTotalPieza = precioPieza;
        recalcularTotal();
    }


    private void recalcularTotal() {
        this.total =  this.precioTotalVehiculo + this.precioTotalPieza;
    }

}
