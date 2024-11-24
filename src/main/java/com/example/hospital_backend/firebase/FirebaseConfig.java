package com.example.hospital_backend.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() throws IOException {
        // 환경 변수에서 Firebase 설정 파일 경로 가져오기
        String firebaseConfigPath = System.getenv("FIREBASE_CONFIG_PATH");

        FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath);

        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setStorageBucket("hospital-6ccde.firebasestorage.app") // Storage bucket 설정
            .build();

        FirebaseApp.initializeApp(options);
    }
}
