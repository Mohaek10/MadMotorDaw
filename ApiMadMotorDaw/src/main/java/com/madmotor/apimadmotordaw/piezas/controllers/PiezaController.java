package com.madmotor.apimadmotordaw.piezas.controllers;

import com.madmotor.apimadmotordaw.piezas.dto.PiezaCreateDTO;
import com.madmotor.apimadmotordaw.piezas.dto.PiezaResponseDTO;
import com.madmotor.apimadmotordaw.piezas.dto.PiezaUpdateDTO;
import com.madmotor.apimadmotordaw.piezas.services.PiezaService;
import com.madmotor.apimadmotordaw.utils.PageResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("${api.version}/piezas")
public class PiezaController {
    private final PiezaService piezaService;


    @Autowired
    public PiezaController(PiezaService piezaService) {
        this.piezaService = piezaService;

    }
    @GetMapping()
    public ResponseEntity<PageResponse<PiezaResponseDTO>> getAllPiezas(
            @RequestParam (required = false) Optional<String> name,
            @RequestParam (required = false) Optional<String> description,
            @RequestParam (required = false) Optional<Double> price,
            @RequestParam (required = false) Optional<Integer> stock,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order
    ){
        log.info("Buscando piezas con los siguientes filtros:"+name+" "+description+" "+price+" "+stock);
        Sort sort =order.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page,size,sort);
        return ResponseEntity.ok(PageResponse.of(piezaService.findAll(name,description,price,stock, pageable), sortBy,order));

    }

    @GetMapping("/{id}")
    public ResponseEntity<PiezaResponseDTO> getPiezaById(@PathVariable @Valid UUID id) {
        log.info("Buscando pieza por id: " + id);
        return ResponseEntity.ok(piezaService.findById(id));
    }

    @PostMapping()

    public ResponseEntity<PiezaResponseDTO> createPieza(@Valid @RequestBody PiezaCreateDTO piezaCreateDTO) {
        log.info("Creando pieza: " + piezaCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(piezaService.save(piezaCreateDTO));
    }

    @PutMapping("/{id}")

    public ResponseEntity<PiezaResponseDTO> updatePieza(@PathVariable UUID id, @Valid @RequestBody PiezaUpdateDTO piezaUpdateDTO) {
        log.info("Actualizando pieza por id: " + id + " con pieza: " + piezaUpdateDTO);
        return ResponseEntity.ok(piezaService.update(id, piezaUpdateDTO));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<PiezaResponseDTO> updatePartialPieza(@PathVariable UUID id, @Valid @RequestBody PiezaUpdateDTO piezaUpdateDTO) {
        log.info("Actualizando parcialmente pieza por id: " + id + " con pieza: " +piezaUpdateDTO );
        return ResponseEntity.ok(piezaService.update(id, piezaUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePieza(@PathVariable @Valid UUID id) {
        log.info("Borrando pieza por id: " + id);
        piezaService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
