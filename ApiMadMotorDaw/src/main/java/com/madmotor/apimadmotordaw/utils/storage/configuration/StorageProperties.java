package com.madmotor.apimadmotordaw.utils.storage.configuration;

import com.madmotor.apimadmotordaw.utils.storage.service.StorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración para la gestión de ficheros de almacenamiento
 */
@Configuration
@Slf4j
public class StorageProperties {
    private final StorageService storageService;

    @Value("${upload.delete}")
    private String deleteAll;

    @Autowired
    public StorageProperties(StorageService storageService) {
        this.storageService = storageService;
    }
    /**
     * Método que se encarga de inicializar la gestión de ficheros.
     * Si la propiedad 'upload.delete' es configurada como 'true' en el properties, se borran todos los ficheros de almacenamiento.
     */
    @PostConstruct
    public void init() {
        if (deleteAll.equals("true")) {
            log.info("Borrando ficheros de almacenamiento...");
            storageService.deleteAll();
        }

        storageService.init(); // inicializamos
    }
}
