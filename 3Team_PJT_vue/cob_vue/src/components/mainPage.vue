<template>
  <div class="main-container">
    <!-- Hero Section -->
    <section class="hero">
      <h1>당신의 건강한 목표, 여기서 시작됩니다</h1>
      <p>AI가 맞춤 식단과 운동을 제안해드립니다</p>
      <router-link to="/meal-calendar" class="cta-button">식단 캘린더 시작하기</router-link>
    </section>

    <!-- 핵심 기능 안내 -->
    <section class="features">
      <router-link to="/meal-form" class="feature link-feature">
        <img src="../assets/icon/AI.png" class="icon" alt="AI 식단 아이콘" />
        <h3>AI 맞춤 식단</h3>
        <p>운동 목표와 건강 상태에 맞춘 식단 추천</p>
      </router-link>

      <router-link to="/meal-calendar" class="feature link-feature">
        <img src="../assets/icon/calendar.png" class="icon" alt="캘린더 아이콘" />
        <h3>식단 캘린더</h3>
        <p>주간/월간 식단 일정을 시각적으로 확인</p>
      </router-link>

      <router-link to="/posts" class="feature link-feature">
        <img src="../assets/icon/community.png" class="icon" alt="커뮤니티 아이콘" />
        <h3>커뮤니티</h3>
        <p>다른 사용자와 식단/운동 노하우 공유</p>
      </router-link>

      <router-link to="/meal-score" class="feature link-feature">
        <img src="../assets/icon/3d-lettuce.png" class="icon" alt="분석 아이콘" />
        <h3>오늘의 식단 평가</h3>
        <p>오늘의 식단을 점수로 매겨드립니다</p>
      </router-link>
    </section>

    <!-- 인기 게시글 -->
    <!-- <section class="hot-posts">
      <h2>🔥 인기 커뮤니티 글</h2>
      <ul>
        <li v-for="post in topPosts" :key="post.id">
          <router-link :to="`/posts/${post.id}`">{{ post.title }}</router-link>
        </li>
      </ul>
    </section> -->

    <!-- 가입 유도 배너 -->
    <section class="signup-promo" v-if="!store.token">
      <h2>지금 가입하고 더 많은 기능을 경험해보세요!</h2>
      <router-link to="/register" class="cta-button">회원가입</router-link>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api/axios'
import { useUserStore } from '@/stores/userStore'

const store = useUserStore()
const topPosts = ref([])

// onMounted(async () => {
//   try {
//     const res = await api.get('/api/posts')
//     topPosts.value = res.data.sort((a, b) => b.likes - a.likes).slice(0, 5)
//   } catch (err) {
//     console.error('인기 게시글 로딩 실패', err)
//   }
// })
</script>

<style scoped>
.main-container {
  padding: 40px 20px;
  max-width: 1024px;
  margin: 0 auto;
  font-family: 'Pretendard', sans-serif;
}

/* Hero Section */
.hero {
  text-align: center;
  margin-bottom: 60px;
}
.hero h1 {
  font-size: 32px;
  margin-bottom: 12px;
  color: #272932;
}
.hero p {
  font-size: 16px;
  color: #555;
  margin-bottom: 24px;
}

/* CTA 버튼 (식단 캘린더 시작하기 / 회원가입) */
.cta-button {
  background: linear-gradient(135deg, #a8e063, #56ab2f);
  padding: 14px 30px;
  color: white;
  font-weight: bold;
  font-size: 16px;
  border: none;
  border-radius: 12px;
  box-shadow: 0 4px 14px rgba(88, 182, 66, 0.4);
  transition: all 0.3s ease;
  text-decoration: none;
  display: inline-block;
}
.cta-button:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 18px rgba(88, 182, 66, 0.6);
}

/* Features Section */
.features {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: space-between;
  margin-bottom: 60px;
}

/* Feature 카드 스타일 */
.feature {
  flex: 1 1 220px;
  text-align: center;
  padding: 24px;
  background: rgba(255, 255, 255, 0.7);
  border-radius: 20px;
  backdrop-filter: blur(12px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  cursor: pointer;
}
.feature:hover {
  transform: translateY(-6px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.12);
  background: rgba(255, 255, 255, 0.85);
}

.feature h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 6px;
}
.feature p {
  font-size: 14px;
  color: #666;
}

/* 아이콘 스타일 + hover 애니메이션 */
.icon {
  width: 52px;
  height: 52px;
  margin-bottom: 14px;
  transition: transform 0.3s ease;
}
.feature:hover .icon {
  transform: scale(1.1) rotate(2deg);
}

/* router-link 공통 스타일 */
.link-feature {
  text-decoration: none;
  color: inherit;
}

/* 인기 커뮤니티 글 */
.hot-posts ul {
  padding-left: 20px;
  list-style: disc;
}
.hot-posts h2,
.signup-promo h2 {
  font-size: 20px;
  margin-bottom: 16px;
  color: #272932;
}

/* 가입 유도 배너 */
.signup-promo {
  text-align: center;
  margin-top: 40px;
}
</style>
