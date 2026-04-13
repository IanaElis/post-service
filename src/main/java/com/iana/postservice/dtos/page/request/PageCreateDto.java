package com.iana.postservice.dtos.page.request;

import com.iana.postservice.entities.enums.PageType;
import jakarta.validation.constraints.Size;

public class PageCreateDto {
    @Size(min = 4, max = 255)
    private String title;
    @Size(max = 255)
    private String description;
    private PageType pageType;
    private Integer parentPageId;
    private Integer departmentId;

    public PageCreateDto(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
}
