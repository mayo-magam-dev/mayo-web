package com.example.mayoweb.commons.config;

import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;

@Configuration
public class FirebaseInitializer {

    private static final String DATABASE_URL = "https://mayo-app.firebaseio.com";

    @PostConstruct
    public void init() {
        try {
            InputStream inputStream = new ClassPathResource(
                    "/mayo-app-280d4.json").getInputStream();
            File file = File.createTempFile("key", ".json");
            FileCopyUtils.copy(inputStream.readAllBytes(), file);
            FileInputStream firebaseAccount = new FileInputStream(file);
            FirebaseOptions.Builder optionBuilder = FirebaseOptions.builder();
            FirebaseOptions options = optionBuilder
                    .setCredentials(GoogleCredentials.fromStream(firebaseAccount))
                    .setDatabaseUrl(DATABASE_URL)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("firebase 연결 중 오류 발생", 400, LocalDateTime.now()));
        }
    }
}