package com.example.mayoweb.Adapter;

import com.example.mayoweb.Entity.ItemsEntity;
import com.example.mayoweb.Entity.ReservationEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
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
}
