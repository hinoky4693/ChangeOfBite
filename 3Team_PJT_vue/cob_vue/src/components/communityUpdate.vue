<template>
  <div class="board-update-page">
    <div class="board-update">
      <button type="button" class="back-btn" @click="goBack">← 게시글로 돌아가기</button>
      <h1>게시글 수정</h1>
      <form @submit.prevent="submitUpdate">
        <div class="form-group">
          <label>제목:</label>
          <input v-model="form.title" required />
        </div>

        <div class="form-group">
          <label>내용:</label>
          <textarea v-model="form.content" required></textarea>
        </div>

        <div class="form-group">
          <label>카테고리:</label>
          <select v-model="form.category">
            <option value="벌크업">벌크업</option>
            <option value="다이어트">다이어트</option>
            <option value="린메스업">린메스업</option>
            <option value="체형 유지">체형 유지</option>
          </select>
        </div>

        <div class="form-group">
          <label>파일 첨부:</label>
          <input type="file" multiple @change="handleFiles" />
        </div>

        <button type="submit">수정 완료</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '@/api/axios'
import { useUserStore } from '@/stores/userStore'

const route = useRoute()
const router = useRouter()
const postId = route.params.id
const userStore = useUserStore()

const form = ref({
  title: '',
  content: '',
  category: '',
  userId: userStore.loginUser.userId,
})

const files = ref([])

onMounted(async () => {
  try {
    const res = await api.get(`/api/posts/${postId}`)
    const data = res.data
    form.value.title = data.title
    form.value.content = data.content
    form.value.category = data.category
  } catch (e) {
    console.error('불러오기 실패:', e)
    alert('게시글을 불러오지 못했습니다')
    router.push('/posts')
  }
})

const handleFiles = (e) => {
  files.value = Array.from(e.target.files)
}

const submitUpdate = async () => {
  const formData = new FormData()
  formData.append('title', form.value.title)
  formData.append('content', form.value.content)
  formData.append('category', form.value.category)
  formData.append('userId', form.value.userId)
  files.value.forEach((f) => formData.append('files', f))

  try {
    await api.put(`/api/posts/${postId}`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
    alert('수정 완료')
    router.push(`/posts/${postId}`)
  } catch (e) {
    console.error('수정 실패:', e)
    alert('수정에 실패했습니다')
  }
}

const goBack = () => {
  window.history.back()
}
</script>

<style scoped>
.board-update-page {
  background: url('@/assets/background/food.jpg') no-repeat center center fixed;
  background-size: cover;
  min-height: 100vh;
  padding: 60px 0;
}

.board-update {
  max-width: 600px;
  margin: 0 auto;
  background: rgba(255, 255, 255, 0.95);
  padding: 32px;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(4px);
  position: relative;
}

h1 {
  margin-bottom: 24px;
  font-size: 24px;
  text-align: center;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  font-weight: bold;
  margin-bottom: 8px;
}

input,
textarea,
select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 14px;
}

button[type="submit"] {
  background: #4caf50;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: bold;
  margin-top: 10px;
}

button[type="submit"]:hover {
  background: #388e3c;
}

.back-btn {
  position: absolute;
  top: 16px;
  left: 16px;
  background: #eeeeef;
  color: #272932;
  padding: 10px 10px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
  font-weight: bold;
  transition: background 0.2s ease;
  width: auto;
}


.back-btn:hover {
  background: #dcdcdc;
}
</style>
