package com.electronic.store.Electronic_Store.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//Utility class for generating and validating JWT tokens.

@Component
public class JwtUtil {

    public static final String SECRET_KEY ="SecretKeyOfMyElectronicStoreProject1234567890";

    // ⏱️ 24 hours token validity
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    // Extracts all claims from the JWT token
    private Claims extractAllClaims(String token){
        return Jwts.parser()  // Step 1: Start building the parser
                .verifyWith((SecretKey) getSignKey())   // Step 2: Set the secret key for verifying token[Real Token entered here, to which we need to compare token came from user]
                .build()    // Step 3: Finish building the parser
                .parseSignedClaims(token)   // Step 4: Actually decode and verify the token
                .getPayload();      // Step 5: Get the actual data (claims) from token
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    //Retrieve expiration date from jwt token
    public Date getExpirationDate(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    //Check if token has expired
    public Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDate(token);
        return expiration.before(new Date());
    }

    //Generate Token
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()   //Start creating a JWT (like saying "let's make a new token")
                .claims(claims) //Adds custom claims (key-value pairs) into the token payload.
                .subject(userDetails.getUsername())   // Put the username on the token
                .issuedAt(new Date(System.currentTimeMillis()))  // Write down current time (token made now)
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))  // Write expiration time (like "this token is good for next 24 hours")
                .signWith(getSignKey())     //Insert secret key in token (so no one can fake it)
                .compact();     // Finish and give me the token as a string
    }

    // Key from secret
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
