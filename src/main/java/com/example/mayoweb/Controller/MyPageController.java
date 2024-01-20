package com.example.mayoweb.Controller;

import com.example.mayoweb.Board.BoardDto;
import com.example.mayoweb.Board.BoardService;
import com.example.mayoweb.Items.ItemsDto;
import com.example.mayoweb.Items.ItemsEntity;
import com.example.mayoweb.Items.ItemsService;
import com.example.mayoweb.Store.StoresDto;
import com.example.mayoweb.Store.StoresService;
import com.google.cloud.firestore.DocumentReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MyPageController {

    private final StoresService storesService;
    private final BoardService boardService;
    private final ItemsService itemsService;

    //마이페이지
    @GetMapping("mayo/mypage/{storeid}")
    public String MyPage(@PathVariable String storeid, Model model) throws ExecutionException, InterruptedException {

        StoresDto storesDto = storesService.getStoreById(storeid);

        model.addAttribute("store", storesDto);

        return "mypage";
    }

    //가게 정보
    @GetMapping("/mayo/mypage/{storeid}/storeinfo")
    public String StoreInfo(@PathVariable String storeid, Model model) throws ExecutionException, InterruptedException {

        StoresDto storesDto = storesService.getStoreById(storeid);

        model.addAttribute("store", storesDto);
        model.addAttribute("storeid", storeid);

        return "storeinfo";
    }

    //store 수정 후 버튼 누를 시 store 업데이트
    @PostMapping("/mayo/mypage/{storeid}/storeinfo/update")
    public String StoreUpdate(@PathVariable String storeid , StoresDto dto, Model model) throws Exception {

        StoresDto storesDto = storesService.getStoreById(storeid);

        model.addAttribute("store", storesDto);

        storesService.updateStore(dto);

        return "redirect:/mayo/mypage/" + storeid + "/storeinfo";
    }

    //고객센터
    @GetMapping("/mayo/mypage/{storeid}/service")
    public String CustomerService(@PathVariable String storeid, Model model) throws ExecutionException, InterruptedException {

        StoresDto storesDto = storesService.getStoreById(storeid);

        model.addAttribute("store", storesDto);
        model.addAttribute("storeid", storeid);

        return "customerservice";
    }

    //공지사항
    @GetMapping("/mayo/mypage/{storeid}/notice")
    public String notice(@PathVariable String storeid, Model model) throws ExecutionException, InterruptedException {

        StoresDto storesDto = storesService.getStoreById(storeid);
        List<BoardDto> boards = boardService.getBoard1();

        model.addAttribute("board" , boards);
        model.addAttribute("store", storesDto);
        model.addAttribute("storeid", storeid);

        return "notice";
    }

    //약관 및 정책
    @GetMapping("/mayo/mypage/{storeid}/terms")
    public String terms(@PathVariable String storeid, Model model) throws ExecutionException, InterruptedException {

        StoresDto storesDto = storesService.getStoreById(storeid);
        List<BoardDto> boards = boardService.getBoard0();

        model.addAttribute("board" , boards);
        model.addAttribute("store", storesDto);
        model.addAttribute("storeid", storeid);

        return "terms";
    }

    //약관 및 정책 상세정보
    @GetMapping("/mayo/mypage/{storeid}/terms/{boardid}")
    public String termsDetail(@PathVariable String storeid, @PathVariable String boardid, Model model) throws ExecutionException, InterruptedException {

        StoresDto storesDto = storesService.getStoreById(storeid);
        List<BoardDto> boards = boardService.getBoard0();
        BoardDto boarddto = boardService.getBoardById(boardid);

        model.addAttribute("boarddto", boarddto);
        model.addAttribute("board" , boards);
        model.addAttribute("store", storesDto);
        model.addAttribute("storeid", storeid);

        return "termsdetail";
    }

    //알림 설정
    @GetMapping("/mayo/mypage/{storeid}/alarm")
    public String alarm(@PathVariable String storeid, Model model) throws ExecutionException, InterruptedException {

        StoresDto storesDto = storesService.getStoreById(storeid);

        model.addAttribute("store", storesDto);
        model.addAttribute("storeid", storeid);

        return "alarm";
    }

    //공지사항 상세정보
    @GetMapping("/mayo/mypage/{storeid}/notice/{boardid}")
    public String noticeDetail(@PathVariable String storeid, @PathVariable String boardid, Model model) throws ExecutionException, InterruptedException {

        StoresDto storesDto = storesService.getStoreById(storeid);
        List<BoardDto> boards = boardService.getBoard0();
        BoardDto boarddto = boardService.getBoardById(boardid);

        model.addAttribute("boarddto", boarddto);
        model.addAttribute("board" , boards);
        model.addAttribute("store", storesDto);
        model.addAttribute("storeid", storeid);

        return "noticedetail";
    }

    //상품 정보
    @GetMapping("/mayo/mypage/{storeid}/iteminfo")
    public String noticeDetail(@PathVariable String storeid, Model model) throws ExecutionException, InterruptedException {

        StoresDto storesDto = storesService.getStoreById(storeid);
        List<ItemsDto> items = itemsService.getItemsByStoreRef(storeid);

        model.addAttribute("items", items);
        model.addAttribute("store", storesDto);
        model.addAttribute("storeid", storeid);

        return "iteminfo";
    }

    //상품 정보에서 +버튼을 눌렀을 시
    @GetMapping("/mayo/mypage/{storeid}/iteminfo/create")
    public String create(@PathVariable String storeid, Model model) throws ExecutionException, InterruptedException {
        StoresDto storesDto = storesService.getStoreById(storeid);

        model.addAttribute("store", storesDto);
        model.addAttribute("storeid", storeid);

        return "createitem";
    }

    //상품 상세정보에서 상품추가
    @PostMapping("/mayo/mypage/{storeid}/iteminfo/createitem")
    public String createItem(@PathVariable String storeid, ItemsDto item, Model model) throws ExecutionException, InterruptedException {
        StoresDto storesDto = storesService.getStoreById(storeid);
        DocumentReference storeRef = (DocumentReference) storesService.getDocsRef(storeid);
        item.setStore_ref(storeRef);

        ItemsDto saved = itemsService.save(item, storeid);

        model.addAttribute("store", storesDto);
        model.addAttribute("storeid", storeid);

        return "closewindow";
    }

    //상품 정보에서 아이템 눌렀을 때
    @GetMapping("/mayo/mypage/{storeid}/iteminfo/{itemid}")
    public String create(@PathVariable String storeid, @PathVariable String itemid, Model model) throws Exception {
        StoresDto storesDto = storesService.getStoreById(storeid);
        ItemsDto itemsDto = itemsService.getItemById(itemid);

        model.addAttribute("item", itemsDto);
        model.addAttribute("store", storesDto);
        model.addAttribute("storeid", storeid);

        return "updateitem";
    }

    @PostMapping("/mayo/mypage/{storeid}/iteminfo/{itemid}/updateitem")
    public String ItemUpdate(@PathVariable String storeid, @PathVariable String itemid ,ItemsDto dto, Model model) throws Exception {

        ItemsDto itemsDto = itemsService.getItemById(itemid);

        model.addAttribute("itemsDto", itemsDto);

        itemsService.updateItem(dto);

        return "closewindow";
    }

    //상품 상셋정보에서 아이템 삭제시
    @GetMapping("/mayo/mypage/{storeid}/iteminfo/{itemid}/deleteitem")
    public String ItemDelete(@PathVariable String storeid, @PathVariable String itemid) throws Exception {

        ItemsDto itemsDto = itemsService.getItemById(itemid);

        if(itemsDto != null) {
            itemsService.deleteItem(itemsDto);
        }

        return "closewindow";
    }

}
