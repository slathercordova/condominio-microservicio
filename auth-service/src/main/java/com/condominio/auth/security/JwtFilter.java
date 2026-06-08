package com.condominio.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
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
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Se requiere access token");
                return;
            }

            String username = jwtService.extractUsername(token);
            UUID userId = jwtService.extractUserId(token);
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(token, userDetails)){
                    List<String> roles = jwtService.extractRoles(token);
                    List<GrantedAuthority> authorities = roles.stream()
                            .map(rol -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_"+rol)).toList();
                    UsernamePasswordAuthenticationToken auth
                            = new UsernamePasswordAuthenticationToken(userId,null, authorities);
                    log.info("Roles de JwtFilter token>>> {}", authorities);
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
