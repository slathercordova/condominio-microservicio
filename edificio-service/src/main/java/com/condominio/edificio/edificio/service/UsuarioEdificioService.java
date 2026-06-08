package com.condominio.edificio.edificio.service;

import com.condominio.edificio.common.util.SecurityUtils;
import com.condominio.edificio.edificio.dto.response.ListaEdificiosXUsuarioResponse;
import com.condominio.edificio.edificio.repository.UsuarioEdificioRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class UsuarioEdificioService {
    private final UsuarioEdificioRepository usuarioEdificioRepository;
    private final ModelMapper modelMapper;
    private final SecurityUtils securityUtils;

    public UsuarioEdificioService(UsuarioEdificioRepository usuarioEdificioRepository, ModelMapper modelMapper, SecurityUtils securityUtils) {
        this.usuarioEdificioRepository = usuarioEdificioRepository;
        this.modelMapper = modelMapper;
        this.securityUtils = securityUtils;
    }

    @Transactional(readOnly = true)
    public List<ListaEdificiosXUsuarioResponse> listaEdificiosDeUsuario() {
        List<ListaEdificiosXUsuarioResponse> listaEdificios = usuarioEdificioRepository.listaEdificiosPorUsuario(securityUtils.getCurrentUserId());
        return listaEdificios;
    }
}
