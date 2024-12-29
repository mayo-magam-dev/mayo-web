package com.example.mayoweb.item.service;

import com.example.mayoweb.commons.annotation.FirestoreTransactional;
import com.example.mayoweb.fcm.service.FCMService;
import com.example.mayoweb.item.domain.ItemEntity;
import com.example.mayoweb.item.domain.response.AutoItemResponse;
import com.example.mayoweb.item.repository.AutoItemRepository;
import com.example.mayoweb.item.repository.ItemAdapter;
import com.example.mayoweb.store.domain.StoreEntity;
import com.example.mayoweb.store.repository.StoreAdapter;
import com.example.mayoweb.user.repository.UserAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class AutoItemService {

    private final AutoItemRepository autoItemRepository;
    private final ItemAdapter itemAdapter;
    private final StoreAdapter storeAdapter;
    private final UserAdapter userAdapter;
    private final FCMService fcmService;

    private static final String EXECUTION_UNIT_TIME = "0 */30 * * * *";

    public boolean addAllAutoItemsByStoreId(String storeId) {

        List<ItemEntity> itemList = itemAdapter.getItemsByStoreId(storeId);

        for(ItemEntity item : itemList) {
            autoItemRepository.add(item);
        }

        return true;
    }

    public void deleteByItemId(String itemId) {
        autoItemRepository.deleteByItemId(itemId);
    }

    public List<AutoItemResponse> getAutoItemsByStoreId(String storeId) {
        return autoItemRepository.getAutoItemsByStoreId(storeId).stream().map(AutoItemResponse::from).toList();
    }

    public List<AutoItemResponse> getAutoItems() {
        return autoItemRepository.getAutoItems().stream().map(AutoItemResponse::from).toList();
    }

    @FirestoreTransactional
    @Scheduled(cron = EXECUTION_UNIT_TIME)
    public void scheduleTask() {
        LocalDateTime now = LocalDateTime.now();
        closeTask(now);
        openTask(now);
    }

    private void openTask(LocalDateTime now) {
        List<StoreEntity> storeList = storeAdapter.getOpenTimeStores(now);

        if(storeList.isEmpty()) {
            return;
        }

        for(StoreEntity store : storeList) {
            log.info("start openTask : {}", store.getStoreName());
            storeAdapter.openStore(store.getId());
            List<ItemEntity> itemList = autoItemRepository.getAutoItemsByStoreId(store.getId());

            for(ItemEntity item : itemList) {
                itemAdapter.updateItemOnSaleById(item.getItemId(), 5);
            }

            List<String> tokens = userAdapter.getNoticeUserFCMTokenByStoresRef(store.getId());
            fcmService.sendOpenMessage(tokens, store.getId());
        }
    }

    private void closeTask(LocalDateTime now) {
        List<StoreEntity> storeList = storeAdapter.getCloseTimeStores(now);

        if(storeList.isEmpty()) {
            return;
        }

        for(StoreEntity store : storeList) {
            log.info("start closeTask : {}", store.getStoreName());

            storeAdapter.closeStore(store.getId());
            itemAdapter.updateItemsStateOutOfStock(store.getId());
        }
    }
}
