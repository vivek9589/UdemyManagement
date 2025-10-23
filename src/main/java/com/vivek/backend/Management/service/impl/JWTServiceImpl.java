package com.vivek.backend.Management.service.impl;

import com.vivek.backend.Management.dto.UserResponseDto;
import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.exception.UsernameNotFoundException;
import com.vivek.backend.Management.repository.UserRepository;
import com.vivek.backend.Management.service.JWTService;
import com.vivek.backend.Management.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class JWTServiceImpl implements JWTService {
    private static final Logger logger = LoggerFactory.getLogger(JWTServiceImpl.class);


    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @Value("${jwt.secret}")
    private String secretKey;

/*
    // constructor
    private JWTServiceImpl()
    {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }*/

/*
Jwts is a utility class from the Java JWT library (specifically io.jsonwebtoken.Jwts) used to create, parse, and validate JWT tokens in Java applications.
 */
@Override
public String generateToken(String email) {
    logger.info("Generating token for email: {}", email);

    User user = userRepository.findByEmail(email);
    if (user == null) {
        logger.error("User not found with email: {}", email);
        throw new UsernameNotFoundException("User not found with email: " + email);
    }

    Map<String, Object> claims = new HashMap<>();
    claims.put("firstName", user.getFirstName());
    claims.put("lastName", user.getLastName());
    claims.put("permissions", user.getRole().getPermissions().stream()
            .map(Enum::name)
            .collect(Collectors.toList()));
    claims.put("role", user.getRole().name());

    String token = Jwts.builder()
            .claims()
            .add(claims)
            .subject(email)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // 30 minutes
            .and()
            .signWith(getKey())
            .compact();

    logger.debug("Token generated successfully for email: {}", email);
    return token;
}

    public String createAccessToken(String email) {
        logger.info("Creating access token for email: {}", email);

        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.error("User not found with email: {}", email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("permissions", user.getRole().getPermissions().stream()
                .map(Enum::name)
                .collect(Collectors.toList()));

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // 10 minutes
                .signWith(getKey())
                .compact();

        logger.debug("Access token created for email: {}", email);
        return token;
    }

    public String createRefreshToken(String email) {
        logger.info("Creating refresh token for email: {}", email);

        String token = Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30 * 6)) // 6 months
                .signWith(getKey())
                .compact();

        logger.debug("Refresh token created for email: {}", email);
        return token;
    }

    public Long generateUserIdFromToken(String token) {
        try {
            Claims claim = Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            logger.debug("Extracted user ID from token");
            return Long.valueOf(claim.getSubject());
        } catch (Exception e) {
            logger.error("Failed to extract user ID from token", e);
            throw new JwtException("Invalid token");
        }
    }

    public SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String extractUserName(String token) {
        logger.debug("Extracting username from token");
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String extractFirstName(String token) {
        logger.debug("Extracting first name from token");
        return extractAllClaims(token).get("firstName", String.class);
    }

    @Override
    public String extractLastName(String token) {
        logger.debug("Extracting last name from token");
        return extractAllClaims(token).get("lastName", String.class);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        logger.debug("Validating token for user: {}", userDetails.getUsername());
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            logger.error("Failed to extract claims from token", e);
            throw new JwtException("Invalid token");
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


}
