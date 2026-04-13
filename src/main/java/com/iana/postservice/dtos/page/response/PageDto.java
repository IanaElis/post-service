package com.iana.postservice.dtos.page.response;

import com.iana.postservice.entities.enums.PageType;

import java.time.Instant;

public class PageDto {
    int id;
    String title;
    PageType pageType;
    Integer parentPageId;
    int followersCount;
    Instant createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PageType getPageType() {
        return pageType;
    }

    public void setPageType(PageType pageType) {
        this.pageType = pageType;
    }

    public Integer getParentPageId() {
        return parentPageId;
    }

    public void setParentPageId(Integer parentPageId) {
        this.parentPageId = parentPageId;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
