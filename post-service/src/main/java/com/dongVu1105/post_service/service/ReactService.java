package com.dongVu1105.post_service.service;

import com.dongVu1105.post_service.dto.request.ReactEvent;
import com.dongVu1105.post_service.dto.request.ReactRequest;
import com.dongVu1105.post_service.dto.response.ReactQuantityResponse;
import com.dongVu1105.post_service.dto.response.ReactResponse;
import com.dongVu1105.post_service.entity.React;
import com.dongVu1105.post_service.exception.AppException;
import com.dongVu1105.post_service.exception.ErrorCode;
import com.dongVu1105.post_service.mapper.ReactMapper;
import com.dongVu1105.post_service.repository.PostRepository;
import com.dongVu1105.post_service.repository.ReactRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ReactService {
    ReactRepository reactRepository;
    ReactMapper reactMapper;
    DateTimeFormatter dateTimeFormatter;
    KafkaTemplate<String, Object> kafkaTemplate;
    PostRepository postRepository;

    public ReactResponse click (ReactRequest request){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        React react = reactRepository.findByPostIdAndAndUserId(request.getPostId(), userId);
        if(Objects.isNull(react)){
            react = reactMapper.toReact(request);
            react.setUserId(userId);
            react.setCreatedDate(Instant.now());
            react = reactRepository.save(react);
            ReactResponse reactResponse = toReactResponse(react, true);
            ReactEvent reactEvent = ReactEvent.builder()
                    .subject("react notification")
                    .postId(reactResponse.getPostId())
                    .userId(reactResponse.getUserId())
                    .postOwnerId(postRepository.findById(reactResponse.getPostId()).orElseThrow(
                            () -> new AppException(ErrorCode.POST_NOT_EXISTED)).getUserId())
                    .createdDate(reactResponse.getCreatedDate())
                    .isReact(reactResponse.isReact())
                    .quantity(reactRepository.countByPostId(reactResponse.getPostId()))
                    .build();
            System.out.println("chuan bi gui kafka");
            kafkaTemplate.send("react-notification", reactEvent);
            return reactResponse;
        } else {
            reactRepository.deleteById(react.getId());
            return toReactResponse(react, false);
        }
    }

    public List<ReactResponse> findAllByPostId (String postId){
        return reactRepository.findAllByPostId(postId).stream().map(react -> {
            ReactResponse reactResponse = toReactResponse(react, true);
            return reactResponse;
        }).toList();
    }

    public ReactQuantityResponse countByPostId(String postId){
        return ReactQuantityResponse.builder()
                .quantity(reactRepository.countByPostId(postId))
                .build();
    }

    private ReactResponse toReactResponse (React react, boolean isReact){
        ReactResponse reactResponse = reactMapper.toReactResponse(react);
        reactResponse.setCreatedDate(dateTimeFormatter.format(react.getCreatedDate()));
        reactResponse.setReact(isReact);
        return  reactResponse;
    }
}
