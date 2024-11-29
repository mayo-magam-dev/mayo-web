package com.example.mayoweb.reservation.service;

import com.example.mayoweb.carts.domain.dto.response.ReadCartsResponse;
import com.example.mayoweb.carts.service.CartService;
import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.fcm.service.FCMService;
import com.example.mayoweb.items.domain.response.ReadFirstItemResponse;
import com.example.mayoweb.items.domain.response.ReadItemResponse;
import com.example.mayoweb.items.service.ItemsService;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationDetailResponse;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationListResponse;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationResponse;
import com.example.mayoweb.reservation.repository.ReservationsAdapter;
import com.example.mayoweb.user.domain.dto.response.ReadUserResponse;
import com.example.mayoweb.user.service.UsersService;
import com.google.cloud.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationsAdapter reservationsAdapter;
    private final ItemsService itemsService;
    private final FCMService fcmService;
    private final UsersService userService;
    private final CartService cartService;

    public void reservationAccept(String id){
        reservationsAdapter.reservationProceeding(id);
        ReadReservationResponse dto = getReservationById(id);
        List<String> tokens = userService.getTokensByUserRef(dto.userRef());
        fcmService.sendAcceptMessage(tokens);

    }

    public void reservationFail(String id){
        reservationsAdapter.reservationFail(id);
        ReadReservationResponse dto = getReservationById(id);
        List<String> tokens = userService.getTokensByUserRef(dto.userRef());
        fcmService.sendRejectMessage(tokens);
    }

    public void reservationDone(String id){
        reservationsAdapter.reservationDone(id);
    }

    public List<ReadReservationListResponse> getNewByStoreId(String storeId){
        List<ReadReservationResponse> reservationResponseList = reservationsAdapter.getNewByStoreRef(storeId).stream().map(ReadReservationResponse::fromEntity).toList();

        List<ReadFirstItemResponse> firstItemResponse = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
        List<ReadReservationListResponse> responseList = new ArrayList<>();

        for(int i=0; i<reservationResponseList.size(); i++) {
            ReadReservationListResponse response = ReadReservationListResponse.builder()
                    .reservationId(reservationResponseList.get(i).id())
                    .firstItemName(firstItemResponse.get(i).itemName())
                    .itemQuantity(firstItemResponse.get(i).itemQuantity())
                    .createdAt(reservationResponseList.get(i).createdAt())
                    .pickupTime(reservationResponseList.get(i).pickupTime())
                    .reservationState(reservationResponseList.get(i).reservationState())
                    .build();

            responseList.add(response);
        }

        return responseList;
    }

    public List<ReadReservationListResponse> getProcessingByStoreId(String storeId) {

        List<ReadReservationResponse> reservationResponseList = reservationsAdapter.getProcessingByStoreRef(storeId).stream().map(ReadReservationResponse::fromEntity).toList();

        List<ReadFirstItemResponse> firstItemResponse = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
        List<ReadReservationListResponse> responseList = new ArrayList<>();

        for(int i=0; i<reservationResponseList.size(); i++) {
            ReadReservationListResponse response = ReadReservationListResponse.builder()
                    .reservationId(reservationResponseList.get(i).id())
                    .firstItemName(firstItemResponse.get(i).itemName())
                    .itemQuantity(firstItemResponse.get(i).itemQuantity())
                    .createdAt(reservationResponseList.get(i).createdAt())
                    .pickupTime(reservationResponseList.get(i).pickupTime())
                    .reservationState(reservationResponseList.get(i).reservationState())
                    .build();

            responseList.add(response);
        }

        return responseList;

    }

    public List<ReadReservationListResponse> getEndByStoreIdAndTimestamp(String storeId, Timestamp timestamp) {

        List<ReadReservationResponse> reservationResponseList =  reservationsAdapter.getEndByStoreRefAndTimestamp(storeId, timestamp).stream().map(ReadReservationResponse::fromEntity).toList();
        List<ReadFirstItemResponse> firstItemResponse = itemsService.getFirstItemNamesFromReservations(reservationResponseList);
        List<ReadReservationListResponse> responseList = new ArrayList<>();

        for(int i=0; i<reservationResponseList.size(); i++) {
            ReadReservationListResponse response = ReadReservationListResponse.builder()
                    .reservationId(reservationResponseList.get(i).id())
                    .firstItemName(firstItemResponse.get(i).itemName())
                    .itemQuantity(firstItemResponse.get(i).itemQuantity())
                    .createdAt(reservationResponseList.get(i).createdAt())
                    .pickupTime(reservationResponseList.get(i).pickupTime())
                    .reservationState(reservationResponseList.get(i).reservationState())
                    .build();

            responseList.add(response);
        }

        return responseList;
    }

    public ReadReservationResponse getReservationById(String reservationId) {
        return ReadReservationResponse.fromEntity(reservationsAdapter.findByReservationId(reservationId)
                .orElseThrow( () -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 예약이 없습니다.", 404, LocalDateTime.now())
                )));
    }

    public void sendFCMNewReservation(String storeId, String userId) {
        reservationsAdapter.sendNewReservationFCM(storeId, userId);
    }

    public void reservationFailByStoreId(String storeId) {
        List<ReadReservationResponse> reservationResponseList = reservationsAdapter.getNewByStoreRef(storeId).stream().map(ReadReservationResponse::fromEntity).toList();

        for(ReadReservationResponse response : reservationResponseList) {
            reservationFail(response.id());
        }
    }

    public ReadReservationDetailResponse getReservationDetailById(String reservationId) {

        ReadReservationResponse reservation = ReadReservationResponse.fromEntity(reservationsAdapter.findByReservationId(reservationId)
                .orElseThrow( () -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 예약이 없습니다.", 404, LocalDateTime.now())
                )));

        List<ReadCartsResponse> carts = cartService.getCartsByReservation(reservationId);
        ReadUserResponse user = userService.getUserById(reservation.userRef());
        List<String> itemName = new ArrayList<>();
        List<Integer> itemCount = new ArrayList<>();
        List<Double> subTotal = new ArrayList<>();
        Integer totalQuantity = 0;

        for(ReadCartsResponse cart : carts) {
            ReadItemResponse item = itemsService.getItemByCartId(cart.cartId());
            itemName.add(item.itemName());
            itemCount.add(cart.itemCount());
            subTotal.add(cart.subtotal());
            totalQuantity += cart.itemCount();
        }

        return ReadReservationDetailResponse.builder()
                    .itemName(itemName)
                    .itemCount(itemCount)
                    .subTotal(subTotal)
                    .totalQuantity(totalQuantity)
                    .reservationId(reservationId)
                    .request(reservation.reservationRequest())
                    .createdAt(reservation.createdAt())
                    .pickupTime(reservation.pickupTime())
                    .totalPrice(reservation.totalPrice())
                    .reservationIsPlastic(reservation.reservationIsPlastics())
                    .userNickName(user.displayName())
                    .reservationState(reservation.reservationState())
                    .menuTypeCount(carts.size())
                    .build();
    }

