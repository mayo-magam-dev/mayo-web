package com.example.mayoweb.controller;

import com.example.mayoweb.carts.CartService;
import com.example.mayoweb.carts.CartsDto;
import com.example.mayoweb.items.ItemsDto;
import com.example.mayoweb.items.ItemsService;
import com.example.mayoweb.reservation.ReservationService;
import com.example.mayoweb.reservation.ReservationsDto;
import com.example.mayoweb.store.StoresDto;
import com.example.mayoweb.store.StoresService;
import com.example.mayoweb.User.UsersDto;
import com.example.mayoweb.User.UsersService;
import com.example.mayoweb.response.ProcessingDetailResponse;
import com.example.mayoweb.response.ProcessingResponse;
import com.google.cloud.firestore.DocumentReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/mayo/processing")
public class ProcessingController {


    private final ReservationService reservationService;

    private final ItemsService itemsService;

    private final UsersService usersService;

    private final CartService cartService;

    private final StoresService storesService;


    @GetMapping("/{storeid}")
    public ResponseEntity<ProcessingResponse> home(@PathVariable String storeid) throws ExecutionException, InterruptedException {
        List<ReservationsDto> newReservationList = reservationService.getNewByStoreRef(storeid);
        List<ReservationsDto> processingReservationList = reservationService.getProcessingByStoreRef(storeid);
        List<String> newItem = itemsService.getFirstItemNamesFromReservations(newReservationList);
        List<String> processingItem = itemsService.getFirstItemNamesFromReservations(processingReservationList);
        StoresDto storesDto = storesService.getStoreById(storeid);

        ProcessingResponse response = new ProcessingResponse(
                storeid,
                newReservationList.size(),
                processingReservationList.size(),
                newItem,
                processingItem,
                newReservationList,
                processingReservationList,
                storesDto
        );
        return ResponseEntity.ok(response);
    }

    //처리 중(주문 눌렀을 시)
    @GetMapping("/{storeid}/{reservationid}")
    public ResponseEntity<ProcessingDetailResponse> newprocessing(@PathVariable String storeid, @PathVariable String reservationid) throws Exception {
        ReservationsDto reservation = reservationService.getReservationById(reservationid); //파라미터로 받아온 예약id값으로 reservation dto를 가져옵니다.->created_at, pickup_time, total_price, reservation_request, reservation_is_plastic
        List<ReservationsDto> newReservationList = reservationService.getNewByStoreRef(storeid); //신규 예약 리스트 -> created_at, pickup_time
        List<ReservationsDto> processingReservationList = reservationService.getProcessingByStoreRef(storeid); //진행 예약 리스트
        List<String> newItem = itemsService.getFirstItemNamesFromReservations(newReservationList); //신규 예약 아이템 이름 리스트
        List<String> processingItem = itemsService.getFirstItemNamesFromReservations(processingReservationList); //진행 예약 아이템 이름 리스트
        DocumentReference userRef = reservation.getUser_ref();
        UsersDto user = usersService.getUserByDocRef(userRef);//유저 정보 (display_name)
        List<DocumentReference> cartRef = reservation.getCart_ref();
        List<CartsDto> carts = cartService.getCartsByDocRef(cartRef); //카트 정보 -> itemCount, subtotal
        StoresDto storesDto = storesService.getStoreById(storeid);

        int itemcount = 0;
        List<ItemsDto> items = new ArrayList<>(); //-> item_name(눌렀을 때 상세 정보 나오는 아이템 리스트)
        for (CartsDto cart : carts) {
            DocumentReference itemRef = cart.getItem();
            ItemsDto item = itemsService.getItemByDocRef(itemRef);
            items.add(item);
            itemcount += 1;
        }

        ProcessingDetailResponse response = new ProcessingDetailResponse(
                storeid,
                newReservationList.size(),
                processingReservationList.size(),
                newItem,
                processingItem,
                newReservationList,
                processingReservationList,
                storesDto,
                reservation,
                itemcount,
                user,
                items,
                carts
                );

        return ResponseEntity.ok(response);
    }

    //신규 주문에서 수락 눌렀을 시
    @PostMapping("accept/{storeid}/{reservationid}")
    public ResponseEntity<String> accept(@RequestParam String storeid, @RequestParam String reservationid) throws ExecutionException, InterruptedException {

        reservationService.ReservationAccept(reservationid);

        return ResponseEntity.ok("Reservation accepted for store " + storeid);
    }

    //신규 주문에서 거절 눌렀을 시
    @PostMapping("reject/{storeid}/{reservationid}")
    public ResponseEntity<String> reject(@RequestParam String storeid, @RequestParam String reservationid) throws ExecutionException, InterruptedException {

        reservationService.ReservationFail(reservationid);

        return ResponseEntity.ok("Reservation rejected for store " + storeid);
    }

    //완료 페이지에서 주문 완료 눌렀을 시
    @PostMapping("done/{storeid}/{reservationid}")
    public ResponseEntity<String> done(@RequestParam String storeid, @RequestParam String reservationid) throws ExecutionException, InterruptedException {

        reservationService.ReservationDone(reservationid);

        return ResponseEntity.ok("Reservation end for store " + storeid);
    }
}
