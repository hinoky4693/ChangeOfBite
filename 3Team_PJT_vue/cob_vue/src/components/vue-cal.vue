<template>
  <div class="calendar-wrapper">
    <!-- 상단 헤더 -->
    <div class="calendar-header">
      <h2></h2>
      <div>
        <button class="new-btn" @click="showModal = true">일정 추가하기</button>
      </div>
    </div>

    <!-- 캘린더 -->
    <vue-cal
      sm
      :view="view"
      class="vue-cal"
      :views="['month', 'week', 'day']"
      :time="false"
      :key="calendarKey"
      locale="ko"
      :disable-views="['years', 'year']"
      :selected-date="selectedDate.value"
      :events="events"
      :startWeekOnSunday="true"
      :eventsOnMonthView="true"
      @view-change="handleViewChange"
      @event-click="openEventModal"
    >
      <template #event-content="{ events, view }">
        <div>
          <span v-if="view === 'month'">{{ events.title }}</span>
          <span v-else-if="view === 'week'">{{ events.title }} - {{ events.description }}</span>
          <span v-else-if="view === 'day'">
            {{ events.title }}<br />
            {{ events.time }}
          </span>
        </div>
      </template>
    </vue-cal>

    <!-- 수정 모달 템플릿 -->
    <div v-if="showEditModal" class="modal edit-modal">
      <div class="modal-content">
        <h3>식단 수정</h3>

        <input :value="selectedMeal.date.slice(0, 10)" 
        @input="(val) => selectedMeal.date = val.target.value" type="date" />
        <select v-model="selectedMeal.mealTime">
          <option disabled value="">식사 시간 선택</option>
          <option>아침</option>
          <option>점심</option>
          <option>저녁</option>
          <option>간식</option>
        </select>
        <textarea v-model="selectedMeal.mealDescription" rows="4" />
        <input v-model.number="selectedMeal.totalCalorie" type="number" />

        <input type="file" multiple accept="image/*" @change="handleEditFileChange" />

        <div class="button-group">
          <button @click="submitEdit">수정 완료</button>
          <button @click="showEditModal = false">취소</button>
        </div>
      </div>
    </div>

    <!-- 상세 정보 모달 -->
    <div v-if="showEventModal" class="modal event-modal">
      <div class="modal-content">
        <h3>식단 상세 정보</h3>

        <div class="detail-group">
          <label>날짜</label>
          <p>{{ selectedMeal.date.split(' ')[0] }}</p>
        </div>

        <div class="detail-group">
          <label>식사 시간</label>
          <p>{{ selectedMeal.mealTime }}</p>
        </div>

        <div class="detail-group">
          <label>식단</label>
          <p>{{ selectedMeal.mealDescription }}</p>
        </div>

        <div class="detail-group">
          <label>총 칼로리</label>
          <p>{{ selectedMeal.totalCalorie }} kcal</p>
        </div>

        <div class="detail-group" v-if="selectedMeal.files.length">
          <label>이미지</label>
          <div class="image-preview-group">
            <img
              v-for="(file, idx) in selectedMeal.files"
              :key="idx"
              :src="`http://localhost:8080${file.filePath}`"
              alt="meal"
            />
          </div>
        </div>

        <div class="button-group">
          <button @click="showEventModal = false">닫기</button>
          <button @click="openEditModal">수정</button>
          <button @click="deleteMeal" class="delete-btn">삭제</button>
        </div>
      </div>
    </div>

    <!-- 모달 예시 -->
    <div v-if="showModal" class="modal default">
      <div class="modal-content">
        <h3>식단 등록</h3>

        <input v-model="date" type="date" placeholder="날짜 입력" />
        <select v-model="mealTime">
          <option disabled value="">식사 시간 선택</option>
          <option>아침</option>
          <option>점심</option>
          <option>저녁</option>
          <option>간식</option>
        </select>
        <textarea v-model="mealDescription" placeholder="식단 설명 입력" rows="4" />
        <input v-model.number="totalCalorie" type="number" placeholder="총 칼로리(kcal)" />

        <input type="file" @change="handleFileChange" multiple accept="image/*" />

        <div class="button-group">
          <button @click="addMeal">추가</button>
          <button @click="showModal = false">닫기</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useUserStore } from '@/stores/userStore'
