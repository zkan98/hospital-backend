package com.example.hospital_backend.review.mapper;

import com.example.hospital_backend.review.dto.ReviewDTO;
import com.example.hospital_backend.review.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {

    @Mapping(target = "hospitalId", source = "hospital.id") // hospitalId로 매핑
    @Mapping(target = "userId", source = "user.id")         // userId로 매핑
    @Mapping(target = "photos", source = "photos")          // photos 매핑
    @Mapping(target = "username", source = "user.username") // username 매핑
    ReviewDTO toReviewDTO(Review review);

    @Mapping(target = "hospital", ignore = true) // 서비스에서 직접 병원 설정
    @Mapping(target = "user", ignore = true)     // 서비스에서 직접 유저 설정
    Review toReview(ReviewDTO reviewDTO);
}
