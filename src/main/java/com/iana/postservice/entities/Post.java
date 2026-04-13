package com.iana.postservice.entities;

import com.iana.postservice.entities.enums.PostStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private Page page;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "username")
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @NotNull
    private PostStatus status;

    @Column(name = "content_text")
    private String contentText;

    @Column(name = "created_at", insertable = false, updatable = false, nullable = false)
    private Instant createdAt;

    @Column(name = "published_at")
    private Instant publishedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostMedia> mediaList = new ArrayList<>();

    public Post(){}

    public Post(Page page, Long authorId, String username, PostStatus status, String contentText) {
        this.page = page;
        this.authorId = authorId;
        this.username = username;
        this.status = status;
        this.contentText = contentText;
    }

    public Integer getId() {
        return id;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public List<PostMedia> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<PostMedia> media) {
        this.mediaList = media;
    }



    public void addMedia(PostMedia media) {
        this.mediaList.add(media);
    }

    public void removeMedia(Integer mediaId) {

        this.mediaList.removeIf(media -> media.getMediaId().equals(mediaId));
    }
}
