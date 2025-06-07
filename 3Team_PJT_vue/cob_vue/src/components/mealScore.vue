<template>
  <div class="meal-score-container" :class="gradeClass">
        <div class="sidebar">
         <Navbar />
        </div>
    <div class="background-overlay"></div>
    <button class="back-button" @click="$router.back()">← 돌아가기</button>
    <h2>오늘의 식단 평가</h2>
    <form @submit.prevent="evaluateMeal">
      <div class="meal-input">
        <label>아침</label>
        <input type="text" v-model="mealInput.breakfast" placeholder="예: 토스트, 우유" />
        <label>점심</label>
        <input type="text" v-model="mealInput.lunch" placeholder="예: 김치찌개, 밥" />
        <label>저녁</label>
        <input type="text" v-model="mealInput.dinner" placeholder="예: 샐러드, 닭가슴살" />
      </div>
      <button :disabled="loading">{{ loading ? '평가 중...' : '점수 평가' }}</button>
    </form>

    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>GPT가 식단을 분석 중입니다...</p>
    </div>

    <transition name="pop">
      <div v-if="result" class="result-box">
        <div class="score-pop">
          <div class="big-score">{{ result.score }}점</div>
        </div>

        <div class="gauge-box">
          <svg viewBox="0 0 36 36" class="circular-chart">
            <path class="circle-bg" d="M18 2.0845
              a 15.9155 15.9155 0 0 1 0 31.831
              a 15.9155 15.9155 0 0 1 0 -31.831" />
            <path class="circle" :stroke="gaugeColor" :stroke-dasharray="gaugeValue + ', 100'"
              d="M18 2.0845
              a 15.9155 15.9155 0 0 1 0 31.831
              a 15.9155 15.9155 0 0 1 0 -31.831" />
            <text x="18" y="20.35" class="percentage">{{ result.score }}%</text>
          </svg>
        </div>

        <div class="feedback-box animate-icon">
          <div class="emoji">{{ result.emoji }}</div>
          <div class="bubble">
            <p>{{ result.feedback }}</p>
          </div>
        </div>

        <p class="grade-label">등급: <span>{{ result.grade }}</span></p>
      </div>
    </transition>

    <div v-if="error" class="error-box">
      ⚠️ 평가에 실패했습니다. 다시 시도해 주세요.
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import Navbar from './navbar.vue'

const mealInput = ref({ breakfast: '', lunch: '', dinner: '' })
const result = ref(null)
const error = ref(false)
const loading = ref(false)

// const API_KEY = 

const fullMealText = computed(() => {
  return `아침: ${mealInput.value.breakfast}\n점심: ${mealInput.value.lunch}\n저녁: ${mealInput.value.dinner}`
})

const gradeClass = computed(() => {
  if (!result.value) return ''
  switch (result.value.grade) {
    case 'A': return 'grade-a'
    case 'B': return 'grade-b'
    case 'C': return 'grade-c'
    case 'D': return 'grade-d'
    default: return 'grade-f'
  }
})

const gaugeValue = computed(() => result.value?.score || 0)
const gaugeColor = computed(() => {
  const score = result.value?.score || 0
  if (score >= 85) return '#66bb6a'
  if (score >= 70) return '#ffa726'
  return '#ef5350'
})

