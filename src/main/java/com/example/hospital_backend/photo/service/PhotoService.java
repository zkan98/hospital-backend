package com.example.hospital_backend.photo.service;

import com.example.hospital_backend.exception.BadRequestException;
import com.example.hospital_backend.exception.ResourceNotFoundException;
import com.example.hospital_backend.firebase.FirebaseStorageService;
import com.example.hospital_backend.photo.dto.PhotoDTO;
import com.example.hospital_backend.photo.entity.Photo;
import com.example.hospital_backend.photo.mapper.PhotoMapper;
import com.example.hospital_backend.photo.repository.PhotoRepository;
import com.example.hospital_backend.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;
    private final ReviewService reviewService;
    private final FirebaseStorageService firebaseStorageService;

    public List<PhotoDTO> findPhotosByReviewId(Long reviewId) {
        reviewService.getReviewById(reviewId);  // 리뷰 존재 여부 확인 (예외 발생 시 ResourceNotFoundException)

        return photoRepository.findByReviewId(reviewId).stream()
            .map(photoMapper::toPhotoDTO)
            .collect(Collectors.toList());
    }

    public PhotoDTO savePhoto(MultipartFile file, Long reviewId) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is required for photo upload.");
        }

        String fileUrl = firebaseStorageService.uploadImage(file);

        Photo photo = new Photo();
        photo.setReview(reviewService.getReviewById(reviewId));
        photo.setUrl(fileUrl);
        photo.setUploadedAt(new Date());

        Photo savedPhoto = photoRepository.save(photo);
        return photoMapper.toPhotoDTO(savedPhoto);
    }
}
