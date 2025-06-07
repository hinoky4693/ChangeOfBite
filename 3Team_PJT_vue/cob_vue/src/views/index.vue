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
        <mainPage />
      </div>
      <Footer />
    </div>
  </div>
</template>

<script setup>
import Navbar from '@/components/navbar.vue'
import Header from '@/components/header.vue'
import Footer from '@/components/footer.vue'
import mainPage from '@/components/mainPage.vue'
</script>

<style scoped>
.layout-container {
  display: flex;
  height: 100vh;
  overflow: hidden;
  background-image: url('../assets/background/food.jpg'); /* 실제 경로로 교체 */
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  position: relative;
}

.sidebar {
  width: 220px;
  background-color: transparent;
  padding: 20px;
  box-sizing: border-box;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 20px;
  box-sizing: border-box;
  overflow-y: auto;
}

.calendar-area {
  flex: 1;
  margin: 20px 0;
  overflow: hidden;
}
</style>
