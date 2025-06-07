import axios from 'axios'
import router from '@/router'
import { useUserStore } from '@/stores/userStore'

const api = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000
})

// 요청 인터셉터: 토큰 자동 첨부
api.interceptors.request.use(
  (config) => {
    const token = sessionStorage.getItem("access-token");
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    } else {
      console.log("토큰이 없습니다!")
      router.push({name:"login"});
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 응답 인터셉터: 401 응답 시 로그아웃 처리
api.interceptors.response.use(
  response => response,
  error => {
    const store = useUserStore();
    if (error.response && error.response.status === 401) {
      const msg = error.response.data?.message;

      if (msg === "토큰이 만료되었습니다.") {
        alert("로그인이 만료되었습니다. 다시 로그인 해주세요.");
        store.logout();
        router.push({name:"login"});
      }
    }

    return Promise.reject(error);
  }
)

export default api