async function evaluateMeal() {
  result.value = null
  error.value = false
  loading.value = true

  const prompt = `
너는 영양사야. 아래 식단을 건강 기준으로 평가해.
절대 설명하지 말고 반드시 JSON 형식으로만 응답해.
예시:
{
  "score": 87,
  "grade": "B",
  "feedback": "단백질은 충분하지만 섬유질이 부족합니다.",
  "emoji": "\uD83D\uDE10"
}
식단: ${fullMealText.value}
`

  try {
    const res = await fetch('https://api.openai.com/v1/chat/completions', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${API_KEY}`,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        model: 'gpt-3.5-turbo',
        messages: [{ role: 'user', content: prompt }],
        temperature: 0.7,
      })
    })

    const data = await res.json()
    const raw = data.choices?.[0]?.message?.content || ''
    const jsonMatch = raw.match(/\{[\s\S]*\}/)
    if (jsonMatch) {
      result.value = JSON.parse(jsonMatch[0])
    } else {
      throw new Error('GPT 응답에 JSON 형식이 없습니다.')
    }
  } catch (err) {
    console.error('GPT 평가 실패:', err)
    error.value = true
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.meal-score-container {
  max-width: 640px;
  margin: 40px auto;
  padding: 24px;
  position: relative;
  z-index: 1;
  text-align: center;
  font-family: 'Pretendard', sans-serif;
  transition: background-color 0.5s ease;
}
.meal-score-container.grade-a { background-color: rgba(200, 250, 205, 0.85); }
.meal-score-container.grade-b { background-color: rgba(220, 255, 200, 0.85); }
.meal-score-container.grade-c { background-color: rgba(255, 243, 205, 0.85); }
.meal-score-container.grade-d { background-color: rgba(255, 224, 178, 0.85); }
.meal-score-container.grade-f { background-color: rgba(255, 205, 210, 0.85); }

.background-overlay {
  position: fixed;
  inset: 0;
  background: url('../assets/background/food.jpg') no-repeat center center / cover;
  z-index: -1;
  filter: brightness(0.9);
}

.back-button {
  position: absolute;
  top: 16px;
  left: 16px;
  padding: 6px 12px;
  border-radius: 8px;
  font-weight: bold;
  border: 1px solid #ccc;
  cursor: pointer;
}

.animate-icon .emoji {
  animation: bounce 1s ease infinite alternate;
}
@keyframes bounce {
  0% { transform: translateY(0); }
  100% { transform: translateY(-5px); }
}


.meal-input {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 20px;
  background: rgba(255, 255, 255, 0.85);
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 6px 14px rgba(0, 0, 0, 0.15);
}
.meal-input label {
  text-align: left;
  font-weight: bold;
  margin-top: 4px;
}
.meal-input input {
  padding: 10px;
  border-radius: 8px;
  border: 1px solid #ccc;
}

button {
  background: linear-gradient(135deg, #5e35b1, #7e57c2);
  color: white;
  padding: 12px 24px;
  font-weight: bold;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: 0.3s;
}
button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
button:hover:enabled {
  background: linear-gradient(135deg, #4527a0, #6a1b9a);
}

.loading {
  margin-top: 20px;
  color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.spinner {
  border: 4px solid #eee;
  border-top: 4px solid #5e35b1;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin-bottom: 8px;
}
@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.result-box {
  margin-top: 24px;
  padding: 20px;
  background: rgba(255,255,255,0.95);
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
  animation: fadeIn 0.5s ease;
}

.score-pop {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 16px;
  color: #5e35b1;
  animation: popIn 0.6s ease;
}

@keyframes popIn {
  0% { transform: scale(0.3); opacity: 0; }
  60% { transform: scale(1.15); opacity: 1; }
  100% { transform: scale(1); }
}

.gauge-box {
  margin: 0 auto 20px;
  width: 120px;
  height: 120px;
}
.circular-chart {
  display: block;
  max-width: 100%;
  transform: rotate(-90deg);
}
.circle-bg {
  fill: none;
  stroke: #eee;
  stroke-width: 3.8;
}
.circle {
  fill: none;
  stroke-width: 3.8;
  stroke-linecap: round;
  transition: stroke-dasharray 0.6s ease;
}
.percentage {
  fill: #444;
  font-size: 0.5em;
  text-anchor: middle;
  transform: rotate(90deg);
  transform-origin: center;
  dominant-baseline: middle;
}

.feedback-box {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  justify-content: center;
}
.emoji {
  font-size: 32px;
}
.bubble {
  background: #fff;
  padding: 10px 16px;
  border-radius: 16px;
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
  max-width: 300px;
}
.grade-label {
  margin-top: 12px;
  font-weight: bold;
  font-size: 18px;
}
.grade-a { color: #43a047; }
.grade-b { color: #7cb342; }
.grade-c { color: #ffa726; }
.grade-d { color: #fb8c00; }
.grade-f { color: #e53935; }

.error-box {
  margin-top: 20px;
  color: #d32f2f;
  font-weight: bold;
  background: #ffebee;
  padding: 12px;
  border-radius: 10px;
}

.fade-enter-active, .fade-leave-active,
.pop-enter-active, .pop-leave-active {
  transition: opacity 0.4s, transform 0.4s;
}
.fade-enter-from, .fade-leave-to,
.pop-enter-from, .pop-leave-to {
  opacity: 0;
  transform: scale(0.9);
}
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
