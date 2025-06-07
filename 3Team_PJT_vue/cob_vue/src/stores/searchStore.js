import { ref, computed } from 'vue'
import api from '@/api/axios'

const posts = ref([])
const searchQuery = ref(localStorage.getItem('searchQuery') || '')
const searchField = ref(localStorage.getItem('searchField') || 'title') // 기본값: 제목

const loadPosts = async () => {
  const response = await api.get('http://localhost:8080/api/posts')
  posts.value = response.data
}

const filteredPosts = computed(() => {
  if (!searchQuery.value) return posts.value

  return posts.value.filter((post) => {
    const field = searchField.value
    return post[field]?.toLowerCase().includes(searchQuery.value.toLowerCase())
  })
})

export { posts, searchQuery, searchField, filteredPosts, loadPosts }
