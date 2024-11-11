package com.example.hospital_backend.hospital.controller;

import com.example.hospital_backend.hospital.dto.HospitalDTO;
import com.example.hospital_backend.hospital.entity.Specialty;
import com.example.hospital_backend.hospital.service.HospitalService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hospitals")
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping
    public ResponseEntity<List<HospitalDTO>> getAllHospitals() {
        List<HospitalDTO> hospitals = hospitalService.findAllHospitals();
        return ResponseEntity.ok(hospitals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HospitalDTO> getHospitalById(@PathVariable Long id) {
        HospitalDTO hospitalDTO = hospitalService.findHospitalById(id);
        return ResponseEntity.ok(hospitalDTO);
    }

    @GetMapping("/specialty/{specialty}")
    public ResponseEntity<List<HospitalDTO>> getHospitalsBySpecialty(@PathVariable Specialty specialty) {
        List<HospitalDTO> hospitals = hospitalService.findHospitalsBySpecialty(specialty);
        return ResponseEntity.ok(hospitals);
    }

    @PostMapping
    public ResponseEntity<HospitalDTO> createHospital(@Valid @RequestBody HospitalDTO hospitalDTO) {
        HospitalDTO createdHospital = hospitalService.saveHospital(hospitalDTO);
        return new ResponseEntity<>(createdHospital, HttpStatus.CREATED);
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<HospitalDTO>> getNearbyHospitals(
        @RequestParam double latitude,
        @RequestParam double longitude) {
        List<HospitalDTO> nearbyHospitals = hospitalService.findNearbyHospitals(latitude, longitude);
        return ResponseEntity.ok(nearbyHospitals);
    }

}
