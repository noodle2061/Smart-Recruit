package com.ptit.thesis.smartrecruit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ptit.thesis.smartrecruit.dto.request.CandidateBasicInfoRequest;
import com.ptit.thesis.smartrecruit.dto.request.CandidateContactInfoRequest;
import com.ptit.thesis.smartrecruit.dto.request.CandidateProfileDetailRequest;
import com.ptit.thesis.smartrecruit.dto.response.CandidateProfileResponse;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;

@Mapper(componentModel = "spring",
    uses = {LocationMapper.class, SocialLinkMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CandidateProfileMapper {
    /**
     * chuyển doi tu CandidateBasicInfoRequest -> CandidateProfile
     * <p>
     * @param candidateProfileRequest
     * @param candidateProfile
     */
    void updateEntity(CandidateBasicInfoRequest candidateProfileRequest, @MappingTarget CandidateProfile candidateProfile);

    /**
     * chuyển doi tu CandidateProfileDetailRequest -> CandidateProfile
     * <p>   
     * @param candidateProfileRequest
     * @param candidateProfile
     */
    void updateEntity(CandidateProfileDetailRequest candidateProfileRequest, @MappingTarget CandidateProfile candidateProfile);

    /**
     * chuyển doi tu CandidateContactInfoRequest -> CandidateProfile
     * <p>
     * Các thuộc tính email cần tự cập nhật sau khi gọi hàm này     
     * @param candidateProfileRequest
     * @param candidateProfile
     */
    @Mapping(target = "location", ignore = true)
    void updateEntity(CandidateContactInfoRequest candidateProfileRequest, @MappingTarget CandidateProfile candidateProfile);


    /**
     * chuyển doi tu CandidateProfile -> CandidateProfileResponse
     * <p>
     * Các thuộc tính avatarUrl, email, socialLink cần tự cập nhật sau khi gọi hàm này
     * @param candidateProfile
     * @return
     */
    CandidateProfileResponse toDTO(CandidateProfile candidateProfile);
}
