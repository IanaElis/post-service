package com.iana.postservice.dtos.post.response;

import com.iana.postservice.entities.enums.PostStatus;

import java.time.Instant;

public record PostLightResponseDto(
        int id,
        int pageId,
        String pageTitle,
        PostStatus status,
        Instant timestamp
) {
}
