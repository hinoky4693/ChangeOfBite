<template>
  <div class="navbar">
    <div class="header">
      <div class="community-container">
        <RouterLink to="/" class="logo-link">
          <img src="../assets/icon/LOGO2.png" alt="로고" class="logo-image" />
        </RouterLink>
      </div>
    </div>

    <div class="menu-nav">
      <!-- Community -->
      <div class="dropdown-container" @click="toggleDropdown">
        <div class="button-nav" :class="{ 'active-nav': isCommunityActive }">
          <img class="icon-nav-squares-four" src="../assets/icon/SquaresFour.svg" />
          <div class="text">
            <div class="label" :class="{ 'active-label': isCommunityActive }">커뮤니티</div>
          </div>
          <div class="icon">
            <img class="icon-caret-down" src="../assets/icon/CaretDown.svg" />
          </div>
        </div>

        <div v-if="dropdownOpen" class="dropdown-menu">
          <RouterLink
            to="/posts"
            class="dropdown-item"
            :class="{ 'active-dropdown': route.path === '/posts' }"
            >전체 게시글</RouterLink
          >
          <hr />
          <RouterLink
            to="/posts/category/벌크업"
            class="dropdown-item"
            :class="{ 'active-dropdown': route.path.includes('/posts/category/벌크업') }"
            >벌크업</RouterLink
          >
          <hr />
          <RouterLink
            to="/posts/category/다이어트"
            class="dropdown-item"
            :class="{ 'active-dropdown': route.path.includes('/posts/category/다이어트') }"
            >다이어트</RouterLink
          >
          <hr />
          <RouterLink
            to="/posts/category/체형 유지"
            class="dropdown-item"
            :class="{ 'active-dropdown': route.path.includes('/posts/category/체형 유지') }"
            >체형 유지</RouterLink
          >
          <hr />
          <RouterLink
            to="/posts/category/린메스업"
            class="dropdown-item"
            :class="{ 'active-dropdown': route.path.includes('/posts/category/린메스업') }"
            >린메스업</RouterLink
          >
        </div>
      </div>

      <!-- Other buttons -->
      <RouterLink
        to="/meal-calendar"
        class="button-nav"
        :class="{ 'active-nav': isCalendarActive }"
      >
        <img class="icon-nav-calendar-dots" src="../assets/icon/CalendarDots.svg" />
        <div class="text">
          <div class="label" :class="{ 'active-label': isCalendarActive }">식단 캘린더</div>
        </div>
      </RouterLink>

      <RouterLink to="/chat" class="button-nav" :class="{ 'active-nav': isChatActive }">
        <img class="icon-nav-chat-teardrop-dots" src="../assets/icon/ChatTeardropDots.svg" />
        <div class="text">
          <div class="label" :class="{ 'active-label': isChatActive }">실시간 채팅</div>
        </div>
      </RouterLink>

      <RouterLink to="/meal-form" class="button-nav">
        <img class="icon-nav-fork-knife" src="../assets/icon/ForkKnife.svg" />
        <div class="text"><div class="label">AI 맞춤 식단</div></div>
      </RouterLink>

      <RouterLink to="/meal-score" class="button-nav">
        <img class="icon-nav-bowl-food" src="../assets/icon/BowlFood.svg" />
        <div class="text"><div class="label">오늘의 식단 평가</div></div>
      </RouterLink>

      <div class="button-nav">
        <img class="icon-nav-notebook" src="../assets/icon/notebook.svg" />
        <div class="text"><div class="label">식단 일기</div></div>
      </div>

      <div class="button-nav">
        <img class="icon-nav-chart-line-up" src="../assets/icon/ChartLineUp.svg" />
        <div class="text"><div class="label">변화도</div></div>
      </div>

      <RouterLink
        to="/register"
        class="button-nav"
        v-if="!store.token"
        :class="{ 'active-nav': isRegisterActive }"
      >
        <img class="icon-nav-person-simple-tai-chi" src="../assets/icon/PersonSimpleTaiChi.svg" />
        <div class="text">
          <div class="label" :class="{ 'active-label': isRegisterActive }">회원가입</div>
        </div>
      </RouterLink>

      <RouterLink
        to="/login"
        class="button-nav"
        v-if="!store.token"
        :class="{ 'active-nav': isLoginActive }"
      >
        <img class="icon-nav-heartbeat" src="../assets/icon/heartbeat.svg" />
        <div class="text">
          <div class="label" :class="{ 'active-label': isLoginActive }">로그인</div>
        </div>
      </RouterLink>
    </div>

    <div
      class="button-nav2 logout-button"
      v-if="store.token"
      @click="store.logout"
      :style="{ cursor: 'pointer' }"
    >
      <img class="icon-nav-sign-out" src="../assets/icon/signout.svg" />
      <div class="text"><div class="label">Logout</div></div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useUserStore } from '@/stores/userStore'
