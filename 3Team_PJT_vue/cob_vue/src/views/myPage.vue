<template>
  <div class="mypage-wrapper">
    <div class="mypage-container">
      <div class="back-button">
        <button @click="goToCommunity">← 메인페이지로 돌아가기</button>
      </div>
      <h2>My Page</h2>

      <section class="user-info">
        <p v-if="userInfo"><strong>이름:</strong> {{ userInfo.username }}</p>
        <p v-if="userInfo"><strong>아이디:</strong> {{ userInfo.loginId }}</p>
        <p v-if="userInfo"><strong>이메일:</strong> {{ userInfo.email || '등록된 이메일 없음' }}</p>
        <p v-if="userInfo"><strong>가입일:</strong> {{ userInfo.createdAt || '정보 없음' }}</p>
      </section>

      <section class="user-activity">
        <h3>내가 쓴 글</h3>
        <ul>
          <li v-for="post in userPosts" :key="post.id">
            <router-link :to="`/posts/${post.id}`">{{ post.title }}</router-link>
          </li>
        </ul>

        <h3>좋아요 누른 글</h3>
        <ul>
          <li v-for="post in likedPosts" :key="post.id">
            <router-link :to="`/posts/${post.id}`">{{ post.title }}</router-link>
          </li>
        </ul>
      </section>

      <section class="account-actions">
        <button @click="goToPasswordChange">비밀번호 변경</button>
        <button @click="deleteAccount">회원 탈퇴</button>
      </section>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useUserStore } from '@/stores/userStore'
import api from '@/api/axios'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()

const user = userStore.loginUser
const userPosts = ref([])
const likedPosts = ref([])
const userInfo = ref('')

onMounted(async () => {
  const id = user.userId

  try {
    const res = await api.get('/api/user')
    userInfo.value = res.data
    const postRes = await api.get('/api/posts')
    const allPosts = postRes.data
    userPosts.value = allPosts.filter((post) => String(post.userId) === String(id))

    const likeRes = await api.get(`/api/posts/liked?userId=${user.userId}`)
    likedPosts.value = likeRes.data
  } catch (err) {
    console.error('마이페이지 데이터 불러오기 실패', err)
  }
})

const goToPasswordChange = () => {
  router.push('/change-password')
}

const deleteAccount = async () => {
  if (!confirm('정말 탈퇴하시겠습니까?')) return
  await api.delete(`/api/user`)
  alert('탈퇴되었습니다')
  userStore.logout()
}

const goToCommunity = () => {
  router.push('/posts')
}
</script>

<style scoped>
.mypage-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 40px 20px;
  background: linear-gradient(rgba(255,255,255,0.75), rgba(255,255,255,0.75)), url('@/assets/background/food.jpg') center/cover no-repeat;
  box-sizing: border-box;
}

.mypage-container {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(12px);
  border-radius: 20px;
  padding: 40px;
  max-width: 900px;
  width: 100%;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  color: #272932;
  font-family: 'Pretendard', sans-serif;
}

.back-button {
  margin-bottom: 20px;
}
.back-button button {
  padding: 10px 20px;
  font-size: 14px;
  border-radius: 8px;
  background: #eeeeef;
  cursor: pointer;
  border: none;
  font-weight: 500;
  transition: background 0.2s ease;
}
.back-button button:hover {
  background: #ccc;
}

h2 {
  font-size: 32px;
  margin-bottom: 30px;
  color: #2a2a2a;
  font-weight: 700;
  text-align: center;
}

.user-info {
  background: #ffffffdd;
  padding: 24px;
  border-radius: 12px;
  margin-bottom: 32px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}
.user-info p {
  margin: 8px 0;
  font-size: 16px;
}

.user-activity h3 {
  margin-top: 36px;
  margin-bottom: 12px;
  font-size: 20px;
  color: #333;
  border-bottom: 2px solid #eee;
  padding-bottom: 6px;
}
.user-activity ul {
  margin-top: 12px;
  padding-left: 20px;
  font-size: 15px;
  color: #333;
}
.user-activity li {
  margin-bottom: 8px;
  list-style: disc;
}

.account-actions {
  margin-top: 40px;
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
  justify-content: center;
}
.account-actions button {
  padding: 12px 20px;
  border-radius: 10px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none;
  min-width: 160px;
}
.account-actions button:first-child {
  background: #ffe599;
  color: #333;
}
.account-actions button:first-child:hover {
  background: #ffd966;
}
.account-actions button:last-child {
  background: #f8b3b3;
  color: #fff;
}
.account-actions button:last-child:hover {
  background: #ff7f7f;
}
</style>
