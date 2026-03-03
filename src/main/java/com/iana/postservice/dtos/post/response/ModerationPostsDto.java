package com.iana.postservice.dtos.post.response;

import com.iana.postservice.dtos.post.AuthorDto;
import com.iana.postservice.dtos.post.MediaDto;
import com.iana.postservice.dtos.post.PostModerationInfoDto;
import com.iana.postservice.entities.PostStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ModerationViewDto(
        Integer id,
        Integer pageId,
        AuthorDto author,
        PostStatus status,
        String contentText,
        LocalDateTime createdAt,
        List<MediaDto> media
) {
}
