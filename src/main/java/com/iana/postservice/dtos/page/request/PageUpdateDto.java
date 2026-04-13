package com.iana.postservice.dtos.page.request;

import jakarta.validation.constraints.Size;

public class PageUpdateDto {
    @Size(min = 4, max = 255)
    private String title;
    @Size(max = 255)
    private String description;

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
}
