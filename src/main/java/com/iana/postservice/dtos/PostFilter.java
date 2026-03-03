package com.iana.postservice.dtos;

import com.iana.postservice.entities.PostStatus;

import java.time.LocalDateTime;

public record PostFilterFull (
    PostStatus status,
    Integer pageId,
    Long authorId
){}
