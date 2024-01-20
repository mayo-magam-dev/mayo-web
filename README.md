# mayo-web

---
마요 웹 서버 입니다.
가게 주문 관리 및 오픈, 마감, 상태를 관리합니다.
---

## 기능목록

- [x] 가게 주문 확인 및 상태 변경
    - [x] 가게에 들어온 주문을 분류합니다. 
        - [x] store_ref 값이 해당 가게인 모든 reservation 객체를 가져와 reservation_state 값으로 분류합니다.
    - [x] 받아온 reservation객체의 상태를 변경할 수 있습니다. 
        - [x] thymeleaf 템플릿을 활용하여 에약자, 시간, 전화번호 등을 페이지에 표시합니다.
        - [x] 수락 / 거절 버튼으로 신규 주문의 상태를 바꿉니다.
        - [x] 완료 버튼으로 진행중인 주문의 상태를 바꿉니다.

- [ ] 가게 오픈 등록
    - [x] 해당 가게의 모든 item을 가져와 뷰 템플릿에 표시합니다.
    - [ ] 체크된 item의 item_quantity와 item_on_sale 값을 가져옵니다.
    - [ ] 마감하기 버튼으로 가게의 모든 메뉴의 item_quantity를 0으로 바꾸고 on_sale을 false로 바꿉니다. 가게의 open_state를 false로 바꿉니다.
    - [ ] 오픈하기 버튼으로 체크박스에 선택된 아이템의 id를 받아 각 수량에 맞게 item_quantity를 바꾸고, item_on_sale을 true로 바꿉니다. 가게의 open_state를 true로 바꿉니다.

- [x] 마이페이지
    - [x] 상품 정보에서 가게정보를 입력받아 가게의 모든 아이템을 가져와서 추가, 수정, 삭제를 할 수 있습니다.
    - [ ] 알림 설정에서 알림을 받을 것인지 안받을 것인지 설정할 수 있습니다.
    - [x] 가게 관리에서 가게 이름, 오픈시간, 할인시간 등의 가게 상세 정보를 변경할 수 있습니다.
    - [x] 마요의 약관 및 정책을 볼 수 있습니다.
    - [x] 마요의 공지사항을 볼 수 있습니다.
    - [x] 고객센터 탭에서 마요의 카카오톡 채널에 연결된 링크로 들어갈 수 있습니다.
