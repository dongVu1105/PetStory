package com.dongVu1105.profile_service.service;

import com.dongVu1105.profile_service.dto.request.ProfileCreationRequest;
import com.dongVu1105.profile_service.dto.request.ProfileUpdationRequest;
import com.dongVu1105.profile_service.dto.request.SearchRequest;
import com.dongVu1105.profile_service.dto.response.FileResponse;
import com.dongVu1105.profile_service.dto.response.ProfileResponse;
import com.dongVu1105.profile_service.entity.UserProfile;
import com.dongVu1105.profile_service.exception.AppException;
import com.dongVu1105.profile_service.exception.ErrorCode;
import com.dongVu1105.profile_service.mapper.ProfileMapper;
import com.dongVu1105.profile_service.repository.ProfileRepository;
import com.dongVu1105.profile_service.repository.httpclient.FileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileService {
    ProfileRepository profileRepository;
    ProfileMapper profileMapper;
    FileClient fileClient;

    public ProfileResponse create (ProfileCreationRequest request){
        UserProfile userProfile = profileMapper.toUserProfile(request);
        return profileMapper.toProfileResponse(profileRepository.save(userProfile));
    }

    public ProfileResponse updateMyProfile (ProfileUpdationRequest request){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfile userProfile = profileRepository.findByUserId(userId).orElseThrow(
                () -> new AppException(ErrorCode.PROFILE_NOT_EXISTED));
        profileMapper.update(userProfile, request);
        return profileMapper.toProfileResponse(profileRepository.save(userProfile));
    }

    public ProfileResponse getMyProfile (){

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfile userProfile = profileRepository.findByUserId(userId).orElseThrow(
                () -> new AppException(ErrorCode.PROFILE_NOT_EXISTED));

        return profileMapper.toProfileResponse(userProfile);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ProfileResponse> getAllProfile (){
        return profileRepository.findAll().stream().map(profileMapper::toProfileResponse).toList();
    }

    public ProfileResponse getProfileByUserId (String userId){
        UserProfile userProfile = profileRepository.findByUserId(userId).orElseThrow(
                () -> new AppException(ErrorCode.PROFILE_NOT_EXISTED));
        return profileMapper.toProfileResponse(userProfile);
    }

    public ProfileResponse updateAvatar (MultipartFile file){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfile userProfile = profileRepository.findByUserId(userId).orElseThrow(
                ()-> new AppException(ErrorCode.PROFILE_NOT_EXISTED));
        FileResponse fileResponse = fileClient.uploadMedia(file).getResult();
        userProfile.setAvatar(fileResponse.getUrl());
        return profileMapper.toProfileResponse(profileRepository.save(userProfile));


    }

    public List<ProfileResponse> search (SearchRequest request){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserProfile> userProfiles = profileRepository.findAllByUsernameLike(request.getKey());
        return userProfiles.stream()
                .filter(userProfile -> !userId.equals(userProfile.getUserId()))
                .map(profileMapper::toProfileResponse)
                .toList();
    }
}
