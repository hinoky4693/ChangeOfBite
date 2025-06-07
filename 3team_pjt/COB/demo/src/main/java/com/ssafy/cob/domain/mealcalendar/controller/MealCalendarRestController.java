package com.ssafy.cob.domain.mealcalendar.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.cob.domain.mealcalendar.model.dto.MealCalendar;
import com.ssafy.cob.domain.mealcalendar.service.MealCalendarService;
import com.ssafy.cob.global.jwt.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/meal-calendar")
@CrossOrigin("*")
public class MealCalendarRestController {
	
	private final MealCalendarService mealCalendarService;
	private final JwtUtil jwtUtil;
	
	public MealCalendarRestController(
			MealCalendarService mealCalendarService,
			JwtUtil jwtUtil ){
		this.mealCalendarService = mealCalendarService;
		this.jwtUtil = jwtUtil;
	}
	
	
	@GetMapping
	public ResponseEntity<List<MealCalendar>> getCalendar () {
		List<MealCalendar> calendar = mealCalendarService.getCalendar();
		
		if (calendar.size() > 0) {
			return ResponseEntity.ok().body(calendar);
		} 
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<List<MealCalendar>> getCalendarByUserId (@PathVariable("id") int userId) {
		List<MealCalendar> calendar = mealCalendarService.getCalendarByUserId(userId);
		if (calendar.size() > 0) {
			return ResponseEntity.ok().body(calendar);
		} 
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping
	public ResponseEntity<Void> registMeal(@ModelAttribute MealCalendar mealCalendar,
            @RequestPart(value = "attachs", required = false) List<MultipartFile> files)  {
		
		if (mealCalendarService.registMeal(mealCalendar, files)) {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		return ResponseEntity.badRequest().build();
	}
	
	@PostMapping("/ai")
	public ResponseEntity<Void> registMealWithAi(@RequestBody List<MealCalendar> mealList, HttpServletRequest request)  {
		
		String token = jwtUtil.getTokenFromHeader(request);
		int userId = jwtUtil.getUserIdFromToken(token);
		
		if(mealCalendarService.registMeal(mealList, userId)) {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> removeMeal(@PathVariable("id") int id) {
		
		if(mealCalendarService.removeMeal(id)) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping
	public ResponseEntity<Void> modifyMeal(@ModelAttribute MealCalendar mealCalendar,
            @RequestPart(value = "attachs", required = false) List<MultipartFile> files)  {
		System.out.println(mealCalendar.getId());
		if (mealCalendarService.modifyMeal(mealCalendar, files)) {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		return ResponseEntity.badRequest().build();
	}
}
