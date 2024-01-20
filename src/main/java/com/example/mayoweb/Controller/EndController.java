package com.example.mayoweb.Controller;

import com.example.mayoweb.Carts.CartService;
import com.example.mayoweb.Carts.CartsDto;
import com.example.mayoweb.Items.ItemsDto;
import com.example.mayoweb.Items.ItemsService;
import com.example.mayoweb.Reservation.ReservationService;
import com.example.mayoweb.Reservation.ReservationsDto;
import com.example.mayoweb.Store.StoresDto;
import com.example.mayoweb.Store.StoresService;
import com.example.mayoweb.User.UsersDto;
import com.example.mayoweb.User.UsersService;
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

    //완료 페이지 기본화면
    @GetMapping("/mayo/end/{storeid}")
    public String complete(@PathVariable String storeid, Model model) throws ExecutionException, InterruptedException {
        List<ReservationsDto> EndReservationList = reservationService.getEndByStoreRef(storeid);

        List<String> endItem = itemsService.getFirstItemNamesFromReservations(EndReservationList);

        StoresDto storesDto = storesService.getStoreById(storeid);

        model.addAttribute("storeid", storeid);
        model.addAttribute("endSize", EndReservationList.size());
        model.addAttribute("endItem", endItem);
        model.addAttribute("endReservation", EndReservationList);
        model.addAttribute("store", storesDto);


        return "end";
    }

    //완료 페이지 예약 눌렀을 시
    @GetMapping("/mayo/end/{storeid}/{reservationid}")
    public String completedetail(@PathVariable String storeid, @PathVariable String reservationid, Model model) throws Exception {
        ReservationsDto reservationEntity = reservationService.getReservationById(reservationid);

        List<ReservationsDto> EndReservationList = reservationService.getEndByStoreRef(storeid);

        List<String> endItem = itemsService.getFirstItemNamesFromReservations(EndReservationList);

        DocumentReference userRef = reservationEntity.getUser_ref();

        UsersDto usersEntity = usersService.getUserByDocRef(userRef);

        List<DocumentReference> cartRef = reservationEntity.getCart_ref();

        List<CartsDto> cartsEntity = cartService.getCartsByDocRef(cartRef);

        StoresDto storesDto = storesService.getStoreById(storeid);

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
        model.addAttribute("storeid", storeid);
        model.addAttribute("endSize", EndReservationList.size());
        model.addAttribute("endItem", endItem);
        model.addAttribute("endReservation", EndReservationList);
        model.addAttribute("store", storesDto);

        return "enddetail";
    }

    //완료 페이지에서 주문 완료 눌렀을 시
    @GetMapping("mayo/done/{storeid}/{reservationid}")
    public String reject(@PathVariable String storeid, @PathVariable String reservationid, Model model) throws ExecutionException, InterruptedException {

        reservationService.ReservationDone(reservationid);

        return "redirect:/mayo/processing/" + storeid;
    }
}
