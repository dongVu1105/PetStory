package com.dongVu1105.profile_service.service;

import com.dongVu1105.profile_service.dto.request.FollowEvent;
import com.dongVu1105.profile_service.dto.request.FollowRequest;
import com.dongVu1105.profile_service.dto.response.FollowResponse;
import com.dongVu1105.profile_service.entity.Follow;
import com.dongVu1105.profile_service.exception.AppException;
import com.dongVu1105.profile_service.exception.ErrorCode;
import com.dongVu1105.profile_service.mapper.FollowMapper;
import com.dongVu1105.profile_service.repository.FollowRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FollowService {
    FollowRepository followRepository;
    FollowMapper followMapper;
    DateTimeFormatter dateTimeFormatter;
    KafkaTemplate<String, Object> kafkaTemplate;

    // follower: the people who follow us

    public FollowResponse followOrUnfollow (FollowRequest request){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if(userId.equals(request.getFollowingId())){
            throw new AppException(ErrorCode.CANNOT_FOLLOW);
        }
        Follow follow = followRepository.findByFollowingIdAndAndFollowerId(request.getFollowingId(), userId);
        if(Objects.isNull(follow)){
            follow = Follow.builder()
                    .followingId(request.getFollowingId())
                    .followerId(userId)
                    .createdDate(Instant.now())
                    .build();
            follow = followRepository.save(follow);
            FollowResponse followResponse = toFollowResponse(follow, true);
            kafkaTemplate.send("follow-notification", FollowEvent.builder()
                    .subject("FOLLOW NOTIFICATION")
                    .followingId(followResponse.getFollowingId())
                    .followerId(followResponse.getFollowerId())
                    .createdDate(followResponse.getCreatedDate())
                    .isFollow(followResponse.isFollow())
                    .build());
            return followResponse;
        } else {
            followRepository.deleteById(follow.getId());
            return toFollowResponse(follow, false);
        }
    }

    // the people who follow me
    public List<FollowResponse> findAllMyFollowers (){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Follow> followers = followRepository.findAllByFollowingId(userId);
        return followers.stream().map(follow -> {
            return toFollowResponse(follow, true);
        }).toList();
    }

    // the people who I follow
    public List<FollowResponse> findAllMyFollowings (){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Follow> followings = followRepository.findAllByFollowerId(userId);
        return followings.stream().map(follow -> {
            return toFollowResponse(follow, true);
        }).toList();
    }

    private FollowResponse toFollowResponse (Follow follow, boolean isFollow){
        FollowResponse followResponse = followMapper.toFollowResponse(follow);
        followResponse.setCreatedDate(dateTimeFormatter.format(follow.getCreatedDate()));
        followResponse.setFollow(isFollow);
        return followResponse;
    }
}
