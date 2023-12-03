package com.madmotor.apimadmotordaw.rest.vehiculos.mapper;


import com.madmotor.apimadmotordaw.rest.categorias.models.Categoria;
import com.madmotor.apimadmotordaw.rest.vehiculos.dto.VehiculoCreateDto;
import com.madmotor.apimadmotordaw.rest.vehiculos.dto.VehiculoResponseDto;
import com.madmotor.apimadmotordaw.rest.vehiculos.dto.VehiculoUpdateDto;
import com.madmotor.apimadmotordaw.rest.vehiculos.models.Vehiculo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class VehiculoMapper {
    public Vehiculo toVevhiculo(VehiculoCreateDto dto, Categoria categoria){
        return new Vehiculo(
                null,
                dto.getMarca(),
                dto.getModelo(),
                dto.getYear(),
                dto.getKm(),
                dto.getPrecio(),
                dto.getStock(),
                dto.getImagen(),
                dto.getDescripcion(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                categoria,
                false
        );
    }
    public Vehiculo toVehiculo(VehiculoUpdateDto dto, Vehiculo vehiculo, Categoria categoria){
        return new Vehiculo(
                vehiculo.getId(),
                dto.getMarca() !=null ? dto.getMarca() : vehiculo.getMarca(),
                dto.getModelo() !=null ? dto.getModelo() : vehiculo.getModelo(),
                dto.getYear() !=null ? dto.getYear() : vehiculo.getYear(),
                dto.getKm() !=null ? dto.getKm() : vehiculo.getKm(),
                dto.getPrecio() !=null ? dto.getPrecio() : vehiculo.getPrecio(),
                dto.getStock() !=null ? dto.getStock() : vehiculo.getStock(),
                dto.getImagen() !=null ? dto.getImagen() : vehiculo.getImagen(),
                dto.getDescripcion() !=null ? dto.getDescripcion() : vehiculo.getDescripcion(),
                vehiculo.getCreatedAt(),
                LocalDateTime.now(),
                categoria,
                dto.getIsDeleted() !=null ? dto.getIsDeleted() : vehiculo.getIsDeleted()
        );
    }

    public VehiculoResponseDto toVehiculoResponseDto(Vehiculo vehiculo){
        return new VehiculoResponseDto(
                vehiculo.getId(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                vehiculo.getYear(),
                vehiculo.getKm(),
                vehiculo.getPrecio(),
                vehiculo.getStock(),
                vehiculo.getImagen(),
                vehiculo.getDescripcion(),
                vehiculo.getCreatedAt(),
                vehiculo.getUpdatedAt(),
                vehiculo.getCategoria().getName()
        );
    }


}
