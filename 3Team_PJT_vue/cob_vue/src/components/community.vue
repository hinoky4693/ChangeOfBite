<template>
  <div class="search-filter-bar">
    <input
      type="text"
      v-model="searchQuery"
      placeholder="검색어를 입력하세요"
      @keydown.enter="performSearch"
    />

    <select v-model="searchField">
      <option value="title">제목</option>
      <option value="writer">글쓴이</option>
      <option value="category">목표</option>
    </select>

    <button @click="performSearch">검색</button>
  </div>
  <div class="table-header-food-diary2">
    <!-- 등록일 -->
    <div class="cell-date2" @click="setSort('createdAt')">
      등록일
      <span class="sort-icon" :class="{ active: sortKey === 'createdAt' }">
        <span v-if="sortKey === 'createdAt' && sortOrder === 'asc'">▲</span>
        <span v-else-if="sortKey === 'createdAt' && sortOrder === 'desc'">▼</span>
        <span v-else>▲▼</span>
      </span>
    </div>

    <!-- 목표 -->
    <div class="cell-category3" @click="setSort('category')">
      목표
      <span class="sort-icon" :class="{ active: sortKey === 'category' }">
        <span v-if="sortKey === 'category' && sortOrder === 'asc'">▲</span>
        <span v-else-if="sortKey === 'category' && sortOrder === 'desc'">▼</span>
        <span v-else>▲▼</span>
      </span>
    </div>

    <!-- 제목 -->
    <div class="cell-menu2" @click="setSort('title')">
      제목
      <span class="sort-icon" :class="{ active: sortKey === 'title' }">
        <span v-if="sortKey === 'title' && sortOrder === 'asc'">▲</span>
        <span v-else-if="sortKey === 'title' && sortOrder === 'desc'">▼</span>
        <span v-else>▲▼</span>
      </span>
    </div>

    <!-- 글쓴이 -->
    <div class="cell-amount2" @click="setSort('writer')">
      글쓴이
      <span class="sort-icon" :class="{ active: sortKey === 'writer' }">
        <span v-if="sortKey === 'writer' && sortOrder === 'asc'">▲</span>
        <span v-else-if="sortKey === 'writer' && sortOrder === 'desc'">▼</span>
        <span v-else>▲▼</span>
      </span>
    </div>

    <!-- 조회수 -->
    <div class="cell-calories2" @click="setSort('viewCnt')">
      조회수
      <span class="sort-icon" :class="{ active: sortKey === 'viewCnt' }">
        <span v-if="sortKey === 'viewCnt' && sortOrder === 'asc'">▲</span>
        <span v-else-if="sortKey === 'viewCnt' && sortOrder === 'desc'">▼</span>
        <span v-else>▲▼</span>
      </span>
    </div>

    <!-- 좋아요 -->
    <div class="cell-sugar2" @click="setSort('likes')">
      좋아요
      <span class="sort-icon" :class="{ active: sortKey === 'likes' }">
        <span v-if="sortKey === 'likes' && sortOrder === 'asc'">▲</span>
        <span v-else-if="sortKey === 'likes' && sortOrder === 'desc'">▼</span>
        <span v-else>▲▼</span>
      </span>
    </div>
  </div>

  <div class="table-row-food-diary2" v-for="post in paginatedPosts" :key="post.id">
    <img class="checkbox2" src="../assets/icon/Check.svg" />
    <div class="cells2">
      <!-- 등록일 -->
      <div class="cell-date2">
        <div class="text2">{{ post.createdAt }}</div>
      </div>

      <!-- 카테고리 -->
      <div class="cell-category3">
        <div class="badge-category-meal-time" :class="getCategoryClass(post.category)">
          <div class="text4">{{ post.category }}</div>
        </div>
      </div>

      <!-- 제목 -->
      <div class="cell-menu2">
        <router-link :to="`/posts/${post.id}`" class="text5">
          {{ post.title }}
        </router-link>
      </div>

      <!-- 글쓴이 -->
      <div class="cell-amount2">
        <div class="text3">{{ post.writer }}</div>
      </div>

      <!-- 조회수 -->
      <div class="cell-calories2">
        <div class="text2">{{ post.viewCnt }}</div>
      </div>

      <!-- 좋아요 -->
      <div class="cell-sugar2">
        <div class="text2">{{ post.likes }}</div>
      </div>
    </div>
  </div>

  <button class="write-btn" @click="goToWrite">＋ 등록</button>
  <div class="pagination">
    <button @click="goToPage(currentPage - 1)" :disabled="currentPage === 1">이전</button>

    <button
      v-for="page in totalPages"
      :key="page"
      :class="{ active: page === currentPage }"
      @click="goToPage(page)"
    >
      {{ page }}
    </button>

    <button @click="goToPage(currentPage + 1)" :disabled="currentPage === totalPages">다음</button>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  filteredPosts as originalPosts,
  loadPosts,
  searchQuery,
  searchField,
} from '@/stores/searchStore'

