package com.ptit.thesis.smartrecruit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ptit.thesis.smartrecruit.dto.response.UserResponse;
import com.ptit.thesis.smartrecruit.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "firebaseUid", source = "userFirebaseUid")
    @Mapping(target = "userName", source = "username")
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "firebaseCustomToken", ignore = true)
    UserResponse toUserResponse(User user);

    @Mapping(target = "userRoles", ignore = true)
    User toUserEntity(UserResponse userResponse);
}