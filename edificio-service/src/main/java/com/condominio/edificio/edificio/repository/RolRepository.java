package com.condominio.edificio.edificio.repository;

import com.condominio.edificio.edificio.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface RolRepository extends JpaRepository<RolEntity, UUID> , JpaSpecificationExecutor<RolEntity> {
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre,UUID id);
    Optional<RolEntity> findById(UUID id);
}
