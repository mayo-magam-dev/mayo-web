package com.example.mayoweb.reservation.service;

import com.example.mayoweb.cart.domain.CartEntity;
import com.example.mayoweb.cart.repository.CartAdapter;
import com.example.mayoweb.commons.annotation.FirestoreTransactional;
import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.fcm.service.FCMService;
import com.example.mayoweb.item.domain.ItemEntity;
import com.example.mayoweb.item.domain.response.ReadFirstItemResponse;
import com.example.mayoweb.item.repository.ItemAdapter;
import com.example.mayoweb.reservation.domain.ReservationEntity;
import com.example.mayoweb.reservation.domain.dto.response.*;
import com.example.mayoweb.reservation.domain.type.ReservationState;
import com.example.mayoweb.reservation.repository.ReservationAdapter;
import com.example.mayoweb.store.domain.StoreEntity;
import com.example.mayoweb.store.repository.StoreAdapter;
import com.example.mayoweb.user.domain.UserEntity;
import com.example.mayoweb.user.repository.UserAdapter;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FirestoreTransactional
public class ReservationService {

    private final ReservationAdapter reservationAdapter;
    private final ItemAdapter itemAdapter;
    private final FCMService fcmService;
    private final CartAdapter cartAdapter;
    private final UserAdapter userAdapter;
    private final StoreAdapter storeAdapter;

    public void reservationAccept(String id){
        reservationAdapter.reservationProceeding(id);
        ReadReservationResponse dto = getReservationById(id);
        List<String> tokens = userAdapter.getFCMTokenByUserRef(dto.userRef());
        fcmService.sendAcceptMessage(tokens);

    }

    public void reservationFail(String id){
        reservationAdapter.reservationFail(id);
        ReadReservationResponse dto = getReservationById(id);
        List<String> tokens = userAdapter.getFCMTokenByUserRef(dto.userRef());
        fcmService.sendRejectMessage(tokens);
    }

    public void reservationDone(String id){
        reservationAdapter.reservationDone(id);
    }

