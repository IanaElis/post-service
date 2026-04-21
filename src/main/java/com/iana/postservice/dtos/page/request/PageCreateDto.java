package com.iana.postservice.dtos.page.request;

import com.iana.postservice.entities.enums.PageType;
import jakarta.validation.constraints.Size;

public record PageCreateDto(
    @Size(min = 4, max = 255)
    String title,
    @Size(max = 255)
    String description,
    PageType pageType,
    Integer parentPageId
//    private Integer departmentId;
){}
