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

@Controller
@Slf4j
public class ProcessingController {

    @Autowired
    ReservationService reservationService;

    @Autowired
    ItemsService itemsService;

    @Autowired
    UsersService usersService;

    @Autowired
    CartService cartService;

    @Autowired
    StoresService storesService;


    //처리중 기본화면
    @GetMapping("mayo/processing/{storeid}")
    public String home(@PathVariable String storeid, Model model) throws ExecutionException, InterruptedException {

        List<ReservationsDto> NewReservationList = reservationService.getNewByStoreRef(storeid);

        List<ReservationsDto> ProcessingReservationList = reservationService.getProcessingByStoreRef(storeid);

        List<String> newItem = itemsService.getFirstItemNamesFromReservations(NewReservationList);

        List<String> processingItem = itemsService.getFirstItemNamesFromReservations(ProcessingReservationList);

        StoresDto storesDto = storesService.getStoreById(storeid);

        model.addAttribute("storeid", storeid);
        model.addAttribute("newSize", NewReservationList.size());
        model.addAttribute("processingSize", ProcessingReservationList.size());
        model.addAttribute("newItem", newItem);
        model.addAttribute("processingItem", processingItem);
        model.addAttribute("newReservation", NewReservationList);
        model.addAttribute("processing", ProcessingReservationList);
        model.addAttribute("store", storesDto);

        return "processing";
    }

    //처리 중(신규 id 눌렀을 시)
    @GetMapping("mayo/processingnew/{storeid}/{reservationid}")
    public String newprocessing(@PathVariable String storeid, @PathVariable String reservationid, Model model) throws Exception {

        //reservation 엔티티 가져옴
        ReservationsDto reservationEntity = reservationService.getReservationById(reservationid);

        List<ReservationsDto> NewReservationList = reservationService.getNewByStoreRef(storeid);

        List<ReservationsDto> ProcessingReservationList = reservationService.getProcessingByStoreRef(storeid);

        List<String> newItem = itemsService.getFirstItemNamesFromReservations(NewReservationList);

        List<String> processingItem = itemsService.getFirstItemNamesFromReservations(ProcessingReservationList);

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

        model.addAttribute("storeid", storeid);
        model.addAttribute("reservation", reservationEntity);
        model.addAttribute("user", usersEntity);
        model.addAttribute("carts", cartsEntity);
        model.addAttribute("items", itemsDto);
        model.addAttribute("itemcount", itemcount);
        model.addAttribute("newReservation", NewReservationList);
        model.addAttribute("processing", ProcessingReservationList);
        model.addAttribute("newSize", NewReservationList.size());
        model.addAttribute("processingSize", ProcessingReservationList.size());
        model.addAttribute("newItem", newItem);
        model.addAttribute("processingItem", processingItem);
        model.addAttribute("reservationid", reservationid);
        model.addAttribute("store", storesDto);
        //모델에 데이터 등록하기
        //뷰 페이지 반환하기
        return "processingnew";
    }

    //처리중(진행 중 눌렀을 시)
    @GetMapping("mayo/processing/{storeid}/{reservationid}")
    public String proceeding(@PathVariable String storeid, @PathVariable String reservationid, Model model) throws Exception {

        ReservationsDto reservationEntity = reservationService.getReservationById(reservationid);

        List<ReservationsDto> NewReservationList = reservationService.getNewByStoreRef(storeid);

        List<ReservationsDto> ProcessingReservationList = reservationService.getProcessingByStoreRef(storeid);

        List<String> newItem = itemsService.getFirstItemNamesFromReservations(NewReservationList);

        List<String> processingItem = itemsService.getFirstItemNamesFromReservations(ProcessingReservationList);

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

        model.addAttribute("storeid", storeid);
        model.addAttribute("reservation", reservationEntity);
        model.addAttribute("user", usersEntity);
        model.addAttribute("carts", cartsEntity);
        model.addAttribute("items", itemsDto);
        model.addAttribute("itemcount", itemcount);
        model.addAttribute("newReservation", NewReservationList);
        model.addAttribute("processing", ProcessingReservationList);
        model.addAttribute("newSize", NewReservationList.size());
        model.addAttribute("processingSize", ProcessingReservationList.size());
        model.addAttribute("newItem", newItem);
        model.addAttribute("processingItem", processingItem);
        model.addAttribute("reservationid", reservationid);
        model.addAttribute("store", storesDto);
        //모델에 데이터 등록하기
        //뷰 페이지 반환하기
        return "proceeding";
    }

    //신규 주문에서 수락 눌렀을 시
    @GetMapping("mayo/accept/{storeid}/{reservationid}")
    public String accept(@PathVariable String storeid, @PathVariable String reservationid, Model model) throws ExecutionException, InterruptedException {

        reservationService.ReservationAccept(reservationid);

        return "redirect:/mayo/processing/" + storeid;
    }

    //신규 주문에서 거절 눌렀을 시
    @GetMapping("mayo/reject/{storeid}/{reservationid}")
    public String reject(@PathVariable String storeid, @PathVariable String reservationid, Model model) throws ExecutionException, InterruptedException {

        reservationService.ReservationFail(reservationid);

        return "redirect:/mayo/processing/" + storeid;
    }
}
