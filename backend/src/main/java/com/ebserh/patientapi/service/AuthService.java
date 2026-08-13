package com.ebserh.patientapi.service;

import com.ebserh.patientapi.model.User;
import com.ebserh.patientapi.model.dto.UserDTO;
import com.ebserh.patientapi.repository.UserRepository;
import com.ebserh.patientapi.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public User register(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Nome de usuário já existe");
        }
        
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        // Temporariamente sem encoding para testes
        user.setPassword(userDTO.getPassword());
        user.setFullName(userDTO.getFullName());
        user.setRole(userDTO.getRole() != null ? userDTO.getRole() : "USER");
        user.setIsActive(true);
        
        return userRepository.save(user);
    }
    
    public Optional<User> authenticate(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        
        if (user.isPresent() && user.get().getIsActive()) {
            // Temporariamente sem BCrypt para testes
            if (password.equals(user.get().getPassword())) {
                return user;
            }
        }
        
        return Optional.empty();
    }
    
    public String generateToken(User user) {
        return jwtUtil.generateToken(user.getUsername(), user.getRole());
    }
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}