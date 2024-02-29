package com.example.mayoweb.Store;

import com.example.mayoweb.Store.StoresAdapter;
import com.example.mayoweb.Store.StoresDto;
import com.example.mayoweb.Store.StoresEntity;
import com.google.cloud.firestore.DocumentReference;
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

    public void closeStore(String storeRef) throws ExecutionException, InterruptedException {
        storesAdapter.closeStore(storeRef);
    }

    public void openStore(String storeRef) throws ExecutionException, InterruptedException {
        storesAdapter.openStore(storeRef);
    }

    public DocumentReference getDocsRef(String storeRef) {
        return storesAdapter.getDocsRef(storeRef);
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
