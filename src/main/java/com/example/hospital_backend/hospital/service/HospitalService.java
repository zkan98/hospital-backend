package com.example.hospital_backend.hospital.service;

import com.example.hospital_backend.exception.DuplicateHospitalException;
import com.example.hospital_backend.exception.ResourceNotFoundException;
import com.example.hospital_backend.hospital.dto.HospitalDTO;
import com.example.hospital_backend.hospital.entity.Hospital;
import com.example.hospital_backend.hospital.entity.Specialty;
import com.example.hospital_backend.hospital.mapper.HospitalMapper;
import com.example.hospital_backend.hospital.repository.HospitalRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final HospitalMapper hospitalMapper;

    public List<HospitalDTO> findAllHospitals() {
        return hospitalRepository.findAll().stream()
            .map(hospitalMapper::toHospitalDTO)
            .collect(Collectors.toList());
    }

    public HospitalDTO findHospitalById(Long id) {
        return hospitalRepository.findById(id)
            .map(hospitalMapper::toHospitalDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + id));
    }

    public List<HospitalDTO> findHospitalsBySpecialty(Specialty specialty) {
        return hospitalRepository.findBySpecialty(specialty).stream()
            .map(hospitalMapper::toHospitalDTO)
            .collect(Collectors.toList());
    }

    public HospitalDTO saveHospital(HospitalDTO hospitalDTO) {
        // 중복된 병원 이름 확인
        if (hospitalRepository.existsByName(hospitalDTO.getName())) {
            throw new DuplicateHospitalException("Hospital already exists with name: " + hospitalDTO.getName());
        }

        Hospital hospital = hospitalMapper.toHospital(hospitalDTO);
        Hospital savedHospital = hospitalRepository.save(hospital);
        return hospitalMapper.toHospitalDTO(savedHospital);
    }

    public List<HospitalDTO> findNearbyHospitals(double latitude, double longitude) {
        double distanceThreshold = 10.0; // 반경 10km 설정
        return hospitalRepository.findAll().stream()
            .filter(hospital -> hospital.getLatitude() != null && hospital.getLongitude() != null) // null 체크 추가
            .filter(hospital -> calculateDistance(latitude, longitude,
                hospital.getLatitude(), hospital.getLongitude()) <= distanceThreshold)
            .limit(40) // 최대 40개의 병원만 반환
            .map(hospitalMapper::toHospitalDTO)
            .collect(Collectors.toList());
    }

    // 거리 계산 함수
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; // 지구 반경 (km)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c; // 거리 (km)
    }

}
