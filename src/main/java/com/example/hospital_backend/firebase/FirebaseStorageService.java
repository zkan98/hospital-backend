// FirebaseStorageService.java
package com.example.hospital_backend.firebase;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.create(fileName, file.getBytes(), file.getContentType());

        // Firebase Storage URL 반환
        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
            bucket.getName(), fileName.replace("/", "%2F"));
    }
}
