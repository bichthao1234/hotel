package com.my.hotel.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtils {
	
	public static String generateTokenFromUsername(String issuer, String subject, String username, String authorities, 
			String secretKey, Integer expirationInMs) {
		Date currentDate = new Date();
		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		String jwt = Jwts.builder().setIssuer(issuer).setSubject(subject)
				.claim("username", username)
				.claim("authorities", authorities).setIssuedAt(currentDate)
				.setExpiration(new Date(currentDate.getTime() + expirationInMs)).signWith(key).compact();
		return jwt;
	}
	
	public static Claims claimJwt(String jwt, String secretKey) {
		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
		return claims;
	}

//	public static void main(String[] args) {
//		JwtUtils jwtUtils = new JwtUtils();
//		String jwt = jwtUtils.generateTokenFromUsername("test", "test", "test", "test", SecurityConstants.JWT_KEY,
//				SecurityConstants.JWT_EXPIRATION_IN_MS);
//		System.out.println(jwt);
//
//		jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJNeSBBcHAiLCJzdWIiOiJKV1QgVG9rZW4gVGVzdCIsInVzZXJuYW1lIjoibmh1dC5waGFtQGFhLmFhIiwiYXV0aG9yaXRpZXMiOiJST0xFX1VTRVIsUk9MRV9BRE1JTiIsImlhdCI6MTY1NzAxNjcyOSwiZXhwIjoxNjU3MDE3MzI5fQ.jgyrgdYU-zhmOl27Zxhil463kBQbHg5QKFEGU9xgd34";
//		Claims claims = jwtUtils.claimJwt(jwt, SecurityConstants.JWT_KEY);
//		String username = String.valueOf(claims.get("username"));
//		String authorities = (String) claims.get("authorities");
//
//		System.out.println(username);
//		System.out.println("finish");
//	}

}