package com.dongVu1105.post_service.mapper;

import com.dongVu1105.post_service.dto.response.PostResponse;
import com.dongVu1105.post_service.entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponse toPostResponse (Post post);
}
