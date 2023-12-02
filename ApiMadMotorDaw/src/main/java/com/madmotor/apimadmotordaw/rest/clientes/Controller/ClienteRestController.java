package com.madmotor.apimadmotordaw.rest.clientes.Controller;

import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteCreateRequest;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteReponse;
import com.madmotor.apimadmotordaw.rest.clientes.dto.ClienteUpdateRequest;
import com.madmotor.apimadmotordaw.rest.clientes.services.ClienteService;
import com.madmotor.apimadmotordaw.utils.pagination.PageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("${api.version}/clientes")
public class ClienteRestController {
    private final ClienteService clienteService;

    @Autowired
    public ClienteRestController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping()
    public ResponseEntity<PageResponse<ClienteReponse>> getAllClientes(
            @RequestParam (required = false) Optional<String> nombre,
            @RequestParam (required = false) Optional<String> apellido,
            @RequestParam (required = false) Optional<String> direccion,
            @RequestParam (required = false) Optional<Integer> codigoPostal,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order
    ){
        Sort sort = order.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page,size,sort);
        return ResponseEntity.ok(PageResponse.of(clienteService.findAll(nombre, apellido, direccion, codigoPostal, pageable),sortBy,order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteReponse> getClienteByDni(@PathVariable UUID id){
        return ResponseEntity.ok(clienteService.findByID(id));
    }

    @PostMapping
    public ResponseEntity<ClienteReponse> createCliente(@Valid @RequestBody ClienteCreateRequest clienteCreateRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.savePost((clienteCreateRequest)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteReponse> updateCliente(@PathVariable UUID id, @Valid@RequestBody ClienteUpdateRequest clienteUpdateRequest){
        return ResponseEntity.ok(clienteService.updateByID(id,clienteUpdateRequest));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ClienteReponse>patchClienteUpdate(@PathVariable UUID id, @RequestBody ClienteUpdateRequest clienteUpdateRequest){
        return ResponseEntity.ok(clienteService.updateByID(id,clienteUpdateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteCliente(@PathVariable UUID id) {
        clienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping(value = "/imagen/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClienteReponse> updateImage(@PathVariable UUID id, @RequestPart("imagen") MultipartFile imagen) {
        List<String> datosPermitidos = List.of("image/png", "image/jpg", "image/jpeg", "image/gif");
        try {
            String contentType = imagen.getContentType();

            if (!imagen.isEmpty() && contentType != null && !contentType.isEmpty() && datosPermitidos.contains(contentType.toLowerCase())) {
                return ResponseEntity.ok(clienteService.updateImage(id, imagen));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha enviado una imagen para el cliente ");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede saber el tipo de la imagen");
        }
    }


    /**
     * Manejador de excepciones de Validación: 400 Bad Request
     *
     * @param ex excepción
     * @return Mapa de errores de validación con el campo y el mensaje
     */
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