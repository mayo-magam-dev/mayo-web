---
마요 Manager API 서버 입니다.
---

> 개발 기간 : 2024.8.9 ~ 2024.9.10 <br/>
> - 마감할인 플랫폼 mayo(마감해요)의 사장님용 api 서버입니다. <br/>
> - URL : https://mayomagam.shop

## 개발 환경
- 개발도구: Intellij IDEA - Ultimate
- 언어: Java 17<br>
- 빌드도구: Gradle
- 개발
  - Spring Framework: 6.1.0
  - Spring Boot: 3.1.4

- 데이터베이스
  - Firestore Database
- Storage
  - Firebase Storage
- AWS
  - EC2
- 기타
  - FCM
  - NGINX
  - Caffeine (in-memory-cache)
  <br/>

## 아키텍처

<img width="700" alt="스크린샷 2025-03-25 오후 2 43 28" src="https://github.com/user-attachments/assets/ab8cf055-d155-4b42-a8f3-f9641915868c" />

## 기능목록

- [x] 인가
    - [x] Firebase Authentication으로 Interceptor를 통해 인가처리를 진행합니다.<br>
  

- [x] 트랜잭션
    - [x] FirestoreTransactional 어노테이션으로 transaction AOP를 생성하여 처리합니다.<br>
  

- [x] 게시판
    - [x] 약관 및 정책의 모든 글을 불러옵니다.
    - [x] 공지사항 게시판의 모든 글을 불러옵니다.
    - [x] boardId로 게시판의 상세 정보를 가져올 수 있습니다.<br>
  

- [x] 아이템
    - [x] itemId로 해당 item의 상세 정보를 가져옵니다.
    - [x] storeId 값으로 해당 가게의 item들을 모두 불러옵니다.
    - [x] 아이템 수정 정보를 받아 아이템을 수정합니다.
    - [x] itemId로 아이템을 삭제합니다.
    - [x] 예약들로 각 예약의 첫번째 아이템 이름을 가져옵니다.
    - [x] fcm을 통해 현재 가게의 아이템의 현재 수량을 업데이트합니다.
    - [x] firebase storage에 사진을 저장합니다.<br>
  

- [x] 예약
    - [x] reservationId 값으로 예약 상세 정보를 가져옵니다.
    - [x] storeId 값으로 해당 가게의 신규 예약들을 가져옵니다.
    - [x] storeId 값으로 해당 가게의 진행 예약들을 가져옵니다.
    - [x] storeId 값으로 해당 가게의 완료, 실패 예약들을 가져옵니다.
    - [x] reservationId로 예약 상태를 변경합니다.(진행, 완료, 실패)<br>
  

- [x] 가게
    - [x] storeId로 가게 정보를 가져옵니다.
    - [x] 가게 정보를 받아 업데이트합니다.
    - [x] 가게 상태를 오픈으로 변경합니다.
    - [x] 가게 상태를 마감으로 변경합니다.<br>
  

- [x] 알림
    - [x] 주문 수락 시 알림을 발송합니다.
    - [x] 주문 거절 시 알림을 발송합니다.
    - [x] 가게 오픈 시 알림을 발송합니다.
    - [x] 커스텀 메시징 발송할 수 있는 api를 제공합니다.<br>
  

- [x] 유저
  - [x] 회원가입 기능을 제공합니다. -> 이후 서비스 측에 인가가 필요합니다.