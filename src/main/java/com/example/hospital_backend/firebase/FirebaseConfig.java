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
            new FileInputStream("src/main/resources/hospital-8c922-firebase-adminsdk-d58b7-325babf40f.json");


        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setStorageBucket("hospital-8c922.appspot.com") // Storage bucket 설정
            .build();

        FirebaseApp.initializeApp(options);
    }
}
