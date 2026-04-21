package com.iana.postservice.dtos.post.response;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.iana.postservice.dtos.post.UserDto;
import com.iana.postservice.entities.enums.PostStatus;

import java.time.Instant;
import java.util.List;

@JsonTypeName("POST")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        defaultImpl = ModerationPostDto.class
)
public record ModerationPostDto(
        int id,
        int pageId,
        String pageTitle,
        UserDto user,
        PostStatus status,
        String contentText,
        Instant createdAt,
        List<Integer> media
) {
}
