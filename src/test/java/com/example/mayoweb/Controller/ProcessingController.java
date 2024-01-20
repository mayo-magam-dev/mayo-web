package com.example.mayoweb.Controller;

import com.example.mayoweb.Adapter.CartsAdapter;
import com.example.mayoweb.Adapter.ItemsAdapter;
import com.example.mayoweb.Adapter.ReservationsAdapter;
import com.example.mayoweb.Adapter.UsersAdapter;
import com.example.mayoweb.Dto.*;
import com.example.mayoweb.Entity.CartsEntity;
import com.example.mayoweb.Entity.UsersEntity;
import com.example.mayoweb.Service.*;
import com.google.cloud.firestore.DocumentReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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


    @GetMapping("mayo/processing/{store_ref}")
    public String home(@PathVariable String store_ref, Model model) throws ExecutionException, InterruptedException {

        List<ReservationsDto> NewReservationList = reservationService.getNewByStoreRef(store_ref);

        List<ReservationsDto> ProcessingReservationList = reservationService.getProcessingByStoreRef(store_ref);

        List<String> newItem = itemsService.getFirstItemNamesFromReservations(NewReservationList);

        List<String> processingItem = itemsService.getFirstItemNamesFromReservations(ProcessingReservationList);

        StoresDto storesDto = storesService.getStoreById(store_ref);

        model.addAttribute("store_ref", store_ref);
        model.addAttribute("newSize", NewReservationList.size());
        model.addAttribute("processingSize", ProcessingReservationList.size());
        model.addAttribute("newItem", newItem);
        model.addAttribute("processingItem", processingItem);
        model.addAttribute("newReservation", NewReservationList);
        model.addAttribute("processing", ProcessingReservationList);
        model.addAttribute("store", storesDto);

        return "processing";
    }

    @GetMapping("mayo/processingnew/{store_ref}/{reservationid}")
    public String newprocessing(@PathVariable String store_ref, @PathVariable String reservationid, Model model) throws Exception {

        //reservation 엔티티 가져옴
        ReservationsDto reservationEntity = reservationService.getReservationById(reservationid);

        List<ReservationsDto> NewReservationList = reservationService.getNewByStoreRef(store_ref);

        List<ReservationsDto> ProcessingReservationList = reservationService.getProcessingByStoreRef(store_ref);

        List<String> newItem = itemsService.getFirstItemNamesFromReservations(NewReservationList);

        List<String> processingItem = itemsService.getFirstItemNamesFromReservations(ProcessingReservationList);

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

        //reservation id 조회해 데이터 가져오기
        log.info("newItem = " + newItem.toString());
        log.info("processingItem = " + processingItem.toString());
        log.info(reservationEntity.toString());
        log.info("new" + NewReservationList.toString());
        log.info("processing" + ProcessingReservationList.toString());
        log.info(String.valueOf(NewReservationList.size()));
        log.info(String.valueOf(ProcessingReservationList.size()));
        log.info(usersEntity.toString());
        log.info(cartsEntity.toString());
        log.info(itemsDto.toString());
        System.out.println(itemcount);
        model.addAttribute("store_ref", store_ref);
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

    @GetMapping("mayo/processing/{store_ref}/{reservationid}")
    public String proceeding(@PathVariable String store_ref, @PathVariable String reservationid, Model model) throws Exception {

        ReservationsDto reservationEntity = reservationService.getReservationById(reservationid);

        List<ReservationsDto> NewReservationList = reservationService.getNewByStoreRef(store_ref);

        List<ReservationsDto> ProcessingReservationList = reservationService.getProcessingByStoreRef(store_ref);

        List<String> newItem = itemsService.getFirstItemNamesFromReservations(NewReservationList);

        List<String> processingItem = itemsService.getFirstItemNamesFromReservations(ProcessingReservationList);

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

        model.addAttribute("store_ref", store_ref);
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

    @GetMapping("mayo/accept/{store_ref}/{reservationid}")
    public String accept(@PathVariable String store_ref, @PathVariable String reservationid, Model model) throws ExecutionException, InterruptedException {

        reservationService.ReservationAccept(reservationid);

        return "redirect:/mayo/processing/" + store_ref;
    }

    @GetMapping("mayo/reject/{store_ref}/{reservationid}")
    public String reject(@PathVariable String store_ref, @PathVariable String reservationid, Model model) throws ExecutionException, InterruptedException {

        reservationService.ReservationFail(reservationid);

        return "redirect:/mayo/processing/" + store_ref;
    }
}
