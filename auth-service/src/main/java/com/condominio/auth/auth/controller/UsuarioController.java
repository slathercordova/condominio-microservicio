package com.condominio.auth.auth.controller;

import com.condominio.auth.auth.dto.request.*;
import com.condominio.auth.auth.dto.response.LoginResponse;
import com.condominio.auth.auth.dto.response.RefreshResponse;
import com.condominio.auth.auth.dto.response.RegisterResponse;
import com.condominio.auth.auth.service.UsuarioService;
import com.condominio.auth.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION')")
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
    public ResponseEntity<ApiResponse<RefreshResponse>> refresh(@Valid @RequestBody RefreshRequest refreshRequest, HttpServletRequest httpRequest){
        RefreshResponse refreshResponse = usuarioService.refreshToken(refreshRequest.refreshToken(), httpRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Refresh exitoso", null, refreshResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody RefreshRequest refreshRequest){
        usuarioService.logout(refreshRequest.refreshToken());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Logout exitoso", null, null));
    }

    @PostMapping("/logout-all")
    public ResponseEntity<ApiResponse<Void>> logoutAll(){
        usuarioService.logoutAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Se cerraron todas las sesiones", null, null));
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordRequest cpRequest){
        usuarioService.changePassword(cpRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Se cambió la contraseña correctamente", null, null));
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest fpRequest){
        usuarioService.forgotPassword(fpRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Si el correo existe, se enviaron instrucciones", null, null));
    }

    @PutMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest rpRequest){
        usuarioService.resetPassword(rpRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Contraseña cambiada correctamente, vuelva a ingresar", null, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RegisterResponse>> findById(@PathVariable UUID id){
        RegisterResponse respuesta = usuarioService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Registro encontrado", null, respuesta));
    }

    @PostMapping("/login/edificio/{idEdificio}")
    public ResponseEntity<ApiResponse<LoginResponse>> loginEdificio(@PathVariable UUID idEdificio, HttpServletRequest httpRequest){
        LoginResponse respuesta = usuarioService.loginUsuEdiRol(idEdificio,httpRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Login edificio", null, respuesta));
    }
}
