package com.example.mayoweb.Service;

import com.example.mayoweb.Adapter.ItemsAdapter;
import com.example.mayoweb.Dto.ItemsDto;
import com.example.mayoweb.Dto.ReservationsDto;
import com.example.mayoweb.Entity.ItemsEntity;
import com.example.mayoweb.Entity.ReservationEntity;
import com.google.cloud.firestore.DocumentReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ItemsService {

    private final ItemsAdapter itemsAdapter;
    private final ReservationService reservationService;

    public ItemsDto getItemByDocRef(DocumentReference doc) throws ExecutionException, InterruptedException {
        return toDto(itemsAdapter.getItemByDocRef(doc));
    }

    public List<String> getFirstItemNamesFromReservations(List<ReservationsDto> reservations) {

        ArrayList<ReservationEntity> reservationList = new ArrayList<>();

        for(ReservationsDto reservationsDto : reservations) {
            reservationList.add(reservationsDto.toEntity());
        }

        return itemsAdapter.getFirstItemNamesFromReservations(reservationList);
    }

    private ItemsDto toDto(ItemsEntity entity) {
        return ItemsDto.builder()
                .itemId(entity.itemId)
                .item_name(entity.item_name)
                .item_description(entity.item_description)
                .original_price(entity.original_price)
                .sale_percent(entity.sale_percent)
                .item_created(entity.item_created)
                .item_modified(entity.item_modified)
                .item_quantity(entity.item_quantity)
                .item_on_sale(entity.item_on_sale)
                .item_image(entity.item_image)
                .store_name(entity.store_name)
                .store_address(entity.store_address)
                .user_item_quantity(entity.user_item_quantity)
                .sale_price(entity.sale_price)
                .cooking_time(entity.cooking_time)
                .additional_information(entity.additional_information)
                .cartRef(entity.cartRef)
                .store_ref(entity.store_ref)
                .build();
    }
}
