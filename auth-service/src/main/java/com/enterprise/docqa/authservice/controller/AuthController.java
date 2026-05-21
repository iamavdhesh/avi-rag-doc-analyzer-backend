package com.enterprise.docqa.authservice.controller;

import com.enterprise.docqa.authservice.dto.AuthResponse;
import com.enterprise.docqa.authservice.dto.LoginRequest;
import com.enterprise.docqa.authservice.service.JwtService;
import com.enterprise.docqa.authservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        if (!userService.validateCredentials(request.username(), request.password())) {
            return ResponseEntity.status(401).build();
        }

        var user = userService.loadUserByUsername(request.username());
        var accessToken = jwtService.createAccessToken(user.getUsername(), user.getAuthorities().stream().findFirst().map(Object::toString).orElse("General User"));
        var refreshToken = jwtService.createRefreshToken(user.getUsername());
        var response = new AuthResponse(accessToken, refreshToken, "Bearer", user.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody String refreshToken) {
        try {
            var claims = jwtService.verify(refreshToken);
            var username = claims.getSubject();
            var user = userService.loadUserByUsername(username);
            var accessToken = jwtService.createAccessToken(username, user.getAuthorities().stream().findFirst().map(Object::toString).orElse("General User"));
            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken, "Bearer", username));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }
}
