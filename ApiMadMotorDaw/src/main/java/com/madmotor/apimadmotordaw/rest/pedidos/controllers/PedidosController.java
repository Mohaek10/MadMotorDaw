package com.madmotor.apimadmotordaw.rest.pedidos.controllers;


import com.madmotor.apimadmotordaw.rest.pedidos.dto.CreatePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.ResponsePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.dto.UpdatePedidoDto;
import com.madmotor.apimadmotordaw.rest.pedidos.models.Pedido;
import com.madmotor.apimadmotordaw.rest.pedidos.services.PedidoService;
import com.madmotor.apimadmotordaw.utils.pagination.PageResponse;
import com.madmotor.apimadmotordaw.utils.pagination.PaginationLinksUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${api.version}/pedidos")
@Slf4j
public class PedidosController {
    private final PedidoService pedidosService;
    private final PaginationLinksUtils paginationLinksUtils;

    @Autowired
    public PedidosController(PedidoService pedidosService, PaginationLinksUtils paginationLinksUtils) {
        this.pedidosService = pedidosService;
        this.paginationLinksUtils = paginationLinksUtils;
    }


    @GetMapping()
    public ResponseEntity<PageResponse<ResponsePedidoDto>> getAllPedidos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("Obteniendo todos los pedidos");
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<ResponsePedidoDto> pageResult = pedidosService.findAll(PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    @GetMapping("/{idPedido}")
    public ResponseEntity<ResponsePedidoDto> getPedidoById(@RequestParam ObjectId idPedido) {
        log.info("Obteniendo pedido con id: " + idPedido.toHexString());
        return ResponseEntity.ok(pedidosService.findById(idPedido));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<PageResponse<ResponsePedidoDto>> getPedidoByIdUsuario(
            @RequestParam String idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request) {
        log.info("Obteniendo pedidos del usuario con id: " + idUsuario);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<ResponsePedidoDto> pageResult = pedidosService.findByIdUsuario(idUsuario, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    @PostMapping()
    public ResponseEntity<ResponsePedidoDto> createPedido(@RequestBody @Valid CreatePedidoDto pedido) {
        log.info("Creando pedido: {}", pedido);
        return ResponseEntity.ok(pedidosService.save(pedido));
    }

    @PutMapping("/{idPedido}")
    public ResponseEntity<ResponsePedidoDto> updatePedido(@RequestBody @Valid UpdatePedidoDto pedido, @PathVariable ObjectId idPedido) {
        log.info("Actualizando pedido con id: {}", idPedido);
        return ResponseEntity.ok(pedidosService.update(pedido, idPedido));
    }

    @DeleteMapping("/{idPedido}")
    public ResponseEntity<Void> deletePedido(@PathVariable ObjectId idPedido) {
        log.info("Borrando pedido con id: {}", idPedido);
        pedidosService.delete(idPedido);
        return ResponseEntity.noContent().build();
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
