<template>
  <div class="layout-container">
    <!-- 왼쪽 사이드바 -->
    <div class="sidebar">
      <Navbar />
    </div>

    <!-- 오른쪽 본문 -->
    <div class="main-content">
      <Header />
      <div class="calendar-area">
        <Community />
      </div>
      <Footer />
    </div>
  </div>
</template>

<script setup>
import Navbar from '@/components/navbar.vue'
import Header from '@/components/header.vue'
import Footer from '@/components/footer.vue'
import Community from '@/components/community.vue'
</script>

<style scoped>
.layout-container {
  display: flex;
  height: 100vh;
  overflow: hidden;
  background: linear-gradient(rgba(255,255,255,0.75), rgba(255,255,255,0.75)), url('@/assets/background/food.jpg') center/cover no-repeat;
}

.sidebar {
  width: 220px;
  background-color: #f8f8f8;
  border-right: 1px solid #ddd;
  padding: 20px;
  box-sizing: border-box;
  backdrop-filter: blur(8px);
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 20px;
  box-sizing: border-box;
  overflow-y: auto;
  backdrop-filter: blur(6px);
}

.calendar-area {
  flex: 1;
  margin: 20px 0;
  overflow: hidden;
}
</style>
