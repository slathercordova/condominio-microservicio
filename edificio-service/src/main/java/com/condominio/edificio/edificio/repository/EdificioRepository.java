package com.condominio.edificio.edificio.repository;

import com.condominio.edificio.edificio.entity.EdificioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface EdificioRepository extends JpaRepository<EdificioEntity, UUID>, JpaSpecificationExecutor<EdificioEntity> {
    boolean existsById(UUID id);
}
