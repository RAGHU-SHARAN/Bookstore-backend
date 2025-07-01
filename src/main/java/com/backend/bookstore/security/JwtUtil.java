package com.backend.bookstore.security;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JwtUtil {

	private static final String SECRET = "rs123";
	private static final long EXPIRATION_TIME = 86400000;

	public String generateToken(String username) {

		return JWT.create().withSubject(username).withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.sign(Algorithm.HMAC256(SECRET));
	}

	public String validateTokenAndGetUserName(String token) {
		return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token).getSubject();
	}

}
