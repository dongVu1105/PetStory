package com.dongVu1105.post_service.mapper;

import com.dongVu1105.post_service.dto.request.ReactRequest;
import com.dongVu1105.post_service.dto.response.ReactResponse;
import com.dongVu1105.post_service.entity.React;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReactMapper {
    React toReact (ReactRequest request);
    ReactResponse toReactResponse (React react);
}
