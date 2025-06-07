package com.ssafy.cob.domain.mealcalendar.model.dao;

import java.util.List;

import com.ssafy.cob.domain.mealcalendar.model.dto.MealCalendar;

public interface MealCalendarDao {

	public List<MealCalendar> selectAll();

	public int insertMeal(MealCalendar mealCalendar);

	public int insertMeal(MealCalendar meal, int userId);

	public List<MealCalendar> selectCalendarByDate(String date);

	public int deleteMeal(int calendarId);

	public int updateMeal(MealCalendar mealCalendar);

	public List<MealCalendar> selectCalendarByUserId(int userId);
}

