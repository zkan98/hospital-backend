package com.example.hospital_backend.hospital.entity;

public enum Specialty {
    GENERAL("일반의"),
    INTERNAL_MEDICINE("내과"),
    SURGERY("외과"),
    PEDIATRICS("소아청소년과"),
    MENTAL_HEALTH("정신건강의학과"),
    DERMATOLOGY("피부과"),
    UROLOGY("비뇨기과"),
    OBSTETRICS_GYNECOLOGY("산부인과"),
    OPHTHALMOLOGY("안과"),
    ORTHOPEDICS("정형외과"),
    DENTISTRY("치과"),
    ORIENTAL_MEDICINE("한방"),
    OTHER("기타 진료과목");

    private final String koreanName;

    Specialty(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }
}
