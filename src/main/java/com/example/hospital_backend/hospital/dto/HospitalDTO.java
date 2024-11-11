package com.example.hospital_backend.hospital.dto;

import com.example.hospital_backend.hospital.entity.Specialty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HospitalDTO {
    private Long id;

    @NotBlank(message = "Hospital name은 필수입니다.")
    @Size(max = 100, message = "Hospital name은 최대 100자까지 입력할 수 있습니다.")
    private String name;

    @NotBlank(message = "Address는 필수입니다.")
    @Size(max = 255, message = "Address는 최대 255자까지 입력할 수 있습니다.")
    private String address;

    @Size(max = 50, message = "Phone number는 최대 50자까지 입력할 수 있습니다.")
    private String phoneNumber;

    @Size(max = 255, message = "Website URL은 최대 255자까지 입력할 수 있습니다.")
    private String websiteUrl;

    @NotNull(message = "Specialty는 필수입니다.")
    private Specialty specialty;

    @DecimalMin(value = "0.0", inclusive = true, message = "Rating은 최소 0.0 이상이어야 합니다.")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating은 최대 5.0 이하여야 합니다.")
    private double rating;

    @NotNull(message = "Latitude는 필수입니다.")
    private Double latitude;

    @NotNull(message = "Longitude는 필수입니다.")
    private Double longitude;

    public String getSpecialtyKorean() {
        return specialty != null ? specialty.getKoreanName() : "정보 없음";
    }
}
