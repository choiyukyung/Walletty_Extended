## 📌 Introduction
![image](https://github.com/choiyukyung/Capstone1_class7_team4/assets/80468377/dc97daeb-a7d0-4205-a781-5b77604d78da)
### 🖥 Development Period
2023 Sep. ~ 2023 Dec.
### 🏆 Prize
2023 CAU LINK3.0 캡스톤디자인 경진대회 우수상 [공학/자연 부문]  </br></br>

## 📌 Team Members
김민경, 중앙대학교 소프트웨어학부 21, **🔥Back-End, 🔥AI** </br></br>
이승아, 중앙대학교 소프트웨어학부 20, **🔥Front-End** </br></br>
최유경, 중앙대학교 소프트웨어학부 21, **🔥Back-End, 🔥AI** </br></br>

## 📌 Purpose & Goal
### 💡 Problem
기존의 가계부 서비스는 내가 돈을 어떻게 사용하고 있는지에 대한 현황만 확인할 수 있을 뿐 </br></br>
**어떻게 소비 습관을 개선**해서 불필요한 지출을 줄이고 저금할지에 대한 판단은 </br></br>
온전히 사용자가 부담해야했습니다. </br></br>

### 💡 Solution
💰**￦alletty**💰는 단순 통계 제시에 머무르지 않고 적극적으로 개입하여 </br></br> 
**사용자에게 동기를 부여**하고 쉽고 재밌게 소비 습관을 개선해 나갈 수 있는 서비스입니다.</br></br> 
소비 습관을 개선하고 싶어하는 사람들의 **실천을 돕는 것**을 목표로 삼았습니다. </br></br>
**사용자는 의지가 부족하더라도 소비 습관을 꾸준히 개선**해 나갈 수 있습니다. </br></br>

이러한 목적을 달성하기 위해 단순히 소비 습관에 관해 딱딱한 조언만을 제공하는 것이 아니라 </br></br>
사용자의 소비 내역을 바탕으로 **잔소리 AI 챗봇**이 **맞춤형 피드백**을 **잔소리하는 재미있는 말투**로 줍니다. </br></br>
잔소리 AI 챗봇에 **엄마 캐릭터**를 부여하며 재미를 더했습니다. </br></br>


## 📌 Key Features
### 🔎 사용자 소비 분석
: 사용자의 결제 내역을 Fetch해서 소비 내역을 분석 </br></br>

### 🔎 맞춤형 미션 
: 사용자의 소비 분석 결과와 설문 조사 내용을 토대로 사용자 맞춤형 미션을 제공 </br></br>

### 🔎 소비 습관 피드백 & 미션 수행 조언
: 사용자의 소비 습관에 대한 피드백과 함께 미션 항목을 어떻게 절약할 수 있을지 창의적인 조언을 제공</br></br>

### 🔎 금고
: 가상 계좌에 줄여야 할 미션 금액만큼 입금해야 미션 시작 가능 
- 미션에 성공한다면 해당 금액을 바로 돌려주지만 실패한다면 돈을 2주 뒤에 돌려줌
- 사용자가 미션을 실패하더라도 그 기간동안 강제적으로 그 금액만큼을 쓰지 못하게 함
- 실천력이 부족한 사람도 저절로 절약 습관이 생길 수 있도록 도와주는 장치 </br></br>

### 🔎 캐릭터 성장
: 미션을 성공함에 따라 사용자의 레벨이 높아지고 호칭이 달라지며 캐릭터가 성장 
![스크린샷(73)](https://github.com/choiyukyung/Capstone1_class7_team4/assets/80468377/36b66682-981f-4656-9cad-f5ac810ec11b) </br>

### 🔎 랭킹
: 다른 사용자들과 성공한 미션과 소비 습관 개선 현황을 공유하며 경쟁 가능</br></br>

### 🔎 통계
: 사용자의 소비 분석 결과를 시각화 </br></br>

### 🔎 웹 앱
: 모바일에서도 피드백을 확인하고 실천에 적용할 수 있도록 모바일 뷰 구현 
- 사용자가 피드백을 확인하는 것을 잊지 않도록 주기적으로 알림 
</br></br>

## 📌 Start Guide

### ◾ How To Execute
1. code -> download ZIP으로 파일을 다운 받은 후 압축 풀기 </br></br>
2. demo -> build.gradle 파일을 선택한 후 IntelliJ IDEA를 이용해 파일 열기</br></br>
3. mySQL workbench에서 'capstone1'이라는 이름으로 database 생성  
	```create database capstone1; ```</br></br>
4. User 'host'@'localhost’생성하고 비밀번호 1234로 설정  
	```create user 'host'@'localhost' identified by '1234';```</br></br>
5. 위의 user에게 해당 DB에 대해 모든 권한 부여  
	```grant all privileges on capstone1.* to 'host'@'localhost';```  
	```flush privileges;```</br></br>
6. IntelliJ에서 DemoApplication 실행</br></br>
7. IntelliJ 터미널에서 경로를 demo\src\main\frontend로 설정한 후 npm i</br></br>
8. npm start  </br>

- 참고사항: 해당 프로젝트가 github에 public으로 올려져있기 때문에 Chatgpt API Access Token이 자동으로 만료됐습니다. 이로 인해 피드백과 미션 로직이 제대로 동작하지 않을 수 있습니다.

### ◾ How To Use Test Data
1. 테스트 결제내역을 이용해 실행해보고 싶다면 먼저 회원가입을 진행합니다.</br></br>
2. 브라우저의 주소창에 ```localhost:8080/payment/1/가입한유저아이디``` 라고 입력하면 임의로 만들어 놓은 토스 가상계좌의 테스트 데이터가 db에 저장되어 로그인 될 때 해당 결제 내역이 분석됩니다.</br></br>
3. 다시 로그인 한 후 설문조사를 진행하면 됩니다.</br></br>

### ◾ Project Manual
[project manual.pdf](https://github.com/choiyukyung/Capstone1_class7_team4/files/13743405/project.manual.pdf)


## 📌 System Architecture
![image](https://github.com/choiyukyung/Capstone1_class7_team4/assets/80468377/9bbabc87-0515-47a0-84f8-138840b7fae7)

## 📌 Logic
### ◾ 소비 내역 분석
![image4](https://github.com/choiyukyung/Capstone1_class7_team4/assets/80468377/a4527682-0ed5-4aef-84c8-e1aa6574d090)

### ◾ 미션 & 피드백 제공
![image3](https://github.com/choiyukyung/Capstone1_class7_team4/assets/80468377/df9a80d5-78f4-44d1-869a-5dcb3dd8b35c)

### ◾ 금고
![image2](https://github.com/choiyukyung/Capstone1_class7_team4/assets/80468377/938decb8-68dd-458f-8309-e60dbeaf7842)
