package com.example.hospital_backend.hospital.repository;

import com.example.hospital_backend.hospital.entity.Hospital;
import com.example.hospital_backend.hospital.entity.Specialty;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    // 진료 과목별 병원 리스트 조회
    List<Hospital> findBySpecialty(Specialty specialty);
    boolean existsByName(String name);

    // 이름에 해당 텍스트를 포함하는 병원 검색
    List<Hospital> findByNameContaining(String name);

    @Query(value = "SELECT *, " +
        "(6371 * acos(cos(radians(:latitude)) * cos(radians(h.latitude)) " +
        "* cos(radians(h.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(h.latitude)))) " +
        "AS distance " +
        "FROM hospitals h " +
        "WHERE h.latitude IS NOT NULL AND h.longitude IS NOT NULL " +
        "HAVING distance <= :distanceThreshold " +
        "ORDER BY distance ASC",
        nativeQuery = true)
    List<Hospital> findNearbyHospitals(@Param("latitude") double latitude,
        @Param("longitude") double longitude,
        @Param("distanceThreshold") double distanceThreshold,
        Pageable pageable);
}
