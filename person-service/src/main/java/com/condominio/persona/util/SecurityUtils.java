package com.condominio.persona.util;

import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class SecurityUtils {
    public UUID getCurrentUserId() {
        return UUID.fromString("11111111-1111-1111-1111-111111111111");
    }
}
