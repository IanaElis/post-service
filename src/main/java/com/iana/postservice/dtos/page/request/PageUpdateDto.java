package com.iana.postservice.dtos.page.request;

import jakarta.validation.constraints.Size;

public record PageUpdateDto(
    @Size(min = 4, max = 255)
    String title,
    @Size(max = 255)
    String description
){}

