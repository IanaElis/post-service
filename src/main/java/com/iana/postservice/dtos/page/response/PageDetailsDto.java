package com.iana.postservice.dtos.page.response;

import com.iana.postservice.entities.enums.PageType;

public record PageDetailsDto(
        int id,
        String title,
        String description,
        PageType pageType,
        Integer parentPageId,
        Integer departmentId,
        int followersCount
) {}
