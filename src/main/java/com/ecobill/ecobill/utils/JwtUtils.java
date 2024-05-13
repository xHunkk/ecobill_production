package com.ecobill.ecobill.utils;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    public byte[] generateSecretKey(int keyLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[keyLength];
        secureRandom.nextBytes(key);
        return key;
    }

    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateToken(String id) {
        long expirationTimeInMillieSecondes = 86400000;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTimeInMillieSecondes);

        String token = Jwts.builder()
                .setSubject("Customer")
                .claim("Id", id)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();

        return token;

    }

}
