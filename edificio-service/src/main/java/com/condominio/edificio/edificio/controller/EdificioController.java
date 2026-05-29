package com.condominio.edificio.edificio.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/edificio")
public class EdificioController {
    @GetMapping("/test")
    public String prueba(){
        return "hola, servicio desde edificio";
    }
}

