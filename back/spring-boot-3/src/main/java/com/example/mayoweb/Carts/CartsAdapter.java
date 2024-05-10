package com.example.mayoweb.carts;

import com.example.mayoweb.reservation.ReservationEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
@Slf4j
public class CartsAdapter {

    //List<Document> 값으로 cart객체의 list를 가져오는 쿼리
    public List<CartsEntity> getCartsByDocRef(List<DocumentReference> doc) {
        List<CartsEntity> cartsEntity = new ArrayList<>();

        for (DocumentReference cartRef : doc) {
            ApiFuture<DocumentSnapshot> future = cartRef.get();
            try {
                DocumentSnapshot document = future.get();
                if (document.exists()) {

                    CartsEntity cart = CartsEntity.builder()
                            .cartId(document.getString("cartId"))
                            .ItemCount(document.get("itemCount", Integer.class))
                            .cartActive(document.getBoolean("cartActive"))
                            .created_at(document.getTimestamp("created_at"))
                            .pickup_time(document.getTimestamp("pickup_time"))
                            .subtotal(document.getDouble("subtotal"))
                            .item((DocumentReference) document.get("item"))
                            .userRef((DocumentReference) document.get("userRef"))
                            .store_ref((DocumentReference) document.get("store_ref")).build();

                    cartsEntity.add(cart);

                } else {
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return cartsEntity;
    }

    //reservation객체의 리스트를 받아 첫번째 cart객체를 가져옵니다.
    public List<DocumentReference> getFirstCartsByReservations(List<ReservationEntity> reservations) {
        List<DocumentReference> carts = new ArrayList<>();

        for (ReservationEntity reservation : reservations) {
            List<DocumentReference> cartRefs = reservation.getCart_ref();
            if (!cartRefs.isEmpty()) {
                DocumentReference firstCartRef = cartRefs.get(0);
                carts.add(firstCartRef);
            }
        }
        return carts;
    }
}