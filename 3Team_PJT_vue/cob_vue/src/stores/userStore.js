import { ref, computed } from "vue";
import { defineStore } from "pinia";
import axios from "axios";
import router from "@/router";
import api from "@/api/axios";

const REST_API_URL = "http://localhost:8080/api/user";

// base64url 디코딩 함수 (한글 깨짐 방지)
function base64UrlDecode(str) {
  // base64url -> base64로 변환
  str = str.replace(/-/g, "+").replace(/_/g, "/");
  // 패딩 추가
  while (str.length % 4) {
    str += "=";
  }
  // 디코딩 (한글 지원)
  try {
    return decodeURIComponent(
      Array.prototype.map
        .call(
          atob(str),
          (c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2)
        )
        .join("")
    );
  } catch (e) {
    return atob(str);
  }
}


export const useUserStore = defineStore("user", () => {

  const token = ref(sessionStorage.getItem("access-token") || null);
  const loginUser = ref({
    name: null,
    userId: null
  })



  // 로그인
  const userLogin = async (loginId, password) => {
    try {
      const response = await axios.post(`${REST_API_URL}/login`, {
        loginId,
        password
      })

      const accessToken = response.data["access-token"];
      token.value = accessToken;
      sessionStorage.setItem("access-token", accessToken);

      const payload = JSON.parse(base64UrlDecode(accessToken.split(".")[1]));
      loginUser.value.name = payload["name"];
      loginUser.value.userId = payload["id"];

      console.log(loginUser.value.name, loginUser.value.userId);
      router.push({ name: "index" });
    } catch (err) {
      console.error("로그인 실패:", err);
      throw new Error('로그인 실패'); // 컴포넌트에서 처리하게 위임
    }
  };

  // 회원가입
  const userSignUp = async (data) => {
    try {
      await axios.post(`${REST_API_URL}/signup`, data);
      router.push({ name: "login" });
    } catch (err) {
      alert("회원가입 실패");
    }
  };

  // 로그아웃
  const logout = () => {
    token.value = null;
    loginUser.value = {
      name: null,
      userId: null
    };
    sessionStorage.removeItem("access-token");
    alert("로그아웃 되었습니다.");
    router.push({ name: "index" });
  };

  const fetchUserInfo = async () => {
    if (!token.value) {
      console.warn("❌ 토큰이 없어서 fetchUserInfo 중단됨");
      return;
    }
  
    try {
      const response = await api.get(`${REST_API_URL}/info`);
      const data = response.data;
      loginUser.value.name = data.name;
      loginUser.value.userId = data.userId;
    } catch (err) {
      console.error("유저 정보 불러오기 실패", err);
    }
  };

  return { token, userLogin, loginUser, userSignUp, logout, fetchUserInfo };
});