const route = useRoute()
const selectedCategory = computed(() => route.params.category)
const router = useRouter()

const categoryFilteredPosts = computed(() => {
  if (!selectedCategory.value) return sortedPosts.value
  return sortedPosts.value.filter((post) => post.category === selectedCategory.value)
})

// 페이지네이션
const currentPage = ref(1)
const itemsPerPage = 12

// 정렬 상태
const sortKey = ref('createdAt') // 기본 정렬 기준
const sortOrder = ref('desc') // asc 또는 desc

const sortedPosts = computed(() => {
  return [...originalPosts.value].sort((a, b) => {
    const key = sortKey.value

    let valA = a[key]
    let valB = b[key]

    // 문자열 비교는 localeCompare
    if (typeof valA === 'string') {
      valA = valA.toLowerCase()
      valB = valB.toLowerCase()
      return sortOrder.value === 'asc' ? valA.localeCompare(valB) : valB.localeCompare(valA)
    }

    // 숫자, 날짜는 기본 비교
    return sortOrder.value === 'asc' ? valA - valB : valB - valA
  })
})

const totalPages = computed(() => {
  return Math.ceil(categoryFilteredPosts.value.length / itemsPerPage)
})

const paginatedPosts = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage
  return categoryFilteredPosts.value.slice(start, start + itemsPerPage)
})

const goToPage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
  }
}

const setSort = (key) => {
  if (sortKey.value === key) {
    // 이미 정렬 중이면 순서 토글
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc'
  } else {
    sortKey.value = key
    sortOrder.value = 'asc'
  }
}

onMounted(async () => {
  await loadPosts()
  const storedQuery = localStorage.getItem('searchQuery')
  if (storedQuery) {
    searchQuery.value = storedQuery
    localStorage.removeItem('searchQuery')
  }
})

const getCategoryClass = (category) => {
  switch (category) {
    case '벌크업':
      return 'badge-category-meal-time5'
    case '다이어트':
      return 'badge-category-meal-time2'
    case '린메스업':
      return 'badge-category-meal-time3'
    case '체형 유지':
      return 'badge-category-meal-time4'
    default:
      return 'badge-category-meal-time'
  }
}

function goToWrite() {
  router.push('/posts/write')
}
const performSearch = () => {
  localStorage.setItem('searchQuery', searchQuery.value)
  localStorage.setItem('searchField', searchField.value)
  location.reload()
}
</script>

