package com.iana.postservice.dtos.page;

import com.iana.postservice.entities.PageType;

public class PageCreateDto {
    String title;
    String description;
    PageType pageType;
    Integer parentPageId;
}
