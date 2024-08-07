# mayo-web

---
마요 웹 서버 입니다.
가게 주문 관리 및 오픈, 마감, 상태를 관리합니다.
---

## 기능목록

- [x] 처리중
    - [x] 가게 documentid를 받아 신규 및 진행 주문으로 분류합니다.
        - [x] 신규 주문을 선택하면 reservation의 documentid를 받아 에약의 상세 정보와 수락 및 거절 버튼을 created_at의 내림차순 출력합니다.
        - [x] 수락 버튼을 누르면 reservation의 reservation_state를 1(진행)으로 바꿉니다.
        - [x] 거절 버튼을 누르면 reservation의 reservation_state를 3(실패)으로 바꿉니다.
        - [x] 진행 주문을 선택하면 reservation의 documentid를 받아 에약의 상세 정보와 주문 완료 버튼을 출력합니다.
        - [x] 주문완료 버튼을 누르면 reservation의 reservation_state를 2(완료)으로 바꿉니다.

- [x] 완료
    - [x] 가게 documentid를 받아 완료 주문을 받아와 created_at의 내림차순으로 출력합니다.
    - [x] 완료 주문을 누르면 reservation의 documentid를 받아 완료된 주문, 실패된 주문의 상세 정보를 출력합니다.


- [x] 등록
    - [x] 해당 가게의 모든 item을 가져와 뷰 템플릿에 표시합니다.
    - [x] 체크된 item의 item_quantity 값을 가져옵니다.
    - [x] 마감하기 버튼으로 가게의 모든 메뉴의 item_quantity를 0으로 바꾸고 on_sale을 false로 바꿉니다. 가게의 open_state를 false로 바꿉니다.
    - [x] 오픈하기 버튼으로 체크박스에 선택된 아이템의 id를 받아 각 수량에 맞게 item_quantity를 바꾸고, item_on_sale을 true로 바꿉니다. 가게의 open_state를 true로 바꿉니다.

- [x] 마이페이지
    - [x] 상품 정보에서 가게정보를 입력받아 가게의 모든 아이템을 가져와서 추가, 수정, 삭제를 할 수 있습니다.
    - [ ] 알림 설정에서 알림을 받을 것인지 안받을 것인지 설정할 수 있습니다.
    - [x] 가게 관리에서 가게 이름, 오픈시간, 할인시간 등의 가게 상세 정보를 변경할 수 있습니다.
    - [x] 마요의 약관 및 정책을 볼 수 있습니다.
    - [x] 마요의 공지사항을 볼 수 있습니다.
    - [x] 고객센터 탭에서 마요의 카카오톡 채널에 연결된 링크로 들어갈 수 있습니다.