<style scoped>
.table-row-food-diary2 {
  background: var(--pure-white, #ffffff);
  border-style: solid;
  border-color: var(--gray-line, #e1e1e2);
  border-width: 0px 0px 1px 0px;
  padding: 16px 20px;
  display: flex;
  flex-direction: row;
  gap: 20px;
  align-items: center;
  justify-content: flex-start;
  align-self: stretch;
  flex-shrink: 0;
  position: relative;
  transition: transform 0.2s ease, box-shadow 0.2s ease, background-color 0.2s ease;
}

.table-row-food-diary2:hover {
  transform: translateY(-2px);
  background-color: #f4fdf0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border-left: 4px solid #c2e66e;
}

.table-row-food-diary2:hover .text5 {
  color: #68a500;
  text-decoration: underline;
}

.table-row-food-diary2:hover .badge-category-meal-time,
.table-row-food-diary2:hover .badge-category-meal-time2,
.table-row-food-diary2:hover .badge-category-meal-time3,
.table-row-food-diary2:hover .badge-category-meal-time4,
.table-row-food-diary2:hover .badge-category-meal-time5 {
  transform: scale(1.05);
  transition: transform 0.2s ease;
}
.body,
.body * {
  box-sizing: border-box;
}
.table-header-food-diary2 .cell-date2 {
  flex-direction: row !important;
  width: 7%;
}
.table-header-food-diary2 .cell-category3 {
  margin-left: 14.5%;
  width: 17%;
}
.table-header-food-diary2 .cell-menu2 {
  width: 21.8%;
}
.table-header-food-diary2 .cell-amount2 {
  width: 15%;
}
.table-header-food-diary2 .cell-calories2 {
  width: 15%;
}
.table-header-food-diary2 .cell-sugar2 {
  width: 20%;
}
.logo-image {
  width: 380px; /* 크기 조절 */
  margin: 20px 0 0 20px; /* 상단 왼쪽 여백 */
}
.search-filter-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 16px 20px;
}

.search-filter-bar input,
.search-filter-bar select {
  padding: 6px 12px;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 14px;
}

.search-filter-bar select {
  width: 120px;
}

.search-filter-bar button {
  padding: 6px 12px;
  background-color: #c2e66e;
  border: none;
  border-radius: 6px;
  font-weight: bold;
  cursor: pointer;
}
.sort-icon {
  margin-left: 6px;
  font-size: 12px;
  color: #bbb;
  display: inline-flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  line-height: 1;
}

.sort-icon.active {
  color: #272932;
  font-weight: bold;
}
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 24px;
  gap: 8px;
}

.pagination button {
  background-color: #eeeeef;
  border: none;
  padding: 6px 10px;
  border-radius: 6px;
  font-size: 13px;
  cursor: pointer;
}

.pagination button.active {
  background-color: #c2e66e;
  color: #272932;
  font-weight: bold;
}

.pagination button:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}
.table-header-food-diary2 {
  background: #f6f6f7;
  border-bottom: 1px solid #e1e1e2;
  padding: 16px 20px;
  display: flex;
  flex-direction: row;
  gap: 20px;
  align-items: center;
  justify-content: flex-start;
  position: relative;
  font-weight: bold;
  font-size: 12px;
  color: #272932;
}

.body {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  height: auto; /* 콘텐츠에 따라 자동 확장 */
  overflow-x: hidden; /* 가로 스크롤 방지 */
  overflow-y: auto; /* 세로 스크롤 허용 */
  position: relative;
}

