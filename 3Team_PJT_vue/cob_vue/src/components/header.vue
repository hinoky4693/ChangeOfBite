<template>
  <div class="device-desktop-variant-back-button">
    <div class="title"></div>
    <div class="header-menu">
      <div class="button-icon">
        <img class="icon-bell" src="../assets/icon/Bell.svg" />
        <div class="badge">
          <div class="div-red"></div>
        </div>
      </div>
      <div class="user-profile">
        <div class="avatar">
          <div class="user-image-12">
            <img class="place-image-here" src="../assets/icon/ImageSquare.svg" />
          </div>
        </div>
        <div class="user-name">
          <div class="name" @click="goToMyPage" style="cursor: pointer">
            {{ store.loginUser.name }}
          </div>
          <div class="role">Member</div>
        </div>
        <div class="button-icon2">
          <img class="icon-caret-down" src="../assets/icon/CaretDown.svg" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/userStore'
import { useRouter } from 'vue-router'

const router = useRouter()
const store = useUserStore()

const goToMyPage = () => {
  router.push({name:"MyPage"})
}
</script>

<style scoped>
.device-desktop-variant-back-button,
.device-desktop-variant-back-button * {
  box-sizing: border-box;
}
.device-desktop-variant-back-button {
  padding: 0px 0px 0px 4px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  align-self: stretch;
  flex-shrink: 0;
  height: 50px;
  position: relative;
}
.title {
  padding: 2px 0px 2px 0px;
  display: flex;
  flex-direction: column;
  gap: 6px;
  align-items: flex-start;
  justify-content: flex-start;
  flex-shrink: 0;
  width: 340px;
  position: relative;
}
.back-button {
  display: flex;
  flex-direction: row;
  gap: 6px;
  align-items: center;
  justify-content: flex-start;
  flex-shrink: 0;
  position: relative;
}
.icon-arrow-left {
  flex-shrink: 0;
  width: 16px;
  height: 16px;
  position: relative;
  overflow: visible;
}
.sub-title {
  color: var(--gray-20, #8a8c90);
  text-align: left;
  font-family: var(--title-12px-regular-font-family, 'Poppins-Regular', sans-serif);
  font-size: var(--title-12px-regular-font-size, 12px);
  line-height: var(--title-12px-regular-line-height, 130%);
  font-weight: var(--title-12px-regular-font-weight, 400);
  position: relative;
}
.title2 {
  color: var(--black, #272932);
  text-align: left;
  font-family: var(--h5-22px-semibold-font-family, 'Poppins-SemiBold', sans-serif);
  font-size: var(--h5-22px-semibold-font-size, 22px);
  line-height: var(--h5-22px-semibold-line-height, 108%);
  font-weight: var(--h5-22px-semibold-font-weight, 600);
  position: relative;
  align-self: stretch;
}
.header-menu {
  border-radius: 28px;
  display: flex;
  flex-direction: row;
  gap: 12px;
  align-items: center;
  justify-content: flex-start;
  flex-shrink: 0;
  position: relative;
}
.button-icon {
  background: var(--pure-white, #ffffff);
  border-radius: 12px;
  padding: 9px;
  display: flex;
  flex-direction: row;
  gap: 8px;
  align-items: flex-start;
  justify-content: flex-start;
  flex-shrink: 0;
  position: relative;
}
.icon-bell {
  flex-shrink: 0;
  width: 22px;
  height: 22px;
  position: relative;
  overflow: visible;
}
.badge {
  padding: 4px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  width: 20px;
  height: 20px;
  position: absolute;
  right: 4px;
  top: 4px;
}
.div-red {
  background: var(--orange, #ffa257);
  border-radius: 14px;
  padding: 1px 2px 1px 2px;
  display: flex;
  flex-direction: row;
  gap: 10px;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  width: 8px;
  height: 8px;
  position: relative;
}
.user-profile {
  display: flex;
  flex-direction: row;
  gap: 12px;
  align-items: center;
  justify-content: flex-start;
  flex-shrink: 0;
  position: relative;
}
.avatar {
  border-radius: 12px;
  flex-shrink: 0;
  width: 40px;
  height: 40px;
  position: relative;
  overflow: hidden;
}
.user-image-12 {
  background: var(--saffron, #ffcb65);
  width: 100%;
  height: 100%;
  position: absolute;
  right: 0%;
  left: 0%;
  bottom: 0%;
  top: 0%;
}
.place-image-here {
  border-radius: 12px;
  width: 100%;
  height: 100%;
  position: absolute;
  right: 0%;
  left: 0%;
  bottom: 0%;
  top: 0%;
  object-fit: cover;
}
.user-name {
  display: flex;
  flex-direction: column;
  gap: 4px;
  align-items: flex-start;
  justify-content: flex-start;
  flex-shrink: 0;
  position: relative;
}
.name {
  color: var(--black, #272932);
  text-align: left;
  font-family: var(--title-16px-semibold-font-family, 'Poppins-SemiBold', sans-serif);
  font-size: var(--title-16px-semibold-font-size, 16px);
  line-height: var(--title-16px-semibold-line-height, 124%);
  font-weight: var(--title-16px-semibold-font-weight, 600);
  position: relative;
}
.role {
  color: var(--gray-20, #8a8c90);
  text-align: left;
  font-family: var(--title-11px-regular-font-family, 'Poppins-Regular', sans-serif);
  font-size: var(--title-11px-regular-font-size, 11px);
  line-height: var(--title-11px-regular-line-height, 124%);
  font-weight: var(--title-11px-regular-font-weight, 400);
  position: relative;
}
.button-icon2 {
  background: var(--pure-white, #ffffff);
  border-radius: 12px;
  padding: 5px;
  display: flex;
  flex-direction: row;
  gap: 8px;
  align-items: center;
  justify-content: center;
  align-self: stretch;
  flex-shrink: 0;
  position: relative;
}
.icon-caret-down {
  flex-shrink: 0;
  width: 16px;
  height: 16px;
  position: relative;
  overflow: visible;
}
</style>
