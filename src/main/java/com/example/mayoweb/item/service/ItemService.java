package com.example.mayoweb.item.service;

import com.example.mayoweb.cart.domain.CartEntity;
import com.example.mayoweb.cart.repository.CartAdapter;
import com.example.mayoweb.commons.annotation.FirestoreTransactional;
import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.item.domain.ItemEntity;
import com.example.mayoweb.item.domain.request.CreateItemRequest;
import com.example.mayoweb.item.domain.request.UpdateItemRequest;
import com.example.mayoweb.item.domain.response.ReadFirstItemResponse;
import com.example.mayoweb.item.domain.response.ReadItemResponse;
import com.example.mayoweb.item.repository.ItemAdapter;
import com.example.mayoweb.reservation.domain.ReservationEntity;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationResponse;
import com.example.mayoweb.reservation.repository.ReservationAdapter;
import com.example.mayoweb.storage.service.StorageService;
import com.example.mayoweb.user.repository.UserAdapter;
import com.google.cloud.firestore.DocumentReference;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FirestoreTransactional
public class ItemService {

    private final ItemAdapter itemAdapter;
    private final CartAdapter cartsAdapter;
    private final UserAdapter userAdapter;
    private final ReservationAdapter reservationAdapter;
    private final StorageService storageService;

    public List<ReadFirstItemResponse> getFirstItemNamesFromReservations(List<ReadReservationResponse> readReservationResponseList) {

        List<ReservationEntity> reservationList = new ArrayList<>();

        for(ReadReservationResponse reservation : readReservationResponseList) {
            ReservationEntity reservationEntity = reservationAdapter.findByReservationId(reservation.id()).orElse(null);
            reservationList.add(reservationEntity);
        }

        List<DocumentReference> carts = cartsAdapter.getFirstCartsByReservations(reservationList);

        return itemAdapter.getFirstItemNamesFromCarts(carts);
    }

    public List<ReadItemResponse> getItemsByUserId(String userId) {

        DocumentReference storeRef = userAdapter.getStoreRefByUserId(userId).
                orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                ));

        String storeId = storeRef.getId();

        return itemAdapter.getItemsByStoreId(storeId).stream().map(ReadItemResponse::fromEntity).toList();
    }

    public ReadItemResponse getItemById(String itemId) {

        return ReadItemResponse.fromEntity(itemAdapter.getItemById(itemId)
                .orElseThrow(() -> new ApplicationException(
                    ErrorStatus.toErrorStatus("아이템을 찾을 수 없습니다.", 400, LocalDateTime.now())
                )));
    }

    public void save(CreateItemRequest item, String userId){

        DocumentReference storeRef = userAdapter.getStoreRefByUserId(userId).
                orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                ));

        itemAdapter.saveItem(item.createEntity(storeRef));
    }

    public void save(CreateItemRequest request, String userId, MultipartFile file) {

        DocumentReference storeRef = userAdapter.getStoreRefByUserId(userId).
                orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                ));

        String imageUrl = storageService.uploadFirebaseBucket(file, request.itemName());
        CreateItemRequest createItemRequest = CreateItemRequest.updateItemURL(request, imageUrl);

        itemAdapter.saveItem(createItemRequest.createEntity(storeRef));
    }

    public void updateItem(UpdateItemRequest item) {
        itemAdapter.updateItem(item.updateEntity());
    }

    public void updateItem(UpdateItemRequest request, MultipartFile file) {

        ItemEntity itemEntity = itemAdapter.getItemById(request.itemId())
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 아이템이 없습니다.", 404, LocalDateTime.now())
                ));

        storageService.deleteFirebaseBucket(itemEntity.getItemImage());
        String imageUrl = storageService.uploadFirebaseBucket(file, request.itemName());
        UpdateItemRequest updateItemRequest = UpdateItemRequest.updateItemURL(request, imageUrl);

        itemAdapter.updateItem(updateItemRequest.updateEntity());
    }

    public void deleteItem(String itemId) {
        itemAdapter.deleteItem(itemId);
    }

    public void updateItemQuantityPlus(String itemId) {
        itemAdapter.updateItemQuantityPlus(itemId);
    }

    public void updateItemQuantityMinus(String itemId) {
        itemAdapter.updateItemQuantityMinus(itemId);
    }

    public void updateItemOn(String itemId) {
        itemAdapter.updateItemOn(itemId);
    }

    public void updateItemOff(String itemId) {
        itemAdapter.updateItemOff(itemId);
    }
}
