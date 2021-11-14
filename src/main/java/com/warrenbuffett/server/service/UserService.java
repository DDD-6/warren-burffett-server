package com.warrenbuffett.server.service;

import com.warrenbuffett.server.controller.JwtTokenProvider;
import com.warrenbuffett.server.controller.user.request.LoginRequestDto;
import com.warrenbuffett.server.controller.user.response.LoginResponseDto;
import com.warrenbuffett.server.controller.user.response.UserResponseDto;
import com.warrenbuffett.server.domain.User;
import com.warrenbuffett.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll(){
        return userRepository.findAll().stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }
    public User searchUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        return user;
    }
    public User searchUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user;
    }

    @Transactional
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public LoginResponseDto loginUser(LoginRequestDto loginRequestDto) {
        User user = searchUserByEmail(loginRequestDto.getEmail());
        if (user==null||!user.getPassword().equals(loginRequestDto.getPassword())){
            return null;
        }
        String token = jwtTokenProvider.generateToken(user);
        return new LoginResponseDto(token);
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
}
