<template>
  <div class="meal-bg">
    <div class="sidebar">
      <Navbar />
    </div>
    <div class="meal-form">
      <button type="button" class="exit-btn" @click="goBack">← 나가기</button>
      <h2>AI 맞춤 식단 생성</h2>

      <form @submit.prevent="submitForm">
        <div class="card-grid">
          <div
            v-for="(field, index) in formFields"
            :key="index"
            class="input-card"
            :class="{ filled: isFilled(field.model) }"
          >
            <label :for="field.id">{{ field.label }}</label>
            <component
              :is="field.type"
              :id="field.id"
              :value="field.model"
              @input="(event) => (field.model = event.target.value)"
              @change="(event) => (field.model = event.target.value)"
              v-bind="field.attrs"
            >
              <option v-if="field.type === 'select'" disabled value="">선택하세요</option>
              <option
                v-if="field.type === 'select'"
                v-for="(opt, idx) in field.options"
                :key="idx"
                :value="opt"
              >
                {{ opt }}
              </option>
            </component>
          </div>
        </div>

        <button type="submit">식단 생성 요청</button>
      </form>

      <div v-if="loading" class="loading-overlay">
        <div class="loading-box">
          <p>🍽️ GPT로 식단을 생성 중입니다...</p>
          <div class="progress-bar">
            <div class="progress-fill"></div>
          </div>
        </div>
      </div>

      <div v-if="parsedTable.headers.length" class="meal-result">
        <h3>추천 식단 결과</h3>
        <table class="meal-table">
          <thead>
            <tr>
              <th v-for="(head, i) in parsedTable.headers" :key="i">{{ head }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(row, i) in parsedTable.rows" :key="i">
              <td v-for="(col, j) in row" :key="j">{{ col }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api/axios'
import { useUserStore } from '@/stores/userStore'
import { generateMealPlan } from '@/stores/AIMealService'
import Navbar from '@/components/navbar.vue'

const router = useRouter()
const store = useUserStore()

function isFilled(model) {
  const val = model?.value
  return val !== null && val !== undefined && val.toString().trim() !== ''
}

function goBack() {
  router.back()
}

const user = ref({
  height: '',
  weight: '',
  goal: '',
  allergy: '',
  disease: '',
})

const form = ref({
  targetWeight: '',
  startDate: '',
  periodWeeks: '',
  mealsPerDay: '',
  preferredFoods: '',
  dislikedFoods: '',
})

const mealPlan = ref('')
const jsonMealPlan = ref([])
const parsedTable = ref({ headers: [], rows: [] })
const loading = ref(false)

onMounted(async () => {
  try {
    const response = await api.get(`http://localhost:8080/api/user`)
    user.value = response.data
    console.log('사용자 정보 조회 >>>', user.value)
  } catch (err) {
    console.error('사용자 정보 조회 실패:', err)
  }
})

watch(mealPlan, (newPlan) => {
  const lines = newPlan.split('\n').filter((l) => l.includes('|'))
  if (lines.length < 2) return

  const headers = lines[0].split('|').map((h) => h.trim())
  const rows = lines.slice(2).map((line) => line.split('|').map((cell) => cell.trim()))
  parsedTable.value = { headers, rows }
})

async function submitForm() {
  const requestData = {
    ...user.value,
    ...form.value,
  }

  try {
    loading.value = true
    console.log(requestData, user.value, form.value)
    mealPlan.value = await generateMealPlan(requestData)
    jsonMealPlan.value = parseMealMarkdownTable(mealPlan.value)
    api.post(`http://localhost:8080/api/meal-calendar/ai`, jsonMealPlan.value)
  } catch (err) {
    console.error('GPT 호출 실패:', err)
  } finally {
    loading.value = false
  }
}

function parseMealMarkdownTable(markdown) {
  const lines = markdown
    .trim()
    .split('\n')
    .filter((line) => line.startsWith('|') && !line.includes('----'))

  const mealTimes = ['아침', '점심', '저녁', '간식']
  const result = []

  for (let i = 1; i < lines.length; i++) {
    const columns = lines[i]
      .split('|')
      .map((col) => col.trim())
      .filter(Boolean)

    const [date, breakfast, lunch, dinner, snack, totalCalorie] = columns
    const caloriePerMeal = Math.floor(parseInt(totalCalorie, 10) / 4)

    const meals = [breakfast, lunch, dinner, snack]
    for (let j = 0; j < 4; j++) {
      result.push({
        date,
        mealDescription: meals[j],
        mealTime: mealTimes[j],
        totalCalorie: caloriePerMeal,
      })
    }
  }

  return result
}

const formFields = ref([
  {
    id: 'height',
    label: '키(cm)',
    type: 'input',
    model: computed({
      get: () => user.value.height,
      set: (val) => (user.value.height = val),
    }),
    attrs: { type: 'number', required: true },
  },
  {
    id: 'weight',
    label: '몸무게(kg)',
    type: 'input',
    model: computed({
      get: () => user.value.weight,
      set: (val) => (user.value.weight = val),
    }),
    attrs: { type: 'number', required: true },
  },
  {
    id: 'goal',
    label: '목표',
    type: 'select',
    model: computed({
      get: () => user.value.goal,
      set: (val) => (user.value.goal = val),
    }),
    attrs: { required: true },
    options: ['벌크업', '다이어트', '린메스업', '체형유지'],
  },
  {
    id: 'allergy',
    label: '알레르기',
    type: 'input',
    model: computed({
      get: () => user.value.allergy,
      set: (val) => (user.value.allergy = val),
    }),
    attrs: { type: 'text', required: true },
  },
  {
    id: 'disease',
    label: '지병',
    type: 'input',
    model: computed({
      get: () => user.value.disease,
      set: (val) => (user.value.disease = val),
    }),
    attrs: { type: 'text', required: true },
  },
  {
    id: 'targetWeight',
    label: '목표 체중(kg)',
    type: 'input',
    model: computed({
      get: () => form.value.targetWeight,
      set: (val) => (form.value.targetWeight = val),
    }),
    attrs: { type: 'number', required: true },
  },
  {
    id: 'periodWeeks',
    label: '기간(일 단위)',
    type: 'input',
    model: computed({
      get: () => form.value.periodWeeks,
      set: (val) => (form.value.periodWeeks = val),
    }),
    attrs: { type: 'number', required: true },
  },
  {
    id: 'startDate',
    label: '식단 시작일',
    type: 'input',
    model: computed({
      get: () => form.value.startDate,
      set: (val) => (form.value.startDate = val),
    }),
    attrs: { type: 'date', required: true },
  },
  {
    id: 'mealsPerDay',
    label: '하루 끼니 수',
    type: 'input',
    model: computed({
      get: () => form.value.mealsPerDay,
      set: (val) => (form.value.mealsPerDay = val),
    }),
    attrs: { type: 'number', min: 1, max: 4, required: true },
  },
  {
    id: 'preferredFoods',
    label: '선호 음식',
    type: 'input',
    model: computed({
      get: () => form.value.preferredFoods,
      set: (val) => (form.value.preferredFoods = val),
    }),
    attrs: { type: 'text' },
  },
  {
    id: 'dislikedFoods',
    label: '비선호 음식',
    type: 'input',
    model: computed({
      get: () => form.value.dislikedFoods,
      set: (val) => (form.value.dislikedFoods = val),
    }),
    attrs: { type: 'text' },
  },
])
</script>

<style scoped>
.meal-bg {
  height: 100vh;
  background-image: url('../assets/background/food.jpg');
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
  display: flex;
  justify-content: center;
  align-items: center;
}

.exit-btn {
  position: absolute;
  top: 18px;
  left: 20px;
  background: transparent;
  color: #5e35b1;
  border: 1px solid #5e35b1;
  border-radius: 6px;
  padding: 4px 10px;
  cursor: pointer;
  z-index: 10;
}

.meal-form {
  width: 100%;
  max-width: 900px;
  padding: 30px;
  background: rgba(255, 255, 255, 0.92);
  border-radius: 14px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
  position: relative;
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 16px;
}

.input-card {
  background: white;
  border: 1px solid #cfd8dc;
  border-radius: 12px;
  padding: 16px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.input-card:hover {
  box-shadow: 0 6px 12px rgba(94, 53, 177, 0.15);
  border-color: #5e35b1;
}

.input-card.filled {
  background-color: #d1c4e9; /* 좀 더 진한 보라 계열로 */
  border-color: #5e35b1;
  box-shadow: 0 0 0 2px rgba(94, 53, 177, 0.2);
}

.input-card label {
  font-weight: 600;
  font-size: 13px;
  margin-bottom: 8px;
  display: block;
}

.input-card input,
.input-card select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 13px;

  margin-left: -8px;
}

input:focus,
select:focus {
  outline: none;
  border-color: #5e35b1;
  box-shadow: 0 0 0 2px rgba(94, 53, 177, 0.2);
}

.meal-form button[type='submit'] {
  margin-top: 24px;
  width: 100%;
  padding: 12px;
  background: linear-gradient(to right, #5e35b1, #3949ab);
  color: white;
  font-weight: bold;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.meal-form button[type='submit']:hover {
  background: linear-gradient(to right, #4527a0, #303f9f);
}

.loading-overlay {
  position: fixed;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.35);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.loading-box {
  background: white;
  padding: 24px 36px;
  border-radius: 16px;
  border: 2px solid #7e57c2;
  text-align: center;
}

.progress-bar {
  width: 300px;
  height: 16px;
  background: #f3f3f3;
  border-radius: 10px;
  overflow: hidden;
  margin-top: 12px;
}

.progress-fill {
  width: 50%;
  height: 100%;
  background: linear-gradient(90deg, #00c6ff, #0072ff);
  animation: loadingMove 2s infinite;
}

@keyframes loadingMove {
  0% {
    transform: translateX(-100%);
  }
  100% {
    transform: translateX(100%);
  }
}

.meal-result {
  margin-top: 40px;
  border-top: 2px solid #5c6bc0;
  padding-top: 20px;
}

.meal-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 16px;
  font-size: 13px;
}

.meal-table th,
.meal-table td {
  border: 1px solid #cfd8dc;
  padding: 10px;
  text-align: center;
}

.meal-table th {
  background-color: #e3f2fd;
  color: #1e88e5;
}
</style>