import { useRoute } from 'vue-router'

const store = useUserStore()
const route = useRoute()

const dropdownOpen = ref(false)
const toggleDropdown = () => {
  dropdownOpen.value = !dropdownOpen.value
}

const isCommunityActive = computed(() => route.path.startsWith('/posts'))
const isCalendarActive = computed(() => route.path.startsWith('/meal-calendar'))
const isChatActive = computed(() => route.path.startsWith('/chat'))
const isRegisterActive = computed(() => route.path.startsWith('/register'))
const isLoginActive = computed(() => route.path.startsWith('/login'))
</script>

<style scoped>
.button-nav,
.button-nav2,
.button-nav .label,
.button-nav2 .label {
  text-decoration: none !important;
}
.navbar {
  position: fixed;
  top: 0;
  left: -12px;
  width: 220px;
  height: 100vh;
  padding: 28px 20px;
  display: flex;
  flex-direction: column;
  gap: 28px;
  background: rgba(255, 255, 255, 0.65);
  backdrop-filter: blur(10px);
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.04);
  z-index: 1000;
}
.button-nav2.logout-button {
  margin-bottom: 40px;
}

.logo-image {
  width: 180px;
  margin: 0 auto 16px auto;
  display: block;
}

.menu-nav {
  display: flex;
  flex-direction: column;
  gap: 12px;
  flex: 1;
}

.button-nav,
.button-nav2 {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 12px;
  padding: 12px 10px;
  width: 100%;
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(8px);
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.05);
  transition: all 0.25s ease;
  cursor: pointer;
  position: relative;
  z-index: 1;
}

.button-nav:hover,
.button-nav2:hover {
  background: rgba(255, 255, 255, 0.9);
  transform: translateY(-2px);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
}

.button-nav img,
.button-nav2 img {
  width: 20px;
  height: 20px;
  transition: transform 0.3s ease;
}

.button-nav:hover img,
.button-nav2:hover img {
  transform: scale(1.15) rotate(2deg);
}

.label {
  color: #272932;
  font-family: 'Pretendard', sans-serif;
  font-size: 14px;
  font-weight: 600;
}

.dropdown-container {
  position: relative;
  cursor: pointer;
}

.dropdown-menu {
  position: absolute;
  top: 45px;
  left: 20px;
  width: 180px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  backdrop-filter: blur(6px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  padding: 10px 0;
  z-index: 12;
}

.dropdown-item {
  padding: 10px 20px;
  font-size: 13px;
  color: #333;
  font-weight: 500;
  text-decoration: none;
  display: block;
  transition: background 0.2s ease;
}

.dropdown-item:hover {
  background-color: #f1f1f1;
}

.active-dropdown {
  background-color: #e5f6ce;
  color: #56ab2f;
  font-weight: bold;
}

.active-nav {
  background: rgba(198, 230, 110, 0.2);
  border-left: 4px solid #a8e063;
}

.active-label {
  color: #56ab2f !important;
  font-weight: 700;
}
</style>
