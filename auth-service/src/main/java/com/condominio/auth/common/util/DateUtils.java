package com.condominio.auth.common.util;

import java.time.*;

public class DateUtils {
    private static final ZoneId DEFAULT_ZONE = ZoneId.of("America/Lima");

    private DateUtils() {
        throw new UnsupportedOperationException("Clase utilitaria - no instanciable");
    }

    public static Instant nowInstant(){
        return Instant.now();
    }

    public static LocalDate today(){
        return LocalDate.now(DEFAULT_ZONE);
    }

    public static LocalDateTime nowDateTime(){
        return LocalDateTime.now(DEFAULT_ZONE);
    }

    public static ZonedDateTime nowZoned(){
        return ZonedDateTime.now(DEFAULT_ZONE);
    }

    public static ZonedDateTime nowInZone(ZoneId zone) {
        if (zone == null) {
            throw new IllegalArgumentException("Zona horaria obligatoria");
        }
        return ZonedDateTime.now(zone);
    }

    public static OffsetDateTime nowOffset() {
        return  OffsetDateTime.now(DEFAULT_ZONE);
    }

    public static OffsetDateTime nowOffsetUTC() {
        return OffsetDateTime.now(ZoneOffset.UTC);
    }
}
