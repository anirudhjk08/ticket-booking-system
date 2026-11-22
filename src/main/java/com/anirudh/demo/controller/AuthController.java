package com.anirudh.demo.controller;

import com.anirudh.demo.dto.LoginRequest;
import com.anirudh.demo.dto.RegisterRequest;
import com.anirudh.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {
private final AuthService authService;

public AuthController(AuthService authService){
    this.authService = authService;
}
@PostMapping("/register")
    public ResponseEntity<Map<String,String>> register(@Valid @RequestBody RegisterRequest request){
        authService.register(request.getUsername(),request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("Message","User registered successfully"));
}
@PostMapping("/login")
    public Map<String,String> login(@Valid @RequestBody LoginRequest request){
     String token = authService.login(request.getUsername(), request.getPassword());
     return Map.of(token,"token");
}
}
