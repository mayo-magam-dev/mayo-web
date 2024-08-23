package com.example.mayoweb.storage.service;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;

@Service
public class StorageService {

    @Value("${firebase.storage-url}")
    private String firebaseStorageUrl;

    // 파일 업로드
    public String uploadFirebaseBucket(MultipartFile multipartFile, String fileName) throws IOException {

        Bucket bucket = StorageClient.getInstance().bucket(firebaseStorageUrl);

        BlobInfo blobInfo = BlobInfo.newBuilder(bucket.getName(), fileName)
                .setContentType(multipartFile.getContentType())
                .build();

        bucket.create(fileName, multipartFile.getInputStream(), multipartFile.getContentType());

        bucket.get(fileName).createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        String imageUrl = String.format("https://storage.googleapis.com/%s/%s",
                bucket.getName(),
                URLEncoder.encode(fileName, "UTF-8"));

        return imageUrl;
    }

    // 파일 삭제
    public void deleteFirebaseBucket(String key) {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseStorageUrl);

        bucket.get(key).delete();
    }
}
