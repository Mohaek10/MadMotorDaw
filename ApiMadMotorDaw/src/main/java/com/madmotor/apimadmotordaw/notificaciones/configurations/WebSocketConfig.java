package com.madmotor.apimadmotordaw.notificaciones.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {


    @Value("${api.version}")
    private String apiVersion;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler( webSocketVehiHandler(), "/ws"+apiVersion+"/vehiculos");

    }
    @Bean
    public WebSocketHandler webSocketVehiHandler() {
        return new WebSocketHandler("Vehiculos");
    }
}