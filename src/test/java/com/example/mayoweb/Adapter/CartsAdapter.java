package com.example.mayoweb.Adapter;

import com.example.mayoweb.Entity.CartsEntity;
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
                    String cartId = document.getString("cartId");
                    int itemCount = document.getLong("itemCount").intValue();
                    Double subtotal = document.getDouble("subtotal");
                    DocumentReference item = (DocumentReference) document.get("item");

                    CartsEntity cart = new CartsEntity();
                    cart.setCartId(cartId);
                    cart.setItemCount(itemCount);
                    cart.setSubtotal(subtotal);
                    cart.setItem(item);

                    cartsEntity.add(cart);
                } else {
                    System.out.println("문서가 존재하지 않습니다.");
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return cartsEntity;
    }
}
