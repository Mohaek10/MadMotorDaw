package com.madmotor.apimadmotordaw.rest.storage.controllers;

import com.madmotor.apimadmotordaw.rest.storage.service.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
/**
 * Controlador de la clase Storage
 * Fijamos la ruta de acceso a este controlador con la anotación @RequestMapping
 *
 * @Autowired es una anotación que nos permite inyectar dependencias basadas  en las anotaciones @Controller, @Service, @Component, etc.
 * y que se encuentren en nuestro contenedor de Spring.
 */

@RestController
@Slf4j
@RequestMapping("/storage")
public class StorageController {
    // Indicamos las dependencias que se van a inyectar
    private final StorageService storageService;
    // Inyectamos la dependencia StorageService
    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    /*
    * Método que nos permite devoler un fichero del sistema de ficheros
    * @param filename nombre del fichero
    * @param request
    * @return ResponseEntity<Resource>
     */

    @GetMapping(value = "{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename, HttpServletRequest request) {
        log.info("Se ha solicitado el fichero: " + filename);
        Resource file = storageService.loadAsResource(filename);

        String contentType = null;
        try {
            // Obtenemos el tipo de fichero
            contentType = request.getServletContext().getMimeType(file.getFile().getAbsolutePath());
        } catch (IOException ex) {
            // Si no se puede determinar el tipo de fichero lanzamos una excepción
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede determinar el tipo de fichero");
        }
        // Si el tipo de fichero es nulo le asignamos un tipo por defecto
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        // Devolvemos el fichero
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(file);
    }
}
