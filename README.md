# 🥗 ChangeOfBite

**ChangeOfBite**는 사용자의 건강 정보(키, 체중, 알레르기, 질병 등)를 기반으로  
AI가 개인 맞춤형 식단을 추천해주고, 사용자는 이를 캘린더에 기록하고 관리할 수 있는 웹 서비스입니다.

---

## 🧩 프로젝트 구성

| 파트 | 설명 |
|------|------|
| 🔙 Backend | Spring Boot 기반 API 서버 |
| 🔜 Frontend | Vue 3 기반 싱글 페이지 애플리케이션 |

---

## 🚀 주요 기능

- 사용자 회원가입 및 로그인 (JWT 기반 인증)
- 사용자 정보 입력 (키, 몸무게, 질병, 알레르기 등)
- OpenAI API를 활용한 AI 맞춤 식단 추천
- 식단 캘린더 UI를 통한 식단 관리
- 커뮤니티 기능 (추후 확장 가능)

---

## 🛠️ 사용 기술

### ✅ 프론트엔드 (Vue 3)
- Vue 3 + Composition API
- Pinia (상태 관리)
- Vue Router
- Axios
- vue-cal (캘린더 UI)

### ✅ 백엔드 (Spring Boot)
- Spring Boot 3.x
- Spring Security + JWT
- MyBatis
- MySQL
- OpenAI API 연동

---

## 🗂️ 프로젝트 구조

```
ChangeOfBite/
│
├── 3Team_PJT_vue/     # 프론트엔드
│   ├── App.vue
│   ├── main.js
│   ├── components/
│   ├── views/
│   ├── api/
│   ├── router/
│   └── stores/
│
├── 3team_pjt/         # 백엔드
│   ├── src/
│   ├── pom.xml
│   └── HELP.md
```
---


## 🔮 향후 발전 방향

- 식단 기반 건강 통계 분석
- 사용자 피드백 기반 식단 개선
