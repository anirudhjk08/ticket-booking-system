package com.anirudh.demo.service;

import com.anirudh.demo.UsernameAlreadyExistsException;
import com.anirudh.demo.model.User;
import com.anirudh.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository= userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public User register(String username,String rawPassword){
        if(userRepository.existsByUsername(username)){
            throw new UsernameAlreadyExistsException(username);
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        return userRepository.save(user);
    }
}
