package com.uptc.queenscorner;

import com.uptc.queenscorner.utils.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAsync
public class QueenscornerApplication {
    public static void main(String[] args) {
        FileUtils.inicializarDirectorios();
        
        SpringApplication.run(QueenscornerApplication.class, args);
    }
}