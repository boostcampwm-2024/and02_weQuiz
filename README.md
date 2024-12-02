# WeQuiz~?🍦

스터디 그룹 내에서 학습을 더욱 **효과적으로** 그리고 **즐겁게** 진행할 수 있는 퀴즈 기반 학습 서비스 **WeQuiz~?🍦**에 오신 것을 환영합니다!

저희 팀원들은 **스터디를 하면서 학습에만 초점이 맞춰져 있어, 정리와 복습에 대한 시간이 부족**하다는 문제를 느꼈습니다. <br>

이를 보완하고자, 스터디 그룹 내에서 **퀴즈 형식으로 학습을 정리하고 복습할 수 있는 서비스**를 구상했습니다.

---

**WeQuiz**는 다음 두 가지의 퀴즈 형식을 통해, 더욱 효율적이고 유익한 학습을 제공합니다:

1. **자유롭게 원하는 시간대에 진행할 수 있는 퀴즈**
2. **한 명의 주도 아래 실시간으로 참여 가능한 퀴즈** (Slido 방식과 유사)



<br>



# 🎹 주요 기능

### 실시간 퀴즈 진행 및 결과
  
|화면 분류|관리자 - 참여자|
|     :--:     |:--:|
|     실시간 퀴즈 진행     |<img src = "https://github.com/user-attachments/assets/0cb33ce4-a531-4f7d-9648-25fb31da3df5" width="500" height="560" >|
|     실시간 퀴즈 결과 및 통계     |<img src = "https://github.com/user-attachments/assets/8aff2b13-52d2-4d6e-8ad4-f9cc3eff633b" width="500" height="560">|

<br>

### 일반 퀴즈 진행 및 결과
|퀴즈 진행|결과 및 통계|
|:--:|:--:|
|<img src = "https://github.com/user-attachments/assets/1aeb294c-23b3-410a-8ccc-3d8f1b5652d8" width="252" height="560">|<img src = "https://github.com/user-attachments/assets/be421142-ef45-4977-a2fe-d50a6c63d97b" width="252" height="560">

<br>

### 퀴즈 생성
|AI 퀴즈 생성|낱말 퀴즈 생성|객관식 퀴즈 생성|
|:--:|:--:|:--:|
|<img src = "https://github.com/user-attachments/assets/c1484d17-02c4-4147-b4de-ca6a1cf5f6c1" width="252" height="560">|<img src = "https://github.com/user-attachments/assets/3fc71bf5-e7c2-4be7-9a28-c84bd156a1e9" width="252" height="560">|<img src = "https://github.com/user-attachments/assets/ba2ede25-c8c8-4af5-b989-6226abb4332a" width="252" height="560">

<br>

### 스터디 그룹 관리
|스터디 초대 발송|스터디 초대 확인|
|:--:|:--:|
|<img src = "https://github.com/user-attachments/assets/e43decff-1dc1-448c-8ee0-ee98beaf07e2" width="252" height="560">|<img src = "https://github.com/user-attachments/assets/ffaea0c1-0981-4f6c-894b-3ef246e9f3e9" width="252" height="560">|

<br>

### 로그인/회원가입

|구글 로그인/회원가입|
|:--:|
|<img src = "https://github.com/user-attachments/assets/83434949-53f1-460f-a2f8-f373c1c10d4a" width="252" height="560">


# 📚 기술 스택

