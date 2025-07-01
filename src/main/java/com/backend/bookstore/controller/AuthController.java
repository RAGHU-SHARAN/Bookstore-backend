package com.backend.bookstore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.bookstore.dto.LoginRequest;
import com.backend.bookstore.dto.RegisterRequest;
import com.backend.bookstore.security.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
		String token = authService.register(request);
		return ResponseEntity.ok(token);
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequest request) {
		String token = authService.login(request);
		return ResponseEntity.ok(token);
	}

}
