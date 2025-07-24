package com.bintang.auditrail_entity.service;

import com.bintang.auditrail_entity.dto.AuthRequest;
import com.bintang.auditrail_entity.dto.AuthResponse;
import com.bintang.auditrail_entity.dto.RegisterRequest;
import com.bintang.auditrail_entity.model.User;
import com.bintang.auditrail_entity.repository.UserRepository;
import com.bintang.auditrail_entity.securityconfig.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private AuthenticationManager authManager;

    public String register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        return "User registered";
    }

    public AuthResponse login(AuthRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));
        String token = jwtUtils.generateToken(request.getUsername());
        return new AuthResponse(token);
    }
}
