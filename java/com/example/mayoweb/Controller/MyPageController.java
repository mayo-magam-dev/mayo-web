package com.example.mayoweb.Controller;

import com.example.mayoweb.Adapter.StoresAdapter;
import com.example.mayoweb.Dto.StoresDto;
import com.example.mayoweb.Entity.StoresEntity;
import com.example.mayoweb.Service.StoresService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.concurrent.ExecutionException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MyPageController {

    private final StoresService storesService;


    @GetMapping("mayo/mypage/{store_ref}")
    public String MyPage(@PathVariable String store_ref, Model model) throws ExecutionException, InterruptedException {

        StoresDto storesDto = storesService.getStoreById(store_ref);

        model.addAttribute("store", storesDto);

        return "mypage";
    }

    @GetMapping("/mayo/mypage/{store_ref}/storeinfo")
    public String StoreInfo(@PathVariable String store_ref, Model model) throws ExecutionException, InterruptedException {

        StoresDto storesDto = storesService.getStoreById(store_ref);

        model.addAttribute("store", storesDto);
        model.addAttribute("store_ref", store_ref);

        return "storeinfo";
    }

    //store 수정 후 버튼 누를 시 store 업데이트
    @PostMapping("/mayo/mypage/{store_ref}/storeinfo/update")
    public String StoreUpdate(@PathVariable String store_ref , StoresDto dto, Model model) throws Exception {

        log.info(dto.toString());

        StoresDto storesDto = storesService.getStoreById(store_ref);

        model.addAttribute("store", storesDto);

        storesService.updateStore(dto);

        return "redirect:/mayo/mypage/" + store_ref + "/storeinfo";
    }

}
