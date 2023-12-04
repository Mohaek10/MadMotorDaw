package com.madmotor.apimadmotordaw.rest.piezas.mappers;


import com.madmotor.apimadmotordaw.rest.piezas.dto.PiezaCreateDTO;
import com.madmotor.apimadmotordaw.rest.piezas.dto.PiezaResponseDTO;
import com.madmotor.apimadmotordaw.rest.piezas.dto.PiezaUpdateDTO;
import com.madmotor.apimadmotordaw.rest.piezas.models.Pieza;
import org.springframework.stereotype.Component;

/**
 * Clase PiezaMapper
 *
 * En esta clase se definen los métodos de la clase PiezaMapper
 * @version 1.0
 * @author Rubén Fernández
 */

@Component
public class PiezaMapper {

    /**
     * Método toPieza
     *
     * En este método se crean los atributos de la clase Pieza
     * @param piezaCreateDTO
     * @return pieza creada
     */
    public Pieza toPieza(PiezaCreateDTO piezaCreateDTO) {
        return Pieza.builder()
                .name(piezaCreateDTO.getName())
                .description(piezaCreateDTO.getDescription())
                .price(piezaCreateDTO.getPrice())
                .stock(piezaCreateDTO.getStock())
                .build();
    }

    /**
     * Método toPieza
     *
     * En este método se actualizan los atributos de la clase Pieza
     * @param piezaUpdateDTO
     * @param pieza
     * @return pieza actualizada
     */

    public Pieza toPieza(PiezaUpdateDTO piezaUpdateDTO, Pieza pieza) {
        return Pieza.builder()
                .name(piezaUpdateDTO.getName() != null ? piezaUpdateDTO.getName() : pieza.getName())
                .description(piezaUpdateDTO.getDescription() != null ? piezaUpdateDTO.getDescription() : pieza.getDescription())
                .price(piezaUpdateDTO.getPrice() != null ? piezaUpdateDTO.getPrice() : pieza.getPrice())
                .stock(piezaUpdateDTO.getStock() != null ? piezaUpdateDTO.getStock() : pieza.getStock())
                .build();

    }

    /**
     * Método toPiezaResponse
     *
     * En este método se crean los atributos de la clase PiezaResponseDTO
     * @param pieza
     * @return los datos que se muestran de la pieza
     */

    public PiezaResponseDTO toPiezaResponse(Pieza pieza) {
        return PiezaResponseDTO.builder()
                .id(pieza.getId())
                .name(pieza.getName())
                .description(pieza.getDescription())
                .price(pieza.getPrice())
                .stock(pieza.getStock())
                .image(pieza.getImage())
                .build();
    }


}

