package com.anirudh.demo.service;

import com.anirudh.demo.exception.InvalidCredentialsException;
import com.anirudh.demo.exception.UsernameAlreadyExistsException;
import com.anirudh.demo.model.User;
import com.anirudh.demo.repository.UserRepository;
import io.jsonwebtoken.Jwt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService){
        this.userRepository= userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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
    public String login(String username, String rawPassword){
        User user = userRepository.findByUsername(username).orElseThrow(InvalidCredentialsException::new);
        if(!passwordEncoder.matches(rawPassword,user.getPassword())){
            throw new InvalidCredentialsException();
        }
        return jwtService.generateToken(user.getUsername());
    }
}
