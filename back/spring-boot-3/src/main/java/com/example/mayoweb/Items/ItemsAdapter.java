package com.example.mayoweb.items;

import com.example.mayoweb.reservation.ReservationEntity;
import com.example.mayoweb.items.ItemsEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Repository
@Slf4j
public class ItemsAdapter {

    //가게 도큐먼트 id를 받아 해당 가게의 모든 아이템들을 가져옵니다.
    public List<ItemsEntity> getItemsByStoreRef(String storesRef) throws ExecutionException, InterruptedException {
        List<ItemsEntity> items = new ArrayList<>();
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference storeDocumentId = firestore.collection("stores").document(storesRef);
        CollectionReference itemsRef = firestore.collection("items");
        Query query = itemsRef.whereEqualTo("store_ref", storeDocumentId);
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
        QuerySnapshot querySnapshot = querySnapshotApiFuture.get();
        for (QueryDocumentSnapshot itemDocument : querySnapshot.getDocuments()) {
            ItemsEntity itemsEntity = itemDocument.toObject(ItemsEntity.class);
            items.add(itemsEntity);
        }
        return items;
    }

    //가게 도큐먼트 id를 받아 해당 가게의 모든 아이템들을 소진 처리 시킵니다.
    public void updateItemsStateOutOfStock(String storesRef) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference itemRef = firestore.collection("items");
        DocumentReference storesDocumentId = firestore.collection("stores").document(storesRef);
        QuerySnapshot querySnapshot = itemRef.whereEqualTo("store_ref", storesDocumentId).get().get();
        for (QueryDocumentSnapshot itemDocument : querySnapshot.getDocuments()) {
            DocumentReference item = itemRef.document(itemDocument.getId());
            item.update("item_on_sale", false, "item_quantity", 0);
        }
    }

    //checkbox 여부와 itemid리스트와 개수를 입력받아 해당 아이템의 개수를 바꾸고 item_on_sale값을 true로 바꿉니다.
    public void updateItemOnSale(List<String> itemidList, List<Integer> quantityList) {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference itemRef = firestore.collection("items");

        for(int i=0; i<itemidList.size(); i++) {
            if(quantityList.get(i) > 0) {
                DocumentReference item = itemRef.document(itemidList.get(i));
                item.update("item_on_sale", true, "item_quantity", quantityList.get(i));
            }
        }
    }

    //DocumentReference를 ItemsEntity로 바꿉니다.
    public ItemsEntity getItemByDocRef(DocumentReference doc) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentSnapshot> itemSnapshotFuture = doc.get();
        DocumentSnapshot itemSnapshot = itemSnapshotFuture.get();
        if (itemSnapshot.exists()) {
            ItemsEntity itemsEntity = ItemsEntity.builder()
                    .itemId(itemSnapshot.getId())
                    .item_name(itemSnapshot.getString("item_name"))
                    .item_description(itemSnapshot.getString("item_description"))
                    .original_price(itemSnapshot.get("original_price", Integer.class))
                    .sale_percent(itemSnapshot.getDouble("sale_percent"))
                    .item_created(itemSnapshot.getTimestamp("item_created"))
                    .item_modified(itemSnapshot.getTimestamp("time_modified"))
                    .item_quantity(itemSnapshot.get("item_quantity", Integer.class))
                    .item_on_sale(itemSnapshot.getBoolean("item_on_sale"))
                    .item_image(itemSnapshot.getString("item_image"))
                    .store_name(itemSnapshot.getString("store_name"))
                    .store_address(itemSnapshot.getString("store-address"))
                    .user_item_quantity(itemSnapshot.get("user_item_quantity", Integer.class))
                    .sale_price(itemSnapshot.getDouble("sale_price"))
                    .cooking_time(itemSnapshot.get("cooking_time",Integer.class))
                    .additional_information(itemSnapshot.getString("additional_information"))
                    .cartRef((DocumentReference) itemSnapshot.get("cartRef"))
                    .store_ref((DocumentReference) itemSnapshot.get("store_ref"))
                    .build();

            return itemsEntity;
        } else {
        }
        return null;
    }

    //cart 객체의 리스트를 입력받아 첫 item이름을 가져옵니다.

    public List<String> getFirstItemNamesFromCarts(List<DocumentReference> carts) throws ExecutionException, InterruptedException {
        List<String> firstItemNames = new ArrayList<>();

        for (DocumentReference cart : carts) {
            DocumentSnapshot cartSnapshot = cart.get().get();

            if(cartSnapshot.exists()) {
                DocumentReference itemRef = (DocumentReference) cartSnapshot.get("item");
                if(itemRef != null) {
                    DocumentSnapshot itemSnapshot = itemRef.get().get();
                    if (itemSnapshot.exists()) {
                        String itemName = itemSnapshot.getString("item_name");
                        firstItemNames.add(itemName);
                    }
                }
            }
        }
        return firstItemNames;
    }

    //itemid로 item객체를 가져옵니다.
    public Optional<ItemsEntity> getItemById(String itemId) {
        Firestore db = FirestoreClient.getFirestore();

        try {
            DocumentReference docRef = db.collection("items").document(itemId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot itemSnapshot = future.get();

            if (itemSnapshot.exists()) {

                ItemsEntity itemsEntity = ItemsEntity.builder()
                        .itemId(itemSnapshot.getId())
                        .item_name(itemSnapshot.getString("item_name"))
                        .item_description(itemSnapshot.getString("item_description"))
                        .original_price(itemSnapshot.get("original_price", Integer.class))
                        .sale_percent(itemSnapshot.getDouble("sale_percent"))
                        .item_created(itemSnapshot.getTimestamp("item_created"))
                        .item_modified(itemSnapshot.getTimestamp("time_modified"))
                        .item_quantity(itemSnapshot.get("item_quantity", Integer.class))
                        .item_on_sale(itemSnapshot.getBoolean("item_on_sale"))
                        .item_image(itemSnapshot.getString("item_image"))
                        .store_name(itemSnapshot.getString("store_name"))
                        .store_address(itemSnapshot.getString("store-address"))
                        .user_item_quantity(itemSnapshot.get("user_item_quantity", Integer.class))
                        .sale_price(itemSnapshot.getDouble("sale_price"))
                        .cooking_time(itemSnapshot.get("cooking_time",Integer.class))
                        .additional_information(itemSnapshot.getString("additional_information"))
                        .cartRef((DocumentReference) itemSnapshot.get("cartRef"))
                        .store_ref((DocumentReference) itemSnapshot.get("store_ref"))
                        .build();

                return Optional.ofNullable(itemsEntity);
            }
        }
        catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    //item객체를 받아 firebase에서 새로운 아이템을 만듭니다.
    public void saveItem(ItemsEntity item, String storeRef) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        CollectionReference itemCollection = db.collection("items");
        DocumentReference docRef = db.collection("stores").document(storeRef);

        if(docRef != null) {

            ItemsEntity itemsEntity = ItemsEntity.builder()
                    .itemId(item.getItemId())
                    .item_name(item.getItem_name())
                    .item_description(item.getItem_description())
                    .original_price(item.getOriginal_price())
                    .sale_percent(item.getSale_percent())
                    .item_created(item.getItem_created())
                    .item_modified(item.getItem_modified())
                    .item_quantity(item.getItem_quantity())
                    .item_on_sale(item.getItem_on_sale())
                    .item_image(item.getItem_image())
                    .store_name(item.getStore_name())
                    .store_address(item.getStore_address())
                    .user_item_quantity(item.getUser_item_quantity())
                    .sale_price(item.getSale_price())
                    .cooking_time(item.getCooking_time())
                    .additional_information(item.getAdditional_information())
                    .cartRef(item.getCartRef())
                    .store_ref((DocumentReference) docRef)
                    .build();
        }

        itemCollection.add(item);
    }

    //itemsEntity를 받아 이름, 설명, 할인 전 후 가격, 추가 공지사항, 소요 시간을 업데이트 하는 쿼리
    public void updateItem(ItemsEntity itemsEntity) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference documentReference = db.collection("items").document(itemsEntity.getItemId());
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            documentReference.update("item_name", itemsEntity.getItem_name(),"item_description", itemsEntity.getItem_description()
                    ,"original_price", itemsEntity.getOriginal_price(),"sale_price", itemsEntity.getSale_price(), "sale_percent", itemsEntity.getSale_percent()
                    , "additional_information", itemsEntity.getAdditional_information(), "cooking_time", itemsEntity.getCooking_time());
        }
    }

    //아이템 객체 삭제
    public void deleteItem(ItemsEntity itemsEntity) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection("items").document(itemsEntity.getItemId()).delete();
    }

}