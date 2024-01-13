package com.example.mayoweb.Controller;

import com.example.mayoweb.Dto.*;
import com.example.mayoweb.Service.*;
import com.google.cloud.firestore.DocumentReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Controller
public class EndController {

    @Autowired
    ReservationService reservationService;

    @Autowired
    UsersService usersService;

    @Autowired
    CartService cartService;

    @Autowired
    ItemsService itemsService;

    @Autowired
    StoresService storesService;

    @GetMapping("/mayo/end/{store_ref}")
    public String complete(@PathVariable String store_ref, Model model) throws ExecutionException, InterruptedException {
        List<ReservationsDto> EndReservationList = reservationService.getEndByStoreRef(store_ref);

        List<String> endItem = itemsService.getFirstItemNamesFromReservations(EndReservationList);

        StoresDto storesDto = storesService.getStoreById(store_ref);

        model.addAttribute("store_ref", store_ref);
        model.addAttribute("endSize", EndReservationList.size());
        model.addAttribute("endItem", endItem);
        model.addAttribute("endReservation", EndReservationList);
        model.addAttribute("store", storesDto);


        return "end";
    }

    @GetMapping("/mayo/end/{store_ref}/{reservationid}")
    public String completedetail(@PathVariable String store_ref, @PathVariable String reservationid, Model model) throws Exception {
        ReservationsDto reservationEntity = reservationService.getReservationById(reservationid);

        List<ReservationsDto> EndReservationList = reservationService.getEndByStoreRef(store_ref);

        List<String> endItem = itemsService.getFirstItemNamesFromReservations(EndReservationList);

        DocumentReference userRef = reservationEntity.getUser_ref();

        UsersDto usersEntity = usersService.getUserByDocRef(userRef);

        List<DocumentReference> cartRef = reservationEntity.getCart_ref();

        List<CartsDto> cartsEntity = cartService.getCartsByDocRef(cartRef);

        StoresDto storesDto = storesService.getStoreById(store_ref);

        int itemcount = 0;

        List<ItemsDto> itemsDto = new ArrayList<>();
        for (CartsDto cart : cartsEntity) {
            DocumentReference itemRef = cart.getItem();
            ItemsDto item = itemsService.getItemByDocRef(itemRef);
            itemsDto.add(item);
            itemcount += 1;
        }

        model.addAttribute("reservation", reservationEntity);
        model.addAttribute("user", usersEntity);
        model.addAttribute("carts", cartsEntity);
        model.addAttribute("items", itemsDto);
        model.addAttribute("itemcount", itemcount);
        model.addAttribute("store_ref", store_ref);
        model.addAttribute("endSize", EndReservationList.size());
        model.addAttribute("endItem", endItem);
        model.addAttribute("endReservation", EndReservationList);
        model.addAttribute("store", storesDto);

        return "enddetail";
    }

    @GetMapping("mayo/done/{store_ref}/{reservationid}")
    public String reject(@PathVariable String store_ref, @PathVariable String reservationid, Model model) throws ExecutionException, InterruptedException {

        reservationService.ReservationDone(reservationid);

        return "redirect:/mayo/processing/" + store_ref;
    }
}