.write-btn {
  position: fixed;
  bottom: 32px;
  right: 32px;
  background-color: #c2e66e;
  color: #272932;
  border: none;
  border-radius: 50px;
  padding: 14px 28px;
  font-size: 14px;
  font-weight: bold;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  cursor: pointer;
  transition: background 0.2s ease;
}
.write-btn:hover {
  background-color: #a8d14a;
}
.widget-grocery-list {
  background: var(--pure-white, #ffffff);
  flex-grow: 1; /* 부모 높이에 맞게 확장 */
  width: 100%; /* 전체 폭 차지 */
  position: relative; /* absolute 사용 금지 */
  top: auto;
}
.header-section {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  align-self: stretch;
  flex-shrink: 0;
  position: relative;
}
.left-section {
  display: flex;
  flex-direction: row;
  gap: 10px;
  align-items: center;
  justify-content: flex-start;
  align-self: stretch;
  flex-shrink: 0;
  position: relative;
}
.input-search {
  background: var(--gray-bg, #eeeeef);
  border-radius: 8px;
  padding: 6px 8px 6px 8px;
  display: flex;
  flex-direction: row;
  gap: 4px;
  align-items: center;
  justify-content: flex-start;
  flex-shrink: 0;
  width: 224px;
  position: relative;
}
.icon {
  padding: 2px 0px 2px 0px;
  display: flex;
  flex-direction: row;
  gap: 10px;
  align-items: center;
  justify-content: flex-start;
  flex-shrink: 0;
  position: relative;
}
.icon-magnifying-glass {
  flex-shrink: 0;
  width: 14px;
  height: 14px;
  position: relative;
  overflow: visible;
}
.text {
  padding: 0px 2px 0px 2px;
  display: flex;
  flex-direction: row;
  gap: 10px;
  align-items: center;
  justify-content: flex-start;
  flex-shrink: 0;
  position: relative;
}
.label {
  color: var(--gray-20, #8a8c90);
  text-align: center;
  font-family: var(--title-11px-regular-font-family, 'Poppins-Regular', sans-serif);
  font-size: var(--title-11px-regular-font-size, 11px);
  line-height: var(--title-11px-regular-line-height, 124%);
  font-weight: var(--title-11px-regular-font-weight, 400);
  position: relative;
}
.button-picker {
  background: var(--gray-bg, #eeeeef);
  border-radius: 8px;
  padding: 6px 10px 6px 10px;
  display: flex;
  flex-direction: row;
  gap: 2px;
  align-items: center;
  justify-content: flex-start;
  flex-shrink: 0;
  position: relative;
}
.icon-left {
  padding: 2px 2px 2px 0px;
  display: flex;
  flex-direction: row;
  gap: 10px;
  align-items: center;
  justify-content: flex-start;
  flex-shrink: 0;
  position: relative;
}
.icon-funnel {
  flex-shrink: 0;
  width: 14px;
  height: 14px;
  position: relative;
  overflow: visible;
}
.label2 {
  color: var(--gray-30, #52545b);
  text-align: center;
  font-family: var(--btn-11px-medium-font-family, 'Poppins-Medium', sans-serif);
  font-size: var(--btn-11px-medium-font-size, 11px);
  line-height: var(--btn-11px-medium-line-height, 105%);
  font-weight: var(--btn-11px-medium-font-weight, 500);
  position: relative;
}
.icon-caret-down {
  flex-shrink: 0;
  width: 14px;
  height: 14px;
  position: relative;
  overflow: visible;
}
.right-section {
  display: flex;
  flex-direction: row;
  gap: 10px;
  align-items: center;
  justify-content: flex-start;
  flex-shrink: 0;
  position: relative;
}
.icon-calendar-blank {
  flex-shrink: 0;
  width: 14px;
  height: 14px;
  position: relative;
  overflow: visible;
}
.icon-caret-down2 {
  flex-shrink: 0;
  width: 14px;
  height: 14px;
  position: relative;
  overflow: visible;
}
.button-cta {
  background: var(--green, #c2e66e);
  border-radius: 8px;
  padding: 6px 10px 6px 10px;
  display: flex;
  flex-direction: row;
  gap: 4px;
  align-items: center;
  justify-content: flex-start;
  flex-shrink: 0;
  position: relative;
}
.icon-left2 {
  padding: 2px 0px 2px 0px;
  display: flex;
  flex-direction: row;
  gap: 10px;
  align-items: center;
  justify-content: flex-start;
  flex-shrink: 0;
  position: relative;
}
.icon-plus {
  flex-shrink: 0;
  width: 14px;
  height: 14px;
  position: relative;
  overflow: visible;
}
.label3 {
  color: var(--black, #272932);
  text-align: center;
  font-family: var(--btn-11px-medium-font-family, 'Poppins-Medium', sans-serif);
  font-size: var(--btn-11px-medium-font-size, 11px);
  line-height: var(--btn-11px-medium-line-height, 105%);
  font-weight: var(--btn-11px-medium-font-weight, 500);
  position: relative;
}
.table {
  background: var(--cream-bg, #f9f4f2);
  border-radius: 14px 14px 0px 0px;
  display: flex;
  flex-direction: column;
  gap: 0px;
  align-items: flex-start;
  justify-content: flex-start;
  align-self: stretch;
  flex-shrink: 0;
  position: relative;
  overflow: hidden;
}
.table-row-food-diary {
  border-style: solid;
  border-color: var(--gray-line, #e1e1e2);
  border-width: 0px 0px 1px 0px;
  padding: 16px 20px 16px 20px;
  display: flex;
  flex-direction: row;
  gap: 20px;
  align-items: center;
  justify-content: flex-start;
  align-self: stretch;
  flex-shrink: 0;
  position: relative;
}
.checkbox {
  flex-shrink: 0;
  width: 12px;
  height: 12px;
  position: relative;
  overflow: visible;
}
.cells {
  background: #ffffff;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  flex: 1;
  position: relative;
}
.cell-date {
  display: flex;
  flex-direction: row;
  gap: 0px;
  align-items: flex-start;
  justify-content: flex-start;
  flex-shrink: 0;
  width: 82px;
  position: relative;
}
.title {
  color: var(--gray-20, #8a8c90);
  text-align: left;
  font-family: var(--title-11px-regular-font-family, 'Poppins-Regular', sans-serif);
  font-size: var(--title-11px-regular-font-size, 11px);
  line-height: var(--title-11px-regular-line-height, 124%);
  font-weight: var(--title-11px-regular-font-weight, 400);
  position: relative;
}
.icon-sort {
  flex-shrink: 0;
  width: 14px;
  height: 14px;
  position: relative;
  overflow: visible;
}
.cell-category {
  background: #ffffff;
  display: flex;
  flex-direction: row;
  gap: px;
  align-items: flex-start;
  justify-content: flex-start;
  flex-shrink: 0;
  width: 37px;
  position: relative;
}
.icon-sort2 {
  flex-shrink: 0;
  width: 14px;
  height: 14px;
  position: relative;
  overflow: visible;
}
.cell-menu {
  display: flex;
  flex-direction: row;
  gap: 0px;
  align-items: flex-start;
  justify-content: flex-start;
  flex-shrink: 0;
  width: 220px;
  position: relative;
}
.icon-sort3 {
  flex-shrink: 0;
  width: 14px;
  height: 14px;
  position: relative;
  overflow: visible;
}
.cell-thoughts {
  display: flex;
  flex-direction: row;
  gap: 0px;
  align-items: flex-start;
  justify-content: flex-start;
  flex-shrink: 0;
  width: 136px;
  position: relative;
}
.cell-sugar {
  display: flex;
  flex-direction: row;
  gap: 0px;
  align-items: flex-start;
  justify-content: flex-start;
  flex-shrink: 0;
  width: 48px;
  position: relative;
}
.icon-sort4 {
  flex-shrink: 0;
  width: 14px;
  height: 14px;
  position: relative;
  overflow: visible;
}
.cell-calories {
  display: flex;
  flex-direction: row;
  gap: 0px;
  align-items: flex-start;
  justify-content: flex-start;
  flex-shrink: 0;
  width: 56px;
  position: absolute;
  left: 1330px;
  top: 0px;
}
.icon-sort5 {
  flex-shrink: 0;
  width: 14px;
  height: 14px;
  position: relative;
  overflow: visible;
}
.cell-amount {
  display: flex;
  flex-direction: row;
  gap: 0px;
  align-items: flex-start;
  justify-content: flex-start;
  flex-shrink: 0;
  width: 58px;
  position: absolute;
  left: 1050px;
  top: 1px;
}
.icon-sort6 {
  flex-shrink: 0;
  width: 14px;
  height: 14px;
  position: relative;
  overflow: visible;
}
.table-body {
  background: var(--pure-white, #ffffff);
  display: flex;
  flex-direction: column;
  gap: 0px;
  align-items: flex-start;
  justify-content: flex-start;
  align-self: stretch;
  flex-shrink: 0;
  position: relative;
  overflow-y: visible;
}
.table-row-food-diary2 {
  background: var(--pure-white, #ffffff);
  border-style: solid;
  border-color: var(--gray-line, #e1e1e2);
  border-width: 0px 0px 1px 0px;
  padding: 16px 20px 16px 20px;
  display: flex;
  flex-direction: row;
  gap: 20px;
  align-items: center;
  justify-content: flex-start;
  align-self: stretch;
  flex-shrink: 0;
  position: relative;
}
.checkbox2 {
  flex-shrink: 0;
  width: 12px;
  height: 12px;
  position: relative;
  overflow: visible;
}
.cells2 {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  flex: 1;
  position: relative;
}
.cell-date2 {
  display: flex;
  flex-direction: column;
  gap: 4px;
  align-items: flex-start;
  justify-content: center;
  flex-shrink: 0;
  width: 120px;
  position: relative;
}
.text2 {
  color: var(--black, #272932);
  text-align: left;
  font-family: var(--title-12px-regular-font-family, 'Poppins-Regular', sans-serif);
  font-size: var(--title-12px-regular-font-size, 12px);
  line-height: var(--title-12px-regular-line-height, 130%);
  font-weight: var(--title-12px-regular-font-weight, 400);
  position: relative;
}
.text3 {
  color: var(--gray-20, #8a8c90);
  text-align: left;
  font-family: var(--title-12px-regular-font-family, 'Poppins-Regular', sans-serif);
  font-size: var(--title-12px-regular-font-size, 12px);
  line-height: var(--title-12px-regular-line-height, 130%);
  font-weight: var(--title-12px-regular-font-weight, 400);
  position: relative;
}
.cell-category2 {
  background: #ffffff;
  display: flex;
  flex-direction: row;
  gap: 8px;
  align-items: flex-start;
  justify-content: flex-start;
  flex-shrink: 0;
  width: 78px;
  position: relative;
}
.badge-category-meal-time {
  background: var(--green, #c2e66e);
  border-radius: 20px;
  padding: 6px 12px 6px 12px;
  display: flex;
  flex-direction: row;
  gap: 5px;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  position: absolute;
  left: 0px;
  top: 0px;
}
.text4 {
  color: var(--black, #272932);
  text-align: left;
  font-family: var(--title-11px-regular-font-family, 'Poppins-Regular', sans-serif);
  font-size: var(--title-11px-regular-font-size, 11px);
  line-height: var(--title-11px-regular-line-height, 124%);
  font-weight: var(--title-11px-regular-font-weight, 400);
  position: relative;
}
.cell-menu2 {
  display: flex;
  flex-direction: row;
  gap: 12px;
  align-items: center;
  justify-content: flex-start;
  flex-shrink: 0;
  width: 176px;
  position: relative;
}
.text5 {
  color: var(--black, #272932);
  text-align: left;
  font-family: var(--p-12px-regular-font-family, 'Poppins-Regular', sans-serif);
  font-size: var(--p-12px-regular-font-size, 12px);
  line-height: var(--p-12px-regular-line-height, 150%);
  font-weight: var(--p-12px-regular-font-weight, 400);
  position: relative;
  flex: 1;
}
.cell-amount2 {
  display: flex;
  flex-direction: row;
  gap: 4px;
  align-items: center;
  justify-content: flex-start;
  flex-shrink: 0;
  width: 58px;
  position: relative;
}
.cell-calories2 {
  display: flex;
  flex-direction: row;
  gap: 4px;
  align-items: flex-start;
  justify-content: flex-start;
  flex-shrink: 0;
  width: 56px;
  position: relative;
}
.cell-sugar2 {
  display: flex;
  flex-direction: row;
  gap: 4px;
  align-items: flex-start;
  justify-content: flex-start;
  flex-shrink: 0;
  width: 48px;
  position: relative;
}
.checkbox3 {
  flex-shrink: 0;
  width: 12px;
  height: 12px;
  position: relative;
  overflow: visible;
}
.cell-category3 {
  display: flex;
  flex-direction: row;
  gap: 8px;
  align-items: flex-start;
  justify-content: flex-start;
  flex-shrink: 0;
  width: 78px;
  position: relative;
}
.badge-category-meal-time2 {
  background: var(--saffron, #ffcb65);
  border-radius: 20px;
  padding: 6px 12px 6px 12px;
  display: flex;
  flex-direction: row;
  gap: 5px;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  position: relative;
}
.table-row-food-diary3 {
  background: var(--cream-bg, #f9f4f2);
  border-style: solid;
  border-color: var(--gray-line, #e1e1e2);
  border-width: 0px 0px 1px 0px;
  padding: 16px 20px 16px 20px;
  display: flex;
  flex-direction: row;
  gap: 20px;
  align-items: center;
  justify-content: flex-start;
  align-self: stretch;
  flex-shrink: 0;
  position: relative;
}
.checkbox4 {
  flex-shrink: 0;
  width: 12px;
  height: 12px;
  position: relative;
  overflow: visible;
}
.badge-category-meal-time3 {
  background: var(--orange, #ffa257);
  border-radius: 20px;
  padding: 6px 12px 6px 12px;
  display: flex;
  flex-direction: row;
  gap: 5px;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  position: relative;
}
.checkbox5 {
  flex-shrink: 0;
  width: 12px;
  height: 12px;
  position: relative;
  overflow: visible;
}
.badge-category-meal-time4 {
  background: var(--gray-line, #e1e1e2);
  border-radius: 20px;
  padding: 6px 12px 6px 12px;
  display: flex;
  flex-direction: row;
  gap: 5px;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  position: relative;
}
.checkbox6 {
  flex-shrink: 0;
  width: 12px;
  height: 12px;
  position: relative;
  overflow: visible;
}
.badge-category-meal-time5 {
  background: var(--green, #c2e66e);
  border-radius: 20px;
  padding: 6px 12px 6px 12px;
  display: flex;
  flex-direction: row;
  gap: 5px;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  position: relative;
}
.checkbox7 {
  flex-shrink: 0;
  width: 12px;
  height: 12px;
  position: relative;
  overflow: visible;
}
.checkbox8 {
  flex-shrink: 0;
  width: 12px;
  height: 12px;
  position: relative;
  overflow: visible;
}
.checkbox9 {
  flex-shrink: 0;
  width: 12px;
  height: 12px;
  position: relative;
  overflow: visible;
}
.checkbox10 {
  flex-shrink: 0;
  width: 12px;
  height: 12px;
  position: relative;
  overflow: visible;
}
.checkbox11 {
  flex-shrink: 0;
  width: 12px;
  height: 12px;
  position: relative;
  overflow: visible;
}
.checkbox12 {
  flex-shrink: 0;
  width: 12px;
  height: 12px;
  position: relative;
  overflow: visible;
}
.checkbox13 {
  flex-shrink: 0;
  width: 12px;
  height: 12px;
  position: relative;
  overflow: visible;
}
.footer {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  align-self: stretch;
  flex-shrink: 0;
  position: relative;
  margin-bottom: 16px;
}
.section-result {
  display: flex;
  flex-direction: row;
  gap: 10px;
  align-items: center;
  justify-content: flex-start;
  flex-shrink: 0;
  position: relative;
}
.text6 {
  color: var(--gray-20, #8a8c90);
  text-align: left;
  font-family: var(--title-11px-regular-font-family, 'Poppins-Regular', sans-serif);
  font-size: var(--title-11px-regular-font-size, 11px);
  line-height: var(--title-11px-regular-line-height, 124%);
  font-weight: var(--title-11px-regular-font-weight, 400);
  position: relative;
}
.button {
  background: var(--gray-bg, #eeeeef);
  border-radius: 8px;
  padding: 6px 6px 6px 8px;
  display: flex;
  flex-direction: row;
  gap: 4px;
  align-items: center;
  justify-content: flex-start;
  flex-shrink: 0;
  position: relative;
}
.icon-caret-down3 {
  flex-shrink: 0;
  width: 14px;
  height: 14px;
  position: relative;
  overflow: visible;
}
.pagination {
  display: flex;
  flex-direction: row;
  gap: 8px;
  align-items: center;
  justify-content: flex-start;
  flex-shrink: 0;
  position: relative;
}
.button-icon {
  background: var(--gray-bg-subtle, #f6f6f7);
  border-radius: 8px;
  padding: 6px;
  display: flex;
  flex-direction: row;
  gap: 8px;
  align-items: flex-start;
  justify-content: flex-start;
  flex-shrink: 0;
  position: relative;
}
.icon-caret-left {
  flex-shrink: 0;
  width: 18px;
  height: 18px;
  position: relative;
  overflow: visible;
}
.button2 {
  background: var(--green, #c2e66e);
  border-radius: 8px;
  padding: 6px 10px 6px 10px;
  display: flex;
  flex-direction: row;
  gap: 0px;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  width: 30px;
  position: relative;
}
.text7 {
  padding: 3px 0px 3px 0px;
  display: flex;
  flex-direction: row;
  gap: 10px;
  align-items: center;
  justify-content: flex-start;
  flex-shrink: 0;
  position: relative;
}
.button3 {
  background: var(--gray-bg, #eeeeef);
  border-radius: 8px;
  padding: 6px 10px 6px 10px;
  display: flex;
  flex-direction: row;
  gap: 2px;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  width: 30px;
  position: relative;
}
.button4 {
  padding: 6px 2px 6px 2px;
  display: flex;
  flex-direction: row;
  gap: 0px;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  width: 30px;
  position: relative;
}
.button-icon2 {
  background: var(--gray-bg, #eeeeef);
  border-radius: 8px;
  padding: 6px;
  display: flex;
  flex-direction: row;
  gap: 8px;
  align-items: flex-start;
  justify-content: flex-start;
  flex-shrink: 0;
  position: relative;
}
.icon-caret-right {
  flex-shrink: 0;
  width: 18px;
  height: 18px;
  position: relative;
  overflow: visible;
}
.logo-2 {
  width: 532px;
  height: 140px;
  position: absolute;
  left: 0px;
  top: -27px;
  object-fit: cover;
  aspect-ratio: 532/140;
}
</style>
