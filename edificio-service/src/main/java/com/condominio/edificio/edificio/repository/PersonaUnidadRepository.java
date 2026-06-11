package com.condominio.edificio.edificio.repository;

import com.condominio.edificio.edificio.entity.PersonaUnidadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface PersonaUnidadRepository extends JpaRepository<PersonaUnidadEntity, UUID>, JpaSpecificationExecutor<PersonaUnidadEntity> {
    boolean existsByIdUnidadAndEsResponsableTrueAndEstadoTrue(UUID idUnidad);
    boolean existsByIdUnidadAndEsResponsableTrueAndEstadoTrueAndIdNot(UUID idUnidad, UUID id);
}
