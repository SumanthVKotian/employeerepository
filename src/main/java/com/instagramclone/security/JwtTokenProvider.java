package com.instagramclone.security;

import com.instagramclone.exception.APIException;
import io.jsonwebtoken.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private String jwtSecret = "someSecretKeyOrRandomString";

    /**** Token generation ****/

    public String generateToken(@NotNull Authentication authentication) {

        String username = authentication.getName();

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact();


    }

    /**** abstracting username form token ****/

    public String getUserNameFromJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }


    /**** validating token ****/

    public boolean validateToken(String token) {
        try{
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }catch (SignatureException ex){
            throw new APIException(HttpStatus.BAD_REQUEST,"Invalid JWT signature");
        }catch (MalformedJwtException ex){
            throw new APIException(HttpStatus.BAD_REQUEST,"Invalid JWT token");
        }catch (ExpiredJwtException ex){
            throw new APIException(HttpStatus.BAD_REQUEST,"Expired JWT token");
        }catch (UnsupportedJwtException ex){
            throw new APIException(HttpStatus.BAD_REQUEST,"Unsupported JWT token");
        }catch (IllegalArgumentException ex){
            throw new APIException(HttpStatus.BAD_REQUEST,"JWT claims string is empty");
        }catch (Exception ex){
            throw new APIException(HttpStatus.BAD_REQUEST,ex.getMessage());
        }
    }


}
