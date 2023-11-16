package com.madmotor.apimadmotordaw.vehiculos.mapper;

import com.madmotor.apimadmotordaw.categorias.models.Categoria;
import com.madmotor.apimadmotordaw.vehiculos.dto.VehiculoCreateDto;
import com.madmotor.apimadmotordaw.vehiculos.dto.VehiculoUpdateDto;
import com.madmotor.apimadmotordaw.vehiculos.models.Vehiculo;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VehiculoMapperTest {
    private final Categoria categoria = new Categoria(1L, "TODOTERRENO", LocalDateTime.now(), LocalDateTime.now(), false);

    private final VehiculoMapper vehiculoMapper = new VehiculoMapper();

    @Test
    void toVevhiculo() {
        VehiculoCreateDto vehiculoCreateDto = new VehiculoCreateDto(
                "marca",
                "modelo",
                2021,
                1000.0,
                10000.0,
                10,
                "imagen",
                "descripcion",
                categoria.getName()
        );
        var res = vehiculoMapper.toVevhiculo(vehiculoCreateDto, categoria);
        assertAll(
                () -> assertEquals(vehiculoCreateDto.getMarca(), res.getMarca()),
                () -> assertEquals(vehiculoCreateDto.getModelo(), res.getModelo()),
                () -> assertEquals(vehiculoCreateDto.getYear(), res.getYear()),
                () -> assertEquals(vehiculoCreateDto.getKm(), res.getKm()),
                () -> assertEquals(vehiculoCreateDto.getPrecio(), res.getPrecio()),
                () -> assertEquals(vehiculoCreateDto.getStock(), res.getStock()),
                () -> assertEquals(vehiculoCreateDto.getImagen(), res.getImagen()),
                () -> assertEquals(vehiculoCreateDto.getDescripcion(), res.getDescripcion()),
                () -> assertEquals(vehiculoCreateDto.getCategoria(), res.getCategoria().getName()),
                () -> assertEquals(false, res.getIsDeleted())
        );
    }

    @Test
    void toVehiculo() {
        UUID uuid = UUID.randomUUID();
        VehiculoUpdateDto vehiculoUpdateDto = new VehiculoUpdateDto(
                "Ferrari",
                "F40",
                1990,
                1000.0,
                10000.0,
                10,
                "imagen",
                "descripcion",
                categoria.getName(),
                false
        );
        Vehiculo vehiculo = new Vehiculo(
                uuid,
                vehiculoUpdateDto.getMarca(),
                vehiculoUpdateDto.getModelo(),
                vehiculoUpdateDto.getYear(),
                vehiculoUpdateDto.getKm(),
                vehiculoUpdateDto.getPrecio(),
                vehiculoUpdateDto.getStock(),
                vehiculoUpdateDto.getImagen(),
                vehiculoUpdateDto.getDescripcion(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                categoria,
                false
        );
        var res = vehiculoMapper.toVehiculo(vehiculoUpdateDto, vehiculo, categoria);
        assertAll(
                () -> assertEquals(uuid, res.getId()),
                () -> assertEquals(vehiculoUpdateDto.getMarca(), res.getMarca()),
                () -> assertEquals(vehiculoUpdateDto.getModelo(), res.getModelo()),
                () -> assertEquals(vehiculoUpdateDto.getYear(), res.getYear()),
                () -> assertEquals(vehiculoUpdateDto.getKm(), res.getKm()),
                () -> assertEquals(vehiculoUpdateDto.getPrecio(), res.getPrecio()),
                () -> assertEquals(vehiculoUpdateDto.getStock(), res.getStock()),
                () -> assertEquals(vehiculoUpdateDto.getImagen(), res.getImagen()),
                () -> assertEquals(vehiculoUpdateDto.getDescripcion(), res.getDescripcion()),
                () -> assertEquals(vehiculoUpdateDto.getCategoria(), res.getCategoria().getName()),
                () -> assertEquals(vehiculoUpdateDto.getIsDeleted(), res.getIsDeleted()
                ));
    }

    @Test
    void toVehiculoResponseDto() {
        Vehiculo vehiculo=new Vehiculo(
                UUID.randomUUID(),
                "Ferrari",
                "F40",
                1990,
                1000.0,
                10000.0,
                10,
                "imagen",
                "descripcion",
                LocalDateTime.now(),
                LocalDateTime.now(),
                categoria,
                false
        );
        var res = vehiculoMapper.toVehiculoResponseDto(vehiculo);
        assertAll(
                () -> assertEquals(vehiculo.getId(), res.getId()),
                () -> assertEquals(vehiculo.getMarca(), res.getMarca()),
                () -> assertEquals(vehiculo.getModelo(), res.getModelo()),
                () -> assertEquals(vehiculo.getYear(), res.getYear()),
                () -> assertEquals(vehiculo.getKm(), res.getKm()),
                () -> assertEquals(vehiculo.getPrecio(), res.getPrecio()),
                () -> assertEquals(vehiculo.getStock(), res.getStock()),
                () -> assertEquals(vehiculo.getImagen(), res.getImagen()),
                () -> assertEquals(vehiculo.getDescripcion(), res.getDescripcion()),
                () -> assertEquals(vehiculo.getCategoria().getName(), res.getCategoria())
        );

    }
}