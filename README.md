# healbits (healthy + habits 건강 습관 프로젝트) api

## **프로젝트 주제**

- 사용자가 직접 챌린지(건강 습관)를 개설할 수도 있고 , 참여할 수도 있으며 참여율에 따라 보상을 받을 수 있는 건강 습관 서비스

## **프로젝트 기획 배경 (목적)**

- 참여자들이 좋은 건강 습관을 갖도록 돕는 서비스를 만들고자 합니다
- 기본적인 CRUD 구현 및 학습한 내용들에 대한 프로젝트 적용을 해보고자 합니다.
- 더 나아가 이전에 사용해보지 않은 기술들을 사용해보고 적용함으로써 경험을 쌓고자 합니다.

## **프로젝트 구조**

<img src = "images/Project_Architecture.png"/>

## ERD

Member : Challenge ( 1 : N )

Member : TakeChallenge ( 1 : N )

Member : Transaction ( 1 : N )

Challenge : TakeChallenge ( 1 : N )

TakeChallenge : Verification( 1 : N )

<img src = "images/ERD.png"/>

## 프로젝트 기능

**[회원가입]**

- 이메일 , 이름,  전화번호, 비밀번호, 임의로 설정할 초기 예치금
- 이미 가입된 이메일로 가입을 시도하면 에러가 발생한다.

**[로그인]**

- 로그인 시 정보가 없는 이메일을 이용하여 로그인을 시도하면 에러가 발생한다
- 로그인 시 비밀번호가 일치하지 않는다면 에러가 발생한다.

**[마이페이지]**

- 정보 조회
    - 기본 회원 정보
- 챌린지 시작~종료기간 동안 챌린지 진행상황 확인 (현재까지 인증% 조회 가능)
- 완료한 챌린지 조회

**[등록된 챌린지 조회]**

- 각 카테고리에 해당하는 챌린지 조회 (제목 , 요약 설명 , 챌린지 기간 , 참가자 수 , 시작 일자)

**[챌린지 상세정보 조회]**

- 각 챌린지들에 대한 상세한 정보 조회(제목, 설명 등)

**[챌린지 참여]**

- 회원 ID , 챌린지 ID , 챌린지 참여 금액 등을 저장한다.
- 챌린지 금액 설정 (임의로 회원이 원하는 챌린지 금액 설정 가능)
    - 결제 시 회원에게 임의로 넣어놓은 잔액으로 결제한 것으로 가정 (즉 , 금액이 부족한 경우는 없다)
- 설정한 챌린지 금액만큼 본인의 잔여(Balance) 포인트에서 차감된다.

**[챌린지 개설하기]**

- 카테고리 , 제목, 요약설명, 설명, 챌린지 기간, 참가자 수 , 상태(진행 전, 진행 중, 종료)
    
    , 챌린지 시작 날짜 , 챌린지 종료날짜 등을 저장
    
- 챌린지의 주기는 하루에 한 번 인증을 해야한다고 가정

**[챌린지 인증하기]**

- 챌린지 실행 인증 (이미지를 통해 - S3사용)

**[챌린지 완료]**

- 챌린지 완료 시 참여 %에 따른 보상 지급
    - 100% 참여 시 결제 금액 + 결제금액의 5%
    - 80% 참여 시 챌린지 참여 시 결제한 금액 반환
    - 50% 미만 참여 시 챌린지 참여 시 결제한 금액의 30% 차감 후 반환)

---
## API 명세서

### 1. 회원가입 API

- POST /member/register
- 파라미터 : 이메일,  비밀번호 , 이름 , 연락처
- 정책 : 이미 등록되어 있는 이메일이라면 실패응답
- 성공 응답 : 이메일 , 이름

#### 상세 정보

- 저장이 필요한 정보
- 단, balance 는 다른 api에서 정보를 넣어줄 것임.

| 컬럼명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| id | pk
(Long) | primary key |
| email | String | 이메일 |
| password | String | 비밀번호 |
| name | String | 이름 |
| phone | String | 휴대폰번호(연락처) |
| balance | Long | 잔액 |
| registeredDateTime | LocalDateTime | 등록일시 |
| updatedDatetime | LocalDateTime | 수정일시 |
- 요청 / 응답 구조
    - 요청
    
    ```java
    {
        "email" : "xxxxx@hanmail.net",
        "password" : "1234",
        "name" : "홍길동",
        "phone" : "01011112222"
    }
    ```
    
    - 응답
    
    ```java
    {
        "email" : "xxxxx@hanmail.net",
        "name" : "홍길동"
    }
    ```
    

### 2. 로그인 API

- POST /member/login
- 파라미터 : 이메일,  비밀번호
- 정책 : 등록되지 않은 이메일 , 비밀번호가 일치하지 않을 시 실패응답
- 성공 응답 : grantType, accessToken, refreshToekn

#### 상세 정보

- 요청 / 응답 구조
    - 요청
    
    ```java
    {
        "email" : "xxxxx@hanmail.net",
        "password" : "1234"
    }
    ```
    
    - 응답
    
    ```java
    {
        "grantType": "Bearer",
        "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJraGcyMTU0QGhhbm1haWwubmV0IiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY3NzcyMDAyN30.GnbfK0jHH8XdQ3eexgj1XV5fuuGQvTQo4njX45FtDj4",
        "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2Nzc3MjAwMjd9.fRx3PqZdjKEFgyOjWzf9JGHEBQRzdilidW7AH3Fotkw"
    }
    ```
    

