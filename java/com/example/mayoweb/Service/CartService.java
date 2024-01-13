package com.example.mayoweb.Service;

import com.example.mayoweb.Adapter.CartsAdapter;
import com.example.mayoweb.Dto.CartsDto;
import com.example.mayoweb.Entity.CartsEntity;
import com.google.cloud.firestore.DocumentReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartsAdapter cartsAdapter;

    public List<CartsDto> getCartsByDocRef(List<DocumentReference> doc) throws ExecutionException, InterruptedException {
        return cartsAdapter.getCartsByDocRef(doc).stream().map(this::toDto).toList();
    }

    private CartsDto toDto(CartsEntity entity) {
        return CartsDto.builder()
                .cartId(entity.cartId)
                .ItemCount(entity.ItemCount)
                .cartActive(entity.cartActive)
                .created_at(entity.created_at)
                .pickup_time(entity.pickup_time)
                .subtotal(entity.subtotal)
                .userRef(entity.userRef)
                .item(entity.item)
                .store_ref(entity.store_ref)
                .build();
    }
}
