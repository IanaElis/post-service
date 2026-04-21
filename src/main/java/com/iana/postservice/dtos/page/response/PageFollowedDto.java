package com.iana.postservice.dtos.page.response;

public record PageFollowedDto(
        int id,
        String title,
        boolean isFollowed
) {
}
