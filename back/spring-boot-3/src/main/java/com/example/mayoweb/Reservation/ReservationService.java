package com.example.mayoweb.Reservation;

import com.example.mayoweb.Reservation.ReservationsAdapter;
import com.example.mayoweb.Reservation.ReservationsDto;
import com.example.mayoweb.Reservation.ReservationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationsAdapter reservationsAdapter;
    public void ReservationAccept(String id) throws ExecutionException, InterruptedException {

        reservationsAdapter.ReservationProceeding(id);
    }

    public void ReservationFail(String id) throws ExecutionException, InterruptedException {

        reservationsAdapter.ReservationFail(id);
    }

    public void ReservationDone(String id) throws ExecutionException, InterruptedException {

        reservationsAdapter.ReservationDone(id);
    }

    public List<ReservationsDto> getNewByStoreRef(String storeRef) throws ExecutionException, InterruptedException {
        return reservationsAdapter.getNewByStoreRef(storeRef).stream().map(this::toDto).toList();
    }

    public List<ReservationsDto> getProcessingByStoreRef(String storeRef) throws ExecutionException, InterruptedException {
        return reservationsAdapter.getProcessingByStoreRef(storeRef).stream().map(this::toDto).toList();
    }

    public List<ReservationsDto> getEndByStoreRef(String storeRef) throws ExecutionException, InterruptedException {
        return reservationsAdapter.getEndByStoreRef(storeRef).stream().map(this::toDto).toList();
    }

    public ReservationsDto getReservationById(String reservationId) throws Exception {
        return toDto(reservationsAdapter.findByReservationId(reservationId).orElseThrow());
    }

    private ReservationsDto toDto(ReservationEntity entity) {
        return ReservationsDto.builder()
                .id(entity.id)
                .reservation_id(entity.reservation_id)
                .reservation_state(entity.reservation_state)
                .reservation_request(entity.reservation_request)
                .reservation_is_plastics(entity.reservation_is_plastics)
                .created_at(entity.created_at)
                .quantityList(entity.quantityList)
                .pickup_time(entity.pickup_time)
                .total_price(entity.total_price)
                .store_ref(entity.store_ref)
                .user_ref(entity.user_ref)
                .itemList_ref(entity.itemList_ref)
                .cart_ref(entity.cart_ref)
                .build();
    }
}
