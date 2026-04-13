package com.iana.postservice.dtos.post.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record PostRequestDto(
    @NotBlank
    String contentText,
    List<Integer> mediaIds
){}
