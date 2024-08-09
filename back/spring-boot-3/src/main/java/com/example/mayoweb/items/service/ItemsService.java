package com.example.mayoweb.items.service;

import com.example.mayoweb.carts.domain.CartsEntity;
import com.example.mayoweb.carts.repository.CartsAdapter;
import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.items.domain.request.CreateItemRequest;
import com.example.mayoweb.items.domain.request.UpdateItemRequest;
import com.example.mayoweb.items.domain.response.ReadFirstItemResponse;
import com.example.mayoweb.items.domain.response.ReadItemResponse;
import com.example.mayoweb.items.repository.ItemsAdapter;
import com.example.mayoweb.reservation.domain.ReservationEntity;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationResponse;
import com.example.mayoweb.reservation.repository.ReservationsAdapter;
import com.example.mayoweb.store.repository.StoresAdapter;
import com.google.cloud.firestore.DocumentReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemsService {

    private final ItemsAdapter itemsAdapter;
    private final CartsAdapter cartsAdapter;
    private final StoresAdapter storesAdapter;
    private final ReservationsAdapter reservationsAdapter;

    public ReadItemResponse getItemByDocRef(DocumentReference doc){
        return ReadItemResponse.fromEntity(itemsAdapter.getItemByDocRef(doc).orElseThrow(() -> new ApplicationException(
                ErrorStatus.toErrorStatus("아이템을 찾을 수 없습니다. ", 404, LocalDateTime.now())
        )));
    }

    public ReadItemResponse getItemByCartId(String cartId) {

        CartsEntity cart = cartsAdapter.findCartById(cartId).orElseThrow(() -> new ApplicationException(
                ErrorStatus.toErrorStatus("카트를 찾을 수 없습니다.", 404, LocalDateTime.now()
        )));

        return ReadItemResponse.fromEntity(itemsAdapter.getItemByDocRef(cart.item).orElseThrow(() -> new ApplicationException(
                ErrorStatus.toErrorStatus("아이템을 찾을 수 없습니다.", 404, LocalDateTime.now())
        )));

    }

    public List<ReadFirstItemResponse> getFirstItemNamesFromReservations(List<ReadReservationResponse> readReservationResponseList) {

        List<ReservationEntity> reservationList = new ArrayList<>();

        for(ReadReservationResponse reservation : readReservationResponseList) {
            ReservationEntity reservationEntity = reservationsAdapter.findByReservationId(reservation.id()).orElse(null);
            reservationList.add(reservationEntity);
        }

        List<DocumentReference> carts = cartsAdapter.getFirstCartsByReservations(reservationList);

        return itemsAdapter.getFirstItemNamesFromCarts(carts);
    }

    public List<ReadItemResponse> getItemsByStoreId(String storeId) {
        return itemsAdapter.getItemsByStoreId(storeId).stream().map(ReadItemResponse::fromEntity).toList();
    }

    public ReadItemResponse getItemById(String itemId) {

        return ReadItemResponse.fromEntity(itemsAdapter.getItemById(itemId)
                .orElseThrow(() -> new ApplicationException(
                    ErrorStatus.toErrorStatus("아이템을 찾을 수 없습니다.", 400, LocalDateTime.now())
                )));
    }

    public void save(CreateItemRequest item, String storeId){

        DocumentReference storeRef = storesAdapter.getDocsRef(storeId);

        itemsAdapter.saveItem(item.createEntity(storeRef));
    }

    public void updateItem(UpdateItemRequest item) {
        itemsAdapter.updateItem(item.updateEntity());
    }

    public void deleteItem(String itemId) {
        itemsAdapter.deleteItem(itemId);
    }

    public void closeTask(String storeId) {
        itemsAdapter.updateItemsStateOutOfStock(storeId);
    }

    public void openTask(List<String> itemdIList, List<Integer> quantityList) {
        itemsAdapter.updateItemOnSale(itemdIList, quantityList);
    }
}
