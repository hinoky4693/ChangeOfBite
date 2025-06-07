<template>
  <div class="login-container">
    <div class="login-box">
      <!-- 로고 및 타이틀 -->
      <RouterLink to="/" class="logo-section">
        <img src="@/assets/icon/3d-lettuce.png" alt="로고" class="logo" />
      </RouterLink>

      <h2 class="login-title">로그인</h2>

      <!-- 로그인 폼 -->
      <form @submit.prevent="handleLogin" class="login-form">
        <input
          v-model="loginId"
          type="text"
          placeholder="아이디"
          class="input-field"
        />
        <input
          v-model="password"
          type="password"
          placeholder="비밀번호"
          class="input-field"
        />

        <button type="submit" class="login-button">로그인</button>
      </form>

      <div to="/register" class="signup-link">
        계정이 없으신가요? <RouterLink to="/register" class="highlight">회원가입</RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/userStore'
import { ref } from 'vue'

const loginId = ref('')
const password = ref('')

const store = useUserStore();


const handleLogin = async () => {
  try {
    await store.userLogin(loginId.value, password.value);
  } catch (err) {
    alert(err.message);
    loginId.value = '';
    password.value = '';
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #fffaf5;
}

.login-box {
  background-color: white;
  padding: 40px 30px;
  border-radius: 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  width: 100%;
  max-width: 360px;
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

.login-title {
  font-size: 22px;
  font-weight: bold;
  color: #333;
  margin-bottom: 24px;
}

.login-form {
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

.input-field:focus {
  outline: none;
  border-color: #f7a23b;
  box-shadow: 0 0 0 2px #ffe5c2;
}

.forgot-password {
  text-align: right;
  font-size: 13px;
  color: #f7a23b;
  cursor: pointer;
}

.login-button {
  margin-top: 10px;
  background-color: #f7a23b;
  color: white;
  border: none;
  padding: 12px 0;
  border-radius: 24px;
  font-weight: bold;
  font-size: 15px;
  cursor: pointer;
}

.login-button:hover {
  background-color: #e69126;
}

.signup-link {
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
