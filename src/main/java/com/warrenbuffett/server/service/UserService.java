package com.warrenbuffett.server.service;

import com.warrenbuffett.server.domain.User;
import com.warrenbuffett.server.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository=userRepository;
    }

    public User searchUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        return user;
    }

    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    public boolean deleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user!=null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }
}
