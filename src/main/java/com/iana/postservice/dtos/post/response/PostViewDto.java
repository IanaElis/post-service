package com.iana.postservice.dtos.post.response;

import com.iana.postservice.dtos.post.AuthorDto;
import com.iana.postservice.dtos.post.MediaDto;

import java.time.LocalDateTime;
import java.util.List;

public record PostDto(
        int id,
        AuthorDto author,
        String contentText,
        LocalDateTime createdAt,
        List<Integer> mediaList
) {
}
