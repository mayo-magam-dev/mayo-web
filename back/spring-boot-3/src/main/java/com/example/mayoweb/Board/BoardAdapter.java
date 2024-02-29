package com.example.mayoweb.Board;
import com.example.mayoweb.Store.StoresEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
@Slf4j
public class BoardAdapter {


    //Board 객체 중 category가 0(약관 및 정책)을 가져오는 쿼리
    //가게 도큐먼트 id를 받아 해당 가게의 모든 예약들을 가져옵니다.
    public List<BoardEntity> getBoard0() throws ExecutionException, InterruptedException {
        List<BoardEntity> boards = new ArrayList<>();
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference boardRef = firestore.collection("board");
        Query query = boardRef.whereEqualTo("category", 0);
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
        QuerySnapshot querySnapshot = querySnapshotApiFuture.get();
        for (QueryDocumentSnapshot boardDocument : querySnapshot.getDocuments()) {
            BoardEntity boardEntity = boardDocument.toObject(BoardEntity.class);
            boards.add(boardEntity);
        }
        return boards;
    }


    //Board 객체 중 category가 1(공지사항)을 가져오는 쿼리
    public List<BoardEntity> getBoard1() throws ExecutionException, InterruptedException {
        List<BoardEntity> boards = new ArrayList<>();
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference boardRef = firestore.collection("board");
        Query query = boardRef.whereEqualTo("category", 1);
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
        QuerySnapshot querySnapshot = querySnapshotApiFuture.get();
        for (QueryDocumentSnapshot boardDocument : querySnapshot.getDocuments()) {
            BoardEntity boardEntity = boardDocument.toObject(BoardEntity.class);
            boards.add(boardEntity);
        }
        return boards;
    }

    //board객체의 document id를 받아 board 객체를 얻습니다.
    public Optional<BoardEntity> getBoardById(String boardId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference documentReference = db.collection("board").document(boardId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        return Optional.ofNullable(document.toObject(BoardEntity.class));
    }
}
