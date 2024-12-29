package com.example.mayoweb.item.repository;

import com.example.mayoweb.item.domain.ItemEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AutoItemRepository {

    private final Map<String, ItemEntity> autoItems = new HashMap<>();

    public void add(ItemEntity item) {
        autoItems.put(item.getItemId(), item);
    }

    public List<ItemEntity> getAutoItems() {
        return autoItems.values().stream().toList();
    }

    public List<ItemEntity> getAutoItemsByStoreId(String storeId) {
        return autoItems.values().stream()
                .filter(item -> item.getStoreRef().getId().equals(storeId))
                .toList();
    }

    public void deleteByItemId(String itemId) {
        autoItems.remove(itemId);
    }
}
