package com.condominio.auth.auth.service;

import com.condominio.auth.auth.dto.request.*;
import com.condominio.auth.auth.dto.response.*;
import com.condominio.auth.auth.entity.RefreshTokenEntity;
import com.condominio.auth.auth.entity.UsuarioEntity;
import com.condominio.auth.auth.enums.EstadoUsuario;
import com.condominio.auth.auth.enums.TipoBloqueo;
import com.condominio.auth.auth.repository.RefreshTokenRepository;
import com.condominio.auth.auth.repository.UsuarioRepository;
import com.condominio.auth.common.exception.BusinessException;
import com.condominio.auth.common.exception.ExternalServiceException;
import com.condominio.auth.common.exception.ResourceAlreadyExistsException;
import com.condominio.auth.common.exception.ResourceNotFoundException;
import com.condominio.auth.common.response.ApiResponse;
import com.condominio.auth.common.util.*;
import com.condominio.auth.email.EmailService;
import com.condominio.auth.feignclient.EdificioClientWs;
import com.condominio.auth.feignclient.PersonaClientWs;
import com.condominio.auth.security.JwtService;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    private final SecurityUtils securityUtils;
    private final PersonaClientWs personaClientWs;
    private final EdificioClientWs edificioClientWs;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RequestUtils requestUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailService emailService;
    private static final int MAX_INTENTOS = 5;

    public UsuarioService(UsuarioRepository usuarioRepository, ModelMapper modelMapper, SecurityUtils securityUtils, PersonaClientWs personaClientWs, EdificioClientWs edificioClientWs, PasswordEncoder passwordEncoder, JwtService jwtService, RequestUtils requestUtils, RefreshTokenRepository refreshTokenRepository, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
        this.securityUtils = securityUtils;
        this.personaClientWs = personaClientWs;
        this.edificioClientWs = edificioClientWs;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.requestUtils = requestUtils;
        this.refreshTokenRepository = refreshTokenRepository;
        this.emailService = emailService;
    }

    @Transactional
    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        //  Validar que no exista el nombre usuario actualmente
        if (usuarioRepository.existsByUsername(registerRequest.getUsername())) {
            throw new ResourceAlreadyExistsException("El username ya existe");
        }
        //  Validar que no exista otro usuario con el mismo correo
        if (usuarioRepository.existsByCorreo(registerRequest.getCorreo())) {
            throw new ResourceAlreadyExistsException("El correo ya se encuentra registrado");
        }

        //  Llamar al servicio de persona para traer el ID e identificar si existe o no
        ApiResponse<PersonaDetailResponse> personaDetailResponse = null;
        UUID idPersona = null;
        try {
            personaDetailResponse = personaClientWs.findPersonaPorDocumento(registerRequest.getTipoDocumento(), registerRequest.getNumeroDocumento());
            log.debug("Respuesta ws persona : {}", personaDetailResponse);
            //  Validar que no exista el id persona con otro usuario ya creado
            idPersona = personaDetailResponse.getData().getId();
            if (usuarioRepository.existsByIdPersona(idPersona)) {
                throw new ResourceAlreadyExistsException("La persona ya tiene un usuario creado");
            }
        } catch (FeignException.NotFound e) {
            log.info("Persona no encontrada, se creará");
            //  llamar al servicio de personas para registrar a la persona de no tener ID persona
            PersonaRequest personaRequest = modelMapper.map(registerRequest, PersonaRequest.class);
            personaRequest.setEstado(true);
            log.debug("Llamando a servicio de persona para crearla : {}", personaRequest);
            ApiResponse<PersonaResponse> personaCreateResponse = personaClientWs.createPersona(personaRequest);
            idPersona = personaCreateResponse.getData().getId();
            //  Si ya existe persona ya no hay necesidad de llamar al servicio que crea persona
        } catch (FeignException e) {
            log.error("Error consumiendo person-service: status={} body={}", e.status(), e.contentUTF8());
            throw new ExternalServiceException("Error consumiendo el servicio de personas " + e.getMessage());
        }

        //  Si pasa todas las reglas llenar el usuario entity y mandar a crear
        UsuarioEntity userEntity = modelMapper.map(registerRequest, UsuarioEntity.class);
        userEntity.setIdPersona(idPersona);
        userEntity.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userEntity.setIntentoErroneo(0);
        userEntity.setTipoBloqueo(TipoBloqueo.SIN_BLOQUEO);
        userEntity.setPrimeraVez(true);
        userEntity.setEstado(EstadoUsuario.ACTIVO);

        UsuarioEntity saved = usuarioRepository.save(userEntity);

        //  Si las 2 condiciones anteriores estan ok devolver ok al controller
        return modelMapper.map(saved, RegisterResponse.class);
    }

    @Transactional(noRollbackFor = BusinessException.class)
    public LoginResponse loginUser(LoginRequest loginRequest, HttpServletRequest httpRequest) {
        Instant now = DateUtils.nowInstant();

        UsuarioEntity usuarioEntity = usuarioRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new BusinessException("Credenciales incorrectas"));

        if (usuarioEntity.getEstado() != EstadoUsuario.ACTIVO) {
            String mensaje = "Usuario se encuentra " + usuarioEntity.getEstado().getMensaje();
            if (usuarioEntity.getTipoBloqueo() != TipoBloqueo.SIN_BLOQUEO) {
                mensaje += " - " + usuarioEntity.getTipoBloqueo().getMensaje();
            }
            throw new BusinessException(mensaje);
        }

        //  Verificar contraseña
        if (!passwordEncoder.matches(loginRequest.password(), usuarioEntity.getPassword())) {
            //  Si falla aumentar el número de intentos
            usuarioEntity.setIntentoErroneo(usuarioEntity.getIntentoErroneo() + 1);
            log.warn("Login fallido usuario={} intentos={} ", usuarioEntity.getUsername(), usuarioEntity.getIntentoErroneo());
            //  Si se llega a 5 intentos bloquear a usuario
            if (usuarioEntity.getIntentoErroneo() >= MAX_INTENTOS) {
                usuarioEntity.setEstado(EstadoUsuario.BLOQUEADO);
                usuarioEntity.setTipoBloqueo(TipoBloqueo.INTENTOS_FALLIDOS);
                usuarioEntity.setBloqueoAt(now);
                log.warn("Usuario={} bloqueado por intentos", usuarioEntity.getUsername());
                throw new BusinessException("Contraseña incorrecta, usuario bloqueado");
            }
            //  Mandar error de contraseña inválida
            throw new BusinessException("Credenciales incorrectas");
        }

        //  Si el usuario se loguea exitosamente reset # intentos, actualizar fecha y hora de último login y devolver token
        usuarioEntity.setTipoBloqueo(TipoBloqueo.SIN_BLOQUEO);
        usuarioEntity.setIntentoErroneo(0);
        usuarioEntity.setUltimoLogin(now);

        String accessToken = jwtService.generateToken(usuarioEntity);
        String refreshToken = jwtService.generateRefreshToken(usuarioEntity);

        RefreshTokenEntity rte = new RefreshTokenEntity();
        rte.setUsuarioId(usuarioEntity.getId());
        rte.setTokenHash(HashUtils.hashSha256(refreshToken));
        rte.setExpiracionAt(jwtService.extractExpiration(refreshToken));
        rte.setUsado(false);
        rte.setRevocado(false);
        rte.setDispositivo(requestUtils.getUserAgent(httpRequest));
        rte.setIp(requestUtils.getClientIp(httpRequest));

        RefreshTokenEntity rteSaved = refreshTokenRepository.save(rte);
        if (rteSaved.getId() == null) {
            throw new BusinessException("No se pudo registrar el refresh token");
        }

        return new LoginResponse(accessToken, refreshToken, usuarioEntity.getId(), usuarioEntity.isPrimeraVez(),false);
    }

    @Transactional
    public RefreshResponse refreshToken(String oldRefreshToken, HttpServletRequest httpRequest) {
        log.info(">>>>>INICIO REFRESH");
        if (jwtService.isTokenExpired(oldRefreshToken)) {
            throw new BusinessException("Refresh token expirado");
        }

        String type = jwtService.extractType(oldRefreshToken);

        if (!"refresh".equals(type)) {
            throw new BusinessException("Tipo de token inválido");
        }

        String username = jwtService.extractUsername(oldRefreshToken);

        if (username == null) {
            throw new BusinessException("Username del token inválido");
        }

        UsuarioEntity usuarioEntity = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario del token no encontrado"));

        //  Buscar el token en BD
        RefreshTokenEntity oldRTE = refreshTokenRepository.findByTokenHash(HashUtils.hashSha256(oldRefreshToken))
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token no encontrado"));

        if (oldRTE.getExpiracionAt().isBefore(DateUtils.nowInstant())) {
            throw new BusinessException("Refresh token bd expirado");
        }

        if (oldRTE.getRevocado()) {
            throw new BusinessException("Refresh token está revocado");
        }

        if (oldRTE.getUsado()) {
            throw new BusinessException("Refresh token se encuentra usado");
        }

        oldRTE.setUsado(true);
        refreshTokenRepository.save(oldRTE);

        UUID idEdificio = jwtService.extractEdificioId(oldRefreshToken);
        log.info("Edificio={}", idEdificio);
        String newAccessToken;
        String newRefreshToken;
        if (idEdificio != null) {
            log.info("Consultando roles x edif ws usuario={} edificio={}",usuarioEntity.getId(),idEdificio);
            try {
                //todo validar que exista edificio por ws en todo este servicio
                List<RolResponse> listaRoles = edificioClientWs.findRolesByUsuarioAndEdificio(usuarioEntity.getId(),idEdificio).getData();
                log.info("Roles encontrados={}", listaRoles.size());

                List<String> rolesStr = listaRoles.stream().map(RolResponse::getNombre).toList();

                newAccessToken = jwtService.generateToken(usuarioEntity,idEdificio,rolesStr);
                newRefreshToken = jwtService.generateRefreshToken(usuarioEntity,idEdificio);
            } catch (Exception e) {
                log.error("ERROR FEIGN", e);
                throw e;
            }
        }else {
            newAccessToken = jwtService.generateToken(usuarioEntity);
            newRefreshToken = jwtService.generateRefreshToken(usuarioEntity);
        }



        RefreshTokenEntity newRTE = new RefreshTokenEntity();
        newRTE.setUsuarioId(usuarioEntity.getId());
        newRTE.setTokenHash(HashUtils.hashSha256(newRefreshToken));
        newRTE.setExpiracionAt(jwtService.extractExpiration(newRefreshToken));
        newRTE.setUsado(false);
        newRTE.setRevocado(false);
        newRTE.setDispositivo(requestUtils.getUserAgent(httpRequest));
        newRTE.setIp(requestUtils.getClientIp(httpRequest));

        RefreshTokenEntity rteSaved = refreshTokenRepository.save(newRTE);

        if (rteSaved.getId() == null) {
            throw new BusinessException("No se pudo registrar el new refresh token");
        }

        return new RefreshResponse(newAccessToken, newRefreshToken, usuarioEntity.getId());
    }

    @Transactional
    public void logout(String oldRefreshToken) {
        String type = jwtService.extractType(oldRefreshToken);
        if (!"refresh".equals(type)) {
            throw new BusinessException("Tipo token inválido");
        }

        RefreshTokenEntity rte = refreshTokenRepository.findByTokenHash(HashUtils.hashSha256(oldRefreshToken))
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token no encontrado"));

        if (rte.getRevocado()) {
            throw new BusinessException("Refresh token ya revocado");
        }

        if (rte.getUsado()) {
            throw new BusinessException("Refresh token ya utilizado");
        }

        rte.setRevocado(true);
        refreshTokenRepository.save(rte);
    }

    @Transactional
    public void logoutAll() {
        UsuarioEntity ue = usuarioRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        int registros = refreshTokenRepository.revokeAllByUsuarioId(ue.getId(), securityUtils.getCurrentUserId());
        log.info("Sesiones cerradas de {}, total = {}", ue.getId(), registros);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest cpRequest) {
        UsuarioEntity ue = usuarioRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(cpRequest.oldPassword(), ue.getPassword())) {
            throw new BusinessException("La contraseña actual es incorrecta");
        }

        if (passwordEncoder.matches(cpRequest.newPassword(), ue.getPassword())) {
            throw new BusinessException("La nueva contraseña debe ser distinta");
        }

        ue.setPrimeraVez(false);
        ue.setPassword(passwordEncoder.encode(cpRequest.newPassword()));
        usuarioRepository.save(ue);
        logoutAll();
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest fpRequest){
        UsuarioEntity ue = usuarioRepository.findByCorreo(fpRequest.correo())
                .orElse(null);

        if(ue == null){
            log.info("Forgot password solicitado para correo inexistente {}",fpRequest.correo());
            return;
        }

        //  Actualizamos los campos del usuario
        ue.setIntentoErroneo(0);
        ue.setTipoBloqueo(TipoBloqueo.OLVIDE_CONTRASEÑA);
        ue.setEstado(EstadoUsuario.ACTIVO);
        ue.setUpdatedBy(securityUtils.getCurrentUserId(DatosConstant.PASSWORD_RECOVERY_JOB));

        usuarioRepository.save(ue);

        String tmpAccessToken = jwtService.generatePasswordResetToken(ue);

        emailService.sendRecoveryCode(ue.getCorreo(),tmpAccessToken);

        logoutAllUserId(ue.getId(),DatosConstant.PASSWORD_RECOVERY_JOB);
    }

    @Transactional
    private void logoutAllUserId(UUID userId, UUID updateBy) {
        int registros = refreshTokenRepository.revokeAllByUsuarioId(userId, securityUtils.getCurrentUserId(updateBy));
        log.info("Sesiones cerradas de {}, total = {}", userId, registros);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest rpRequest) {

        if (jwtService.isTokenExpired(rpRequest.token())){
            throw new BusinessException("Token de recuperación expirado");
        }

        String type = jwtService.extractType(rpRequest.token());
        if (!"password_reset".equals(type)) {
            throw new BusinessException("Tipo token inválido para reset");
        }

        UUID userId = jwtService.extractUserId(rpRequest.token());
        if (userId == null){
            throw new BusinessException("User ID token inválido para reset");
        }

        UsuarioEntity ue = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (passwordEncoder.matches(rpRequest.password(), ue.getPassword())) {
            throw new BusinessException("La nueva contraseña debe ser distinta a la actual");
        }

        ue.setPassword(passwordEncoder.encode(rpRequest.password()));
        ue.setIntentoErroneo(0);
        ue.setTipoBloqueo(TipoBloqueo.SIN_BLOQUEO);
        ue.setPrimeraVez(false);
        ue.setEstado(EstadoUsuario.ACTIVO);

        usuarioRepository.save(ue);
        logoutAllUserId(ue.getId(),DatosConstant.PASSWORD_RECOVERY_JOB);
    }

    @Transactional(readOnly = true)
    public RegisterResponse findById(UUID id) {
        UsuarioEntity ue = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        RegisterResponse registerResponse = modelMapper.map(ue, RegisterResponse.class);
        return registerResponse;
    }

    // todo devolver lista de edificios del usuario


    //todo revisar si es necesario que el login te envíe el anterior refresh token para invalidar
    @Transactional
    public LoginResponse loginUsuEdiRol(UUID idEdificio, HttpServletRequest httpRequest) {
        UUID idUsuario = securityUtils.getCurrentUserId();

        //  Verificar la relación de usuario con edificio
        if(!edificioClientWs.existsUsuarioEdificio(idEdificio).getBody().getData()){
            log.error("El usuario no pertenece a este edificio");
            throw new BusinessException("El usuario no pertenece a este edificio");
        }


        ApiResponse<List<RolResponse>> listaRoles;
        try {
            listaRoles = edificioClientWs.findRolesByUsuarioAndEdificio(idUsuario,idEdificio);
        }catch (FeignException.NotFound e) {
            log.error("No se encontraron roles para este usuario en el edificio seleccionado");
            throw new ResourceNotFoundException("No se encontraron roles para este usuario en el edificio seleccionado");
        } catch (FeignException e) {
            log.error("Error consumiendo edificio-service: status={} body={}", e.status(), e.contentUTF8());
            throw new ExternalServiceException("Error consumiendo edificio-service"+e);
        }

        UsuarioEntity usuarioEntity = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<String> listaRolesStr = listaRoles.getData().stream().map(rolResponse -> rolResponse.getNombre()).toList();

        if (listaRoles == null || listaRoles.getData() == null || listaRoles.getData().isEmpty()) {
            throw new BusinessException("El usuario no tiene roles en este edificio");
        }

        String accessToken = jwtService.generateToken(usuarioEntity,idEdificio,listaRolesStr);
        String refreshToken = jwtService.generateRefreshToken(usuarioEntity,idEdificio);

        RefreshTokenEntity rte = new RefreshTokenEntity();
        rte.setUsuarioId(usuarioEntity.getId());
        rte.setTokenHash(HashUtils.hashSha256(refreshToken));
        rte.setExpiracionAt(jwtService.extractExpiration(refreshToken));
        rte.setUsado(false);
        rte.setRevocado(false);
        rte.setDispositivo(requestUtils.getUserAgent(httpRequest));
        rte.setIp(requestUtils.getClientIp(httpRequest));

        RefreshTokenEntity rteSaved = refreshTokenRepository.save(rte);
        if (rteSaved.getId() == null) {
            throw new BusinessException("No se pudo registrar el refresh token");
        }
        log.info("Login exitoso");
        return new LoginResponse(accessToken, refreshToken, usuarioEntity.getId(), usuarioEntity.isPrimeraVez(),true);
    }
}
