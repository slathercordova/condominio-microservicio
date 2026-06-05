package com.condominio.auth.auth.repository;

import com.condominio.auth.auth.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, UUID>, JpaSpecificationExecutor<UsuarioEntity> {
    boolean existsByCorreo(String correo);
    boolean existsByUsername(String username);
    Optional<UsuarioEntity> findByUsername(String username);
    boolean existsByIdPersona(UUID idPersona);
    Optional<UsuarioEntity> findByCorreo(String correo);
    Optional<UsuarioEntity> findById(UUID id);
}
