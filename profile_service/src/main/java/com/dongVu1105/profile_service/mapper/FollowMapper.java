package com.dongVu1105.profile_service.mapper;


import com.dongVu1105.profile_service.dto.request.FollowRequest;
import com.dongVu1105.profile_service.dto.response.FollowResponse;
import com.dongVu1105.profile_service.entity.Follow;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FollowMapper {
    Follow toFollow (FollowRequest request);
    FollowResponse toFollowResponse (Follow follow);
}
