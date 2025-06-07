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
        <VueCal />
      </div>
      <Footer />
    </div>
  </div>
</template>

<script setup>
import Navbar from '@/components/navbar.vue'
import Header from '@/components/header.vue'
import VueCal from '@/components/vue-cal.vue'
import Footer from '@/components/footer.vue'
</script>

<style scoped>
.layout-container {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

.sidebar {
  width: 220px;
  background-color: #f8f8f8;
  border-right: 1px solid #ddd;
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
