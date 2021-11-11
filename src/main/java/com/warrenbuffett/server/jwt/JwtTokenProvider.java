package com.warrenbuffett.server.jwt;

import com.warrenbuffett.server.controller.dto.TokenDto;
import com.warrenbuffett.server.controller.dto.UserDto;
import com.warrenbuffett.server.domain.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
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

//    private Claims parseClaims(String token) {
//        try{
//            return Jwts.parser()
//                .setSigningKey(jwtProperties.getSecret())
//                .parseClaimsJws(token)
//                .getBody();
//        } catch (SecurityException e) {
//            //log.info("Invalid JWT signature.");
//            throw new CustomJwtRuntimeException();
//        } catch (MalformedJwtException e) {
//            //log.info("Invalid JWT token.");
//            throw new CustomJwtRuntimeException();
//        } catch (ExpiredJwtException e) {
//            //log.info("Expired JWT token.");
//            throw new CustomJwtRuntimeException();
//        } catch (UnsupportedJwtException e) {
//            //log.info("Unsupported JWT token.");
//            throw new CustomJwtRuntimeException();
//        } catch (IllegalArgumentException e) {
//            //log.info("JWT token compact of handler are invalid.");
//            throw new CustomJwtRuntimeException();
//        }
//    }
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token);
            return true;
        // 유효한 토큰인지 인증
        } catch (SecurityException | MalformedJwtException e) {
//            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
//            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
//            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
//            log.info("JWT 토큰이 잘못되었습니다.");
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
        // 토큰 복호화
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
    public String getUserEmailFromToken(String token) {
        return (String) parseClaims(token).get("email");
    }
    public Claims getTokenData(String token) {
        return parseClaims(token);
    }

    // Security filter 정보 확인
    public UserDto getUserDtoOf(String authorizationHeader) {
        validationAuthorizationHeader(authorizationHeader);

        String token = extractToken(authorizationHeader);
        System.out.println(token);
        Claims claims = parseClaims(token);
        return new UserDto(claims);
    }

    private void validationAuthorizationHeader(String header) {
        if (header == null || !header.startsWith(jwtProperties.getTokenPrefix())) {
//            logger.error("JWT Token is either missing from HTTP header or has been provided in an incorrect format");
            throw new AuthenticationCredentialsNotFoundException(
                    "JWT Token is either missing from HTTP header or has been provided in an incorrect format!");
//            throw new IllegalArgumentException();
        }
    }

    private String extractToken(String authorizationHeader) {
        return authorizationHeader.substring(jwtProperties.getTokenPrefix().length());
    }
}