import { useCalendarStore } from '@/stores/calendarStore'
import api from '@/api/axios'
import { VueCal } from 'vue-cal'
import 'vue-cal/style'

const showModal = ref(false)

const showEventModal = ref(false)
const showEditModal = ref(false)

const selectedMeal = ref(null)

const editFiles = ref([])

const selectedDate = ref(new Date())
const calendarKey = ref(0)

const userStore = useUserStore()
const calendarStore = useCalendarStore()

const date = ref('')
const mealTime = ref('')
const mealDescription = ref('')
const totalCalorie = ref('')
const files = ref([])

const events = ref([])

const view = ref('month');



const openEventModal = (event) => {
  selectedMeal.value = event.event.originalData
  console.log(event.event.originalData)
  showEventModal.value = true
}

const openEditModal = () => {
  showEventModal.value = false
  showEditModal.value = true
}

onMounted(async () => {
  // await fetchMealCalendar(userStore.loginUser.userId)
  calendarKey.value++
})

const handleFileChange = (e) => {
  files.value = Array.from(e.target.files)
}

const handleEditFileChange = (e) => {
  editFiles.value = Array.from(e.target.files)
}

const fetchMealCalendar = async (userId) => {
  const response = await api.get(`http://localhost:8080/api/meal-calendar/${userId}`)
  events.value = response.data
  events.value = events.value.map((meal) => {
    let startTime = '00:00:00'
    let endTime = '01:00:00'

    switch (meal.mealTime) {
      case '아침':
        startTime = '10:00'
        endTime = '11:00'
        break
      case '점심':
        startTime = '12:00'
        endTime = '13:00'
        break
      case '저녁':
        startTime = '14:00'
        endTime = '15:00'
        break
      case '간식':
        startTime = '16:00'
        endTime = '17:00'
        break
    }
    const date = new Date(meal.date)
    date.setDate(date.getDate() + 1)
    const formattedDate = date.toISOString().split('T')[0]

    return {
      start: `${formattedDate} ${startTime}`,
      end: `${formattedDate} ${endTime}`,
      title: `${meal.mealTime} - ${meal.mealDescription}`,
      class: meal.mealTime,
      meal: meal.desc,
      originalData: meal,
    }
  })
  calendarKey.value++
  console.log(events.value)
}

const addMeal = async () => {
  const formData = new FormData()
  formData.append('userId', userStore.loginUser.userId)
  formData.append('date', date.value)
  formData.append('mealTime', mealTime.value)
  formData.append('mealDescription', mealDescription.value)
  formData.append('totalCalorie', totalCalorie.value)

  files.value.forEach((file) => {
    formData.append('attachs', file) // 'files'는 백엔드에서 받을 이름
  })

  await calendarStore.addMeal(formData)
  await fetchMealCalendar(userStore.loginUser.userId)
  showModal.value = false
}

const submitEdit = async () => {
  const formData = new FormData()
  formData.append('id', selectedMeal.value.id)
  formData.append('date', selectedMeal.value.date)
  formData.append('mealTime', selectedMeal.value.mealTime)
  formData.append('mealDescription', selectedMeal.value.mealDescription)
  formData.append('totalCalorie', selectedMeal.value.totalCalorie)

  // 여러 이미지 추가
  editFiles.value.forEach((file) => {
    formData.append('attachs', file) // 백엔드 필드명에 맞게
  })

  try {
    await api.put(`http://localhost:8080/api/meal-calendar`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })

    alert('수정 완료!')
    showEditModal.value = false
    await fetchMealCalendar(userStore.loginUser.userId)
    calendarKey.value++
  } catch (err) {
    console.error('수정 실패:', err)
    alert('수정 중 오류 발생!')
  }
}

const deleteMeal = async () => {
  const confirmed = confirm('정말 이 식단을 삭제하시겠습니까?')
  if (!confirmed) return

  try {
    await api.delete(`http://localhost:8080/api/meal-calendar/${selectedMeal.value.id}`)
    alert('삭제가 완료되었습니다.')
    showEventModal.value = false
    await fetchMealCalendar(userId.value) // 캘린더 새로고침
  } catch (err) {
    console.error('삭제 중 오류:', err)
    alert('삭제 중 문제가 발생했습니다.')
  }
}