//    public CompletableFuture<List<ReadReservationResponse>> getProceedingReservationsByStoreId(String storeId) {
//        return reservationsAdapter.getProceedingByStoreIdAsync(storeId).thenApply(reservationEntities ->
//                reservationEntities.stream().map(ReadReservationResponse::fromEntity).collect(Collectors.toList())
//        );
//    }
//
//    public List<ReadReservationResponse> getEndByStoreId(String storeId) {
//        return reservationsAdapter.getEndByStoreRef(storeId).stream().map(ReadReservationResponse::fromEntity).toList();


//    public CompletableFuture<List<ReadReservationResponse>> getEndReservationsByStoreId(String storeId) {
//        return reservationsAdapter.getEndByStoreIdAsync(storeId).thenApply(reservationEntities ->
//                reservationEntities.stream().map(ReadReservationResponse::fromEntity).collect(Collectors.toList())
//        );
//    }
//
//    public Slice<ReadReservationResponse> getReservationsByStoreIdAndTimeSlice(String storeId, Timestamp timestamp, int page, int size) throws ExecutionException, InterruptedException {
//        Pageable pageable = PageRequest.of(page, size);
//        return reservationsAdapter.findEndReservationsByStoreIdAndTime(storeId, pageable, timestamp).get().map(ReadReservationResponse::fromEntity);
//    }

    //    public CompletableFuture<List<ReadReservationResponse>> getNewReservationsByStoreIdSse(String clientId, String storeId) throws ExecutionException, InterruptedException {
//        return reservationsAdapter.getNewByStoreIdSse(clientId, storeId).thenApply(reservationEntities ->
//                reservationEntities.stream().map(ReadReservationResponse::fromEntity).toList()
//        );
//    }

    //    public Slice<ReadReservationResponse> getReservationsByStoreIdSlice(String storeId, int page, int size) throws ExecutionException, InterruptedException {
//        Pageable pageable = PageRequest.of(page, size);
//        return reservationsAdapter.findEndReservationsByStoreId(storeId, pageable).get().map(ReadReservationResponse::fromEntity);
//    }

//    public SseEmitter getNewReservationsByStoreIdSse(String clientId, String storeId) {
//        return reservationsAdapter.streamNewReservations(clientId, storeId);
//    }

//    public CompletableFuture<List<ReadReservationResponse>> getNewReservationsByStoreId(String storeId) {
//        return reservationsAdapter.getNewByStoreIdAsync(storeId).thenApply(reservationEntities ->
//                reservationEntities.stream().map(ReadReservationResponse::fromEntity).toList()
//        );
//    }

}
