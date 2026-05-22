package com.condominio.auth.auth.repository;

import com.condominio.auth.auth.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID>, JpaSpecificationExecutor<RefreshTokenEntity> {
    Optional<RefreshTokenEntity> findByTokenHash(String tokenHash);

    @Modifying
    @Query(""" 
        UPDATE RefreshTokenEntity r 
                SET
                         r.revocado = true,
                         r.updatedBy = :updatedBy,
                         r.updatedAt = CURRENT_TIMESTAMP
                WHERE
                        r.usuarioId = :usuarioId
        """)
    int revokeAllByUsuarioId(@Param("usuarioId") UUID usuarioId, @Param("updatedBy") UUID updatedBy);
}
