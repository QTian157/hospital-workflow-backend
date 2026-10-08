package com.tq.hospitalequipmenttracking.service;

import com.tq.hospitalequipmenttracking.model.UserAccount;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secrete;
    @Value("${jwt.expiration}")
    private long expiration;

    // generate toke
    public String generateToken(UserAccount user) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secrete.getBytes());

        return Jwts.builder()
                .subject(user.getUsername())
                .claim(
                        "userRole",
                        user.getUserRole().name() // UseRole is Enum, so not toString()
                )
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + expiration
                        )
                )
                .signWith(secretKey)
                .compact();
    }

    // get UserName

    public String extractUsername(String token){
        return Jwts.parser()
                .verifyWith(
                        Keys.hmacShaKeyFor(secrete.getBytes())
                )
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // check if token is valid or not
    public boolean isTokenValid(String token, UserAccount user) {
        String username = extractUsername(token);
        return username.equals(user.getUsername());

    }
}
