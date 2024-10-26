package com.example.hospital_backend.photo.controller;

import com.example.hospital_backend.photo.dto.PhotoDTO;
import com.example.hospital_backend.photo.service.PhotoService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @GetMapping("/review/{reviewId}")
    public ResponseEntity<List<PhotoDTO>> getPhotosByReview(@PathVariable Long reviewId) {
        List<PhotoDTO> photos = photoService.findPhotosByReviewId(reviewId);
        return ResponseEntity.ok(photos);
    }

    @PostMapping("/upload")
    public ResponseEntity<PhotoDTO> uploadPhoto(
        @RequestParam("reviewId") Long reviewId,
        @RequestPart(value = "file",required = false) MultipartFile file) {
        try {
            PhotoDTO savedPhoto = photoService.savePhoto(file, reviewId);
            return new ResponseEntity<>(savedPhoto, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 적절한 에러 처리
        }
    }


}
