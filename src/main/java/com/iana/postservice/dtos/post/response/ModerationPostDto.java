package com.iana.postservice.dtos.post.response;

import com.iana.postservice.dtos.post.UserDto;
import com.iana.postservice.entities.enums.PostStatus;

import java.time.Instant;
import java.util.List;

public record ModerationPostDto(
        String type,
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
