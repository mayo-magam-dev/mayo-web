package com.example.mayoweb.Service;

import com.example.mayoweb.Adapter.StoresAdapter;
import com.example.mayoweb.Dto.StoresDto;
import com.example.mayoweb.Entity.StoresEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class StoresService {

    private final StoresAdapter storesAdapter;

    public StoresDto getStoreById(String store_ref) throws ExecutionException, InterruptedException {
        return toDto(storesAdapter.findByStoresRef(store_ref).orElseThrow());
    }

    public void updateStore(StoresDto storesDto) throws ExecutionException, InterruptedException {
        storesAdapter.updateStore(storesDto.toEntity());
    }
    private StoresDto toDto(StoresEntity entity) {
        return StoresDto.builder()
                .id(entity.id)
                .store_name(entity.store_name)
                .open_state(entity.open_state)
                .sale_start(entity.sale_start)
                .sale_end(entity.sale_end)
                .store_image(entity.store_image)
                .additional_comment(entity.additional_comment)
                .open_time(entity.open_time)
                .close_time(entity.close_time)
                .address(entity.address)
                .store_number(entity.store_number)
                .build();
    }
}
