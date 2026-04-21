package com.iana.postservice.dtos.page.response;

import com.iana.postservice.entities.enums.PageType;

import java.time.Instant;

public record PageDto(
    int id,
    String title,
    PageType pageType,
    Integer parentPageId,
    int followersCount,
    Instant createdAt
){}
