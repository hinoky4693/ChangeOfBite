package com.ssafy.cob.global.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {
	private String key = "SSAFY_NonMajor_JavaTrack_SecretKey";
	private SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
	
	//토큰 생성시 다양한 데이터를 저장할 수 있음 (DTO or Map)
	public String createToken(String name, int userId) {
		//유효기간 
		Date exp = new Date(System.currentTimeMillis()+ 1000*60*60); //1시간
		return Jwts.builder().header().add("typ", "JWT").and()
				.claim("name", name).claim("id", userId).expiration(exp)
				.signWith(secretKey).compact();
	}
	
	
	//유효성 검증 (실제로 내용물을 확인하기 위함은 아님 / 에러나면 유효기간 지난것
	//이거 실행했을때 에러나면 유효기간 지난거....
	public Claims validate(String token) {
	    try {
	        return Jwts.parser()
	                .verifyWith(secretKey)
	                .build()
	                .parseSignedClaims(token)
	                .getPayload();
	    } catch (JwtException e) {
	    	System.err.println("JWT 검증 실패: " + e.getMessage()); // 🔥 원인 추적
	        throw new RuntimeException("유효하지 않은 토큰입니다.");
	    }
	}



	public int getUserIdFromToken(String token) {
	    Claims claims = Jwts.parser()
	        .verifyWith(secretKey)
	        .build()
	        .parseSignedClaims(token)
	        .getPayload();

	    return (int) claims.get("id");
	}

	public String getTokenFromHeader(HttpServletRequest request) {
		// 1. Authorization 헤더에서 토큰 추출
		String authorizationHeader = request.getHeader("Authorization");
		
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return null;
		}
		
		// 2. "Bearer " 접두사 제거
		return authorizationHeader.substring(7); // 토큰 넘겨주기
	}


}