    public List<ReadReservationListResponse> getNewByUserId(String userId){

        DocumentReference storeRef = userAdapter.getStoreRefByUserId(userId).
                orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                ));

        String storeId = storeRef.getId();

        List<ReservationEntity> reservationList = reservationAdapter.getNewByStoreId(storeId).stream().toList();
        List<DocumentReference> carts = cartAdapter.getFirstCartsByReservations(reservationList);
        List<ReadFirstItemResponse> firstItemResponse = itemAdapter.getFirstItemNamesFromCarts(carts);

        List<ReadReservationListResponse> responseList = new ArrayList<>();

        for(int i=0; i<reservationList.size(); i++) {

            log.info("createdAt : {}, itemName : {}", reservationList.get(i).getCreatedAt(), firstItemResponse.get(i).itemName());

            ReadReservationListResponse response = ReadReservationListResponse.builder()
                    .reservationId(reservationList.get(i).getId())
                    .firstItemName(firstItemResponse.get(i).itemName())
                    .itemQuantity(firstItemResponse.get(i).itemQuantity())
                    .createdAt(reservationList.get(i).getCreatedAt())
                    .pickupTime(reservationList.get(i).getPickupTime())
                    .reservationState(reservationList.get(i).getReservationState())
                    .build();

            responseList.add(response);
        }

        return responseList;
    }

    public List<ReadReservationListResponse> getProcessingByStoreId(String userId) {

        DocumentReference storeRef = userAdapter.getStoreRefByUserId(userId).
                orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                ));

        String storeId = storeRef.getId();

        List<ReservationEntity> reservationList = reservationAdapter.getProcessingByStoreId(storeId).stream().toList();
        List<DocumentReference> carts = cartAdapter.getFirstCartsByReservations(reservationList);
        List<ReadFirstItemResponse> firstItemResponse = itemAdapter.getFirstItemNamesFromCarts(carts);

        List<ReadReservationListResponse> responseList = new ArrayList<>();

        for(int i=0; i<reservationList.size(); i++) {
            ReadReservationListResponse response = ReadReservationListResponse.builder()
                    .reservationId(reservationList.get(i).getId())
                    .firstItemName(firstItemResponse.get(i).itemName())
                    .itemQuantity(firstItemResponse.get(i).itemQuantity())
                    .createdAt(reservationList.get(i).getCreatedAt())
                    .pickupTime(reservationList.get(i).getPickupTime())
                    .reservationState(reservationList.get(i).getReservationState())
                    .build();

            responseList.add(response);
        }

        return responseList;

    }

    public List<ReadReservationListResponse> getEndByStoreIdAndTimestamp(String userId, Timestamp timestamp) {

        DocumentReference storeRef = userAdapter.getStoreRefByUserId(userId)
                .orElseThrow(() -> new ApplicationException(
                    ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                ));

        String storeId = storeRef.getId();

        List<ReservationEntity> reservationList =  reservationAdapter.getEndByStoreRefAndTimestamp(storeId, timestamp);
        List<DocumentReference> carts = cartAdapter.getFirstCartsByReservations(reservationList);
        List<ReadFirstItemResponse> firstItemResponse = itemAdapter.getFirstItemNamesFromCarts(carts);
        List<ReadReservationListResponse> responseList = new ArrayList<>();

        for(int i=0; i<reservationList.size(); i++) {
            ReadReservationListResponse response = ReadReservationListResponse.builder()
                    .reservationId(reservationList.get(i).getId())
                    .firstItemName(firstItemResponse.get(i).itemName())
                    .itemQuantity(firstItemResponse.get(i).itemQuantity())
                    .createdAt(reservationList.get(i).getCreatedAt())
                    .pickupTime(reservationList.get(i).getPickupTime())
                    .reservationState(reservationList.get(i).getReservationState())
                    .build();

            responseList.add(response);
        }

        return responseList;
    }

    public List<ReadAllReservationResponse> getReservationByYearAndMonth(int year, int month) {

        List<ReadAllReservationResponse> responseList = new ArrayList<>();
        List<ReservationEntity> reservationList = reservationAdapter.getReservationByYearAndMonth(year, month);

        for(ReservationEntity reservation : reservationList) {
            StoreEntity store = storeAdapter.findByStoreId(reservation.getStoreRef().getId())
                    .orElseThrow(() -> new ApplicationException(
                            ErrorStatus.toErrorStatus("해당 가게가 없습니다.", 404, LocalDateTime.now())
                    ));

            int itemCount = reservation.getCartRef().size();

            UserEntity user = userAdapter.findByUserId(reservation.getUserRef().getId())
                    .orElse(null);

            String userName = "유저 이름 없음";

            if(user != null) {
                userName = user.getName();
            }

            responseList.add(ReadAllReservationResponse.fromEntity(reservation, store.getStoreName(), itemCount, userName));
        }

        return responseList;
    }

    public TotalReservationResponse getReservationsByDate(String userId, LocalDate date) {

        DocumentReference storeRef = userAdapter.getStoreRefByUserId(userId)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                ));

        String storeId = storeRef.getId();

        List<ReservationEntity> reservationList = reservationAdapter.getReservationsByStoreRefAndDate(storeId, date);
        List<DocumentReference> carts = cartAdapter.getFirstCartsByReservations(reservationList);
        List<ReadFirstItemResponse> firstItemResponse = itemAdapter.getFirstItemNamesFromCarts(carts);
        List<ReadReservationListResponse> responseList = new ArrayList<>();

        int newCount = 0;
        int processingCount = 0;
        int endCount = 0;
        int failCount = 0;
        Double totalAmount = 0.0;

        for(int i=0; i<reservationList.size(); i++) {
            ReadReservationListResponse response = ReadReservationListResponse.builder()
                    .reservationId(reservationList.get(i).getId())
                    .firstItemName(firstItemResponse.get(i).itemName())
                    .itemQuantity(firstItemResponse.get(i).itemQuantity())
                    .createdAt(reservationList.get(i).getCreatedAt())
                    .pickupTime(reservationList.get(i).getPickupTime())
                    .reservationState(reservationList.get(i).getReservationState())
                    .build();

            int state = reservationList.get(i).getReservationState();

            if(state == ReservationState.NEW.getState()) {
                newCount++;
            } else if (state == ReservationState.PROCEEDING.getState()) {
                processingCount++;
            } else if (state == ReservationState.END.getState()) {
                endCount++;
                totalAmount += reservationList.get(i).getTotalPrice();
            } else {
                failCount++;
            }

            responseList.add(response);
        }

        return TotalReservationResponse.builder()
                .totalAmount(totalAmount)
                .newCount(newCount)
                .processingCount(processingCount)
                .endCount(endCount)
                .failCount(failCount)
                .reservationList(responseList)
                .build();
    }

    public ReadReservationResponse getReservationById(String reservationId) {
        return ReadReservationResponse.fromEntity(reservationAdapter.findByReservationId(reservationId)
                .orElseThrow( () -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 예약이 없습니다.", 404, LocalDateTime.now())
                )));
    }

    public void createListener(String userId) {

        DocumentReference storeRef = userAdapter.getStoreRefByUserId(userId).
                orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                ));

        reservationAdapter.createListener(storeRef.getId(), userId);
    }

    public void stopListener(String userId) {
        reservationAdapter.stopListener(userId);
    }

    public void reservationFailByStoreId(String userId) {

        DocumentReference storeRef = userAdapter.getStoreRefByUserId(userId).
                orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                ));

        List<ReadReservationResponse> reservationResponseList = reservationAdapter.getNewByStoreId(storeRef.getId()).stream().map(ReadReservationResponse::fromEntity).toList();

        for(ReadReservationResponse response : reservationResponseList) {
            reservationFail(response.id());
        }
    }

    public ReadReservationDetailResponse getReservationDetailById(String reservationId) {

        ReservationEntity reservation = reservationAdapter.findByReservationId(reservationId)
                .orElseThrow( () -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 예약이 없습니다.", 404, LocalDateTime.now())
                ));

        List<CartEntity> carts = cartAdapter.getCartsByDocRef(reservation.getCartRef()).stream().toList();

        UserEntity user = userAdapter.findByUserId(reservation.getUserRef().getId())
                .orElseThrow( () -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 유저가 없습니다.", 404, LocalDateTime.now())
                ));
      
        List<String> itemName = new ArrayList<>();
        List<Integer> itemCount = new ArrayList<>();
        List<Double> subTotal = new ArrayList<>();
        Integer totalQuantity = 0;

        for(CartEntity cart : carts) {
            DocumentReference itemRef = cart.getItem();
            ItemEntity item = itemAdapter.getItemByDocRef(itemRef)
                    .orElseThrow( () -> new ApplicationException(
                            ErrorStatus.toErrorStatus("해당하는 아이템이 없습니다.", 404, LocalDateTime.now())
                    ));
            itemName.add(item.getItemName());
            itemCount.add(cart.getItemCount());
            subTotal.add(cart.getSubtotal());
            totalQuantity += cart.getItemCount();
        }

        return ReadReservationDetailResponse.builder()
                    .itemName(itemName)
                    .itemCount(itemCount)
                    .subTotal(subTotal)
                    .totalQuantity(totalQuantity)
                    .reservationId(reservationId)
                    .request(reservation.getReservationRequest())
                    .createdAt(reservation.getCreatedAt())
                    .pickupTime(reservation.getPickupTime())
                    .totalPrice(reservation.getTotalPrice())
                    .reservationIsPlastic(reservation.getReservationIsPlastics())
                    .userNickName(user.getDisplayName())
                    .reservationState(reservation.getReservationState())
                    .menuTypeCount(carts.size())
                    .build();
    }
}
