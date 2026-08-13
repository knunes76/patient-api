package com.ebserh.patientapi.controller;

import com.ebserh.patientapi.model.User;
import com.ebserh.patientapi.model.dto.UserDTO;
import com.ebserh.patientapi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        try {
            User user = authService.register(userDTO);
            return ResponseEntity.ok(createUserResponse(user));
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        Optional<User> user = authService.authenticate(username, password);
        
        if (user.isPresent()) {
            String token = authService.generateToken(user.get());
            Map<String, Object> response = createUserResponse(user.get());
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Credenciais inválidas");
            return ResponseEntity.status(401).body(error);
        }
    }
    
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        Optional<User> user = authService.findByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(createUserResponse(user.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    private Map<String, Object> createUserResponse(User user) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("fullName", user.getFullName());
        response.put("role", user.getRole());
        response.put("isActive", user.getIsActive());
        return response;
    }
}