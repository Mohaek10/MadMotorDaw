package com.madmotor.apimadmotordaw.rest.vehiculos.controller;

import com.madmotor.apimadmotordaw.rest.categorias.models.Categoria;
import com.madmotor.apimadmotordaw.rest.vehiculos.controller.VehiculoRestController;
import com.madmotor.apimadmotordaw.utils.pagination.PageResponse;
import com.madmotor.apimadmotordaw.rest.vehiculos.dto.VehiculoCreateDto;
import com.madmotor.apimadmotordaw.rest.vehiculos.dto.VehiculoUpdateDto;
import com.madmotor.apimadmotordaw.rest.vehiculos.models.Vehiculo;
import com.madmotor.apimadmotordaw.rest.vehiculos.services.VehiculoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehiculoTestControllerMock {
    private final Categoria categoria = new Categoria(1L, "Camion", LocalDateTime.now(), LocalDateTime.now(), false);
    private final Vehiculo vehiculo1 = new Vehiculo(UUID.randomUUID(), "Mercedes", "Actros", 2019, 100000.0, 100000.0, 100, "https://loremflickr.com/150/150", "Camion de 3 ejes", LocalDateTime.now(), LocalDateTime.now(), categoria, false);
    private final Vehiculo vehiculo2 = new Vehiculo(UUID.randomUUID(), "TruckLeganes", "Track", 1999, 20.0, 300000.0, 2, "https://loremflickr.com/150/150", "Camion de 5 ejes", LocalDateTime.now(), LocalDateTime.now(), categoria, false);


    @InjectMocks
    private VehiculoRestController vehiculoController;

    @Mock
    private VehiculoService vehiculoService;

    @Test
    void getAllVehiculos() {
        Page<Vehiculo> mockPage = mock(Page.class);
        when(mockPage.getContent()).thenReturn(List.of(vehiculo1, vehiculo2));
        when(vehiculoService.findAll(any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(mockPage);

        ResponseEntity<PageResponse<Vehiculo>> responseEntity = vehiculoController.getAllVehiculos(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                0, 10, "id", "asc"
        );

        verify(vehiculoService, times(1)).findAll(any(), any(), any(), any(), any(), any(), any(), any(), any());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, responseEntity.getBody().content().size());
    }

    @Test
    void getVehiculoById() {
        // Creación de un UUID para la prueba
        UUID id = UUID.randomUUID();

        // Mock de la respuesta del servicio
        Vehiculo vehiculo = new Vehiculo();
        when(vehiculoService.findById(id.toString())).thenReturn(vehiculo);

        // Ejecución del método del controlador
        ResponseEntity<Vehiculo> responseEntity = vehiculoController.getVehiculoById(id);

        // Verificación de resultados
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void createVehiculo() {
        // Mock de la respuesta del servicio
        VehiculoCreateDto vehiculoDto = new VehiculoCreateDto(
                "CocheNuevoHDFull", "LoUltimo", 2030, 0.0, 100000.0, 100, "https://loremflickr.com/150/150", "Camion de 3 ejes", "Camion"
        );
        Vehiculo vehiculo = new Vehiculo();
        when(vehiculoService.save(any(VehiculoCreateDto.class))).thenReturn(vehiculo);

        // Ejecución del método del controlador
        ResponseEntity<Vehiculo> responseEntity = vehiculoController.createVehiculo(vehiculoDto);

        // Verificación de resultados
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void updateVehiculo() {
        // Creación de un UUID para la prueba
        UUID id = UUID.randomUUID();

        // Mock de la respuesta del servicio
        VehiculoUpdateDto vehiculoDto = new VehiculoUpdateDto("NuevoMercedesActualizado", "Actros", 2019, 100000.0, 100000.0, 100, "https://loremflickr.com/150/150", "Camion de 3 ejes", "Camion", false);
        Vehiculo vehiculo = new Vehiculo();
        when(vehiculoService.update(eq(id.toString()), any(VehiculoUpdateDto.class))).thenReturn(vehiculo);

        // Ejecución del método del controlador
        ResponseEntity<Vehiculo> responseEntity = vehiculoController.updateVehiculo(id, vehiculoDto);

        // Verificación de resultados
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void patchVehiculo() {
        // Creación de un UUID para la prueba
        UUID id = UUID.randomUUID();

        // Mock de la respuesta del servicio
        VehiculoUpdateDto vehiculoDto = new VehiculoUpdateDto("NuevoMercedesActualizado", "Actros", 2019, 100000.0, 100000.0, 100, "https://loremflickr.com/150/150", "Camion de 3 ejes", "Camion", false);
        Vehiculo vehiculo = new Vehiculo();
        when(vehiculoService.update(eq(id.toString()), any(VehiculoUpdateDto.class))).thenReturn(vehiculo);

        // Ejecución del método del controlador
        ResponseEntity<Vehiculo> responseEntity = vehiculoController.patchVehiculo(id, vehiculoDto);

        // Verificación de resultados
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void deleteVehiculo() {
        // Creación de un UUID para la prueba
        UUID id = UUID.randomUUID();

        // Configuración del servicio mock
        doNothing().when(vehiculoService).deleteById(id.toString());

        // Ejecución del método del controlador
        ResponseEntity<Void> responseEntity = vehiculoController.deleteVehiculo(id);

        // Verificación de resultados
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }
}
