package com.madmotor.apimadmotordaw.rest.piezas.mappers;

import com.madmotor.apimadmotordaw.rest.piezas.dto.PiezaCreateDTO;
import com.madmotor.apimadmotordaw.rest.piezas.dto.PiezaUpdateDTO;
import com.madmotor.apimadmotordaw.rest.piezas.mappers.PiezaMapper;
import com.madmotor.apimadmotordaw.rest.piezas.models.Pieza;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PiezaMapperTest {
    private final PiezaMapper pie = new PiezaMapper();

    @Test
    void toPieza() {
        // Arrange
        PiezaCreateDTO piezaCreateDTO = PiezaCreateDTO.builder()
                .name("name1")
                .description("description1")
                .price(1.0)
                .stock(1)
                .build();
        var res = pie.toPieza(piezaCreateDTO);

        // Assert
        assertAll(
                () ->assertNotNull(res),
                () -> assertEquals(piezaCreateDTO.getName(), res.getName()),
                () -> assertEquals(piezaCreateDTO.getDescription(), res.getDescription()),
                () -> assertEquals(piezaCreateDTO.getPrice(), res.getPrice()),
                () -> assertEquals(piezaCreateDTO.getStock(), res.getStock())
        );
    }

    @Test
    void toPiezaUpdate() {
        PiezaUpdateDTO piezaUpdateDTO = PiezaUpdateDTO.builder()
                .name("name1")
                .description("description1")
                .price(1.0)
                .stock(1)
                .updatedAt(LocalDateTime.now())
                .build();

        Pieza pieza = Pieza.builder()
                .id(UUID.randomUUID())
                .name(piezaUpdateDTO.getName())
                .description(piezaUpdateDTO.getDescription())
                .price(piezaUpdateDTO.getPrice())
                .stock(piezaUpdateDTO.getStock())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();





        var res = pie.toPieza(piezaUpdateDTO, pieza);

        // Assert
        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(piezaUpdateDTO.getName(), res.getName()),
                () -> assertEquals(piezaUpdateDTO.getDescription(), res.getDescription()),
                () -> assertEquals(piezaUpdateDTO.getPrice(), res.getPrice()),
                () -> assertEquals(piezaUpdateDTO.getStock(), res.getStock())


        );
    }

    @Test
    void toPiezaResponseDto() {
        // Arrange
        Pieza pieza = Pieza.builder()
                .id(UUID.randomUUID())
                .name("name1")
                .description("description1")
                .price(1.0)
                .stock(1)
                .image("image1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();


        // Act
        var res = pie.toPiezaResponse(pieza);

        // Assert
        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(pieza.getId(), res.getId()),
                () -> assertEquals(pieza.getName(), res.getName()),
                () -> assertEquals(pieza.getDescription(), res.getDescription()),
                () -> assertEquals(pieza.getPrice(), res.getPrice()),
                () -> assertEquals(pieza.getStock(), res.getStock()),
                () -> assertEquals(pieza.getImage(), res.getImage())
        );
    }
}