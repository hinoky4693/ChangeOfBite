import axios from 'axios'

const OPENAI_API_URL = 'https://api..com/v1/chat/completions'
// const API_KEY = // TODO: 실제 키로 교체하거나 .env 처리

export async function generateMealPlan(inputData) {
  const prompt = createPrompt(inputData)

  const response = await axios.post(
    OPENAI_API_URL,
    {
      model: 'gpt-4',
      messages: [{ role: 'user', content: prompt }],
      temperature: 0.7,
    },
    {
      headers: {
        Authorization: `Bearer ${API_KEY}`,
        'Content-Type': 'application/json',
      },
    },
  )
  console.log(response.data);
  return response.data.choices[0].message.content
}

function createPrompt(data) {
  console.log(data);
  return `당신은 식단 전문가입니다. 다음 사용자 정보를 바탕으로 ${data.periodWeeks}일 간의 주간 식단표를 생성해주세요.

- 키: ${data.height}cm
- 몸무게: ${data.weight}kg
- 목표: ${data.goal}
- 목표 체중: ${data.targetWeight}kg
- 식단 시작일 : ${data.startDate}
- 하루 끼니 수: ${data.mealsPerDay}끼
- 알레르기: ${data.allergy || '없음'}
- 지병: ${data.disease || '없음'}
- 선호 음식: ${data.preferredFoods || '없음'}
- 비선호 음식: ${data.dislikedFoods || '없음'}

요구사항:
- 결과 값은 날짜, 아침, 점심, 저녁, 간식, 총 칼로리, 탄단지 비율의 형태로 줘.
- 음식 중복 최소화.
- 출력은 
| 날짜       | 아침     | 점심     | 저녁     | 간식     | 총 칼로리 | 탄단지 비율 |
|------------|----------|----------|----------|----------|----------|------------|
| 2025-05-26 | 오트밀  | 닭가슴살 샐러드 | 소고기 스테이크 | 단백질 쉐이크 | 2500     | 50:30:20  |
| 2025-05-27 | 스크램블드 에그 | 치킨 브레스트 | 연어구이 | 아몬드    | 2600     | 40:40:20  |
| 2025-05-28 | 케일 스무디 | 치킨 샐러드 | 퀴노아 볼 | 카세인 프로틴 | 2700     | 45:35:20  |
| 2025-05-29 | 복숭아 요거트 | 치킨 라이스 | 닭가슴살 볶음 | 땅콩버터 샌드위치 | 2500     | 40:30:30  |
| 2025-05-30 | 바나나 팬케이크 | 치킨 파스타 | 훈제 삼치 구이 | 단백질 바 | 2400     | 50:20:30  |

위 주간 식단표는 벌크업을 목표로 하는 사용자를 위한 것으로, 하루에 4끼를 섭취하며, 주의사항으로 토마토를 비선호 음식으로 설정하였습니다. 각 식사에서는 고단백질 음식을 중심으로 구성하였으며, 탄수화물과 지방의 비율도 적절히 조절하여 목표 체중에 도달할 수 있도록 하였습니다. 각 날짜별로 총 칼로리와 탄단지 비율이 다르게 설정되어 있어, 다양한 영양소를 섭취할 수 있습니다. 주간 식단표는 음식의 중복을 최소화하였으며, 선호하는 음식인 치킨을 다양한 요리로 활용하였습니다.
이걸 모양 그대로 내용만 다르게 해줘.

- 탄단지 비율, 총 칼로리 매일 다르게.
`
}

