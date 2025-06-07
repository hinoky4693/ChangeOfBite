import { createRouter, createWebHistory } from 'vue-router'
import MealCalendar from '@/views/mealCalendar.vue'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'
import Community from '@/views/community.vue'
import CommunityDetail from '@/components/communityDetail.vue'
import CommunityUpdate from '@/components/communityUpdate.vue'
import CommunityInsert from '@/components/communityInsert.vue'
import index from '@/views/index.vue'
import ChatRoom from '@/components/chatRoom.vue'
import ChatRoomSelector from '@/components/chatRoomSelector.vue'
import MealScore from '@/components/mealScore.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'index',
      component: index,
    },
    {
      path: '/meal-calendar',
      name: 'mealCalendar',
      component: MealCalendar,
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
    },
    {
      path: '/register',
      name: 'register',
      component: Register,
    },
    {
      path: '/posts',
      name: 'posts',
      component: Community,
    },
    {
      path: '/posts/:id',
      component: CommunityDetail,
      props: true,
    },
    {
      path: '/posts/:id/edit',
      name: 'postEdit',
      component: CommunityUpdate,
    },
    {
      path: '/posts/write',
      component: CommunityInsert,
    },
    {
      path: '/posts/category/:category',
      name: 'CommunityByCategory',
      component: () => import('@/views/community.vue'),
    },
    {
      path: '/posts',
      name: 'Community',
      component: () => import('@/views/community.vue'),
    },
    {
      path: '/mypage',
      name: 'MyPage',
      component: () => import('@/views/myPage.vue'),
    },
    {
      path: '/meal-form',
      name: 'MealForm',
      component: () => import('@/views/AIMealForm.vue'),
    },
    {
      path: '/chat/:room',
      name: 'ChatRoom',
      component: ChatRoom
    },
    {
      path: '/chat',
      name: 'ChatRoomSelector',
      component: ChatRoomSelector
    },
    {
       path: '/meal-score',
       name: 'MealScore',
       component: MealScore,
    },
  ],
})

export default router
