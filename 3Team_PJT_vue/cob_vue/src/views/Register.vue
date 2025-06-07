<template>
  <div class="register-container">
    <div class="register-box">
      <!-- 로고 -->
      <RouterLink to="/" class="logo-section">
        <img src="@/assets/icon/3d-lettuce.png" alt="로고" class="logo" />
      </RouterLink>

      <h2 class="register-title">회원가입</h2>

      <form @submit.prevent="handleRegister" class="register-form">
        <!-- 기본 입력 -->
        <input v-model.trim="loginId" type="text" placeholder="아이디" class="input-field" />
        <input v-model.trim="password" type="password" placeholder="비밀번호" class="input-field" />
        <input v-model.trim="confirmPassword" type="password" placeholder="비밀번호 확인" class="input-field" />
        <input v-model.trim="username" type="text" placeholder="이름" class="input-field" />
        <input v-model.trim="email" type="email" placeholder="이메일" class="input-field" />

        <!-- 추가 입력 필드 -->
        <input v-model.trim="phone" type="text" placeholder="전화번호 (예: 010-1234-5678)" class="input-field" />
        <input v-model.trim="birth" type="date" class="input-field" />

        <select v-model.trim="gender" class="input-field">
          <option disabled value="">성별 선택</option>
          <option>남</option>
          <option>여</option>
        </select>

        <input v-model.trim="height" type="number" placeholder="키 (cm)" class="input-field" />
        <input v-model.trim="weight" type="number" placeholder="몸무게 (kg)" class="input-field" />

        <textarea
          v-model.trim="allergy"
          placeholder="알레르기 음식 (예: 밀가루, 우유)"
          class="textarea-field"
        ></textarea>

        <textarea
          v-model.trim="disease"
          placeholder="병력 (예: 고혈압, 당뇨 등)"
          class="textarea-field"
        ></textarea>

        <div class="radio-group">
          <label class="radio-label">운동 목적:</label>
          <label><input type="radio" value="벌크업" v-model.trim="goal" /> 벌크업</label>
          <label><input type="radio" value="다이어트" v-model.trim="goal" /> 다이어트</label>
          <label><input type="radio" value="린메스업" v-model.trim="goal" /> 린메스업</label>
        </div>

        <button type="submit" class="register-button">회원가입</button>
      </form>

      <div class="login-link">
        이미 계정이 있으신가요?
        <router-link to="/login" class="highlight">로그인</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/userStore';
import axios from 'axios'
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router';


const route = useRouter();

const store = useUserStore();

const loginId = ref('')
const password = ref('')
const confirmPassword = ref('')
const username = ref('')
const email = ref('')
const phone = ref('')
const birth = ref('')
const gender = ref('')
const height = ref(null)
const weight = ref(null)
const allergy = ref('')
const disease = ref('')
const goal = ref('')



const handleRegister = () => {
  if (password.value !== confirmPassword.value) {
    alert('비밀번호가 일치하지 않습니다.')
    return
  }

  const data = {
    loginId: loginId.value,
    password: password.value,
    username: username.value,
    email: email.value,
    phone: phone.value,
    birth: birth.value,
    gender: gender.value,
    height: height.value,
    weight: weight.value,
    allergy: allergy.value,
    disease: disease.value,
    goal: goal.value
  }

  store.userSignUp(data);
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #fffaf5;
}

.register-box {
  background-color: white;
  padding: 40px 30px;
  border-radius: 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  width: 100%;
  max-width: 420px;
  text-align: center;
}

.logo-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 16px;
}

.logo {
  width: 48px;
  height: 48px;
  margin-bottom: 6px;
}

.app-title {
  font-weight: bold;
  font-size: 18px;
  color: #333;
  line-height: 1.4;
}

.register-title {
  font-size: 22px;
  font-weight: bold;
  color: #333;
  margin-bottom: 24px;
}

.register-form {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.input-field {
  padding: 12px 16px;
  border-radius: 24px;
  border: 1px solid #ddd;
  font-size: 14px;
}

.textarea-field {
  padding: 12px 16px;
  border-radius: 16px;
  border: 1px solid #ddd;
  font-size: 14px;
  resize: vertical;
  min-height: 60px;
}

.input-field:focus,
.textarea-field:focus,
select:focus {
  outline: none;
  border-color: #f7a23b;
  box-shadow: 0 0 0 2px #ffe5c2;
}

.radio-group {
  text-align: left;
  font-size: 14px;
  color: #555;
  margin-top: 8px;
}

.radio-group label {
  display: inline-block;
  margin-right: 12px;
}

.radio-label {
  font-weight: bold;
  margin-right: 8px;
}

.register-button {
  margin-top: 12px;
  background-color: #f7a23b;
  color: white;
  border: none;
  padding: 12px 0;
  border-radius: 24px;
  font-weight: bold;
  font-size: 15px;
  cursor: pointer;
}

.register-button:hover {
  background-color: #e69126;
}

.login-link {
  margin-top: 16px;
  font-size: 14px;
  color: #666;
}

.highlight {
  color: #f7a23b;
  font-weight: bold;
  text-decoration: none;
}

.footer {
  margin-top: 24px;
  font-size: 12px;
  color: #aaa;
}
</style>
