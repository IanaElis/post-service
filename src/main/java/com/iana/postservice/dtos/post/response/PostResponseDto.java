package com.iana.postservice.dtos.post.response;

import com.iana.postservice.dtos.post.UserDto;
import com.iana.postservice.entities.enums.PostStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDto {
    int id;
    int pageId;
    UserDto author;
    PostStatus status;
    String contentText;
    Instant timestamp;
    List<Integer> media;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public UserDto getAuthor() {
        return author;
    }

    public void setAuthor(UserDto author) {
        this.author = author;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public List<Integer> getMedia() {
        return media;
    }

    public void setMedia(List<Integer> media) {
        this.media = media;
    }
}
