package com.ptit.thesis.smartrecruit.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ptit.thesis.smartrecruit.dto.common.SocialLinkDTO;
import com.ptit.thesis.smartrecruit.dto.request.CandidateBasicInfoRequest;
import com.ptit.thesis.smartrecruit.dto.request.CandidateContactInfoRequest;
import com.ptit.thesis.smartrecruit.dto.request.CandidateProfileDetailRequest;
import com.ptit.thesis.smartrecruit.dto.response.CandidateProfileResponse;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.Location;
import com.ptit.thesis.smartrecruit.entity.SocialLink;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.LinkableType;
import com.ptit.thesis.smartrecruit.exception.S3ErrorException;
import com.ptit.thesis.smartrecruit.mapper.CandidateProfileMapper;
import com.ptit.thesis.smartrecruit.mapper.LocationMapper;
import com.ptit.thesis.smartrecruit.mapper.SocialLinkMapper;
import com.ptit.thesis.smartrecruit.repository.CandidateProfileRepository;
import com.ptit.thesis.smartrecruit.repository.LocationRepository;
import com.ptit.thesis.smartrecruit.repository.SocialLinkRepository;
import com.ptit.thesis.smartrecruit.repository.UserRepository;
import com.ptit.thesis.smartrecruit.service.CandidateProfileService;
import com.ptit.thesis.smartrecruit.service.S3Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CandidateProfileServiceImpl implements CandidateProfileService {

    CandidateProfileRepository candidateProfileRepository;
    SocialLinkRepository socialLinkRepository;
    UserRepository userRepository;
    LocationRepository locationRepository;

    SocialLinkMapper socialLinkMapper;
    CandidateProfileMapper candidateProfileMapper;
    LocationMapper locationMapper;

    S3Service s3Service;

    @Override
    @Transactional
    public CandidateProfileResponse updateProfile(CandidateBasicInfoRequest request, User user) {

        CandidateProfile candidateProfile = candidateProfileRepository.findByUser(user)
                .orElseThrow(
                        () -> new EntityNotFoundException("Candidate profile not found for user: " + user.getId()));
        candidateProfileMapper.updateEntity(request, candidateProfile);

        CandidateProfile savedCandidateProfile = candidateProfileRepository.save(candidateProfile);

        return toResponseDTO(savedCandidateProfile, user);
    }

    @Override
    @Transactional
    public CandidateProfileResponse updateProfile(CandidateProfileDetailRequest request, User user) {
        CandidateProfile candidateProfile = candidateProfileRepository.findByUser(user)
                .orElseThrow(
                        () -> new EntityNotFoundException("Candidate profile not found for user: " + user.getId()));
        candidateProfileMapper.updateEntity(request, candidateProfile);

        CandidateProfile savedCandidateProfile = candidateProfileRepository.save(candidateProfile);

        return toResponseDTO(savedCandidateProfile, user);
    }

    @Override
    @Transactional
    public CandidateProfileResponse updateProfile(List<SocialLinkDTO> socialLinks, User user) {
        CandidateProfile candidateProfile = candidateProfileRepository.findByUser(user)
                .orElseThrow(
                        () -> new EntityNotFoundException("Candidate profile not found for user: " + user.getId()));
        socialLinkRepository.deleteAllByLinkableIdAndLinkableType(candidateProfile.getId(),
                LinkableType.CANDIDATE_PROFILE);

        List<SocialLink> socialLinkEntities = socialLinkMapper.toSocialLinks(socialLinks).stream()
                .map(socialLink -> {
                    socialLink.setLinkableId(candidateProfile.getId());
                    socialLink.setLinkableType(LinkableType.CANDIDATE_PROFILE);
                    return socialLink;
                }).toList();
        socialLinkRepository.saveAll(socialLinkEntities);

        return toResponseDTO(candidateProfile, user);
    }

    @Override
    @Transactional
    public CandidateProfileResponse updateProfile(CandidateContactInfoRequest request, User user) {
        CandidateProfile candidateProfile = candidateProfileRepository.findByUser(user)
                .orElseThrow(
                        () -> new EntityNotFoundException("Candidate profile not found for user: " + user.getId()));
        candidateProfileMapper.updateEntity(request, candidateProfile);

        user.setEmail(request.getEmail());
        User savedUser = userRepository.save(user);

        // location
        Location location = locationMapper.toLocationEntity(request.getLocation());
        Location savedLocation = locationRepository
                .findByLatitudeAndLongitude(location.getLatitude(), location.getLongitude())
                .orElseGet(() -> {
                    return locationRepository.save(location);
                });

        candidateProfile.setLocation(savedLocation);

        CandidateProfile savedCandidateProfile = candidateProfileRepository.save(candidateProfile);

        return toResponseDTO(savedCandidateProfile, savedUser);
    }

    @Override
    public void uploadAvatar(MultipartFile file, User user) {

        CandidateProfile candidateProfile = candidateProfileRepository.findByUser(user)
                .orElseThrow(
                        () -> new EntityNotFoundException("Candidate profile not found for user: " + user.getId()));

        try {
            String newAvatarKey = s3Service.uploadFile(file, "candidate/avatar/");
            String oldAvatarKey = candidateProfile.getAvatarUrl();
            candidateProfile.setAvatarUrl(newAvatarKey);
            if (oldAvatarKey != null) {
                s3Service.deleteFileByKey(oldAvatarKey);
            }
            candidateProfileRepository.save(candidateProfile);
        } catch (IOException e) {
            throw new S3ErrorException("Error uploading avatar to the S3: " + e.getMessage());
        }
    }

    /**
     * Cập nhật trường email, avatar url sau khi mapper
     * 
     * @param dto
     * @param user
     * @return dto
     */
    public CandidateProfileResponse toResponseDTO(CandidateProfile savedCandidateProfile, User user) {
        CandidateProfileResponse dto = candidateProfileMapper.toDTO(savedCandidateProfile);

        // xử lý mail, avatar url, social link
        if (savedCandidateProfile.getAvatarUrl() != null) {
            dto.setAvatarUrl(s3Service.generatePresignedUrl(savedCandidateProfile.getAvatarUrl()));
        }

        dto.setEmail(user.getEmail());

        List<SocialLink> socialLinks = socialLinkRepository
                .findByLinkableIdAndLinkableType(savedCandidateProfile.getId(), LinkableType.CANDIDATE_PROFILE);
        dto.setSocialLinks(socialLinkMapper.toSocialLinkDTOs(socialLinks));

        return dto;
    }
}
