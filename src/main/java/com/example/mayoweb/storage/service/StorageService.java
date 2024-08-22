package com.example.mayoweb.storage.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class StorageService {

    @Value("${firebase.storage-url}")
    private String firebaseStorageUrl;

    // 파일 업로드
    public String uploadFirebaseBucket(MultipartFile multipartFile, String fileName) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseStorageUrl);

        Blob blob = bucket.create(fileName,
                multipartFile.getInputStream(), multipartFile.getContentType());

        return blob.getMediaLink();
    }

    // 파일 삭제
    public void deleteFirebaseBucket(String key) {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseStorageUrl);

        bucket.get(key).delete();
    }
}
