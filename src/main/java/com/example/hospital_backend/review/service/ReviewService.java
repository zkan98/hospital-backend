package com.example.hospital_backend.review.service;

import com.example.hospital_backend.User.entity.User;
import com.example.hospital_backend.User.repository.UserRepository;
import com.example.hospital_backend.exception.InvalidReviewException;
import com.example.hospital_backend.hospital.entity.Hospital;
import com.example.hospital_backend.hospital.repository.HospitalRepository;
import com.example.hospital_backend.review.dto.ReviewDTO;
import com.example.hospital_backend.review.entity.Review;
import com.example.hospital_backend.review.mapper.ReviewMapper;
import com.example.hospital_backend.review.repository.ReviewRepository;
import com.example.hospital_backend.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final HospitalRepository hospitalRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    public List<ReviewDTO> findReviewsByHospitalId(Long hospitalId) {
        // 병원 존재 여부 확인
        Hospital hospital = hospitalRepository.findById(hospitalId)
            .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + hospitalId));

        return reviewRepository.findByHospitalId(hospitalId).stream()
            .map(reviewMapper::toReviewDTO)
            .collect(Collectors.toList());
    }

    public List<ReviewDTO> findReviewsByUserId(Long userId) {
        // 사용자 존재 여부 확인
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return reviewRepository.findByUserId(userId).stream()
            .map(reviewMapper::toReviewDTO)
            .collect(Collectors.toList());
    }

    public ReviewDTO saveReview(ReviewDTO reviewDTO) {

        // 병원 존재 여부 확인
        Hospital hospital = hospitalRepository.findById(reviewDTO.getHospitalId())
            .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + reviewDTO.getHospitalId()));

        // 사용자 존재 여부 확인
        User user = userRepository.findById(reviewDTO.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + reviewDTO.getUserId()));

        // 리뷰 생성
        Review review = reviewMapper.toReview(reviewDTO);
        review.setHospital(hospital);
        review.setUser(user);

        Review savedReview = reviewRepository.save(review);

        // 병원의 평균 평점 계산 및 반영
        List<Review> reviews = reviewRepository.findByHospitalId(hospital.getId());
        double averageRating = reviews.stream()
            .mapToInt(Review::getRating)
            .average()
            .orElse(0.0);

        hospital.setRating(averageRating); // 병원 엔티티에 평균 평점 설정
        hospitalRepository.save(hospital); // 병원 엔티티 업데이트

        return reviewMapper.toReviewDTO(savedReview);
    }


    // 리뷰 ID로 리뷰를 조회하는 메서드
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + reviewId));
    }

    // 리뷰가 존재하는지 확인하는 메서드 (Repository에서 호출)
    public boolean existsById(Long reviewId) {
        return reviewRepository.existsById(reviewId);
    }
}
