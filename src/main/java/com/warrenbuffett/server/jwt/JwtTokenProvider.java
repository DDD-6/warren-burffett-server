package com.warrenbuffett.server.jwt;

import com.warrenbuffett.server.controller.dto.TokenDto;
import com.warrenbuffett.server.domain.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = Duration.ofHours(9).toMillis(); // 9시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일
    private static final String AUTHORITIES_KEY = "auth";


    public TokenDto generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date now = new Date();
        Date accessTokenExpiresIn = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken =  Jwts.builder()
                .setSubject(authentication.getName())
                .setHeader(createHeader())
                .claim(AUTHORITIES_KEY,authorities)
                .setIssuedAt(now)  // 발급 시간
                .setExpiration(new Date(now.getTime()+ ACCESS_TOKEN_EXPIRE_TIME)) // 유효기간 3시간
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256,jwtProperties.getSecret())
                .compact();

        return TokenDto.builder()
                .grantType(jwtProperties.getTokenPrefix())
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ","JWT");
        header.put("alg","HS256"); // 해시 256 암호화
        return header;
    }

    private Map<String, Object> createClaims(User user) { // payload
        Map<String, Object> claims = new HashMap<>();
        claims.put("id",user.getId());
        claims.put("email",user.getEmail());
        claims.put(AUTHORITIES_KEY,"ROLE_USER");
        return claims;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token);
            return true;
        // 유효한 토큰인지 인증
        } catch (SecurityException | MalformedJwtException e) {
//            log.info("Invalid JWT token.");
        } catch (SignatureException e) {
//            log.info("Invalid JWT signature.");
        } catch (ExpiredJwtException e) {
//            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
//            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
//            log.info("JWT token compact of handler are invalid.");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
}
