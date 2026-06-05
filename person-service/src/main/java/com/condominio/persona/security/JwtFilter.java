package com.condominio.persona.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (!jwtService.isAccessToken(token)){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String username = jwtService.extractUsername(token);
            UUID userId = jwtService.extractUserId(token);
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null){
                if (jwtService.isTokenValid(token)){
                    UsernamePasswordAuthenticationToken auth
                            = new UsernamePasswordAuthenticationToken(userId, null, List.of());

                    SecurityContext securityContext = SecurityContextHolder.getContext();
                    securityContext.setAuthentication(auth);
                    SecurityContextHolder.setContext(securityContext);
                }
            }
        }catch (Exception e){
            //  token invalido
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
