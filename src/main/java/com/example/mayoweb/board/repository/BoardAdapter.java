package com.example.mayoweb.board.repository;
import com.example.mayoweb.board.domain.BoardEntity;
import com.example.mayoweb.board.domain.type.BoardType;
import com.example.mayoweb.commons.annotation.FirestoreTransactional;

import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
@FirestoreTransactional
public class BoardAdapter {

    private final Firestore firestore;

    public List<BoardEntity> getTermsBoard() {

        List<BoardEntity> boards = new ArrayList<>();

        CollectionReference boardRef = firestore.collection("board");
        Query query = boardRef.whereEqualTo("category", BoardType.TERMSDETAIL.getState());
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();

        QuerySnapshot querySnapshot = null;

        try {
            querySnapshot = querySnapshotApiFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("약관 및 정책을 가져오는데 에러가 발생하였습니다.", 400, LocalDateTime.now()));
        }

        for (QueryDocumentSnapshot boardDocument : querySnapshot.getDocuments()) {
            BoardEntity boardEntity = boardDocument.toObject(BoardEntity.class);
            boards.add(boardEntity);
        }

        return boards;
    }


    public List<BoardEntity> getNoticeBoard() {

        List<BoardEntity> boards = new ArrayList<>();

        CollectionReference boardRef = firestore.collection("board");
        Query query = boardRef.whereEqualTo("category", BoardType.NOTICE.getState());

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
        QuerySnapshot querySnapshot = null;

        try {
            querySnapshot = querySnapshotApiFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("공지사항을 가져오는 도중 에러가 발생하였습니다.", 400, LocalDateTime.now()));
        }

        for (QueryDocumentSnapshot boardDocument : querySnapshot.getDocuments()) {
            BoardEntity boardEntity = boardDocument.toObject(BoardEntity.class);
            boards.add(boardEntity);
        }
        return boards;
    }

    public Optional<BoardEntity> getBoardById(String boardId) {

        DocumentReference documentReference = firestore.collection("board").document(boardId);

        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = null;

        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("해당 게시글을 가져오는데 오류가 발생하였습니다.", 400, LocalDateTime.now()));
        }

        return Optional.ofNullable(document.toObject(BoardEntity.class));
    }

}
