package com.server.ticketmanagement.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public final class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private JwtUtil() {

    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return createToken(claims, userDetails.getUsername(), roles);
    }

    private String createToken(Map<String, Object> claims, String email, List<String> roles) {


        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .claim("roles", roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Token valid for 10 hours
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

    }

    public static UUID parseUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }


}
