package com.controlepresenca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Classe principal do Sistema de Controle de Presença
 * 
 * Esta aplicação Spring Boot fornece uma API REST para gerenciar
 * turmas, alunos e registros de presença.
 * 
 * @author Sistema de Controle de Presença
 * @version 1.0.0
 */
@SpringBootApplication
public class ControlePresencaApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(ControlePresencaApplication.class, args);
        System.out.println("=================================================");
        System.out.println("Sistema de Controle de Presença iniciado!");
        System.out.println("API disponível em: http://localhost:8080/api");
        System.out.println("=================================================");
    }

    /**
     * Configuração de CORS para permitir requisições do frontend React
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                    "http://localhost:5173",      // Vite/React dev server
                    "http://127.0.0.1:5173",      // Vite/React alternative
                    "http://localhost:3000",      // Create React App dev server  
                    "http://127.0.0.1:3000"       // Create React App alternative
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}