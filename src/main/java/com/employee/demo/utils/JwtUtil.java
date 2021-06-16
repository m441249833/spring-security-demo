package com.employee.demo.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.employee.demo.security.SecurityUser;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.HashMap;

public class JwtUtil  {

    private static long TOKEN_EXPIRY_TIME = 86400000;

    private static String TOKEN_SECRET = "my-system";

    private static String ISSUER = "MK";

    /**
     * JWT token creation based on user details
     * @param user
     * @return
     */
    public static String sign(SecurityUser user){
        Gson gson = new Gson();
        //Expire date
        Date date = new Date(System.currentTimeMillis() + TOKEN_EXPIRY_TIME);

        //Token algorithm
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);

        //header info of jwt
        HashMap<String,Object> jwtHeader = new HashMap<>(2);
        jwtHeader.put("alg","HS256");
        jwtHeader.put("typ","JWT");

        return JWT.create().withHeader(jwtHeader)
                .withIssuer(ISSUER)
                .withClaim("username",user.getUsername())
                .withClaim("email",user.getUserInfo().getEmail())
                .withClaim("auth",gson.toJson(user.getAuthorities()))
                .withExpiresAt(date)
                .sign(algorithm);

    }

    public static String getUsernameByToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("username").asString();
        }catch (IllegalArgumentException | JWTVerificationException e){
            e.printStackTrace();
            return null;
        }
    }


}
