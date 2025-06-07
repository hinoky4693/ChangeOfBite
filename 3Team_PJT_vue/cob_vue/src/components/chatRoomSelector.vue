<template>
  <div class="room-select-wrapper">
    <div class="room-grid">
      <div
        v-for="room in rooms"
        :key="room.value"
        class="room-card"
        @click="enterRoom(room.value)"
        :style="{ backgroundColor: room.color }"
      >
        <div class="room-icon">
          <img :src="`/chatIcons/${room.value}.png`" :alt="room.label" />
        </div>
        <div class="room-title">{{ room.label }}</div>
        <div class="room-desc">{{ room.desc }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const rooms = [
  { value: 'all', label: '자유 채팅방', desc: '모두와 함께 자유롭게 이야기해요.', color: 'rgba(255,255,255,0.85)' },
  { value: 'bulkup', label: '벌크업 채팅방', desc: '근육 증가를 목표로 모인 사람들의 공간.', color: 'rgba(245, 199, 199, 0.85)' },
  { value: 'diet', label: '다이어트 채팅방', desc: '감량과 건강한 식습관을 위한 채팅방.', color: 'rgba(198, 245, 213, 0.85)' },
  { value: 'leanmass', label: '린메스업 채팅방', desc: '군살 없이 체형을 만드는 사람들이 모인 곳.', color: 'rgba(198, 224, 245, 0.85)' },
  { value: 'maintain', label: '체형유지 채팅방', desc: '현재 상태를 꾸준히 유지하고 싶은 분들을 위한 채널.', color: 'rgba(245, 240, 198, 0.85)' }
]

function enterRoom(roomName) {
  router.push({ name: 'ChatRoom', params: { room: roomName } })
}
</script>

<style scoped>
.room-select-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(rgba(255, 255, 255, 0.7), rgba(255, 255, 255, 0.7)),
    url('@/assets/background/food.jpg') center/cover no-repeat;
  padding: 40px;
  box-sizing: border-box;
}

.room-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
  width: 100%;
  max-width: 1200px;
}

.room-card {
  backdrop-filter: blur(8px);
  border-radius: 16px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  padding: 40px 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  text-align: center;
  transition: transform 0.2s ease, background-color 0.3s ease;
  color: #2c3e50;
}

.room-card:hover {
  transform: scale(1.05);
}

.room-title {
  font-size: 20px;
  font-weight: bold;
  margin-top: 10px;
}

.room-desc {
  font-size: 14px;
  color: #555;
  margin-top: 6px;
}

.room-icon img {
  width: 48px;
  height: 48px;
}
</style>
