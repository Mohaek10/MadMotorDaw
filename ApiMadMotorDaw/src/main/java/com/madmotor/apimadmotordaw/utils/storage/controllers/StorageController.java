package com.madmotor.apimadmotordaw.utils.storage.controllers;

import com.madmotor.apimadmotordaw.utils.storage.service.StorageService;
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

@RestController
@Slf4j
// Es la ruta del controlador
@RequestMapping("/storage")
public class StorageController {
    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Obtiene un fichero del sistema de almacenamiento
     *  @param request  Objeto HttpServletRequest para obtener información sobre el tipo de contenido.
     * @param filename Nombre del fichero a obtener
     * @return Fichero y el tipo de contenido
     */
    @GetMapping(value = "{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename, HttpServletRequest request) {
        // Carga el fichero como recurso
        Resource file = storageService.loadAsResource(filename);

        // Obtiene el tipo de contenido del fichero.
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(file.getFile().getAbsolutePath());
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede determinar el tipo de fichero");
        }
        // Si no se puede determinar el tipo de contenido, se establece como binario.
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        // Retorna el ResponseEntity con el recurso del fichero y el tipo de contenido.
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(file);
    }
}
