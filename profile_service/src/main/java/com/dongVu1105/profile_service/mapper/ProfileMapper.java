package com.dongVu1105.profile_service.mapper;

import com.dongVu1105.profile_service.dto.request.ProfileCreationRequest;
import com.dongVu1105.profile_service.dto.request.ProfileUpdationRequest;
import com.dongVu1105.profile_service.dto.response.ProfileResponse;
import com.dongVu1105.profile_service.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    UserProfile toUserProfile (ProfileCreationRequest request);
    ProfileResponse toProfileResponse (UserProfile userProfile);

    void update (@MappingTarget UserProfile userProfile, ProfileUpdationRequest request);
}
