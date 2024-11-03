package com.example.mayoweb.reservation.service;

import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.reservation.domain.dto.response.ReadReservationResponse;
import com.example.mayoweb.reservation.repository.ReservationsAdapter;
import com.example.mayoweb.sse.SseService;
import com.google.cloud.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
            reservationEntities.stream().map(ReadReservationResponse::fromEntity).toList()
        );
    }

//    public CompletableFuture<List<ReadReservationResponse>> getNewReservationsByStoreIdSse(String clientId, String storeId) throws ExecutionException, InterruptedException {
//        return reservationsAdapter.getNewByStoreIdSse(clientId, storeId).thenApply(reservationEntities ->
//                reservationEntities.stream().map(ReadReservationResponse::fromEntity).toList()
//        );
//    }

    public SseEmitter getNewReservationsByStoreIdSse(String clientId, String storeId) {
        return reservationsAdapter.streamNewReservations(clientId, storeId);
    }

    public List<ReadReservationResponse> getProcessingByStoreId(String storeId){
        try {
            return reservationsAdapter.getProcessingByStoreRef(storeId).stream().map(ReadReservationResponse::fromEntity).toList();
        } catch (ExecutionException | InterruptedException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("진행중인 주문을 가져오는데 실패하였습니다.", 400, LocalDateTime.now()));
        }
    }

    public CompletableFuture<List<ReadReservationResponse>> getProceedingReservationsByStoreId(String storeId) {
        return reservationsAdapter.getProceedingByStoreIdAsync(storeId).thenApply(reservationEntities ->
                reservationEntities.stream().map(ReadReservationResponse::fromEntity).collect(Collectors.toList())
        );
    }

    public List<ReadReservationResponse> getEndByStoreId(String storeId) {
        return reservationsAdapter.getEndByStoreRef(storeId).stream().map(ReadReservationResponse::fromEntity).toList();
    }

    public List<ReadReservationResponse> getEndByStoreIdAndTimestamp(String storeId, Timestamp timestamp) {
        return reservationsAdapter.getEndByStoreRefAndTimestamp(storeId, timestamp).stream().map(ReadReservationResponse::fromEntity).toList();
    }

    public CompletableFuture<List<ReadReservationResponse>> getEndReservationsByStoreId(String storeId) {
        return reservationsAdapter.getEndByStoreIdAsync(storeId).thenApply(reservationEntities ->
                reservationEntities.stream().map(ReadReservationResponse::fromEntity).collect(Collectors.toList())
        );
    }

//    public Slice<ReadReservationResponse> getReservationsByStoreIdSlice(String storeId, int page, int size) throws ExecutionException, InterruptedException {
//        Pageable pageable = PageRequest.of(page, size);
//        return reservationsAdapter.findEndReservationsByStoreId(storeId, pageable).get().map(ReadReservationResponse::fromEntity);
//    }

    public Slice<ReadReservationResponse> getReservationsByStoreIdAndTimeSlice(String storeId, Timestamp timestamp, int page, int size) throws ExecutionException, InterruptedException {
        Pageable pageable = PageRequest.of(page, size);
        return reservationsAdapter.findEndReservationsByStoreIdAndTime(storeId, pageable, timestamp).get().map(ReadReservationResponse::fromEntity);
    }

    public ReadReservationResponse getReservationById(String reservationId) {
        return ReadReservationResponse.fromEntity(reservationsAdapter.findByReservationId(reservationId)
                .orElseThrow( () -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 예약이 없습니다.", 404, LocalDateTime.now())
                )));
    }

    public void sendFCMNewReservation(String storeId) {
        reservationsAdapter.sendNewReservationFCM(storeId);
    }

}
