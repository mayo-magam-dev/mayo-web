package com.example.mayoweb.Controller;

import com.example.mayoweb.Items.ItemsDto;
import com.example.mayoweb.Items.ItemsService;
import com.example.mayoweb.Store.StoresDto;
import com.example.mayoweb.Store.StoresService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Controller
public class EnrollController {

    @Autowired
    StoresService storesService;

    @Autowired
    ItemsService itemsService;

    @GetMapping("/mayo/enroll/{storeid}")
    public String enroll(@PathVariable String storeid, Model model) throws ExecutionException, InterruptedException {

        StoresDto storesDto = storesService.getStoreById(storeid);
        List<ItemsDto> itemsDtoList = itemsService.getItemsByStoreRef(storeid);

        model.addAttribute("itemlist", itemsDtoList);
        model.addAttribute("storeid", storeid);
        model.addAttribute("store", storesDto);

        return "enroll";
    }

    @PostMapping("/mayo/enroll/{storeid}/open")
    public String open(@PathVariable String storeid, @RequestParam("checkbox") List<String> checkbox ,@RequestParam("itemid") List<String> itemIdList,
                       @RequestParam("quantityList") List<Integer> quantityList) throws ExecutionException, InterruptedException {

        itemsService.updateItemOnSale(checkbox ,itemIdList, quantityList);
        storesService.openStore(storeid);

        return "redirect:/mayo/processing/" + storeid;
    }

    @GetMapping("/mayo/enroll/{storeid}/close")
    public String close(@PathVariable String storeid) throws ExecutionException, InterruptedException {
        itemsService.updateItemsStateOutOfStock(storeid);
        storesService.closeStore(storeid);

        return "redirect:/mayo/processing/" + storeid;
    }
}
