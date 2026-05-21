package com.condominio.auth.auth.controller;

import com.condominio.auth.auth.dto.request.LoginRequest;
import com.condominio.auth.auth.dto.request.RefreshRequest;
import com.condominio.auth.auth.dto.request.RegisterRequest;
import com.condominio.auth.auth.dto.response.LoginResponse;
import com.condominio.auth.auth.dto.response.RefreshResponse;
import com.condominio.auth.auth.dto.response.RegisterResponse;
import com.condominio.auth.auth.service.UsuarioService;
import com.condominio.auth.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/test")
    public String prueba(){
        return "hola, servicio desde auth";
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> registrarUsuario(@Valid @RequestBody RegisterRequest registerRequest){
        RegisterResponse registerResponse = usuarioService.registerUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Usuario creado correctamente", null, registerResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest httpRequest){
        LoginResponse loginResponse = usuarioService.loginUser(loginRequest, httpRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Login exitoso", null, loginResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshResponse>> refresh(@Valid @RequestBody RefreshRequest refreshRequest){
        RefreshResponse refreshResponse = usuarioService.refreshToken(refreshRequest.refreshToken());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Refresh exitoso", null, refreshResponse));
    }
}