### 3. 회원정보 조회 API

- GET /member/info?email={email}
- 파라미터 : 이메일
- 정책 : 등록되지 않은 이메일 실패응답
- 성공 응답 : MemberInfo(이메일, 이름, 연락처, 잔액)

#### 상세 정보

- 요청 / 응답 구조
    - 요청
    
    ```java
    GET /member/info?email={email}
    ex) /member/info?email=xxxxx@daum.net
    ```
    
    - 응답
    
    ```java
    {
        "email": "xxxxxx@daum.net",
        "name": "홍길동",
        "phone": "01011112222",
        "balance": 0
    }
    ```
    
## 4. 챌린지 등록 API

- POST /challenge/register
- 파라미터 : 챌린지명 , 챌린지 카테고리 , 요약설명, 설명, 시작시간, 종료시간
- 정책 : 등록되지 않은 이메일, 카테고리 혹은 시작 및 종료 일자가 yyyy-MM-dd 형식이 아니라면 실패응답, 시작 일자가 현재 일자보다 과거일 경우 , 시작 일자가 종료 일자보다 미래일 경우 실패 응답
- 성공 응답 : 이메일 , 챌린지명

### 상세 정보

- 저장이 필요한 정보

| 컬럼명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| id | pk
(Long) | primary key |
| registeredMember | Member | 회원테이블과 챌린지 테이블은 1:N 관계이다. |
| challengeName | String | 챌린지명 |
| challengeCategory | String | 챌린지 카테고리 |
| summary | String | 챌린지에 대한 요약설명 |
| contents | String | 챌리지에 대한 설 |
| participantsNum | Long | 챌린지 참여자 수 |
| startDate | LocalDate | 챌린지 시작일자 |
| endDate | LocalDate | 챌린지 종료일자 |
| registeredDateTime | LocalDateTime | 등록일시 |
| updatedDatetime | LocalDateTime | 수정일시 |
- 요청 / 응답 구조
    - 요청
    
    ```java
    {
      "challengeName" : "challenge",
      "challengeCategory" : "HEALTH",
      "summary" : "운동",
      "contents" : "주 3회 운동하기",
      "startDate" : "2023-03-06",
      "endDate" : "2023-03-13"
    }
    ```
    
    - 응답
    
    ```java
    {
    	"email":"abcd@gmail.com",
    	"challengeName":"challenge",
    }
    ```
    

## 5. 카테고리에 따른 챌린지 목록 조회 API

- GET /challenge/list?challengeCategory={challengeCategory}
- 파라미터 : 챌린지 카테고리명
- 정책 : 시작날짜가 현재 날짜보다 크거나 같은 챌린지 목록을 카테고리별로 조회 가능
          등록되지 않은 카테고리 실패응답
- 성공 응답 : 챌린지명, 챌린지 카테고리, 요약설명, 참가자 수, 챌린지 기간 , 시작 일까지 남은 일

### 상세 정보

- 요청 / 응답 구조
    - 요청
    
    ```java
    GET /challenge/list?challengeCategory={challengeCategory}
    ```
    
    - 응답
    
    ```java
    [
        {
            "challengeName": "취미1",
            "challengeCategory": "HOBBY",
            "summary": "취미:음악",
            "participantsNum": 0,
            "duration": 8,
            "remainingDays": 0
        },
        {
            "challengeName": "취미2",
            "challengeCategory": "HOBBY",
            "summary": "취미:미술",
            "participantsNum": 0,
            "challengeDuration": 5,
            "remainingDaysToStart": 3
        }
    ]
    ```
    

## 6. 챌린지 상세 조회 API

- GET /challenge/detail?challengeId={challengeId}
- 파라미터 : 챌린지Id
- 정책 : 현재 일자가 챌린지 시작 일자보다 작다면 CategoryStatus = READY 
          현재 일자가 챌린지 시작 일자 - 챌린지 종료일자라면 CategoryStatus = IN_PROGRESS 
          챌린지 종료일자보다 더 미래라면 CategoryStatus = CLOSED 
          해당 챌린지의 상세한 정보를 조회할 수 있는 API
          등록되지 않은 챌린지의 경우 실패응답
- 성공 응답 : 챌린지명, 챌린지 카테고리, 챌린지 설명, 챌린지 참여자 수 , 챌린지 상태 ,
                  챌린지 시작일자, 챌린지 종료일자

### 상세 정보

- 요청 / 응답 구조
    - 요청
    
    ```java
    GET /challenge/detail?challengeId={challengeId}
    ```
    
    - 응답
    
    ```java
    {
        "challengeName": "challenge",
        "challengeCategory": "ENVIRONMENT",
        "contents": "환경지킴이",
        "participantsNum": 1000,
        "challengeStatus": "ACTIVE",
        "startDate": "2023-03-10",
        "endDate": "2023-03-11"
    }
    ```
---
## 사용 기술스택

- SpringBoot
- Java
- MySQL
- JPA
- Spring Batch
- Spring Security
- redis
- jwt
