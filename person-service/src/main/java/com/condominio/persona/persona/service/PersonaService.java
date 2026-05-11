package com.condominio.persona.persona.service;

import com.condominio.persona.common.exception.ResourceNotFoundException;
import com.condominio.persona.common.util.SecurityUtils;
import com.condominio.persona.persona.dto.request.PersonaRequest;
import com.condominio.persona.persona.dto.response.PersonaDetailResponse;
import com.condominio.persona.persona.dto.response.PersonaResponse;
import com.condominio.persona.persona.entity.PersonaEntity;
import com.condominio.persona.persona.repository.PersonaRepository;
import com.condominio.persona.tipodocumento.entity.TipoDocumentoEntity;
import com.condominio.persona.tipodocumento.repository.TipoDocumentoRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class PersonaService {
    private final PersonaRepository personaRepository;
    private final ModelMapper modelMapper;
    private final SecurityUtils securityUtils;
    private final TipoDocumentoRepository tipoDocumentoRepository;

    public PersonaService(PersonaRepository personaRepository, ModelMapper modelMapper, SecurityUtils securityUtils, TipoDocumentoRepository tipoDocumentoRepository) {
        this.personaRepository = personaRepository;
        this.modelMapper = modelMapper;
        this.securityUtils = securityUtils;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    @Transactional
    public PersonaResponse createPersona(PersonaRequest personaRequest) {
        log.info("Iniciando persona request");
        log.debug("Request : {}", personaRequest);

        //  procesamos dni
        TipoDocumentoEntity  tipDocEnt = tipoDocumentoRepository.findById(personaRequest.getTipoDocumento())
                .orElseThrow(()-> new ResourceNotFoundException("Tipo Documento no encontrado"));

        PersonaEntity personaEntity = modelMapper.map(personaRequest, PersonaEntity.class);
        personaEntity.setTipoDocumento(tipDocEnt);
        personaEntity.setCreatedBy(securityUtils.getCurrentUserId());
        PersonaEntity saved = personaRepository.save(personaEntity);

        return modelMapper.map(saved, PersonaResponse.class);
    }

    @Transactional
    public PersonaDetailResponse updatePersona(UUID id, PersonaRequest personaRequest) {
        log.info("Iniciando persona request update");
        log.debug("Request : {}", personaRequest);

        //  procesamos dni
        TipoDocumentoEntity  tipDocEnt = tipoDocumentoRepository.findById(personaRequest.getTipoDocumento())
                .orElseThrow(()-> new ResourceNotFoundException("Tipo Documento no encontrado"));

        PersonaEntity personaFind = personaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Persona a actualizar no encontrada"));

        modelMapper.map(personaRequest, personaFind);
        personaFind.setTipoDocumento(tipDocEnt);
        personaFind.setUpdatedBy(securityUtils.getCurrentUserId());
        PersonaEntity saved = personaRepository.save(personaFind);
        return modelMapper.map(saved, PersonaDetailResponse.class);
    }

    @Transactional
    public void deletePersona(UUID id) {
        log.info("Iniciando persona request delete");
        log.debug("Request : {}", id);

        PersonaEntity personaFind = personaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Persona a eliminar no encontrada"));
        personaRepository.delete(personaFind);
    }

    @Transactional(readOnly = true)
    public PersonaDetailResponse findPersona(UUID id) {
        log.info("Iniciando persona request find");
        log.debug("Request : {}", id);

        PersonaEntity personaFind = personaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Persona buscada no encontrada"));
        return modelMapper.map(personaFind, PersonaDetailResponse.class);
    }
}
