package com.dongVu1105.post_service.service;

import com.dongVu1105.post_service.dto.request.PostRequest;
import com.dongVu1105.post_service.dto.response.PageResponse;
import com.dongVu1105.post_service.dto.response.PostResponse;
import com.dongVu1105.post_service.entity.Post;
import com.dongVu1105.post_service.mapper.PostMapper;
import com.dongVu1105.post_service.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    DateTimeFormatter dateTimeFormatter;

    public PostResponse createPost (PostRequest request){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Post post = Post.builder()
                .content(request.getContent())
                .userId(userId)
                .createdDate(Instant.now())
                .modifiedDate(Instant.now())
                .build();
        post = postRepository.save(post);
        return postMapper.toPostResponse(post);
    }

    public PageResponse<PostResponse> getMyPost (int page, int size){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page -1, size, sort);
        Page<Post> postPage = postRepository.findAllByUserId(userId, pageable);

        var postData = postPage.getContent().stream().map(post -> {
            PostResponse postResponse = postMapper.toPostResponse(post);
            postResponse.setFormatedCreateDate(dateTimeFormatter.format(postResponse.getCreatedDate()));
            return postResponse;
        }).toList();

        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(postPage.getSize())
                .totalElements(postPage.getTotalElements())
                .totalPages(postPage.getTotalPages())
                .data(postData)
                .build();
    }
}
