<template>
  <div class="layout-container">
    <div class="sidebar">
      <Navbar />
    </div>
  </div>
  <div class="chat-wrapper" :style="{ background: currentStyle.bg }">
    <div class="chat-container">
      <header class="chat-header">
        <div class="room-header">
          <img :src="currentStyle.icon" class="room-icon" />
          <h2>{{ roomTitle }}</h2>
        </div>
        <p class="username">사용자: {{ username }}</p>
        <button @click="leaveChat" class="exit-btn">나가기</button>
      </header>

      <section class="chat-messages" ref="messageList">
        <div
          v-for="(msg, i) in messages"
          :key="i"
          :class="['message', msg.sender === username ? 'sent' : 'received']"
        >
          <div class="meta">
            <span class="time">{{ msg.regdate }}</span>
          </div>
          <div :class="msg.code === '4' ? 'bubble-image' : 'bubble-text'">
            <template v-if="msg.code === '3'">
              {{ msg.content }}
            </template>
            <template v-else-if="msg.code === '4'">
              <img :src="getEmoticonPath(msg.content)" :alt="msg.content" />
            </template>
            <template v-else-if="msg.code === '5'">
              <a :href="msg.content" download>{{ getFileName(msg.content) }}</a>
            </template>
            <template v-else>
              <div class="notice">{{ msg.content }}</div>
            </template>
          </div>
        </div>
      </section>

      <footer class="chat-input" v-if="ws">
        <button class="emoticon-btn" @click="toggleEmoticon">😀</button>
        <button class="file-btn" @click="triggerFileSelect">📎</button>
        <input type="file" ref="fileInput" @change="handleFileUpload" style="display: none" />

        <input v-model="input" @keydown.enter="sendMessage" placeholder="메시지를 입력하세요..." />
        <button @click="sendMessage">입력</button>
        <EmoticonPicker :visible="showEmoticon" @select="sendEmoticon" />
      </footer>
    </div>
  </div>
</template>

<script setup>
import Navbar from './navbar.vue'
import { ref, onMounted, nextTick } from 'vue'
import { useUserStore } from '@/stores/userStore'
import EmoticonPicker from './EmoticonPicker.vue'
import { useRouter, useRoute } from 'vue-router'
import foodBg from '@/assets/background/food.jpg'

const router = useRouter()
const route = useRoute()
const store = useUserStore()
const username = store.loginUser.name

const ws = ref(null)
const messages = ref([])
const input = ref('')
const messageList = ref(null)
const showEmoticon = ref(false)
const fileInput = ref(null)
const room = route.params.room

const roomTitles = {
  all: '자유 채팅방',
  bulkup: '벌크업 채팅방',
  diet: '다이어트 채팅방',
  leanmass: '린메스업 채팅방',
  maintain: '체형유지 채팅방'
}
const roomTitle = roomTitles[room] || '실시간 채팅'

const roomStyles = {
  all: {
    bg: `linear-gradient(rgba(255,255,255,0.7), rgba(255,255,255,0.7)), url(${foodBg}) center/cover no-repeat`,
    icon: '/chatIcons/all.png'
  },
  bulkup: {
    bg: `linear-gradient(to right, #ffe5ecaa, #ffd6e0aa), url(${foodBg}) center/cover no-repeat`,
    icon: '/chatIcons/bulkup.png'
  },
  diet: {
    bg: `linear-gradient(to right, #e0ffe8aa, #c3f2ccaa), url(${foodBg}) center/cover no-repeat`,
    icon: '/chatIcons/diet.png'
  },
  leanmass: {
    bg: `linear-gradient(to right, #e0f0ffaa, #c3d7f2aa), url(${foodBg}) center/cover no-repeat`,
    icon: '/chatIcons/leanmass.png'
  },
  maintain: {
    bg: `linear-gradient(to right, #fff9e6aa, #f7efc3aa), url(${foodBg}) center/cover no-repeat`,
    icon: '/chatIcons/maintain.png'
  }
}

const currentStyle = roomStyles[room] || roomStyles.all

onMounted(() => {
  connect()
})

function connect() {
  ws.value = new WebSocket(`ws://172.30.1.82:8080/chatserver/${room}`)

  ws.value.onopen = () => {
    send('1', '대화방에 입장했습니다.')
  }

  ws.value.onmessage = (event) => {
    const msg = JSON.parse(event.data)
    messages.value.push(msg)
    scrollToBottom()
  }

  window.addEventListener('beforeunload', disconnect)
}

function leaveChat() {
  disconnect()
  router.back()
}

function sendMessage() {
  if (!input.value.trim()) return
  const code = input.value.startsWith('/') ? '4' : '3'
  const content = code === '4' ? input.value.substring(1) : input.value
  send(code, content)
  input.value = ''
}

