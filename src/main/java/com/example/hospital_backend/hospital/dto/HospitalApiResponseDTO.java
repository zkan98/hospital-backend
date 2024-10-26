package com.example.hospital_backend.hospital.dto;

import com.example.hospital_backend.hospital.entity.Hospital;
import com.example.hospital_backend.hospital.entity.Specialty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HospitalApiResponseDTO {
    @JsonProperty("response")
    private Response response;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        @JsonProperty("body")
        private Body body;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        @JsonProperty("totalCount")
        private int totalCount;

        @JsonProperty("items")
        private Items items;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items {
        @JsonProperty("item")
        private List<Item> itemList;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        @JsonProperty("addr")
        private String addr;

        @JsonProperty("clCd")
        private String clCd;

        @JsonProperty("XPos")
        private Double XPos;

        @JsonProperty("YPos")
        private Double YPos;

        @JsonProperty("yadmNm")
        private String yadmNm;

        @JsonProperty("telno")
        private String telno;

        @JsonProperty("hospUrl")
        private String hospUrl;

        public Hospital toEntity() {
            return Hospital.builder()
                .name(yadmNm)
                .address(addr)
                .latitude(YPos)
                .longitude(XPos)
                .phoneNumber(telno)
                .specialty(mapSpecialtyCode(clCd))
                .websiteUrl(hospUrl)
                .build();
        }

        private Specialty mapSpecialtyCode(String code) {
            switch (code) {
                case "01":
                    return Specialty.INTERNAL_MEDICINE;  // 내과
                case "10":
                    return Specialty.PEDIATRICS;         // 소아청소년과
                case "03":
                    return Specialty.MENTAL_HEALTH;      // 정신건강의학과
                case "05":
                    return Specialty.ORTHOPEDICS;        // 정형외과
                case "12":
                    return Specialty.OPHTHALMOLOGY;      // 안과
                case "14":
                    return Specialty.DERMATOLOGY;        // 피부과
                case "15":
                    return Specialty.UROLOGY;            // 비뇨기과
                case "49":
                case "50":
                case "51":
                case "52":
                case "53":
                case "54":
                case "55":
                case "56":
                case "57":
                case "58":
                case "59":
                case "60":
                case "61":
                    return Specialty.DENTISTRY;          // 치과 관련 진료과목
                case "28":
                case "80":
                case "81":
                case "82":
                case "83":
                case "84":
                case "85":
                case "86":
                case "87":
                case "88":
                    return Specialty.ORIENTAL_MEDICINE;  // 한방 관련 진료과목
                case "04":
                    return Specialty.SURGERY;            // 외과
                default:
                    return Specialty.GENERAL;            // 기타 진료과목
            }
        }

    }

    public int getTotalCount() {
        return response != null && response.getBody() != null ? response.getBody().getTotalCount() : 0;
    }

    public List<Item> getItems() {
        return response != null && response.getBody() != null && response.getBody().getItems() != null
            ? response.getBody().getItems().getItemList()
            : null;
    }
}
