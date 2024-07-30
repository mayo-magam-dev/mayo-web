package com.example.mayoweb.reservation.controller;

import com.example.mayoweb.carts.domain.dto.response.ReadCartsResponse;
import com.example.mayoweb.carts.service.CartService;
import com.example.mayoweb.items.domain.response.ReadFirstItemResponse;
import com.example.mayoweb.items.domain.response.ReadItemResponse;
import com.example.mayoweb.items.service.ItemsService;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationDetailResponse;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationListResponse;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationResponse;
import com.example.mayoweb.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservationRestController {

    private final ReservationService reservationService;
    private final ItemsService itemsService;
    private final CartService cartService;

    @GetMapping("/reservation")
    public ResponseEntity<ReadReservationResponse> getReservationById(@RequestParam String reservationId) {
        return ResponseEntity.ok(reservationService.getReservationById(reservationId));
    }

    @GetMapping("/reservation-new")
    public ResponseEntity<List<ReadReservationListResponse>> getNewReservationsByStoreId(String storeId) {

        List<ReadReservationResponse> reservationResponseList = reservationService.getNewByStoreId(storeId);
        List<ReadFirstItemResponse> firstItemResponse = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
        List<ReadReservationListResponse> responseList = new ArrayList<>();

        for(int i=0; i<reservationResponseList.size(); i++) {
            ReadReservationListResponse response = ReadReservationListResponse.builder()
                    .firstItemName(firstItemResponse.get(i).itemName())
                    .itemQuantity(firstItemResponse.get(i).itemQuantity())
                    .createdAt(reservationResponseList.get(i).createdAt())
                    .pickupTime(reservationResponseList.get(i).pickupTime())
                    .build();

            responseList.add(response);
        }

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/reservation-proceed")
    public ResponseEntity<List<ReadReservationListResponse>> getProceedingReservationsByStoreId(String storeId) {
        List<ReadReservationResponse> reservationResponseList = reservationService.getProcessingByStoreId(storeId);
        List<ReadFirstItemResponse> firstItemResponse = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
        List<ReadReservationListResponse> responseList = new ArrayList<>();

        for(int i=0; i<reservationResponseList.size(); i++) {
            ReadReservationListResponse response = ReadReservationListResponse.builder()
                    .firstItemName(firstItemResponse.get(i).itemName())
                    .itemQuantity(firstItemResponse.get(i).itemQuantity())
                    .createdAt(reservationResponseList.get(i).createdAt())
                    .pickupTime(reservationResponseList.get(i).pickupTime())
                    .build();

            responseList.add(response);
        }

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/reservation-done")
    public ResponseEntity<List<ReadReservationListResponse>> getDoneReservationsByStoreId(String storeId) {
        List<ReadReservationResponse> reservationResponseList = reservationService.getEndByStoreId(storeId);
        List<ReadFirstItemResponse> firstItemResponse = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
        List<ReadReservationListResponse> responseList = new ArrayList<>();

        for(int i=0; i<reservationResponseList.size(); i++) {
            ReadReservationListResponse response = ReadReservationListResponse.builder()
                    .firstItemName(firstItemResponse.get(i).itemName())
                    .itemQuantity(firstItemResponse.get(i).itemQuantity())
                    .createdAt(reservationResponseList.get(i).createdAt())
                    .pickupTime(reservationResponseList.get(i).pickupTime())
                    .build();

            responseList.add(response);
        }

        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/reservation-accept")
    public ResponseEntity<Void> updateAccept(@RequestParam String reservationId) {

        reservationService.reservationAccept(reservationId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reservation-fail")
    public ResponseEntity<Void> updateFail(@RequestParam String reservationId) {

        reservationService.reservationFail(reservationId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reservation-done")
    public ResponseEntity<Void> updateDone(@RequestParam String reservationId) {

        reservationService.reservationDone(reservationId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservation-detail")
    public ResponseEntity<ReadReservationDetailResponse> getItemByReservationId(@RequestParam String reservationId) {
        ReadReservationResponse reservation = reservationService.getReservationById(reservationId);
        List<ReadCartsResponse> carts = cartService.getCartsByReservation(reservation.reservationId());
        List<String> itemName = new ArrayList<>();
        List<Integer> itemCount = new ArrayList<>();
        List<Double> subTotal = new ArrayList<>();

        for(ReadCartsResponse cart : carts) {
            ReadItemResponse item = itemsService.getItemByCartId(cart.cartId());
            itemName.add(item.itemName());
            itemCount.add(cart.itemCount());
            subTotal.add(cart.subtotal());
        }

        return ResponseEntity.ok(ReadReservationDetailResponse.builder()
                .itemName(itemName)
                .itemCount(itemCount)
                .subTotal(subTotal)
                .totalQuantity(carts.size())
                .reservationId(reservation.reservationId())
                .request(reservation.reservationRequest())
                .createdAt(reservation.createdAt())
                .pickupTime(reservation.pickupTime())
                .totalPrice(reservation.totalPrice())
                .reservationIsPlastic(reservation.reservationIsPlastics())
                .build());
    }

}
