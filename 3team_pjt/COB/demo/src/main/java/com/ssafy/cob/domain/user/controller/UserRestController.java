package com.ssafy.cob.domain.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.cob.domain.user.model.dto.User;
import com.ssafy.cob.domain.user.service.UserService;
import com.ssafy.cob.global.jwt.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
public class UserRestController {

	private UserService userService;
	private JwtUtil jwtUtil;

	public UserRestController(UserService userService, JwtUtil jwtUtil) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
	}
	
	// 회원가입을 진행해보자
	@PostMapping("/signup")
	public ResponseEntity<Void> signup(@RequestBody User user) {
		System.out.println(user);
		if(userService.signup(user)) {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		return ResponseEntity.badRequest().build();
	}

	
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody User user, HttpSession session) {
		User loginUser = userService.login(user.getLoginId(), user.getPassword());
		Map<String, Object> result = new HashMap<>();

		if (loginUser != null) {
			result.put("access-token", jwtUtil.createToken(loginUser.getUsername(), loginUser.getId()));
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/logout")
	public ResponseEntity<Void> logout(HttpSession session) {
		if (session.getAttribute("loginUser") != null) {
			session.invalidate();
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/info")
	public ResponseEntity<Map<String, Object>> getUserInfo(HttpServletRequest request) {
		
		String token = jwtUtil.getTokenFromHeader(request);
	    int userId = jwtUtil.getUserIdFromToken(token);  // 커스텀 메서드
	    
	    User user = userService.findById(userId);
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }

	    Map<String, Object> response = new HashMap<>();
	    response.put("userId", user.getId());
	    response.put("name", user.getUsername());
	    
	    return ResponseEntity.ok(response);
	}
	
	@GetMapping
	public ResponseEntity<User> getUserById(HttpServletRequest request) {
		
		String token = jwtUtil.getTokenFromHeader(request);
	    int userId = jwtUtil.getUserIdFromToken(token);  // 커스텀 메서드
	    User user = userService.findById(userId);
		if(user != null) {
			return ResponseEntity.ok(user);
		}
		
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping
	public ResponseEntity<Void> removeUser(HttpServletRequest request) {
		
		String token = jwtUtil.getTokenFromHeader(request);
	    int userId = jwtUtil.getUserIdFromToken(token);  // 커스텀 메서드
	    if(userService.removeUser(userId)>=1) {
	    	return ResponseEntity.ok().build();
	    }
		return ResponseEntity.notFound().build();
	}
}
