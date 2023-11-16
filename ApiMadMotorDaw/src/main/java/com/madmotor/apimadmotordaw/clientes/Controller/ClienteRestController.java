package com.madmotor.apimadmotordaw.clientes.Controller;

import com.madmotor.apimadmotordaw.clientes.dto.ClienteCreateRequest;
import com.madmotor.apimadmotordaw.clientes.dto.ClienteReponse;
import com.madmotor.apimadmotordaw.clientes.dto.ClienteUpdateRequest;
import com.madmotor.apimadmotordaw.clientes.services.ClienteService;
import com.madmotor.apimadmotordaw.utils.PageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@CacheConfig(cacheNames = {"clientes"})
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

    @Cacheable
    @GetMapping("/{id}")
    public ResponseEntity<ClienteReponse> getClienteByDni(@PathVariable String id){
        return ResponseEntity.ok(clienteService.findByDni(id));
    }

    @CachePut
    @PostMapping
    public ResponseEntity<ClienteReponse> createCliente(@Valid @RequestBody ClienteCreateRequest clienteCreateRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.savePost((clienteCreateRequest)));
    }

    @CachePut
    @PutMapping("/{id}")
    public ResponseEntity<ClienteReponse> updateCliente(@PathVariable String id, @Valid@RequestBody ClienteUpdateRequest clienteUpdateRequest){
        return ResponseEntity.ok(clienteService.updateByDni(id,clienteUpdateRequest));
    }

    @Cacheable
    @PatchMapping("/{id}")
    public ResponseEntity<ClienteReponse>patchClienteUpdate(@PathVariable String id, @RequestBody ClienteUpdateRequest clienteUpdateRequest){
        return ResponseEntity.ok(clienteService.updateByDni(id,clienteUpdateRequest));
    }
    @CacheEvict
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteCliente(@PathVariable String id) {
        clienteService.deleteByDni(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping(value = "/imagen/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClienteReponse> updateImage(@PathVariable String id, @RequestPart("imagen") MultipartFile imagen) {
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
