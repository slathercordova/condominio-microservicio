package com.condominio.auth.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityUtils {
    public UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(">>>>>>>>>>>>>>>> auth"+auth);
        System.out.println(auth.getPrincipal());
        System.out.println(auth.getCredentials());
        System.out.println(auth.getDetails());
        System.out.println(auth.getAuthorities());

        if (auth == null
                || !auth.isAuthenticated()
                ||  auth.getPrincipal().equals("anonymousUser")) {
            return UUID.fromString("11111111-1111-1111-1111-111111111111");
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof UUID uuid) {
            return uuid;
        }
        return null;
    }
}
