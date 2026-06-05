package com.condominio.edificio.edificio.service;

import com.condominio.edificio.common.exception.ExternalServiceException;
import com.condominio.edificio.common.exception.ResourceAlreadyExistsException;
import com.condominio.edificio.common.exception.ResourceNotFoundException;
import com.condominio.edificio.common.response.ApiResponse;
import com.condominio.edificio.edificio.dto.request.UsuarioEdificioRolRequest;
import com.condominio.edificio.edificio.dto.response.RegisterResponse;
import com.condominio.edificio.edificio.dto.response.RolResponse;
import com.condominio.edificio.edificio.dto.response.UsuarioEdificioRolResponse;
import com.condominio.edificio.edificio.entity.UsuarioEdificioRolEntity;
import com.condominio.edificio.edificio.repository.EdificioRepository;
import com.condominio.edificio.edificio.repository.RolRepository;
import com.condominio.edificio.edificio.repository.UsuarioEdificioRolRepository;
import com.condominio.edificio.feignclient.AuthClientWs;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UsuarioEdificioRolService {
    private final UsuarioEdificioRolRepository usuarioEdificioRolRepository;
    private final RolRepository rolRepository;
    private final EdificioRepository edificioRepository;
    private final AuthClientWs  authClientWs;
    private final ModelMapper modelMapper;

    public UsuarioEdificioRolService(UsuarioEdificioRolRepository usuarioEdificioRolRepository, RolRepository rolRepository, EdificioRepository edificioRepository, AuthClientWs authClientWs, ModelMapper modelMapper) {
        this.usuarioEdificioRolRepository = usuarioEdificioRolRepository;
        this.rolRepository = rolRepository;
        this.edificioRepository = edificioRepository;
        this.authClientWs = authClientWs;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public UsuarioEdificioRolResponse create(UsuarioEdificioRolRequest usuarioEdificioRolRequest) {
        if (usuarioEdificioRolRepository.existsByIdUsuarioAndIdEdificioAndIdRol(
                usuarioEdificioRolRequest.getIdUsuario(),
                usuarioEdificioRolRequest.getIdEdificio(),
                usuarioEdificioRolRequest.getIdRol()
        )){
            throw new ResourceAlreadyExistsException("El Usuario ya tiene ese rol asignado en el edificio");
        }

        //  Validar que exista edificio
        if (!edificioRepository.existsById(usuarioEdificioRolRequest.getIdEdificio())){
            throw new ResourceNotFoundException("El edificio no existe");
        }

        //  Validar que exista rol
        if (!rolRepository.existsById(usuarioEdificioRolRequest.getIdRol())){
            throw new ResourceNotFoundException("El rol no existe");
        }

        //  Validar que exista usuario
        ApiResponse<RegisterResponse> rr = null;
        try {
             rr = authClientWs.findAuthById(usuarioEdificioRolRequest.getIdUsuario());
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("El usuario no existe");
        } catch (FeignException e){
            throw new ExternalServiceException("Error consumiendo auth-service");
        }

        if (rr == null){
            throw new ResourceNotFoundException("El Usuario no existe");
        }

        UsuarioEdificioRolEntity uere =  modelMapper.map(usuarioEdificioRolRequest, UsuarioEdificioRolEntity.class);
        uere.setEstado(true);
        UsuarioEdificioRolEntity saved = usuarioEdificioRolRepository.save(uere);
        return modelMapper.map(saved, UsuarioEdificioRolResponse.class);
    }

    @Transactional
    public void delete(UUID id) {
        UsuarioEdificioRolEntity uere = usuarioEdificioRolRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Registro no encontrado"));

        uere.setEstado(false);
        usuarioEdificioRolRepository.save(uere);
    }


    @Transactional(readOnly = true)
    public List<UsuarioEdificioRolResponse> findAll() {
        List<UsuarioEdificioRolResponse> listaUer = usuarioEdificioRolRepository.findAll()
                .stream()
                .map(i-> modelMapper.map(i, UsuarioEdificioRolResponse.class))
                .toList();
        return listaUer;
    }

    @Transactional(readOnly = true)
    public UsuarioEdificioRolResponse find(UUID id) {
        UsuarioEdificioRolEntity uere = usuarioEdificioRolRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Registro no encontrado"));

        return modelMapper.map(uere, UsuarioEdificioRolResponse.class);
    }

    @Transactional(readOnly = true)
    public List<RolResponse> findRolesByUsuarioAndEdificio(UUID idUsuario, UUID idEdificio) {
        List<RolResponse> listaRoles = usuarioEdificioRolRepository.findRolesByUsuarioAndEdificio(idUsuario, idEdificio);
        return listaRoles;
    }
}
