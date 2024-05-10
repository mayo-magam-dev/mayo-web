package com.example.mayoweb.controller;

import com.example.mayoweb.carts.CartService;
import com.example.mayoweb.carts.CartsDto;
import com.example.mayoweb.items.ItemsDto;
import com.example.mayoweb.items.ItemsService;
import com.example.mayoweb.reservation.ReservationService;
import com.example.mayoweb.reservation.ReservationsDto;
import com.example.mayoweb.store.StoresDto;
import com.example.mayoweb.store.StoresService;
import com.example.mayoweb.user.UsersDto;
import com.example.mayoweb.user.UsersService;
import com.example.mayoweb.response.EndDetailResponse;
import com.example.mayoweb.response.EndResponse;
import com.google.cloud.firestore.DocumentReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/mayo/end")
public class EndController {

    private final ReservationService reservationService;

    private final UsersService usersService;

    private final CartService cartService;

    private final ItemsService itemsService;

    private final StoresService storesService;

    //완료 페이지 기본화면
    @GetMapping("/{storeid}")
    public ResponseEntity<EndResponse> home(@PathVariable String storeid) throws ExecutionException, InterruptedException {
        List<ReservationsDto> endReservationList = reservationService.getEndByStoreRef(storeid);
        List<String> endItem = itemsService.getFirstItemNamesFromReservations(endReservationList);
        StoresDto storesDto = storesService.getStoreById(storeid);

        EndResponse response = new EndResponse(
                storeid,
                endReservationList.size(),
                endItem,
                endReservationList,
                storesDto
        );
        return ResponseEntity.ok(response);
    }


    //완료 페이지 예약 눌렀을 시
    @GetMapping("/{storeid}/{reservationid}")
    public ResponseEntity<EndDetailResponse> completedetail(@PathVariable String storeid, @PathVariable String reservationid) throws Exception {
        ReservationsDto reservation = reservationService.getReservationById(reservationid); //파라미터로 받아온 예약id값으로 reservation dto를 가져옵니다.->created_at, pickup_time, total_price, reservation_request, reservation_is_plastic
        List<ReservationsDto> endReservationList = reservationService.getEndByStoreRef(storeid); //신규 예약 리스트 -> created_at, pickup_time
        List<String> endItem = itemsService.getFirstItemNamesFromReservations(endReservationList); //신규 예약 아이템 이름 리스트
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

        EndDetailResponse response = new EndDetailResponse(
                storeid,
                endReservationList.size(),
                endItem,
                endReservationList,
                storesDto,
                reservation,
                itemcount,
                user,
                items,
                carts
        );

        return ResponseEntity.ok(response);
    }
}
