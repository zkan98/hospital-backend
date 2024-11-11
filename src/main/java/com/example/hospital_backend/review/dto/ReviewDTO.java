package com.example.hospital_backend.review.dto;

import com.example.hospital_backend.photo.dto.PhotoDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;
import java.util.Date;

@Data
public class ReviewDTO {
    private Long id;

    @Min(value = 1, message = "Rating은 최소 1 이상이어야 합니다.")
    @Max(value = 5, message = "Rating은 최대 5 이하여야 합니다.")
    private int rating; // 별점 (1-5)

    @Size(max = 1000, message = "Content는 최대 1000자까지 입력할 수 있습니다.")
    private String content; // 리뷰 내용 (선택 사항)

    private Date createdAt; // 생성일자는 서버에서 자동 설정

    private Long hospitalId; // 병원의 ID

    private Long userId;     // 사용자의 ID

    private String username;

    private List<PhotoDTO> photos;    // 추가: 리뷰에 첨부된 사진 정보

}
