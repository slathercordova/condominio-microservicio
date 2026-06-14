package com.condominio.auth.service;

import com.condominio.auth.auth.dto.response.LoginResponse;
import com.condominio.auth.auth.dto.response.RolResponse;
import com.condominio.auth.auth.entity.RefreshTokenEntity;
import com.condominio.auth.auth.entity.UsuarioEntity;
import com.condominio.auth.auth.repository.RefreshTokenRepository;
import com.condominio.auth.auth.repository.UsuarioRepository;
import com.condominio.auth.auth.service.UsuarioService;
import com.condominio.auth.common.exception.BusinessException;
import com.condominio.auth.common.exception.ExternalServiceException;
import com.condominio.auth.common.response.ApiResponse;
import com.condominio.auth.common.util.RequestUtils;
import com.condominio.auth.common.util.SecurityUtils;
import com.condominio.auth.feignclient.EdificioClientWs;
import com.condominio.auth.security.JwtService;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private EdificioClientWs edificioClientWs;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private RequestUtils requestUtils;

    @Test
    void loginUsuEdiRol_happyPath() {

        UUID idEdificio = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(securityUtils.getCurrentUserId()).thenReturn(idUsuario);

        ApiResponse<Boolean> existsResponse = new ApiResponse<>(true,"exito",null,true);
        when(edificioClientWs.existsUsuarioEdificio(idEdificio))
                .thenReturn(ResponseEntity.ok(existsResponse));

        RolResponse rol = new RolResponse();
        rol.setNombre("ADMIN");

        ApiResponse<List<RolResponse>> rolesResponse =
                new ApiResponse<>(true, "Lista de roles",null, List.of(rol));

        when(edificioClientWs.findRolesByUsuarioAndEdificio(idUsuario, idEdificio))
                .thenReturn(rolesResponse);

        UsuarioEntity user = new UsuarioEntity();
        user.setId(idUsuario);
        user.setPrimeraVez(false);

        when(usuarioRepository.findById(idUsuario))
                .thenReturn(java.util.Optional.of(user));

        when(jwtService.generateToken(any(), any(), anyList()))
                .thenReturn("access-token");

        when(jwtService.generateRefreshToken(any(), any()))
                .thenReturn("refresh-token");

        when(jwtService.extractExpiration(any()))
                .thenReturn(new Date().toInstant());

        when(refreshTokenRepository.save(any()))
                .thenAnswer(invocation -> {
                    RefreshTokenEntity entity = invocation.getArgument(0);
                    entity.setId(UUID.randomUUID());
                    return entity;
                });

        when(requestUtils.getUserAgent(any()))
                .thenReturn("chrome");

        when(requestUtils.getClientIp(any()))
                .thenReturn("127.0.0.1");

        LoginResponse response = usuarioService.loginUsuEdiRol(idEdificio, request);

        assertNotNull(response);
        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
        assertEquals(idUsuario, response.id());
    }

    @Test
    void loginUsuEdiRol_Usuario_Sin_Roles() {

        UUID idEdificio = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(securityUtils.getCurrentUserId()).thenReturn(idUsuario);

        ApiResponse<Boolean> existsResponse =
                new ApiResponse<>(true, "ok", null, false);

        when(edificioClientWs.existsUsuarioEdificio(idEdificio))
                .thenReturn(ResponseEntity.ok(existsResponse));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> usuarioService.loginUsuEdiRol(idEdificio, request)
        );

        assertEquals("El usuario no pertenece a este edificio", ex.getMessage());
    }

    @Test
    void loginUsuEdiRol_FeignCaido() {
        UUID idEdificio = UUID.randomUUID();
        UUID idUsuario = UUID.randomUUID();

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(securityUtils.getCurrentUserId()).thenReturn(idUsuario);

        ApiResponse<Boolean> existsResponse =
                new ApiResponse<>(true, "ok", null, true);

        when(edificioClientWs.existsUsuarioEdificio(any()))
                .thenReturn(ResponseEntity.ok(existsResponse));

        when(edificioClientWs.findRolesByUsuarioAndEdificio(any(), any()))
                .thenThrow(FeignException.class);

        ExternalServiceException ex = assertThrows(
                ExternalServiceException.class,
                () -> usuarioService.loginUsuEdiRol(idEdificio, request)
        );

        assertTrue(ex.getMessage().contains("Error consumiendo edificio-service"));
    }
}