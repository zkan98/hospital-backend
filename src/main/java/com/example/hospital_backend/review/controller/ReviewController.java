package com.example.hospital_backend.review.controller;

import com.example.hospital_backend.review.dto.ReviewDTO;
import com.example.hospital_backend.review.service.ReviewService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByHospitalId(@PathVariable Long hospitalId) {
        List<ReviewDTO> reviews = reviewService.findReviewsByHospitalId(hospitalId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUserId(@PathVariable Long userId) {
        List<ReviewDTO> reviews = reviewService.findReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<ReviewDTO> createReview(
        @RequestPart("review") @Valid ReviewDTO reviewDTO,
        @RequestPart(value = "image", required = false) MultipartFile image,
        @RequestParam Long userId,
        @RequestParam Long hospitalId) throws IOException {

        ReviewDTO createdReview = reviewService.saveReview(reviewDTO, image, userId, hospitalId);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{reviewId}", consumes = { "multipart/form-data" })
    public ResponseEntity<ReviewDTO> updateReview(
        @PathVariable Long reviewId,
        @RequestPart("review") @Valid ReviewDTO reviewDTO,
        @RequestPart(value = "image", required = false) MultipartFile image,
        @RequestParam Long userId,
        @RequestParam Long hospitalId) throws IOException {

        ReviewDTO updatedReview = reviewService.updateReview(reviewId, reviewDTO, image, userId, hospitalId);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
