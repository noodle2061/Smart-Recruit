package com.ptit.thesis.smartrecruit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.ptit.thesis.smartrecruit.dto.response.UserResponse;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "firebaseUid", source = "userFirebaseUid")
    @Mapping(target = "userName", source = "username")
    @Mapping(target = "role", source = "role.roleName")
    @Mapping(target = "fullName", source = "candidateProfile", qualifiedByName = "getFullName")
    @Mapping(target = "firebaseCustomToken", ignore = true)
    UserResponse toUserResponse(User user);

    @Mapping(target = "role", ignore = true)
    User toUserEntity(UserResponse userResponse);

    @Named("getFullName")
    default String getFullName(CandidateProfile candidateProfile) {
        if (candidateProfile != null) {
            return candidateProfile.getFullName();
        }
        return null;
    }
}