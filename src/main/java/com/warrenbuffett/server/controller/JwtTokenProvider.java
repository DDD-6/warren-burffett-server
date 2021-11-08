package com.warrenbuffett.server.controller;

import com.warrenbuffett.server.common.exception.CustomJwtRuntimeException;
import com.warrenbuffett.server.controller.user.UserDto;
import com.warrenbuffett.server.domain.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

//    @Value("${jwt.secret}")
//    private String secretKey;
//    @Value("${jwt.tokenPrefix}")
//    private String tokenPrefix;

    private String secretKey="secretkey";
    private String tokenPrefix="Bearer";
    private final Long expiredTime = 1000 * 60L * 60L * 3L; // 유효기간 3시간


    public String generateToken(User user) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(user.getUser_name())
                .setHeader(createHeader())
                .setClaims(createClaims(user))
                .setIssuedAt(now)  // 발급 시간
                .setExpiration(new Date(now.getTime()+ Duration.ofHours(3).toMillis())) // 유효기간 3시간
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ","JWT");
        header.put("alg","HS256"); // 해시 256 암호화
        header.put("regDate",System.currentTimeMillis());
        return header;
    }

    private Map<String, Object> createClaims(User user) { // payload
        Map<String, Object> claims = new HashMap<>();
        claims.put("id",user.getId());
        claims.put("email",user.getEmail());
        return claims;
    }

    private Claims getClaims(String token) {
        try{
            return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        // 유효한 토큰인지 인증
        } catch (SecurityException e) {
            //log.info("Invalid JWT signature.");
            throw new CustomJwtRuntimeException();
        } catch (MalformedJwtException e) {
            //log.info("Invalid JWT token.");
            throw new CustomJwtRuntimeException();
        } catch (ExpiredJwtException e) {
            //log.info("Expired JWT token.");
            throw new CustomJwtRuntimeException();
        } catch (UnsupportedJwtException e) {
            //log.info("Unsupported JWT token.");
            throw new CustomJwtRuntimeException();
        } catch (IllegalArgumentException e) {
            //log.info("JWT token compact of handler are invalid.");
            throw new CustomJwtRuntimeException();
        }
    }
    public String getUserEmailFromToken(String token) {
        return (String) getClaims(token).get("email");
    }

    // Security 인가 정보 확인
    public UserDto getUserDtoOf(String authorizationHeader) {
        validationAuthorizationHeader(authorizationHeader);

        String token = extractToken(authorizationHeader);
        Claims claims = getClaims(token);
        return new UserDto(claims);
    }

    private void validationAuthorizationHeader(String header) {
        if (header == null || !header.startsWith(tokenPrefix)) {
            throw new IllegalArgumentException();
        }
    }

    private String extractToken(String authorizationHeader) {
        return authorizationHeader.substring(tokenPrefix.length());
    }
}
