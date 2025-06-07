<template>
  <div>
    <RouterView />
    <!-- 항상 하단 고정 -->
  </div>
</template>

<script setup>
// import mealCalendar from '@/views/mealCalendar.vue';
import header from '@/components/header.vue'
import { RouterView } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { onMounted, onBeforeMount } from 'vue'
import router from './router'
import ChatRoom from './components/chatRoom.vue'
const store = useUserStore()

// onMounted(() => {
//   const store = useUserStore();

//   if (store.token && !store.loginUser.name) {
//     store.fetchUserInfo();
//   }
// });

onBeforeMount(async () => {
  const store = useUserStore();
  if (store.token && !store.loginUser.name) {
    await store.fetchUserInfo();
  } else {
    router.push({name:'index'});
  }
})
</script>

<style scoped></style>