const handleViewChange = ({ startDate }, view) => {
  selectedDate.value = new Date(startDate)

}

watch(
  () => userStore.loginUser.userId,
  (userId) => {
    if (userId) {
      // 이제 userId가 있음 → 필요한 작업 수행
      console.log('watch 유저 ID:', userId)
      fetchMealCalendar(userId)
    }
  },
  { immediate: true },
)
</script>

<style scoped>
.calendar-wrapper {
  font-family: 'Segoe UI', sans-serif;
  padding: 20px;
}

/* ✅ 캘린더 상단 헤더 */
.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.new-btn,
.back-btn {
  padding: 8px 12px;
  background: #4caf50;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.filters label {
  margin-right: 15px;
}

.vuecal {
  --vuecal-primary-color: #4caf50;
  --vuecal-secondary-color: #ffffff;
  --vuecal-base-color: #000000;
  --vuecal-contrast-color: #573a3a;
  --vuecal-border-color: #ffffff;
  --vuecal-header-color: #ffffff;
  --vuecal-event-color: var(--vuecal-contrast-color);
  --vuecal-event-border-color: currentColor;
  --vuecal-border-radius: 6 px;
  --vuecal-height: 800px;
  --vuecal-min-schedule-width: 0 px;
  --vuecal-min-cell-width: 20 px;
  --vuecal-transition-duration: 0 s;
}

.vuecal__event {
  color: #ffffff;
  border: 1px solid;
  background-color: #111;
}

:deep(.vuecal__event) {
  height: 33px !important;
  font-size: 14px !important;
  line-height: 30px !important;
  padding: 0 10px !important;
  color: #fff;
}

:deep(.vuecal__event.아침) {
  background-color: #5c2a5dd9;
}

:deep(.vuecal__event.점심) {
  background-color: #3137abd9;
}

:deep(.vuecal__event.저녁) {
  background-color: #acdf1ed9;
}

:deep(.vuecal__event.간식) {
  background-color: #c4d414d9;
}

.vuecal__view--day .vuecal__event {
  min-height: 60px;
  padding: 12px 16px;
  font-size: 16px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* ✅ 모달 배경 */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

/* ✅ 모달 콘텐츠 박스 */
.modal-content {
  background: #ffffff;
  padding: 28px;
  border-radius: 12px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
  width: 100%;
  max-width: 480px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: center;
  /* 중앙 정렬 */
}

.modal-content h3 {
  margin-bottom: 8px;
  font-size: 20px;
  font-weight: bold;
  color: #333;
  text-align: center;
}

/* ✅ 모달 입력 요소 스타일 */
.modal-content input,
.modal-content select,
.modal-content textarea {
  width: 100%;
  max-width: 100%;
  box-sizing: border-box;
  padding: 12px 14px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 6px;
}

.modal-content input:focus,
.modal-content select:focus,
.modal-content textarea:focus {
  border-color: #4caf50;
  outline: none;
}

.modal-content textarea {
  resize: vertical;
  min-height: 100px;
}

.modal-content input[type='file'] {
  border: 1px solid #ccc;
  border-radius: 6px;
  padding: 12px;
  font-size: 16px;
  background: #fafafa;
  cursor: pointer;
}

/* ✅ 버튼 그룹 */
.button-group {
  width: 100%;
  display: flex;
  justify-content: space-between;
  gap: 12px;
}

.button-group button {
  flex: 1;
  padding: 12px;
  font-size: 16px;
  font-weight: bold;
  border-radius: 6px;
  border: none;
  cursor: pointer;
  transition: background 0.3s;
}

.button-group button:first-child {
  background-color: #4caf50;
  color: white;
}

.button-group button:first-child:hover {
  background-color: #43a047;
}

.button-group button:last-child {
  background-color: #e0e0e0;
  color: #333;
}

.button-group button:last-child:hover {
  background-color: #cfcfcf;
}

.detail-group {
  display: flex;
  flex-direction: column;
  align-items: center;
  /* 수평 가운데 정렬 */
  justify-content: center;
  /* 수직 가운데 정렬 (높이 필요할 경우) */
  text-align: center;
  /* 텍스트 자체도 가운데 정렬 */
  /* margin-bottom: 8px; 아래 요소와 간격 */
}
</style>
