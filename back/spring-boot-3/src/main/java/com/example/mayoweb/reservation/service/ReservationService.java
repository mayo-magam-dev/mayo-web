package com.example.mayoweb.reservation.service;

import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.reservation.domain.ReservationEntity;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationResponse;
import com.example.mayoweb.reservation.repository.ReservationsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationsAdapter reservationsAdapter;

    public void reservationAccept(String id){
        reservationsAdapter.reservationProceeding(id);
    }

    public void reservationFail(String id){
        reservationsAdapter.reservationFail(id);
    }

    public void reservationDone(String id){
        reservationsAdapter.reservationDone(id);
    }

    public List<ReadReservationResponse> getNewByStoreId(String storeId){
        return reservationsAdapter.getNewByStoreRef(storeId).stream().map(ReadReservationResponse::fromEntity).toList();
    }

    public CompletableFuture<List<ReadReservationResponse>> getNewReservationsByStoreId(String storeId) {
        return reservationsAdapter.getNewByStoreIdAsync(storeId).thenApply(reservationEntities ->
            reservationEntities.stream().map(ReadReservationResponse::fromEntity).collect(Collectors.toList())
        );
    }

    public List<ReadReservationResponse> getProcessingByStoreId(String storeId){
        try {
            return reservationsAdapter.getProcessingByStoreRef(storeId).stream().map(ReadReservationResponse::fromEntity).toList();
        } catch (ExecutionException | InterruptedException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("진행중인 주문을 가져오는데 실패하였습니다.", 400, LocalDateTime.now()));
        }
    }

    public List<ReadReservationResponse> getEndByStoreId(String storeId) {
        return reservationsAdapter.getEndByStoreRef(storeId).stream().map(ReadReservationResponse::fromEntity).toList();
    }

    public ReadReservationResponse getReservationById(String reservationId) {
        return ReadReservationResponse.fromEntity(reservationsAdapter.findByReservationId(reservationId)
                .orElseThrow( () -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 예약이 없습니다.", 404, LocalDateTime.now())
                )));
    }

}
