package com.dongVu1105.post_service.service;

import com.dongVu1105.post_service.dto.request.PostRequest;
import com.dongVu1105.post_service.dto.response.FileResponse;
import com.dongVu1105.post_service.dto.response.PageResponse;
import com.dongVu1105.post_service.dto.response.PostResponse;
import com.dongVu1105.post_service.dto.response.ProfileResponse;
import com.dongVu1105.post_service.entity.Post;
import com.dongVu1105.post_service.exception.AppException;
import com.dongVu1105.post_service.exception.ErrorCode;
import com.dongVu1105.post_service.mapper.PostMapper;
import com.dongVu1105.post_service.repository.PostRepository;
import com.dongVu1105.post_service.repository.httpclient.FileClient;
import com.dongVu1105.post_service.repository.httpclient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    DateTimeFormatter dateTimeFormatter;
    ProfileClient profileClient;
    FileClient fileClient;

    public PostResponse createPost (PostRequest request, MultipartFile file){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String mediaUrl = "";
        if(Objects.nonNull(file)){
            FileResponse fileResponse = fileClient.uploadMedia(file).getResult();
            if(Objects.isNull(fileResponse)){
                throw new AppException(ErrorCode.UPLOAD_FILE_ERROR);
            }
            mediaUrl = fileResponse.getUrl();
        }
        Post post = Post.builder()
                .content(request.getContent())
                .mediaUrl(mediaUrl)
                .userId(userId)
                .createdDate(Instant.now())
                .modifiedDate(Instant.now())
                .build();
        post = postRepository.save(post);
        return toPostResponse(post);
    }

    public PageResponse<PostResponse> getMyPost (int page, int size){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page -1, size, sort);
        Page<Post> postPage = postRepository.findAllByUserId(userId, pageable);

        var postData = postPage.getContent().stream().map(this::toPostResponse).toList();

        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(postPage.getSize())
                .totalElements(postPage.getTotalElements())
                .totalPages(postPage.getTotalPages())
                .data(postData)
                .build();
    }

    private PostResponse toPostResponse (Post post){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        ProfileResponse profileResponse = null;

        try {
            profileResponse = profileClient.getProfile(userId).getResult();
        } catch (Exception e){
            log.error("Error while getting user profile", e);
        }
        String username = profileResponse != null ? profileResponse.getUsername() : null;

        PostResponse postResponse = postMapper.toPostResponse(post);
        postResponse.setFormatedCreateDate(dateTimeFormatter.format(postResponse.getCreatedDate()));
        postResponse.setUsername(username);
        return postResponse;
    }
}
