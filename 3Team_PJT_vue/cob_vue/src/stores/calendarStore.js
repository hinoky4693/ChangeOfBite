import { ref, computed } from "vue";
import { defineStore } from "pinia";
import { useUserStore } from "./userStore";
import api from "@/api/axios";

const REST_API_URL = "http://localhost:8080/api/meal-calendar";

export const useCalendarStore = defineStore("calendar", () => {
  const mealList = ref([]);

  const getMealList = async (userId) => {
    const response = await api.get(`${REST_API_URL}/${userId}`);
    mealList.value = response.data;
    mealList.value = mealList.value.map((meal) => {
      let startTime = '00:00:00'
      let endTime = '01:00:00'

      switch (meal.mealTime) {
        case '아침':
          startTime = '02:00:00'
          endTime = '04:00:00'
          break
        case '점심':
          startTime = '05:00:00'
          endTime = '07:00:00'
          break
        case '저녁':
          startTime = '08:00:00'
          endTime = '10:00:00'
          break
        case '간식':
          startTime = '11:00:00'
          endTime = '12:00:00'
          break
      }

      const formattedDate = new Date(meal.date).toISOString().split('T')[0]

      return {
        start: `${formattedDate}T${startTime}`,
        end: `${formattedDate}T${endTime}`,
        title: `${meal.mealTime} - ${meal.mealDescription}`,
        class: 'meal',
        desc: meal.mealDescription
      }
    })
    console.log(mealList.value);
  }

  const addMeal = async (formData) => {
      for (const [key, value] of formData.entries()) {
      }
    try {
      const response = await api.post(REST_API_URL, formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      await getMealList(formData.get("userId")); // 등록 후 목록 새로고침
    } catch (err) {
      console.error("식단 등록 실패:", err);
        }
  };

    const updateMeal = async (mealId, updatedFormData) => {
    try {
      await api.put(`${REST_API_URL}/${mealId}`, updatedFormData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      await getMealList(); // 수정 후 목록 새로고침
    } catch (err) {
      console.error("식단 수정 실패:", err);
    }
  };

    const deleteMeal = async (mealId) => {
    try {
      await api.delete(`${REST_API_URL}/${mealId}`);
      mealList.value = mealList.value.filter((meal) => meal.id !== mealId); // 프론트에서 바로 반영
    } catch (err) {
      console.error("식단 삭제 실패:", err);
    }
  };


  return {
    mealList,
    getMealList,
    addMeal,
    updateMeal,
    deleteMeal,
  };
});