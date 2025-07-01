package com.backend.bookstore.security;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.bookstore.dto.LoginRequest;
import com.backend.bookstore.dto.RegisterRequest;
import com.backend.bookstore.entities.User;
import com.backend.bookstore.repositories.UserRepository;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder passwordEncoder;

	public AuthService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
		this.passwordEncoder = passwordEncoder;
	}

	public String register(RegisterRequest request) {
		Optional<User> userExists = userRepository.findByUsername(request.getUsername());
		if (userExists.isPresent()) {
			throw new RuntimeException("User already exists");
		}

		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole("USER");

		userRepository.save(user);
		return jwtUtil.generateToken(user.getUsername());

	}

	public String login(LoginRequest request) {
		User user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new RuntimeException("Invalid credentials");
		}

		return jwtUtil.generateToken(user.getUsername());
	}

}
