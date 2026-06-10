package com.condominio.edificio.edificio.service;

import com.condominio.edificio.common.exception.ExternalServiceException;
import com.condominio.edificio.common.exception.ResourceNotFoundException;
import com.condominio.edificio.common.response.ApiResponse;
import com.condominio.edificio.common.security.JwtService;
import com.condominio.edificio.common.util.SecurityUtils;
import com.condominio.edificio.edificio.dto.request.EmpresaRequest;
import com.condominio.edificio.edificio.dto.response.EmpresaResponse;
import com.condominio.edificio.edificio.dto.response.ConsultaSunatResponse;
import com.condominio.edificio.edificio.dto.response.PersonaDetailResponse;
import com.condominio.edificio.edificio.entity.EmpresaEntity;
import com.condominio.edificio.edificio.repository.EmpresaRepository;
import com.condominio.edificio.feignclient.PersonaClientWs;
import com.condominio.edificio.feignclient.SunatClientWs;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class EmpresaService {
    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;
    private final SecurityUtils securityUtils;
    private final JwtService jwtService;
    private final PersonaClientWs personaClientWs;
    private final SunatClientWs sunatClientWs;
    @Value("${api.token}")
    private String apiToken;

    public EmpresaService(EmpresaRepository empresaRepository, ModelMapper modelMapper, SecurityUtils securityUtils, JwtService jwtService, PersonaClientWs personaClientWs, SunatClientWs sunatClientWs) {
        this.empresaRepository = empresaRepository;
        this.modelMapper = modelMapper;
        this.securityUtils = securityUtils;
        this.jwtService = jwtService;
        this.personaClientWs = personaClientWs;
        this.sunatClientWs = sunatClientWs;
    }

    @Transactional
    public EmpresaResponse create(EmpresaRequest empresaRequest) {
        log.info("Creando empresa con RUC {}",empresaRequest.toString());
        ApiResponse<PersonaDetailResponse> persona;
        try {
            persona = personaClientWs.findPersonaPorDocumento(empresaRequest.getTipoDocumentoRepre(), empresaRequest.getNumeroDocumentoRepre());
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException(
                    "Representante no se encuentra como persona"
            );
        } catch (FeignException e) {
            throw new ExternalServiceException(
                    "Error consumiendo persona-service"
            );
        }
        log.info("Consulta interna client ws {}",persona.getData());

        if (persona == null || persona.getData() == null) {
            throw new ResourceNotFoundException("Representante no se encuentra como persona");
        }

        ConsultaSunatResponse sunatResponseDto;
        try {
            sunatResponseDto = sunatClientWs.getSunat(empresaRequest.getRuc(),apiToken);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException(
                    "No se encontraron datos del RUC ingresado"
            );
        } catch (FeignException e) {
            log.error("MESSAGE={}", e.getMessage());
            log.error("STATUS={}", e.status());
            log.error("BODY={}", e.contentUTF8());
            log.error("ERROR COMPLETO", e);
            e.printStackTrace();
            Throwable cause = e.getCause();
            while (cause != null) {
                log.error("CAUSE={}", cause.getMessage(), cause);
                cause = cause.getCause();
            }
            throw e;
//            throw new ExternalServiceException(
//                    "Error consumiendo servicio SUNAT"
//            );
        }
        log.info("Consulta sunat ws {}",sunatResponseDto.toString());

        if (sunatResponseDto == null) {
            throw new ResourceNotFoundException("No se encontraron datos del ruc ingresado");
        }

        EmpresaEntity ee = new EmpresaEntity();
        ee.setRazonSocial(sunatResponseDto.razonSocial());
        ee.setDireccion(sunatResponseDto.direccion());
        ee.setRuc(sunatResponseDto.numeroDocumento());
        ee.setTelefono(empresaRequest.getTelefono());
        ee.setCelular(empresaRequest.getCelular());
        ee.setCorreo(empresaRequest.getCorreo());
        ee.setIdRepresentante(persona.getData().getId());
        ee.setLogoUrl(empresaRequest.getLogoUrl());
        ee.setEstado(true);

        EmpresaEntity empresaSaved = empresaRepository.save(ee);

        return modelMapper.map(empresaSaved, EmpresaResponse.class);
    }
}
