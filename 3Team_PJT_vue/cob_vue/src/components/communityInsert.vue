<template>
  <div class="write-background">
  <div class="write-container">
      <button type="button" class="back-btn" @click="goToList">← 게시글 목록으로</button>

    <h1>게시글 등록</h1>
    <form @submit.prevent="submitPost">
      <div class="form-group">
        <label>제목</label>
        <input v-model="title" type="text" required />
      </div>

      <div class="form-group">
        <label>카테고리</label>
        <select v-model="category" required>
          <option value="">선택</option>
          <option value="벌크업">벌크업</option>
          <option value="다이어트">다이어트</option>
          <option value="린메스업">린메스업</option>
          <option value="체형 유지">체형 유지</option>
        </select>
      </div>

      <div class="form-group">
        <label>내용</label>
        <textarea v-model="content" rows="6" required></textarea>
      </div>

      <div class="form-group">
        <label>첨부 파일</label>
        <input type="file" multiple @change="handleFileChange" />
      </div>

      <button type="submit">등록</button>
    </form>
  </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api/axios'
import { useUserStore } from '@/stores/userStore'

const router = useRouter()
const title = ref('')
const category = ref('')
const content = ref('')
const files = ref([])
const writer = ref('')

const userStore = useUserStore()

const goToList = () => {
  router.push('/posts')
}


const handleFileChange = (e) => {
  files.value = Array.from(e.target.files)
}

const submitPost = async () => {
  const formData = new FormData()
  formData.append('title', title.value)
  formData.append('category', category.value)
  formData.append('content', content.value)
  formData.append('userId', userStore.loginUser.userId)
  formData.append('writer', userStore.loginUser.name)
  files.value.forEach((file) => {
    formData.append('attachs', file) // 'files'는 백엔드에서 받을 이름
  })

  // 파일 첨부 시: formData.append("attachs", fileInput.files[0])

  await api.post('http://localhost:8080/api/posts', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })

  // 등록 성공 후 이동
  alert('등록 완료')
  router.push(`/posts`)
}
</script>

<style scoped>
.back-btn {
  margin-bottom: 10px;
  background: #eeeeef;
  color: #272932;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: bold;
  transition: background 0.2s ease;
}

.back-btn:hover {
  background: #dcdcdc;
}

.write-background {
  background: url('../assets/background/food.jpg') no-repeat center center fixed;
  background-size: cover;
  min-height: 100vh;
  padding: 60px 0;
}

.write-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 32px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.12);
  backdrop-filter: blur(4px);
  transition: all 0.3s ease;
}

.write-container:hover {
  transform: scale(1.01);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

h1 {
  margin-bottom: 24px;
  font-size: 24px;
  color: #333;
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
select,
textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 14px;
}

button[type='submit'] {
  background: #c2e66e;
  color: #272932;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: bold;
}

button[type='submit']:hover {
  background: #a8d14a;
}
</style>

