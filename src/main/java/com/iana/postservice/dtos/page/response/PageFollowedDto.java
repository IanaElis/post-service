package com.iana.postservice.dtos.page.response;

import com.iana.postservice.entities.enums.PageType;

public record PageFollowedDto(
        int id,
        String title,
        PageType pageType,
        boolean isFollowed
) {
}
