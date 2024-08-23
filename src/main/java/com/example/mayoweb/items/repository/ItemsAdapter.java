package com.example.mayoweb.items.repository;

import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.example.mayoweb.items.domain.ItemsEntity;
import com.example.mayoweb.items.domain.response.ReadFirstItemResponse;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Repository
public class ItemsAdapter {

    public List<ItemsEntity> getItemsByStoreId(String storeId) {

        List<ItemsEntity> items = new ArrayList<>();

        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference storeDocumentId = firestore.collection("stores").document(storeId);
        CollectionReference itemsRef = firestore.collection("items");
        Query query = itemsRef.whereEqualTo("store_ref", storeDocumentId);
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
        QuerySnapshot querySnapshot = null;

        try {
            querySnapshot = querySnapshotApiFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("스토어로 아이템을 가져오는 중 에러가 발생하였습니다.", 400, LocalDateTime.now()));
        }

        for (QueryDocumentSnapshot itemDocument : querySnapshot.getDocuments()) {
            ItemsEntity itemsEntity = itemDocument.toObject(ItemsEntity.class);
            items.add(itemsEntity);
        }

        return items;
    }

    public void updateItemsStateOutOfStock(String storesRef) {

        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference itemRef = firestore.collection("items");
        DocumentReference storesDocumentId = firestore.collection("stores").document(storesRef);
        QuerySnapshot querySnapshot = null;

        try {
            querySnapshot = itemRef.whereEqualTo("store_ref", storesDocumentId).get().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("해당 가게에 아이템이 없습니다.", 400, LocalDateTime.now()));
        }

        for (QueryDocumentSnapshot itemDocument : querySnapshot.getDocuments()) {
            DocumentReference item = itemRef.document(itemDocument.getId());
            item.update("item_on_sale", false, "item_quantity", 0);
        }
    }

    public void updateItemOnSale(List<String> itemidList, List<Integer> quantityList) {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference itemRef = firestore.collection("items");

        for(int i=0;i<itemidList.size();i++) {
            if(quantityList.get(i) != 0) {
                DocumentReference item = itemRef.document(itemidList.get(i));
                item.update("item_on_sale", true, "item_quantity", quantityList.get(i));
            }
        }
    }

    public Optional<ItemsEntity> getItemByDocRef(DocumentReference doc){
        ApiFuture<DocumentSnapshot> itemSnapshotFuture = doc.get();
        DocumentSnapshot itemSnapshot = null;
        try {
            itemSnapshot = itemSnapshotFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("해당 item을 찾을 수 없습니다.", 404, LocalDateTime.now()));
        }
        if (itemSnapshot.exists()) {
            return Optional.ofNullable(fromDocument(itemSnapshot));
        }
        return Optional.empty();
    }

    public List<ReadFirstItemResponse> getFirstItemNamesFromCarts(List<DocumentReference> carts){

        List<ReadFirstItemResponse> firstItemResponse = new ArrayList<>();

        for (DocumentReference cart : carts) {
            DocumentSnapshot cartSnapshot = null;
            try {
                cartSnapshot = cart.get().get();
            } catch (InterruptedException | ExecutionException e) {
                throw new ApplicationException(ErrorStatus.toErrorStatus("cart객체에서 아이템을 가져오는데 실패했습니다.", 400, LocalDateTime.now()));
            }

            if(cartSnapshot.exists()) {
                DocumentReference itemRef = (DocumentReference) cartSnapshot.get("item");
                if(itemRef != null) {
                    DocumentSnapshot itemSnapshot = null;
                    try {
                        itemSnapshot = itemRef.get().get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new ApplicationException(ErrorStatus.toErrorStatus("cart로 아이템을 가져오는데 실패했습니다.", 400, LocalDateTime.now()));
                    }
                    if (itemSnapshot.exists()) {
                        ReadFirstItemResponse response = ReadFirstItemResponse.builder()
                                        .itemName(itemSnapshot.getString("item_name"))
                                        .itemQuantity(cartSnapshot.get("itemCount", Integer.class))
                                        .build();

                        firstItemResponse.add(response);
                    } else {
                        firstItemResponse.add(ReadFirstItemResponse.builder()
                                .itemName(" ")
                                .itemQuantity(0)
                                .build());
                    }
                }
            }
        }
        return firstItemResponse;
    }

    public Optional<ItemsEntity> getItemById(String itemId) {
        Firestore db = FirestoreClient.getFirestore();

        try {
            DocumentReference docRef = db.collection("items").document(itemId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot itemSnapshot = future.get();

            if (itemSnapshot.exists()) {
                return Optional.ofNullable(fromDocument(itemSnapshot));
            }
        }
        catch (ExecutionException | InterruptedException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("아이템을 찾는데 에러가 발생하였습니다.", 400, LocalDateTime.now()));
        }

        return Optional.empty();
    }

    public void saveItem(ItemsEntity item){

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference itemCollection = db.collection("items");

        itemCollection.add(item);
    }

    public void updateItem(ItemsEntity itemsEntity){

        Firestore db = FirestoreClient.getFirestore();
        DocumentReference documentReference = db.collection("items").document(itemsEntity.getItemId());
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = null;

        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("item을 업데이트 하는 도중 에러가 발생하였습니다.", 400, LocalDateTime.now()));
        }

        if (document.exists()) {
            documentReference.update(
                    "item_name", itemsEntity.getItemName(),
                    "item_image", itemsEntity.getItemImage(),
                    "item_description", itemsEntity.getItemDescription(),
                    "original_price", itemsEntity.getOriginalPrice(),
                    "sale_price", itemsEntity.getSalePrice(),
                    "sale_percent", itemsEntity.getSalePercent(),
                    "additional_information", itemsEntity.getAdditionalInformation(),
                    "cooking_time", itemsEntity.getCookingTime());
        }
    }

    public void deleteItem(String itemId) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("items").document(itemId).delete();
    }

    private ItemsEntity fromDocument(DocumentSnapshot document) {
        return ItemsEntity.builder()
                .itemId(document.getId())
                .itemName(document.getString("item_name"))
                .itemDescription(document.getString("item_description"))
                .originalPrice(document.get("original_price", Integer.class))
                .salePercent(document.getDouble("sale_percent"))
                .itemCreated(document.getTimestamp("item_created"))
                .itemModified(document.getTimestamp("time_modified"))
                .itemQuantity(document.get("item_quantity", Integer.class))
                .itemOnSale(document.getBoolean("item_on_sale"))
                .itemImage(document.getString("item_image"))
                .storeName(document.getString("store_name"))
                .storeAddress(document.getString("store-address"))
                .userItemQuantity(document.get("user_item_quantity", Integer.class))
                .salePrice(document.getDouble("sale_price"))
                .cookingTime(document.get("cooking_time",Integer.class))
                .additionalInformation(document.getString("additional_information"))
                .storeRef((DocumentReference) document.get("store_ref"))
                .build();
    }
}