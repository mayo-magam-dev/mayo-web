package com.example.mayoweb.storage.service;

import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;

@Service
public class StorageService {

    @Value("${firebase.storage-url}")
    private String firebaseStorageUrl;

    // 파일 업로드
    public String uploadFirebaseBucket(MultipartFile multipartFile, String fileName){

        Bucket bucket = StorageClient.getInstance().bucket(firebaseStorageUrl);

        BlobInfo blobInfo = BlobInfo.newBuilder(bucket.getName(), fileName)
                .setContentType(multipartFile.getContentType())
                .build();

        try {
            bucket.create(fileName, multipartFile.getInputStream(), multipartFile.getContentType());
        } catch (IOException e) {
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("firebase storage 이미지 업로드 중 알 수 없는 에러가 발생하였습니다.", 500, LocalDateTime.now()
                    ));
        }

        bucket.get(fileName).createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        String imageUrl = null;
        try {
            imageUrl = String.format("https://storage.googleapis.com/%s/%s",
                    bucket.getName(),
                    URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            ErrorStatus.toErrorStatus("firebase storage 이미지 업로드 중 알 수 없는 에러가 발생하였습니다.", 500, LocalDateTime.now()
            );
        }

        return imageUrl;
    }

    // 파일 삭제
    public void deleteFirebaseBucket(String key) {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseStorageUrl);

        bucket.get(key).delete();
    }
}
