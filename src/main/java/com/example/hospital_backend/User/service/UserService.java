package com.example.hospital_backend.User.service;

import com.example.hospital_backend.User.dto.UserDTO;
import com.example.hospital_backend.User.entity.User;
import com.example.hospital_backend.User.mapper.UserMapper;
import com.example.hospital_backend.User.repository.UserRepository;
import com.example.hospital_backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 메모리 기반 Refresh Token 저장소
    private final Map<String, String> refreshTokenStore = new HashMap<>();

    public Map<String, String> registerUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalStateException("이미 존재하는 사용자명입니다.");
        }

        User user = userMapper.toUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);

        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());

        refreshTokenStore.put(user.getUsername(), refreshToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    public Map<String, String> authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (passwordEncoder.matches(password, user.getPassword())) {
            String accessToken = jwtTokenProvider.createAccessToken(username);
            String refreshToken = jwtTokenProvider.createRefreshToken(username);

            refreshTokenStore.put(username, refreshToken);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            tokens.put("userId", user.getId().toString()); // userId 추가
            return tokens;
        } else {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
    }

    public String refreshAccessToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다.");
        }

        String username = jwtTokenProvider.getAuthentication(refreshToken).getName();

        if (!refreshToken.equals(refreshTokenStore.get(username))) {
            throw new IllegalArgumentException("Refresh Token이 일치하지 않습니다.");
        }

        return jwtTokenProvider.createAccessToken(username);
    }

    public void logout(String username) {
        refreshTokenStore.remove(username);
    }
}
