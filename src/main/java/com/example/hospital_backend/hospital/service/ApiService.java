package com.example.hospital_backend.hospital.service;

import com.example.hospital_backend.hospital.dto.HospitalApiResponseDTO;
import com.example.hospital_backend.hospital.entity.Hospital;
import com.example.hospital_backend.hospital.repository.HospitalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiService {

    private final HospitalRepository hospitalRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${service.key}")
    private String serviceKey;

    public void processAndSaveHospitals() {
        int pageNo = 1;
        int numOfRows = 1000;
        int totalCount = getTotalCount(pageNo, numOfRows);

        System.out.println("Parsed total count from DTO: " + totalCount);
        if (totalCount == 0) {
            System.out.println("데이터가 없습니다. (총 개수: 0)");
            return;
        }

        int totalPages = (int) Math.ceil((double) totalCount / numOfRows);

        for (pageNo = 1; pageNo <= totalPages; pageNo++) {
            try {
                String apiUrl = buildUrl(pageNo, numOfRows);
                HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");

                int responseCode = conn.getResponseCode();
                BufferedReader rd = (responseCode >= 200 && responseCode <= 300)
                    ? new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))
                    : new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                conn.disconnect();

                String responseBody = sb.toString();
                System.out.println("Raw XML Response: " + responseBody);

                JSONObject xmlJSONObj = XML.toJSONObject(responseBody);
                String jsonResponse = xmlJSONObj.toString(4);

                System.out.println("Converted JSON Response: " + jsonResponse);

                HospitalApiResponseDTO dto = objectMapper.readValue(jsonResponse, HospitalApiResponseDTO.class);

                if (dto != null && dto.getItems() != null) {
                    List<Hospital> hospitals = dto.getItems().stream()
                        .map(HospitalApiResponseDTO.Item::toEntity)
                        .collect(Collectors.toList());

                    System.out.println("Hospitals to save (Count): " + hospitals.size());
                    if (!hospitals.isEmpty()) {
                        System.out.println("Saving hospitals to database...");
                        hospitalRepository.saveAll(hospitals);
                        System.out.println("Hospitals saved successfully.");
                    } else {
                        System.out.println("No hospitals to save.");
                    }
                } else {
                    System.out.println("데이터를 찾을 수 없습니다.");
                }

            } catch (IOException e) {
                System.err.println("데이터를 불러오는 도중 에러 발생: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private int getTotalCount(int pageNo, int numOfRows) {
        try {
            String apiUrl = buildUrl(pageNo, numOfRows);
            HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            String responseBody = sb.toString();
            System.out.println("API 응답 XML (총 개수): " + responseBody);

            JSONObject xmlJSONObj = XML.toJSONObject(responseBody);
            String jsonResponse = xmlJSONObj.toString(4);
            System.out.println("Converted JSON Response (for total count): " + jsonResponse);

            HospitalApiResponseDTO dto = objectMapper.readValue(jsonResponse, HospitalApiResponseDTO.class);

            int totalCount = dto != null ? dto.getTotalCount() : 0;
            System.out.println("Parsed total count from DTO: " + totalCount);
            return totalCount;

        } catch (IOException e) {
            System.err.println("총 데이터 수를 불러오는 도중 에러 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    private String buildUrl(int pageNo, int numOfRows) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/B551182/hospInfoServicev2/getHospBasisList");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(serviceKey, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(pageNo), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(numOfRows), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));

        return urlBuilder.toString();
    }
}
