package com.iana.postservice.dtos.post.response;

public record ModerationRequestDto(
        String type,
        ModerationPostDto post
) {
}
