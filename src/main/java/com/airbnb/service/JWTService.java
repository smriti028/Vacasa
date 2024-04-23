package com.airbnb.service;

import com.airbnb.entity.PropertyUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {
    @Value("${jwt.algorithm.key}")
    private String algorithmKey;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiry.duration}")
    private int expiryTime;
    private Algorithm algorithm;

    private static final String USER_NAME="username";

    //@PostConstruct- when we start our application it will run automatically, and it will run only once.
    @PostConstruct
    public void postConstruct(){
        algorithm = Algorithm.HMAC256(algorithmKey);
    }

    public String generateToken(PropertyUser propertyUser){
        //this line will the token and return the token[TEkNIK TO RMMBR TOKEN -CEIS-
        // CEIS- Computer Engineer Is Seeking job]
       return JWT.create().
                withClaim(USER_NAME,propertyUser.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+expiryTime))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String getUserName(String token){
        DecodedJWT decodedJwt = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        return decodedJwt.getClaim(USER_NAME).asString();
    }
}
