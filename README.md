---
마요 API 서버 입니다.
Swagger를 통해 API 명세서를 제공합니다.
---

## 기능목록

- [x] 인가
    - [x] Interceptor를 통해 인가처리를 진행합니다.<br><br>

- [x] 트랜잭션
    - [x] FirestoreTransactional 어노테이션으로 transaction AOP를 생성하여 처리합니다.<br><br>


- [x] 게시판
    - [x] 약관 및 정책의 모든 글을 불러옵니다.
    - [x] 공지사항 게시판의 모든 글을 불러옵니다.
    - [x] boardId로 게시판의 상세 정보를 가져올 수 있습니다.<br><br>

- [x] 아이템
    - [x] itemId로 해당 item의 상세 정보를 가져옵니다.
    - [x] storeId 값으로 해당 가게의 item들을 모두 불러옵니다.
    - [x] 아이템 수정 정보를 받아 아이템을 수정합니다.
    - [x] itemId로 아이템을 삭제합니다.
    - [x] 예약들로 각 예약의 첫번째 아이템 이름을 가져옵니다.
    - [x] fcm을 통해 현재 가게의 아이템의 현재 수량을 업데이트합니다.
    - [x] firebase storage에 사진을 저장합니다.<br><br>


- [x] 예약
    - [x] reservationId 값으로 예약 상세 정보를 가져옵니다.
    - [x] storeId 값으로 해당 가게의 신규 예약들을 가져옵니다.
    - [x] storeId 값으로 해당 가게의 진행 예약들을 가져옵니다.
    - [x] storeId 값으로 해당 가게의 완료, 실패 예약들을 가져옵니다.
    - [x] reservationId로 예약 상태를 변경합니다.(진행, 완료, 실패)<br><br>


- [x] 가게
    - [x] storeId로 가게 정보를 가져옵니다.
    - [x] 가게 정보를 받아 업데이트합니다.
    - [x] 가게 상태를 오픈으로 변경합니다.
    - [x] 가게 상태를 마감으로 변경합니다.<br><br>


- [x] 알림
    - [x] 주문 수락 시 알림을 발송합니다.
    - [x] 주문 거절 시 알림을 발송합니다.
    - [x] 가게 오픈 시 알림을 발송합니다. 
