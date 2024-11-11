package com.example.hospital_backend.User.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;

    @NotBlank(message = "Username은 필수입니다.")
    @Size(min = 3, max = 50, message = "Username은 3자 이상, 50자 이하이어야 합니다.")
    private String username;

    @NotBlank(message = "Email은 필수입니다.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    @Size(max = 100, message = "Email은 최대 100자까지 입력할 수 있습니다.")
    private String email;

    @NotBlank(message = "Password는 필수입니다.")
    private String password;
}
