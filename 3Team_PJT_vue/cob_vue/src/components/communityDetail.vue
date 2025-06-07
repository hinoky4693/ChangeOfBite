<template>
  <div class="post-detail-wrapper">
  <div v-if="post" class="post-detail">
    <div class="post-header">
      <span class="category">{{ post.category }}</span>
      <h1 class="title">{{ post.title }}</h1>
      <div class="meta">
        <span class="author">{{ post.writer }}</span> ·
        <span class="date">{{ post.createdAt }}</span> ·
        <span class="views">조회수 {{ post.viewCnt }}</span> ·
        <span class="likes">좋아요 {{ post.likes }}</span>
      </div>
    </div>

    <div class="post-body">
      <p>{{ post.content }}</p>
      <img :src="post.imageUrl" alt="식단 사진" class="post-image" v-if="post.imageUrl" />
      <div v-if="post.files && post.files.length">
        <div v-for="file in post.files" :key="file.id">
          <img
            v-if="
              file.filePath.endsWith('.jpg') ||
              file.filePath.endsWith('.png') ||
              file.filePath.endsWith('.jpeg')
            "
            :src="`http://localhost:8080${file.filePath}`"
            alt="첨부 이미지"
            style="width: 300px; height: auto; margin-bottom: 10px"
          />
        </div>
      </div>
    </div>

    <div class="post-footer">
      <button class="like-btn" @click="toggleLike">
        {{ hasLiked ? '💔 좋아요 취소' : '👍 좋아요' }}
      </button>
      <div class="post-footer" v-if="post && userStore.loginUser.userId === post.userId">
        <button class="edit-btn" @click="goToEdit">✏️ 수정하기</button>
        <button class="delete-btn" @click="deletePost">🗑️ 삭제하기</button>
      </div>
      <button class="back-btn" @click="goToList">← 목록으로</button>
    </div>
  </div>
  <div v-else>로딩 중...</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import api from '@/api/axios'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'

const router = useRouter()
const route = useRoute()
const postId = route.params.id
const post = ref(null)
const userStore = useUserStore()
const hasLiked = ref(false)

function goToList() {
  router.push('/posts')
}

onMounted(async () => {
  try {
    const res = await api.get(`http://localhost:8080/api/posts/${postId}`)
    post.value = res.data
    const likeCheck = await api.get(`http://localhost:8080/api/posts/${postId}/like/check`, {
      params: { userId: userStore.loginUser.userId },
    })
    hasLiked.value = likeCheck.data.liked === true
  } catch (err) {
    console.error('게시글 불러오기 실패:', err)
  }
})

async function toggleLike() {
  try {
    if (hasLiked.value) {
      await api.post(`http://localhost:8080/api/posts/${postId}/dislike`, {
        userId: userStore.loginUser.userId,
      })
      post.value.likes -= 1
      hasLiked.value = false
    } else {
      await api.post(`http://localhost:8080/api/posts/${postId}/like`, {
        userId: userStore.loginUser.userId,
      })
      post.value.likes += 1
      hasLiked.value = true
    }
  } catch (err) {
    console.error('좋아요 처리 실패:', err)
  }
}

function goToEdit() {
  router.push(`/posts/${postId}/edit`)
}

async function deletePost() {
  if (!confirm('정말 삭제하시겠습니까?')) return

  try {
    await api.delete(`http://localhost:8080/api/posts/${postId}`)
    alert('삭제되었습니다.')
    router.push('/posts')
  } catch (err) {
    console.error('삭제 실패:', err)
    alert('삭제 중 오류가 발생했습니다.')
  }
}
</script>

<style scoped>
.post-detail-wrapper {
  background: url('../assets/background/food.jpg') no-repeat center center fixed;
  background-size: cover;
  min-height: 100vh;
  padding: 40px 0;
}

.post-detail {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 32px;
  max-width: 768px;
  margin: 60px auto;
  font-family: 'Pretendard', sans-serif;
  color: #272932;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  backdrop-filter: blur(3px);
  transition: all 0.3s ease;
}

.post-detail:hover {
  transform: scale(1.01);
  box-shadow: 0 14px 36px rgba(0, 0, 0, 0.25);
}

.post-header .category {
  display: inline-block;
  background-color: #c2e66e;
  color: #272932;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  margin-bottom: 8px;
  font-weight: bold;
}

.post-header .title {
  font-size: 28px;
  margin: 12px 0 8px;
  font-weight: 700;
  color: #333;
}

.meta {
  font-size: 13px;
  color: #777;
  margin-bottom: 24px;
}

.post-body {
  font-size: 16px;
  line-height: 1.8;
  margin-bottom: 32px;
}

.post-image {
  width: 100%;
  max-height: 400px;
  object-fit: cover;
  border-radius: 10px;
  margin-top: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.attached-files {
  margin-top: 20px;
  font-size: 14px;
}

.attached-files ul {
  list-style: none;
  padding: 0;
}

.attached-files li {
  margin-bottom: 8px;
}

.post-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
  gap: 10px;
}

.like-btn,
.back-btn,
.edit-btn,
.delete-btn {
  background: #eeeeef;
  border: none;
  padding: 10px 18px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.like-btn:hover,
.back-btn:hover {
  background: #dcdcdc;
}

.edit-btn {
  background: #ffd966;
}

.edit-btn:hover {
  background: #ffcc00;
}

.delete-btn {
  background: #ff8a8a;
  color: white;
}

.delete-btn:hover {
  background: #ff5e5e;
}
</style>
