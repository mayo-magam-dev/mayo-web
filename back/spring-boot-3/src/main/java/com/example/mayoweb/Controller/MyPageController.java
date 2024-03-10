package com.example.mayoweb.controller;

import com.example.mayoweb.board.BoardDto;
import com.example.mayoweb.board.BoardService;
import com.example.mayoweb.items.ItemsDto;
import com.example.mayoweb.items.ItemsService;
import com.example.mayoweb.store.StoresDto;
import com.example.mayoweb.store.StoresService;
import com.example.mayoweb.response.*;
import com.google.cloud.firestore.DocumentReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/mayo/mypage")
public class MyPageController {

    private final StoresService storesService;
    private final BoardService boardService;
    private final ItemsService itemsService;

    //마이페이지, 가게 정보, 고객 센터, 알림 설정, 상품정보에서 +버튼 눌렀을 때,
    @GetMapping("mayo/mypage/{storeid}")
    public ResponseEntity<BasicResponse> MyPage(@PathVariable String storeid) throws ExecutionException, InterruptedException {

        StoresDto storesDto = storesService.getStoreById(storeid);

        BasicResponse response = new BasicResponse(storeid, storesDto);

        return ResponseEntity.ok(response);
    }

    //store 수정 후 버튼 누를 시 store 업데이트
    @PostMapping("/{storeid}/storeinfo/update")
    public ResponseEntity<String> StoreUpdate(@PathVariable String storeid , StoresDto dto) throws Exception {

        StoresDto storesDto = storesService.getStoreById(storeid);

        storesService.updateStore(dto);

        return ResponseEntity.ok("Store Update");
    }

    //공지사항
    @GetMapping("/{storeid}/notice")
    public ResponseEntity<NoticeResponse> notice(@PathVariable String storeid) throws ExecutionException, InterruptedException {

        StoresDto storesDto = storesService.getStoreById(storeid);
        List<BoardDto> boards = boardService.getNoticeBoard();

        NoticeResponse response = new NoticeResponse(boards, storeid, storesDto);

        return ResponseEntity.ok(response);
    }

    //약관 및 정책
    @GetMapping("/{storeid}/terms")
    public ResponseEntity<NoticeResponse> terms(@PathVariable String storeid) throws ExecutionException, InterruptedException {

        StoresDto storesDto = storesService.getStoreById(storeid);
        List<BoardDto> boards = boardService.getTermsBoard();

        NoticeResponse response = new NoticeResponse(boards, storeid, storesDto);

        return ResponseEntity.ok(response);
    }

    //약관 및 정책 상세정보
    @GetMapping("/{storeid}/terms/{boardid}")
    public ResponseEntity<NoticeDetailResponse> termsDetail(@PathVariable String storeid, @PathVariable String boardid) throws ExecutionException, InterruptedException {

        StoresDto storesDto = storesService.getStoreById(storeid);
        List<BoardDto> boards = boardService.getTermsBoard();
        BoardDto boardDto = boardService.getBoardById(boardid);

        NoticeDetailResponse response = new NoticeDetailResponse(boards, boardDto, storeid, storesDto);

        return ResponseEntity.ok(response);
    }


    //공지사항 상세정보
    @GetMapping("/{storeid}/notice/{boardid}")
    public ResponseEntity<NoticeDetailResponse> noticeDetail(@PathVariable String storeid, @PathVariable String boardid) throws ExecutionException, InterruptedException {

        StoresDto storesDto = storesService.getStoreById(storeid);
        List<BoardDto> boards = boardService.getNoticeBoard();
        BoardDto boardDto = boardService.getBoardById(boardid);

        NoticeDetailResponse response = new NoticeDetailResponse(boards, boardDto, storeid, storesDto);

        return ResponseEntity.ok(response);
    }

    //상품 정보
    @GetMapping("/{storeid}/iteminfo")
    public ResponseEntity<EnrollResponse> noticeDetail(@PathVariable String storeid) throws ExecutionException, InterruptedException {

        StoresDto storesDto = storesService.getStoreById(storeid);
        List<ItemsDto> items = itemsService.getItemsByStoreRef(storeid);

        EnrollResponse response = new EnrollResponse(
                items,
                storeid,
                storesDto
        );

        return ResponseEntity.ok(response);
    }

    //상품 상세정보에서 상품추가
    @PostMapping("/{storeid}/iteminfo/createitem")
    public ResponseEntity<BasicResponse> createItem(@PathVariable String storeid, ItemsDto item) throws ExecutionException, InterruptedException {
        StoresDto storesDto = storesService.getStoreById(storeid);
        DocumentReference storeRef = (DocumentReference) storesService.getDocsRef(storeid);
        item.setStore_ref(storeRef);

        ItemsDto saved = itemsService.save(item, storeid);

        BasicResponse response = new BasicResponse(storeid, storesDto);

        return ResponseEntity.ok(response);
    }

    //상품 정보에서 아이템 눌렀을 때
    @GetMapping("/{storeid}/iteminfo/{itemid}")
    public ResponseEntity<ItemInfoResponse> create(@PathVariable String storeid, @PathVariable String itemid) throws Exception {
        StoresDto storesDto = storesService.getStoreById(storeid);
        ItemsDto itemsDto = itemsService.getItemById(itemid);

        ItemInfoResponse response = new ItemInfoResponse(itemsDto, storeid, storesDto);

        return ResponseEntity.ok(response);
    }

    //아이템 업데이트 시
    @PostMapping("/{storeid}/iteminfo/{itemid}/updateitem")
    public ResponseEntity<String> ItemUpdate(@PathVariable String storeid, @PathVariable String itemid ,ItemsDto dto) throws Exception {

        ItemsDto itemsDto = itemsService.getItemById(itemid);
        StoresDto storesDto = storesService.getStoreById(storeid);

        itemsService.updateItem(dto);

        return ResponseEntity.ok("item update");

    }

    //상품 상세정보에서 아이템 삭제시
    @GetMapping("/{storeid}/iteminfo/{itemid}/deleteitem")
    public ResponseEntity<String> ItemDelete(@PathVariable String storeid, @PathVariable String itemid) throws Exception {

        ItemsDto itemsDto = itemsService.getItemById(itemid);

        if(itemsDto != null) {
            itemsService.deleteItem(itemsDto);
        }

        return ResponseEntity.ok("item delete");
    }

}
