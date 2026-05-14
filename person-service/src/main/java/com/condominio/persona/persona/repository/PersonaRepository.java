package com.condominio.persona.persona.repository;

import com.condominio.persona.persona.entity.PersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface PersonaRepository extends JpaRepository<PersonaEntity, UUID>, JpaSpecificationExecutor<PersonaEntity> {
    boolean existsByCorreo(String correo);
    boolean existsByCorreoAndIdNot(String correo, UUID id);
    boolean existsByTipoDocumentoIdAndNumeroDocumentoAndIdNot(UUID tipoDocumento, String numeroDocumento, UUID id);
    boolean existsByTipoDocumentoIdAndNumeroDocumento(UUID tipoDocumento, String numeroDocumento);
    Optional<PersonaEntity> findByTipoDocumentoIdAndNumeroDocumento(UUID tipoDocumento, String numeroDocumento);
}