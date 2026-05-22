package com.condominio.persona.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityUtils {
    public UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(">>>>>>>>>>>>>>>> persona "+auth);
        System.out.println(auth.getPrincipal());
        System.out.println(auth.getCredentials());
        System.out.println(auth.getDetails());
        System.out.println(auth.getAuthorities());

        if (auth == null
                || !auth.isAuthenticated()
                ||  "anonymousUser".equals(auth.getPrincipal())) {
            return DatosConstant.MASTER;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof UUID uuid) {
            return uuid;
        }
        return null;
    }
}
