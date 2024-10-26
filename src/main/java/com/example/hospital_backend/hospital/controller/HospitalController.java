package com.example.hospital_backend.hospital.controller;

import com.example.hospital_backend.hospital.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hospitalData")
public class HospitalController {

    private final ApiService apiService;

    @GetMapping("/hospitalDataLoad")
    public void loadHospitalData() {
        apiService.processAndSaveHospitals();
    }
}
