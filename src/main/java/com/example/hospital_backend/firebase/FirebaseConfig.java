// firebase/FirebaseConfig.java
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

        FileInputStream serviceAccount =
            new FileInputStream("C:\\Users\\Administrator\\Desktop\\firebase\\hospital-6ccde-firebase-adminsdk-1h0az-7b093be1d3.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setStorageBucket("hospital-6ccde.firebasestorage.app") // Storage bucket 설정
            .build();

        FirebaseApp.initializeApp(options);
    }
}
