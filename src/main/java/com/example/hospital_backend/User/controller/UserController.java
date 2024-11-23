package com.example.hospital_backend.User.controller;

import com.example.hospital_backend.User.dto.UserDTO;
import com.example.hospital_backend.User.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody UserDTO userDTO) {
        Map<String, String> tokens = userService.registerUser(userDTO);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserDTO userDTO) {
        Map<String, String> tokens = userService.authenticate(userDTO.getUsername(), userDTO.getPassword());
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        String newAccessToken = userService.refreshAccessToken(refreshToken);

        // 새로운 액세스 토큰 반환
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        userService.logout(username);
        return ResponseEntity.ok("로그아웃 완료");
    }
}
