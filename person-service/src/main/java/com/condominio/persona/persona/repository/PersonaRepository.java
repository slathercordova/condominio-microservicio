package com.condominio.persona.persona.repository;

import com.condominio.persona.persona.entity.PersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface PersonaRepository extends JpaRepository<PersonaEntity, UUID>, JpaSpecificationExecutor<PersonaEntity> {
    boolean existsByCorreo(String correo);
    boolean existsByCorreoAndIdNot(String correo, UUID id);
}