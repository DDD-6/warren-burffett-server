package com.warrenbuffett.server.service;

import com.warrenbuffett.server.common.SecurityUtil;
import com.warrenbuffett.server.controller.dto.*;
import com.warrenbuffett.server.domain.RefreshToken;
import com.warrenbuffett.server.jwt.JwtTokenProvider;
import com.warrenbuffett.server.domain.User;
import com.warrenbuffett.server.repository.RefreshTokenRepository;
import com.warrenbuffett.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll(){
        return userRepository.findAll().stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }
    public User searchUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public User searchUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public boolean deleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user!=null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }
    @Transactional
    public User resetUserPassword(PasswordResetRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElse(null);
        user.setPassword(passwordEncoder.encode(requestDto.getNewpassword()));
        return userRepository.save(user);
    }

    @Transactional
    public TokenDto loginUser(LoginRequestDto loginRequestDto) {
        // Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();

        // 검증(사용자 비밀번호 체크)
        // authenticate 메서드가 실행될 때 CustomUserDetailService 에서 만들었던 loadUserByUsername 메서드 호출
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        if (!jwtTokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // get user ID from Access Token
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // get Refresh Token
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);
        return tokenDto;
    }

    @Transactional(readOnly = true)
    public User getUserInfo(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // 현재 SecurityContext 에 있는 유저 정보 가져오기
    @Transactional(readOnly = true)
    public User getMyInfo() {
        return userRepository.findById(SecurityUtil.getCurrentMemberId()).orElse(null);
    }
}
