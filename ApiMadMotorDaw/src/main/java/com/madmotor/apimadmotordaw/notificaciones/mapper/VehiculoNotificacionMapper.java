package com.madmotor.apimadmotordaw.notificaciones.mapper;


import com.madmotor.apimadmotordaw.notificaciones.dto.VehiculoNotificacionDto;
import com.madmotor.apimadmotordaw.vehiculos.models.Vehiculo;
import org.springframework.stereotype.Component;

@Component
public class VehiculoNotificacionMapper {
    public VehiculoNotificacionDto toVehiculoNotificacionDto(Vehiculo vehiculo) {
        return new VehiculoNotificacionDto(vehiculo.getId(), vehiculo.getMarca(), vehiculo.getModelo(), vehiculo.getYear(), vehiculo.getKm(), vehiculo.getPrecio(), vehiculo.getStock(), vehiculo.getImagen(), vehiculo.getDescripcion(), vehiculo.getCreatedAt().toString(), vehiculo.getUpdatedAt().toString(), vehiculo.getCategoria().getName(), vehiculo.getIsDeleted());
    }
}
