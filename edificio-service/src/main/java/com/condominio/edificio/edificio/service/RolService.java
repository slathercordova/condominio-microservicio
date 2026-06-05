package com.condominio.edificio.edificio.service;

import com.condominio.edificio.common.exception.ResourceAlreadyExistsException;
import com.condominio.edificio.common.exception.ResourceNotFoundException;
import com.condominio.edificio.edificio.dto.request.RolRequest;
import com.condominio.edificio.edificio.dto.response.RolResponse;
import com.condominio.edificio.edificio.entity.RolEntity;
import com.condominio.edificio.edificio.repository.RolRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class RolService {
    private final RolRepository rolRepository;
    private final ModelMapper modelMapper;

    public RolService(RolRepository rolRepository, ModelMapper modelMapper) {
        this.rolRepository = rolRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public RolResponse createRol(RolRequest rolRequest){
        if (rolRepository.existsByNombreIgnoreCase(rolRequest.getNombre())){
            throw new ResourceAlreadyExistsException("El rol ya existe");
        }

        RolEntity rolEntity = modelMapper.map(rolRequest, RolEntity.class);
        rolEntity.setNombre(rolRequest.getNombre().toUpperCase());
        RolEntity rolSaved = rolRepository.save(rolEntity);
        log.info("Rol creado correctamente. Id={}", rolSaved.getId());
        return modelMapper.map(rolSaved, RolResponse.class);
    }

    @Transactional
    public RolResponse updateRol(UUID id, RolRequest rolRequest){
        if (rolRepository.existsByNombreIgnoreCaseAndIdNot(rolRequest.getNombre(),id)){
            throw new ResourceAlreadyExistsException("Ya existe un rol con ese nombre");
        }
        RolEntity rolEntity = rolRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Registro no encontrado"));

        modelMapper.map(rolRequest, rolEntity);
        rolEntity.setNombre(rolEntity.getNombre().toUpperCase());
        rolRepository.save(rolEntity);
        return modelMapper.map(rolEntity, RolResponse.class);
    }

    @Transactional
    public void deleteRol(UUID id){
        RolEntity rolEntity = rolRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Registro no encontrado"));
        rolRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<RolResponse> getAllRol(){
        List<RolResponse> listaResponse = rolRepository.findAll(Sort.by(Sort.Direction.ASC,"nombre"))
                .stream()
                .map(aea-> modelMapper.map(aea, RolResponse.class))
                .toList();
        return listaResponse;
    }

    @Transactional(readOnly = true)
    public RolResponse getRol(UUID id){
        RolEntity rolEntity = rolRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Rol no encontrado"));

        return modelMapper.map(rolEntity, RolResponse.class);
    }
}
