package com.madmotor.apimadmotordaw.rest.vehiculos.services;

import com.madmotor.apimadmotordaw.rest.categorias.models.Categoria;
import com.madmotor.apimadmotordaw.rest.categorias.services.CategoriaService;
import com.madmotor.apimadmotordaw.config.websockets.WebSocketConfig;
import com.madmotor.apimadmotordaw.config.websockets.WebSocketHandler;
import com.madmotor.apimadmotordaw.websockets.notificaciones.mapper.VehiculoNotificacionMapper;
import com.madmotor.apimadmotordaw.websockets.notificaciones.models.Notificacion;
import com.madmotor.apimadmotordaw.rest.vehiculos.dto.VehiculoCreateDto;
import com.madmotor.apimadmotordaw.rest.vehiculos.dto.VehiculoUpdateDto;
import com.madmotor.apimadmotordaw.rest.vehiculos.exceptions.VehiculoNotFound;
import com.madmotor.apimadmotordaw.rest.vehiculos.mapper.VehiculoMapper;
import com.madmotor.apimadmotordaw.rest.vehiculos.models.Vehiculo;
import com.madmotor.apimadmotordaw.rest.vehiculos.repositories.VehiculoRepository;
import com.madmotor.apimadmotordaw.rest.vehiculos.services.VehiculoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehiculoServiceImplTest {
    private final Categoria categoria = new Categoria(1L, "Electrico", LocalDateTime.now(), LocalDateTime.now(), false);

    private final Vehiculo vehiculo1 = new Vehiculo(UUID.fromString("f5d2c8a0-0b7e-4b1a-8b1a-6b9b8b3b4b4b"), "Tesla", "Model 3", 2020, 0.0, 50000.0, 10, "https://www.tesla.com/sites/default/files/modelsx-new/social/model-x-social.jpg", "El mejor coche electrico", LocalDateTime.now(), LocalDateTime.now(), categoria, false);
    private final Vehiculo vehiculo2 = new Vehiculo(UUID.fromString("25ca24d5-0552-40f1-b674-5ecde9d542e3"), "Porche", "Taycan", 2023, 30000.0, 100000.0, 5, "porche.img", "El mejor coche electrico", LocalDateTime.now(), LocalDateTime.now(), categoria, false);

    WebSocketHandler webSocketHandlerMock = mock(WebSocketHandler.class);
    @Mock
    private VehiculoRepository vehiculoRepository;
    @Mock
    private CategoriaService categoriaService;
    @Mock
    VehiculoMapper vehiculoMapper;
    @Mock
    private WebSocketConfig webSocketConfig;
    @Mock
    private VehiculoNotificacionMapper productoNotificationMapper;
    @InjectMocks
    private VehiculoServiceImpl vehiculoService;
    @Captor
    private ArgumentCaptor<Vehiculo> vehiculoArgumentCaptor;

    @BeforeEach
    void setUp() {
        vehiculoService.setWebSocketService(webSocketHandlerMock);
    }

    @Test
    void setWebSocketService() {
    }

    @Test
    void findAll_CuandoNoHayParametros() {
        List<Vehiculo> vehiculosE = Arrays.asList(vehiculo1, vehiculo2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending()); // ejemplo de creación de un objeto Pageable
        Page<Vehiculo> expectPage = new PageImpl<>(vehiculosE);

        when(vehiculoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectPage);
        Page<Vehiculo> pageA = vehiculoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(pageA),
                () -> assertFalse(pageA.isEmpty()),
                () -> assertEquals(expectPage, pageA)
        );
    }

    @Test
    void findAll_DevuelveConMarca() {
        Optional<String> marca = Optional.of("Tesla");
        List<Vehiculo> vehiculosE = Arrays.asList(vehiculo1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Vehiculo> expectPage = new PageImpl<>(vehiculosE);

        when(vehiculoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectPage);
        Page<Vehiculo> pageA = vehiculoService.findAll(marca, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        assertAll(
                () -> assertNotNull(pageA),
                () -> assertFalse(pageA.isEmpty()),
                () -> assertEquals(expectPage, pageA)
        );
    }

    @Test
    void findAll_DevuelveConCategoria() {
        Optional<String> categoria = Optional.of("Electrico");
        List<Vehiculo> vehiculosE = Arrays.asList(vehiculo1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Vehiculo> expectPage = new PageImpl<>(vehiculosE);

        when(vehiculoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectPage);
        Page<Vehiculo> pageA = vehiculoService.findAll(Optional.empty(), categoria, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        assertAll(
                () -> assertNotNull(pageA),
                () -> assertFalse(pageA.isEmpty()),
                () -> assertEquals(expectPage, pageA)
        );
    }

    @Test
    void findAll_DevuelveConModelo() {
        Optional<String> modelo = Optional.of("Model 3");
        List<Vehiculo> vehiculosE = Arrays.asList(vehiculo1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Vehiculo> expectPage = new PageImpl<>(vehiculosE);

        when(vehiculoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectPage);
        Page<Vehiculo> pageA = vehiculoService.findAll(Optional.empty(), Optional.empty(), modelo, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        assertAll(
                () -> assertNotNull(pageA),
                () -> assertFalse(pageA.isEmpty()),
                () -> assertEquals(expectPage, pageA)
        );
    }

    @Test
    void findAll_DevuelveConMinYear() {
        Optional<Integer> minYear = Optional.of(2020);
        List<Vehiculo> vehiculosE = Arrays.asList(vehiculo1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Vehiculo> expectPage = new PageImpl<>(vehiculosE);

        when(vehiculoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectPage);
        Page<Vehiculo> pageA = vehiculoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), minYear, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        assertAll(
                () -> assertNotNull(pageA),
                () -> assertFalse(pageA.isEmpty()),
                () -> assertEquals(expectPage, pageA)
        );
    }

    @Test
    void findAll_DevuelveConIsDelete() {
        Optional<Boolean> isDelete = Optional.of(false);
        List<Vehiculo> vehiculosE = Arrays.asList(vehiculo1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Vehiculo> expectPage = new PageImpl<>(vehiculosE);

        when(vehiculoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectPage);
        Page<Vehiculo> pageA = vehiculoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), isDelete, Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        assertAll(
                () -> assertNotNull(pageA),
                () -> assertFalse(pageA.isEmpty()),
                () -> assertEquals(expectPage, pageA)
        );
    }

    @Test
    void findAll_DevuelveConKmMax() {
        Optional<Double> kmMax = Optional.of(30000.0);
        List<Vehiculo> vehiculosE = Arrays.asList(vehiculo1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Vehiculo> expectPage = new PageImpl<>(vehiculosE);

        when(vehiculoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectPage);
        Page<Vehiculo> pageA = vehiculoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), kmMax, Optional.empty(), Optional.empty(), pageable);
        assertAll(
                () -> assertNotNull(pageA),
                () -> assertFalse(pageA.isEmpty()),
                () -> assertEquals(expectPage, pageA)
        );
    }

    @Test
    void findAll_DevuelveConPrecioMax() {
        Optional<Double> precioMax = Optional.of(100000.0);
        List<Vehiculo> vehiculosE = Arrays.asList(vehiculo1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Vehiculo> expectPage = new PageImpl<>(vehiculosE);

        when(vehiculoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectPage);
        Page<Vehiculo> pageA = vehiculoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), precioMax, Optional.empty(), pageable);
        assertAll(
                () -> assertNotNull(pageA),
                () -> assertFalse(pageA.isEmpty()),
                () -> assertEquals(expectPage, pageA)
        );
    }

    @Test
    void findAll_DevuelveConStockMin() {
        Optional<Integer> stockMin = Optional.of(5);
        List<Vehiculo> vehiculosE = Arrays.asList(vehiculo2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Vehiculo> expectPage = new PageImpl<>(vehiculosE);

        when(vehiculoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectPage);
        Page<Vehiculo> pageA = vehiculoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), stockMin, pageable);
        assertAll(
                () -> assertNotNull(pageA),
                () -> assertFalse(pageA.isEmpty()),
                () -> assertEquals(expectPage, pageA)
        );
    }

    @Test
    void findAll_DevuelveVacio() {
        Optional<Integer> stockMin = Optional.of(5);
        List<Vehiculo> vehiculosE = Arrays.asList();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Vehiculo> expectPage = new PageImpl<>(vehiculosE);
        when(vehiculoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectPage);
        Page<Vehiculo> pageA = vehiculoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), stockMin, pageable);
        assertAll(
                () -> assertNotNull(pageA),
                () -> assertTrue(pageA.isEmpty()),
                () -> assertEquals(expectPage, pageA)
        );
    }


    @Test
    void findById_DevuelveBien() {
        UUID id = UUID.fromString("f5d2c8a0-0b7e-4b1a-8b1a-6b9b8b3b4b4b");
        when(vehiculoRepository.findById(id)).thenReturn(Optional.of(vehiculo1));

        Vehiculo vehiculoAct = vehiculo1;
        Vehiculo vehiculoAc = vehiculoService.findById(id.toString());
        assertEquals(vehiculoAct, vehiculoAc);
        verify(vehiculoRepository, times(1)).findById(id);
    }

    @Test
    void findById_DevuelveNotFound() {
        UUID id = UUID.fromString("f5d2c8a0-0b7e-4b1a-8b1a-6b9b8b3b4b4b");
        when(vehiculoRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> vehiculoService.findById(id.toString()));
        verify(vehiculoRepository, times(1)).findById(id);

    }

    @Test
    void save_devuelveElProductoBien() throws IOException {
        VehiculoCreateDto vehiculoCreateDto = new VehiculoCreateDto("marca",
                "modelo",
                2021,
                1000.0,
                10000.0,
                10,
                "imagen",
                "descripcion",
                categoria.getName());
        Vehiculo vehisculoEsperado = new Vehiculo(UUID.randomUUID(), "marca", "modelo", 2021, 1000.0, 10000.0, 10, "imagen", "descripcion", LocalDateTime.now(), LocalDateTime.now(), categoria, false);
        when(categoriaService.findByName(vehiculoCreateDto.getCategoria())).thenReturn(categoria);
        when(vehiculoMapper.toVevhiculo(vehiculoCreateDto, categoria)).thenReturn(vehisculoEsperado);
        when(vehiculoRepository.save(vehisculoEsperado)).thenReturn(vehisculoEsperado);
        doNothing().when(webSocketHandlerMock).sendMessage(any());

        Vehiculo actVehiculo = vehiculoService.save(vehiculoCreateDto);

        assertEquals(vehisculoEsperado, actVehiculo);

        verify(vehiculoRepository, times(1)).save(vehiculoArgumentCaptor.capture());
        verify(categoriaService, times(1)).findByName(vehiculoCreateDto.getCategoria());
        verify(vehiculoMapper, times(1)).toVevhiculo(vehiculoCreateDto, categoria);

    }

    @Test
    void updateCuandoVehiculoEsValido() throws IOException {
        UUID id = UUID.randomUUID();
        VehiculoUpdateDto vehiculoUpdateDto = new VehiculoUpdateDto("marca", "modelo", 2021, 1000.0, 10000.0, 10, "imagen", "descripcion", categoria.getName(), false);
        Vehiculo esistente = vehiculo1;
        when(vehiculoRepository.findById(id)).thenReturn(Optional.of(esistente));
        when(categoriaService.findByName(vehiculoUpdateDto.getCategoria())).thenReturn(categoria);
        when(vehiculoMapper.toVehiculo(vehiculoUpdateDto, vehiculo1, categoria)).thenReturn(esistente);
        when(vehiculoRepository.save(esistente)).thenReturn(esistente);
        doNothing().when(webSocketHandlerMock).sendMessage(any());

        Vehiculo actVehiculo = vehiculoService.update(id.toString(), vehiculoUpdateDto);

        assertEquals(esistente, actVehiculo);

        verify(vehiculoRepository, times(1)).findById(id);
        verify(categoriaService, times(1)).findByName(vehiculoUpdateDto.getCategoria());
        verify(vehiculoMapper, times(1)).toVehiculo(vehiculoUpdateDto, esistente, categoria);
        verify(vehiculoRepository, times(1)).save(vehiculoArgumentCaptor.capture());
    }
    @Test
    void Update_DevuelveNotFound(){
        UUID id = UUID.randomUUID();
        VehiculoUpdateDto vehiculoUpdateDto = new VehiculoUpdateDto("marca", "modelo", 2021, 1000.0, 10000.0, 10, "imagen", "descripcion", categoria.getName(), false);
        when(vehiculoRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(VehiculoNotFound.class, () -> vehiculoService.update(id.toString(), vehiculoUpdateDto));
        verify(vehiculoRepository, times(1)).findById(id);
    }


    @Test
    void deleteById() {
        UUID id = UUID.randomUUID();
        when(vehiculoRepository.findById(id)).thenReturn(Optional.of(vehiculo1));
        doNothing().when(vehiculoRepository).deleteById(id);
        vehiculoService.deleteById(id.toString());
        verify(vehiculoRepository, times(1)).findById(id);
        verify(vehiculoRepository, times(1)).deleteById(id);
    }
    @Test
    void deleteById_DevuelveNotFound(){
        UUID id = UUID.randomUUID();
        when(vehiculoRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(VehiculoNotFound.class, () -> vehiculoService.deleteById(id.toString()));
        verify(vehiculoRepository, times(1)).findById(id);
    }

    @Test
    void onChange_ShouldSendMessage_WhenValidDataProvided() throws IOException {
        doNothing().when(webSocketHandlerMock).sendMessage(any(String.class));

        vehiculoService.onChange(Notificacion.Tipo.CREATE, any(Vehiculo.class));
    }
}