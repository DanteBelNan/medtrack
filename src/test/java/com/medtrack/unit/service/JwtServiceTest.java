package com.medtrack.unit.service;

import com.medtrack.UnitTestBase;
import com.medtrack.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest extends UnitTestBase {

    private JwtService jwtService;

    private final String testSecret = Base64.getEncoder().encodeToString("clave-super-secreta-de-prueba-de-32-caracteres".getBytes());

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", testSecret);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000L); // 1 hora
    }

    @Test
    void shouldGenerateValidToken() {
        String email = "johndoe@email.com";

        String token = jwtService.generateToken(email);

        assertThat(token).isNotNull();
        assertThat(jwtService.extractUsername(token)).isEqualTo(email);
    }

    @Test
    void shouldValidateCorrectToken() {
        String email = "johndoe@email.com";
        String token = jwtService.generateToken(email);

        boolean isValid = jwtService.isTokenValid(token, email);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldFailWhenUsernameDoesNotMatch() {
        String email = "johndoe@email.com";
        String token = jwtService.generateToken(email);

        boolean isValid = jwtService.isTokenValid(token, "janedoe@email.com");

        assertThat(isValid).isFalse();
    }

    @Test
    void shouldExtractExpirationDate() {
        String token = jwtService.generateToken("johndoe@email.com");
        java.util.Date expiration = jwtService.extractClaim(token, io.jsonwebtoken.Claims::getExpiration);

        assertThat(expiration).isAfter(new java.util.Date());
    }
}