package com.madmotor.apimadmotordaw.personal.controllers;

import com.madmotor.apimadmotordaw.personal.dto.PersonalCreateDTO;
import com.madmotor.apimadmotordaw.personal.dto.PersonalResponseDTO;
import com.madmotor.apimadmotordaw.personal.dto.PersonalUpdateDTO;
import com.madmotor.apimadmotordaw.personal.exceptions.PersonalNotFound;
import com.madmotor.apimadmotordaw.personal.services.PersonalService;

import com.madmotor.apimadmotordaw.utils.PageResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("${api.version}/personal")

public class PersonalRestController {
    private final PersonalService personalService;

    @Autowired
    public PersonalRestController(PersonalService personalService) {
        this.personalService = personalService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<PersonalResponseDTO> getPersonalById(@PathVariable @Valid Long id) {
        log.info("Buscando personal por id: " + id);
        return ResponseEntity.ok(personalService.findById(id));

    }

    @PostMapping()

    public ResponseEntity<PersonalResponseDTO> createPersonal(@Valid @RequestBody PersonalCreateDTO personalCreateDTO) {
        log.info("Creando personal: " + personalCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(personalService.save(personalCreateDTO));
    }

    @GetMapping()
    public ResponseEntity<PageResponse<PersonalResponseDTO>> getAllPersonal(
            @RequestParam (required = false) Optional<String> dni,
            @RequestParam (required = false) Optional<String> nombre,
            @RequestParam (required = false) Optional<String> apellidos,
            @RequestParam (required = false) Optional<String> fechaNacimiento,
            @RequestParam (required = false) Optional<String> direccion,
            @RequestParam (required = false) Optional<String> iban,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order
    ){
        log.info("Buscando personal con los siguientes filtros:");
        Sort sort =order.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page,size,sort);
        return ResponseEntity.ok(PageResponse.of(personalService.findAll(dni, nombre, apellidos, fechaNacimiento, direccion, iban, pageable), sortBy, order));

    }


    @PutMapping("/{id}")
    public ResponseEntity<PersonalResponseDTO> updatePersonalById(@PathVariable @Valid Long id, @RequestBody @Valid PersonalUpdateDTO personalUpdateDTO) {
        log.info("Actualizando personal con id: " + id);
        return ResponseEntity.ok(personalService.update(id, personalUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonalById(@PathVariable @Valid Long id) {
        log.info("Borrando personal con id: " + id);
        try{
            personalService.findById(id);
        } catch (Exception e) {
            PersonalNotFound personalNotFound = new PersonalNotFound("Personal no encontrado");
            return ResponseEntity.notFound().build();
        }
        personalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}