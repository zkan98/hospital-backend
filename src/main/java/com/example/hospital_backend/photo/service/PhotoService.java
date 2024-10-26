package com.example.hospital_backend.photo.service;

import com.example.hospital_backend.exception.BadRequestException;
import com.example.hospital_backend.exception.ResourceNotFoundException;
import com.example.hospital_backend.firebase.FirebaseStorageService;
import com.example.hospital_backend.photo.dto.PhotoDTO;
import com.example.hospital_backend.photo.entity.Photo;
import com.example.hospital_backend.photo.mapper.PhotoMapper;
import com.example.hospital_backend.photo.repository.PhotoRepository;
import com.example.hospital_backend.review.service.ReviewService;
import java.io.IOException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;
    private final ReviewService reviewService;
    private final FirebaseStorageService firebaseStorageService;  // Firebase Storage 서비스 주입

    public List<PhotoDTO> findPhotosByReviewId(Long reviewId) {
        // 리뷰가 존재하는지 확인
        if (!reviewService.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review not found with ID: " + reviewId);
        }
        return photoRepository.findByReviewId(reviewId).stream()
            .map(photoMapper::toPhotoDTO)
            .collect(Collectors.toList());
    }

    public PhotoDTO savePhoto(MultipartFile file, Long reviewId) throws IOException {
        // 리뷰가 존재하는지 확인
        if (!reviewService.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review not found with ID: " + reviewId);
        }

        // Firebase에 파일 업로드
        String fileUrl = firebaseStorageService.uploadImage(file);

        // Photo 엔티티 생성 및 저장
        Photo photo = new Photo();
        photo.setReview(reviewService.getReviewById(reviewId)); // Review 엔티티 설정
        photo.setUrl(fileUrl); // Firebase에서 받은 URL 저장
        photo.setUploadedAt(new Date());

        Photo savedPhoto = photoRepository.save(photo);
        return photoMapper.toPhotoDTO(savedPhoto);
    }


}
