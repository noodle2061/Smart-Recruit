package com.ptit.thesis.smartrecruit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ptit.thesis.smartrecruit.dto.response.UserResponse;
import com.ptit.thesis.smartrecruit.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * chuyển doi tu User -> UserResponse
     * <p>
     * Các thuộc tính fullName, firebaseCustomToken cần tự cập nhật sau khi gọi hàm
     * 
     * @param user
     * @return
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "userName", source = "username")
    @Mapping(target = "role", source = "role.name")
    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "firebaseCustomToken", ignore = true)
    UserResponse toUserResponse(User user);

    @Mapping(target = "role", ignore = true)
    User toUserEntity(UserResponse userResponse);
}   