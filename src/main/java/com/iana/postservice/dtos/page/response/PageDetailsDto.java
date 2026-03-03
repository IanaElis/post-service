package com.iana.postservice.dtos.page;

import com.iana.postservice.entities.PageType;

public class PageDetailsDto {
    Integer id;
    String title;
    String description;
    PageType pageType;
    Integer parentPageId;
    int followersCount;
}