function send(code, content) {
  const msg = {
    code,
    sender: username,
    receiver: '',
    content,
    regdate: new Date().toLocaleString(),
  }
  ws.value?.send(JSON.stringify(msg))
}

function disconnect() {
  send('2', '대화방에서 나갔습니다.')
  ws.value?.close()
  ws.value = null
}

function toggleEmoticon() {
  showEmoticon.value = !showEmoticon.value
}

function sendEmoticon(name) {
  send('4', name)
  showEmoticon.value = false
}

function scrollToBottom() {
  nextTick(() => {
    const el = messageList.value
    if (el) el.scrollTop = el.scrollHeight
  })
}

function triggerFileSelect() {
  fileInput.value?.click()
}

async function handleFileUpload(event) {
  const file = event.target.files[0]
  if (!file) return

  const formData = new FormData()
  formData.append('file', file)

  try {
    const res = await fetch('http://localhost:8080/uploads', {
      method: 'POST',
      body: formData,
    })
    const data = await res.json()
    send('5', data.url)
  } catch (err) {
    alert('파일 업로드 실패')
  }
}

function getEmoticonPath(name) {
  return `/emoticons/${name}.png`
}

function getFileName(url) {
  return decodeURIComponent(url.split('/').pop())
}
</script>


<style scoped>
.room-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.room-icon {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
  box-shadow: 0 0 6px rgba(0, 0, 0, 0.15);
  background-color: white;
  padding: 2px;
}

/* 이하 기존 스타일 유지 */
.exit-btn {
  position: absolute;
  top: 10px;
  left: 10px;
  padding: 6px 10px;
  background-color: #150949;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 12px;
  cursor: pointer;
}
.exit-btn:hover {
  background-color: #701379;
}
.file-link {
  color: #1e88e5;
  text-decoration: underline;
  font-size: 14px;
}
.file-btn {
  background: transparent;
  font-size: 20px;
  border: none;
  cursor: pointer;
  margin-left: 6px;
}
.bubble-text {
  display: inline-block;
  padding: 2px 6px;
  border-radius: 12px;
  font-size: 14px;
  background-color: #dcf8c6;
  border: 1px solid #cde6a2;
  text-align: left;
  line-height: 1.4;
  white-space: pre-wrap;
  word-break: break-word;
  width: fit-content;
  max-width: 250px;
}
.message.received .bubble-text {
  background-color: #ffffff;
  border-color: #ddd;
  border-top-left-radius: 0;
}
.message.sent .bubble-text {
  border-top-right-radius: 0;
}
.bubble-image {
  display: inline-flex;
  padding: 0;
  border: none;
  background: transparent;
  align-items: center;
  justify-content: center;
}
.bubble-image img {
  max-width: 50px;
  max-height: 50px;
  object-fit: contain;
}
.chat-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-image: url('../assets/background/food.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  transition: background 0.5s ease;
}
.chat-container {
  display: flex;
  flex-direction: column;
  width: 400px;
  height: 600px;
  border-radius: 16px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.55);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  color: #222;
  font-family: 'Pretendard', sans-serif;
}
.chat-header {
  background: linear-gradient(90deg, #b6d7a8, #a4c639);
  padding: 20px 10px 10px;
  text-align: center;
  position: relative;
  border-bottom: 1px solid #ccc;
}
.username {
  font-size: 13px;
  margin-top: 5px;
}
.chat-messages {
  flex-grow: 1;
  padding: 12px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}
.message {
  margin-bottom: 12px;
  display: flex;
  flex-direction: column;
  max-width: 80%;
}
.message.sent {
  align-self: flex-end;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}
.message.received {
  align-self: flex-start;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
.meta {
  font-size: 11px;
  color: #777;
  margin-bottom: 2px;
  display: flex;
  justify-content: space-between;
}
.notice {
  font-size: 12px;
  color: #999;
  text-align: center;
  margin-top: 6px;
}
.chat-input {
  display: flex;
  align-items: center;
  padding: 10px;
  background-color: #f5f5f5;
  border-top: 1px solid #ddd;
  position: relative;
}
.chat-input input {
  flex-grow: 1;
  padding: 10px;
  border-radius: 6px;
  border: 1px solid #ccc;
  font-size: 14px;
  margin: 0 8px;
}
.chat-input button {
  padding: 10px 12px;
  background-color: #e91e63;
  color: white;
  border: none;
  border-radius: 6px;
  font-weight: bold;
  cursor: pointer;
}
.chat-input button:hover {
  background-color: #d81b60;
}
.emoticon-btn {
  background: transparent;
  font-size: 20px;
  border: none;
  cursor: pointer;
}
</style>