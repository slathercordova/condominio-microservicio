package com.condominio.edificio.common.audit;

import com.condominio.edificio.common.util.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("auditorAware")
public class AuditorAwareImpl implements AuditorAware<UUID> {

    private final SecurityUtils securityUtils;

    public AuditorAwareImpl(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    @Override
    public Optional<UUID> getCurrentAuditor() {
        return Optional.ofNullable(securityUtils.getCurrentUserId());
    }
}