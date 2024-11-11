package com.example.hospital_backend.review.service;

import com.example.hospital_backend.User.entity.User;
import com.example.hospital_backend.User.repository.UserRepository;
import com.example.hospital_backend.exception.ResourceNotFoundException;
import com.example.hospital_backend.firebase.FirebaseStorageService;
import com.example.hospital_backend.hospital.entity.Hospital;
import com.example.hospital_backend.hospital.repository.HospitalRepository;
import com.example.hospital_backend.photo.entity.Photo;
import com.example.hospital_backend.review.dto.ReviewDTO;
import com.example.hospital_backend.review.entity.Review;
import com.example.hospital_backend.review.mapper.ReviewMapper;
import com.example.hospital_backend.review.repository.ReviewRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final HospitalRepository hospitalRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;
    private final FirebaseStorageService firebaseStorageService;

    public List<ReviewDTO> findReviewsByHospitalId(Long hospitalId) {
        hospitalRepository.findById(hospitalId)
            .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + hospitalId));

        return reviewRepository.findByHospitalId(hospitalId).stream()
            .map(reviewMapper::toReviewDTO)
            .collect(Collectors.toList());
    }

    public List<ReviewDTO> findReviewsByUserId(Long userId) {
        userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return reviewRepository.findByUserId(userId).stream()
            .map(reviewMapper::toReviewDTO)
            .collect(Collectors.toList());
    }

    public ReviewDTO saveReview(ReviewDTO reviewDTO, MultipartFile image, Long userId, Long hospitalId) throws IOException {
        Hospital hospital = hospitalRepository.findById(hospitalId)
            .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + hospitalId));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Review review = reviewMapper.toReview(reviewDTO);
        review.setHospital(hospital);
        review.setUser(user);

        // photos 리스트 초기화 확인
        if (review.getPhotos() == null) {
            review.setPhotos(new ArrayList<>());
        }

        // 파일이 이미지인지 확인
        if (image != null && !image.isEmpty()) {
            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("유효하지 않은 파일 형식입니다. 이미지 파일만 업로드할 수 있습니다.");
            }

            // 유효한 이미지일 경우 업로드
            String imageUrl;
            try {
                imageUrl = firebaseStorageService.uploadImage(image);
            } catch (IOException e) {
                throw new IOException("이미지 업로드 중 오류가 발생했습니다.", e);
            }

            Photo photo = new Photo();
            photo.setUrl(imageUrl);
            photo.setReview(review);
            photo.setUploadedAt(new Date());

            review.getPhotos().add(photo); // 리뷰에 사진 추가
        }

        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toReviewDTO(savedReview);
    }

    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO, MultipartFile image, Long userId, Long hospitalId) throws IOException {
        Review existingReview = getReviewById(reviewId);

        // 병원과 유저 설정
        Hospital hospital = hospitalRepository.findById(hospitalId)
            .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + hospitalId));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        existingReview.setHospital(hospital);
        existingReview.setUser(user);
        existingReview.setContent(reviewDTO.getContent());
        existingReview.setRating(reviewDTO.getRating());

        if (image != null && !image.isEmpty()) {
            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("유효하지 않은 파일 형식입니다. 이미지 파일만 업로드할 수 있습니다.");
            }

            String imageUrl;
            try {
                imageUrl = firebaseStorageService.uploadImage(image);
            } catch (IOException e) {
                throw new IOException("이미지 업로드 중 오류가 발생했습니다.", e);
            }

            existingReview.getPhotos().clear();
            Photo photo = new Photo();
            photo.setUrl(imageUrl);
            photo.setReview(existingReview);
            photo.setUploadedAt(new Date());

            existingReview.getPhotos().add(photo);
        }

        Review updatedReview = reviewRepository.save(existingReview);
        return reviewMapper.toReviewDTO(updatedReview);
    }

    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review not found with ID: " + reviewId);
        }
        reviewRepository.deleteById(reviewId);
    }

    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + reviewId));
    }
}
