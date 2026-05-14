package com.condominio.auth.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class authController {

    @GetMapping("/test")
    public String prueba(){
        return "hola, servicio desde auth";
    }
}
