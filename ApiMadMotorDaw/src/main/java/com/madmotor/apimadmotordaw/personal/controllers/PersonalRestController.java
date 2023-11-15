package com.madmotor.apimadmotordaw.personal.controllers;

import com.madmotor.apimadmotordaw.personal.dto.PersonalCreateDTO;
import com.madmotor.apimadmotordaw.personal.dto.PersonalResponseDTO;
import com.madmotor.apimadmotordaw.personal.dto.PersonalUpdateDTO;
import com.madmotor.apimadmotordaw.personal.services.PersonalService;
import com.madmotor.apimadmotordaw.utils.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
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

    @GetMapping("/dni/{dni}")
    public ResponseEntity<PersonalResponseDTO> getPersonalByDni(@PathVariable String dni) {
        log.info("Buscando personal por dni: " + dni);
        return ResponseEntity.ok(personalService.findByDni(dni));

    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonalResponseDTO> getPersonalById(@PathVariable Long id) {
        log.info("Buscando personal por id: " + id);
        return ResponseEntity.ok(personalService.findById(id));

    }

    @PostMapping()
    public ResponseEntity<PersonalResponseDTO> createPersonal(@RequestBody PersonalCreateDTO personalCreateDTO) {
        log.info("Creando personal: " + personalCreateDTO);
        return ResponseEntity.ok(personalService.save(personalCreateDTO));

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

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> deletePersonalByDni(@PathVariable String dni) {
        log.info("Borrando personal con dni: " + dni);
        personalService.deleteByDni(dni);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{dni}")
    public ResponseEntity<PersonalResponseDTO> updatePersonalByDni(@PathVariable String dni, @RequestBody PersonalUpdateDTO personalUpdateDTO) {
        log.info("Actualizando personal con dni: " + dni);
        return ResponseEntity.ok(personalService.update(dni, personalUpdateDTO));
    }


}