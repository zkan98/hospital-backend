package com.example.hospital_backend.hospital.mapper;

import com.example.hospital_backend.hospital.dto.HospitalApiResponseDTO;
import com.example.hospital_backend.hospital.dto.HospitalDTO;
import com.example.hospital_backend.hospital.entity.Hospital;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HospitalMapper {

    HospitalDTO toHospitalDTO(Hospital hospital);

    Hospital toHospital(HospitalDTO hospitalDTO);
}
