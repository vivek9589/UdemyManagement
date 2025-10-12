package com.vivek.backend.Management.service.impl;

import com.vivek.backend.Management.dto.UserResponseDto;
import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.repository.UserRepository;
import com.vivek.backend.Management.service.JWTService;
import com.vivek.backend.Management.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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


@Service
public class JWTServiceImpl implements JWTService {

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


   public String generateToken(String email) {

        Map<String,Object> claims = new HashMap<>();

        User user = userRepository.findByEmail(email);


       claims.put("firstName", user.getFirstName()); // Add custom claim
       claims.put("lastName", user.getLastName());   // Add custom claim
       //claims.put("role", user.getRole());           // Add custom claim


       return Jwts.builder()
                .claims()
                .add(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // 30 minutes
                .and()
                .signWith(getKey())
                .compact();
    }


    public String createAccessToken(String email) {
        Map<String, Object> claims = new HashMap<>();

        User user = userRepository.findByEmail(email);

        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());

        return Jwts.builder()
                .setClaims(claims) // ✅ Correct way to set claims
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) // 10 mins
                .signWith(getKey()) // ✅ Explicit algorithm required
                .compact();
    }
    public String createRefreshToken(String email) {    //create refresh token
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ 1000L *60*60*24*30*6)) //here we have to set it for long time 6 months
                .signWith(getKey())
                .compact();

    }


    public Long generateUserIdFromToken(String token) {
        Claims claim = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.valueOf(claim.getSubject());
    }


    public SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }



    @Override
    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }



    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public String extractFirstName(String token) {
        return extractAllClaims(token).get("firstName", String.class);

    }

    @Override
    public String extractLastName(String token) {
        return extractAllClaims(token).get("lastName", String.class);

    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }



    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }



}
