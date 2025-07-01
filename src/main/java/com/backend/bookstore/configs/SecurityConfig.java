package com.backend.bookstore.configs;

import java.io.IOException;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.OncePerRequestFilter;

import com.backend.bookstore.security.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {
	private final JwtUtil jwtUtil;

	public SecurityConfig(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						auth -> auth.requestMatchers("/", "/api/auth/**", "/h2-console/**", "/swagger-ui/**",
								"/v3/api-docs/**", "/swagger-ui.html").permitAll().anyRequest().authenticated())
				.headers(headers -> headers.frameOptions().disable()).addFilterBefore(new JwtFilter(jwtUtil),
						org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	static class JwtFilter extends OncePerRequestFilter {

		private final JwtUtil jwtUtil;

		JwtFilter(JwtUtil jwtUtil) {
			this.jwtUtil = jwtUtil;
		}

		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
				FilterChain filterChain) throws IOException, jakarta.servlet.ServletException {
			String authHeader = request.getHeader("Authorization");

			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				String token = authHeader.substring(7);
				try {
					String username = jwtUtil.validateTokenAndGetUserName(token);
					var auth = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
					SecurityContextHolder.getContext().setAuthentication(auth);
				} catch (Exception ignored) {
					// Token is invalid or expired
				}
			}

			filterChain.doFilter(request, response);
		}
	}
}
