package com.example.mayoweb.Items;

import com.example.mayoweb.Reservation.ReservationEntity;
import com.google.api.core.ApiFuture;
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
    public void updateItemOnSale(List<String> checkbox ,List<String> itemidList, List<Integer> quantityList) {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference itemRef = firestore.collection("items");

        System.out.println(checkbox.size());
        System.out.println("check : " + checkbox.get(0) + "item : " + itemidList.get(0) + "quantity : " + quantityList.get(0));

        for(int i=0; i<checkbox.size(); i++) {
            if("on".equals(checkbox.get(i))) {
                log.info(checkbox.get(i) + itemidList.get(i) + quantityList.get(i));
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
            ItemsEntity itemsEntity = new ItemsEntity();

            String itemName = itemSnapshot.getString("item_name");

            itemsEntity.setItem_name(itemName);

            return itemsEntity;
        } else {
            System.out.println("User document does not exist!");
        }
        return null;
    }

    //reservationEntity List에서 각 reservation 객체의 첫 item이름을 가져옵니다.
    public List<String> getFirstItemNamesFromReservations(List<ReservationEntity> reservations) {
        List<String> firstItemNames = new ArrayList<>();

        for (ReservationEntity reservation : reservations) {
            List<DocumentReference> cartRefs = reservation.getCart_ref();
            if (!cartRefs.isEmpty()) {
                DocumentReference firstCartRef = cartRefs.get(0);
                try {
                    DocumentSnapshot cartSnapshot = firstCartRef.get().get();
                    if (cartSnapshot.exists()) {
                        DocumentReference itemRef = (DocumentReference) cartSnapshot.get("item");
                        if (itemRef != null) {
                            DocumentSnapshot itemSnapshot = itemRef.get().get();
                            if (itemSnapshot.exists()) {
                                String itemName = itemSnapshot.getString("item_name");
                                firstItemNames.add(itemName);
                            }
                        }
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        return firstItemNames;
    }

    //itemid로 item객체를 가져옵니다.
    public Optional<ItemsEntity> getItemById(String itemId) {
        Firestore db = FirestoreClient.getFirestore();
        ItemsEntity itemsEntity = null;

        try {
            DocumentReference docRef = db.collection("items").document(itemId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {

                itemsEntity = new ItemsEntity();
                itemsEntity.setItemId(itemId);
                itemsEntity.setItem_name(document.getString("item_name"));
                itemsEntity.setItem_description(document.getString("item_description"));
                itemsEntity.setOriginal_price((document.get("original_price", Integer.class)));
                itemsEntity.setSale_percent(document.getDouble("sale_percent"));
//                itemsEntity.setItem_created(Timestamp.of(document.getDate("created_at")));
//                itemsEntity.setItem_modified(Timestamp.of(document.getDate("pickup_time")));
                itemsEntity.setItem_quantity((document.get("item_quantity", Integer.class)));
                itemsEntity.setItem_on_sale(document.getBoolean("item_on_sale"));
                itemsEntity.setItem_image(document.getString("item_image"));
                itemsEntity.setStore_name(document.getString("store_name"));
                itemsEntity.setStore_address(document.getString("store_address"));
                itemsEntity.setUser_item_quantity((document.get("user_item_quantity", Integer.class)));
                itemsEntity.setSale_price(document.getDouble("sale_price"));
                itemsEntity.setCooking_time((document.get("cooking_time", Integer.class)));
                itemsEntity.setAdditional_information(document.getString("additional_information"));


                DocumentReference storeRef = (DocumentReference) document.get("store_ref");
                if (storeRef != null) {
                    ApiFuture<DocumentSnapshot> store_ref = storeRef.get();
                    DocumentSnapshot storeDocument = store_ref.get();
                    if (storeDocument.exists()) {
                        itemsEntity.setStore_ref(storeDocument.getReference());
                    }
                }

                DocumentReference cartRef = (DocumentReference) document.get("cartRef");
                if (cartRef != null) {
                    ApiFuture<DocumentSnapshot> cart_ref = cartRef.get();
                    DocumentSnapshot cartDocument = cart_ref.get();
                    if (cartDocument.exists()) {
                        itemsEntity.setCartRef(cartDocument.getReference());
                    }
                }
            }

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(itemsEntity);
    }

    //item객체를 받아 firebase에서 새로운 아이템을 만듭니다.
    public void saveItem(ItemsEntity item, String storeRef) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        CollectionReference itemCollection = db.collection("items");
        DocumentReference docRef = db.collection("stores").document(storeRef);

        if(docRef != null) {
            item.setStore_ref(docRef);
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

    public void deleteItem(ItemsEntity itemsEntity) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection("items").document(itemsEntity.getItemId()).delete();
    }

}
