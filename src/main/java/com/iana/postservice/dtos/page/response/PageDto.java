package com.iana.postservice.dtos.page;

import com.iana.postservice.entities.PageType;

public class PageDto {
    Integer id;
    String title;
    PageType pageType;
    Integer parentPageId;
    int followersCount;
}
