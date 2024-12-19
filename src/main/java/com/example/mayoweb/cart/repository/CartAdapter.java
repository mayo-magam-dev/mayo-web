package com.example.mayoweb.cart.repository;

import com.example.mayoweb.cart.domain.CartEntity;
import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.reservation.domain.ReservationEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
public class CartAdapter {

    private final Firestore firestore;

    public Optional<CartEntity> findCartById(String cartId) {

        try {
            DocumentReference docRef = firestore.collection("carts").document(cartId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot cartSnapshot = future.get();

            if (cartSnapshot.exists()) {
                return Optional.ofNullable(fromDocument(cartSnapshot));
            }
        }
        catch (ExecutionException | InterruptedException e) {
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("장바구니를 찾는데 에러가 발생하였습니다.", 400, LocalDateTime.now())
            );
        }

        return Optional.empty();
    }

    public List<CartEntity> getCartsByDocRef(List<DocumentReference> doc) {

        List<CartEntity> cartEntity = new ArrayList<>();

        for (DocumentReference cartRef : doc) {
            ApiFuture<DocumentSnapshot> future = cartRef.get();
            try {
                DocumentSnapshot document = future.get();
                if (document.exists()) {
                    cartEntity.add(fromDocument(document));
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new ApplicationException(ErrorStatus.toErrorStatus("cart를 찾는 도중 오류가 발생하였습니다.", 400, LocalDateTime.now()));
            }
        }

        return cartEntity;
    }

    public List<DocumentReference> getFirstCartsByReservations(List<ReservationEntity> reservations) {

        List<DocumentReference> carts = new ArrayList<>();

        for (ReservationEntity reservation : reservations) {
            List<DocumentReference> cartRefs = reservation.getCartRef();

            if (!cartRefs.isEmpty()) {
                DocumentReference firstCartRef = cartRefs.get(0);
                carts.add(firstCartRef);
            }
        }

        return carts;
    }

    public DocumentReference getFirstCartByReservation(ReservationEntity reservation) {

        List<DocumentReference> cartRefs = reservation.getCartRef();

        if (!cartRefs.isEmpty()) {
            return cartRefs.get(0);
        }

        throw new ApplicationException(
                ErrorStatus.toErrorStatus("해당 주문에 속하는 카트가 없습니다.", 404, LocalDateTime.now())
        );
    }

    private CartEntity fromDocument(DocumentSnapshot document) {
        return CartEntity.builder()
                .cartId(document.getId())
                .itemCount(document.get("itemCount", Integer.class))
                .cartActive(document.getBoolean("cartActive"))
                .createdAt(document.getTimestamp("created_at"))
                .pickupTime(document.getTimestamp("pickup_time"))
                .subtotal(document.getDouble("subtotal"))
                .item((DocumentReference) document.get("item"))
                .userRef((DocumentReference) document.get("userRef"))
                .storeRef((DocumentReference) document.get("store_ref"))
                .build();
    }

}