| Category | TechStack | 관련 문서 |
| --- | --- | --- |
| Architecture | Clean Architecture, Multi Module, MVVM | [클린 아키텍쳐 선택 근거](https://www.notion.so/65af7f3ff4de425ba5ef818a123c13c3?pvs=21)⎮[gradle plugin 활용하기(1)](https://915dbfl.github.io/android/gradle-plugin(1)/) ⎮[gradle plugin 활용하기(2)](https://915dbfl.github.io/android/gradle-plugin(2)/) |  |
| DI | Hilt | [Why Hilt?](https://www.notion.so/65af7f3ff4de425ba5ef818a123c13c3?pvs=21) |
| Network | Retrofit, OkHttp, Kotlin Serialization |  |
| Asynchronous | Coroutines, Flow | [load 데이터를 언제 하면 좋을까?](https://www.notion.so/load-viewModel-init-13d5bfe2c24f8019b310f3d195174655?pvs=21) |
| UI/UX | Compose, Navigation | [LazyColumn/Row의 사실과 오해](https://www.notion.so/LazyColumn-Row-3c6f5b41183a4962b1338c1e582b07ee?pvs=21)⎮[Compose Navigation 적용하기](https://www.notion.so/Compose-navigation-cca9b5763d77445a9288a03831b26352?pvs=21) |
| Image | Coil | [Why Coil? (with Glide)](https://www.notion.so/65af7f3ff4de425ba5ef818a123c13c3?pvs=21) | [확장자, 리사이징 비교](https://www.notion.so/2f1de70729874dc1a57b903a72ff3ff1?pvs=21) |
| CI/CD | Github Actions | [Ktlint 적용기](https://jay20033.tistory.com/31) |
| Data Storage | Firestore, SharedPreferences | [Firebase 세팅](https://www.notion.so/fireStore-with-979d06402b534fa394b96ceeb5b33c55?pvs=21) |
| AI | Clova Studio | [AI 문제 출제](https://www.notion.so/AI-b952b973c50848438341e8c5c379c228?pvs=21) |
| Community | Figma, Slack, Notion |  |

# 🚀 아키텍쳐
![image](https://github.com/user-attachments/assets/d70b2247-3d1d-4795-8eda-247c7ded0b43)



<br>


# 👊 기술적 도전

**1. 이미지 최적화**

> 이미지를 최적화하여 성능을 개선했습니다.
>
> 압축, 리사이징을 적용하여 용량 축소를 하였습니다. 
>
> 이를 통해 업로드, 로딩 속도를 개선하였고, 서버 비용을 절감할 수 있었습니다. 
> 
> [이미지 최적화](https://www.notion.so/2f1de70729874dc1a57b903a72ff3ff1?pvs=21) 
> 

**2. 문제 유형 확장 가능한 구조 설계**

> 추상화를 활용해 문제 유형에 대해 확장 가능한 구조를 설계하였습니다.
> 
> 
> 추후 새로운 문제 유형이 추가될 경우, 기존 코드를 수정하지 않고도 확장이 가능합니다.
> 
> [문제 유형 확장 가능한 구조 설계](https://www.notion.so/f86d5a85bb6c42ed9d06d2c7a76b07d1?pvs=21) 
>

**3. 실시간 퀴즈 구현**


# ❓ 문제 해결
**BasicTextField가 width에 따라 키보드에 가려지는 오류**

> imePadding을 적용하여도 BasicTextField가 키보드에 가려지는 오류를 마주했습니다.
> 
> 
> component tree등을 활용하여 오류를 해결한 과정을 기록했습니다.
> 
> [🚨 [오류해결] basicTextField가 width에 따라 키보드에 가려지는 상황](https://velog.io/@kdogs/MongoDB%EC%97%90%EC%84%9C-%EC%BF%BC%EB%A6%AC-%EC%84%B1%EB%8A%A5-%EB%B6%84%EC%84%9D%ED%95%98%EA%B8%B0)
>

#


# 문서 바로가기
<div align="center">

| 📚 리소스              | 🔗 링크                                                                                                                         | 설명                                   |
| ---------------------- | ------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------- |
| 📖 **GitHub 위키**     | [WeQuiz GitHub Wiki](https://github.com/boostcampwm-2024/and02_weQuiz/wiki)                                                     | 프로젝트 문서화 및 개발 가이드          |
| 📃 **팀 노션**         | [WeQuiz 팀 노션](https://trite-ice-00b.notion.site/we-kids-1275bfe2c24f801e88c0efe0f0fb1071?pvs=4)                             | 프로젝트 계획 및 일정 관리              |
| 🎨 **피그마 디자인**   | [WeQuiz Figma 디자인](https://embed.figma.com/design/OM2OS94tdFHdJ5PnWnxKeZ/%EC%9C%84%ED%80%B4%EC%A6%88?node-id=0-1&theme=system) |    UI 및 UX 디자인 참고                 |
| 📋 **백로그**          | [WeQuiz 백로그](https://github.com/orgs/boostcampwm-2024/projects/4)                                                                                   | 프로젝트 작업 및 우선순위 관리          |

</div>

# 팀원 소개 
---
|[K014_김지훈](https://github.com/jay200333)|[K022_모영민](https://github.com/moyeongmin)|[K023_문유리](https://github.com/915dbfl)|[K041_이훈](https://github.com/pengcon)|[K051_천일영](https://github.com/Cheonilyeong)|
|:-----:|:-----:|:-----:|:-----:|:-----:|
|<img src= https://github.com/user-attachments/assets/6e7b38c8-c6f6-451c-a233-83f9007256ab width=190 height=200 >|<img src= https://github.com/user-attachments/assets/1dc9f39a-edda-4665-bf5a-d01fba4924d3 width=190 height=200 >|<img src="https://github.com/user-attachments/assets/4215d583-1690-4c23-8330-5b5bd8ad125a" width=190 height=200>|<img src="https://github.com/user-attachments/assets/83af745e-f77e-4032-97f4-2cadf0d99556" width=180 height=200>|<img src="https://github.com/user-attachments/assets/46cdda87-30d9-4e51-a9b2-d29f5d5b87b3" width=180 height=200>|
|즐거운 마음으로 | 말 많은 감자입니다 | (스텝 바이 스텝🎶🎶) | 7시간은 자야합니다 | 100010 |
---
