package com.iana.postservice.dtos.post.response;

import com.iana.postservice.entities.enums.PostStatus;

import java.time.Instant;
import java.util.List;

public record ModerationPostsDto(
        int id,
        int pageId,
        String pageTitle,
        Long authorId,
        PostStatus status,
        String contentText,
        Instant createdAt,
        List<Integer> media
) {
}
