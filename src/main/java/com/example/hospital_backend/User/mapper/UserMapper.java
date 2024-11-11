package com.example.hospital_backend.User.mapper;

import com.example.hospital_backend.User.dto.UserDTO;
import com.example.hospital_backend.User.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDTO toUserDTO(User user);
    User toUser(UserDTO userDTO);
}
