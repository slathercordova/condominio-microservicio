package com.condominio.edificio.edificio.repository;

import com.condominio.edificio.edificio.entity.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface EmpresaRepository extends JpaRepository<EmpresaEntity, UUID>, JpaSpecificationExecutor<EmpresaEntity> {
}
