package com.iana.postservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "post_media")
public class PostMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "media_id")
    private Integer mediaId;


//    @Enumerated(EnumType.STRING)
//    @Column(name = "media_type")
//    @NotNull
//    private MediaType mediaType;
//
//    @Column(name = "media_url")
//    @NotBlank
//    @Size(max = 2000)
//    private String mediaUrl;
//
//    @Column(name = "file_size")
//    private Long fileSize;

    public PostMedia() {}

    public PostMedia(Integer mediaId, Post post) {
        this.mediaId = mediaId;
        this.post = post;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
