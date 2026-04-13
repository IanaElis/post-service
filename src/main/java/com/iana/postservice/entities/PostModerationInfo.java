//package com.iana.postservice.entities;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "posts_moderation_info")
//public class PostModerationInfo {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "post_id")
//    private Post post;
//
//    @Column(name = "moderated_by")
//    @NotNull
//    private Long moderatedBy;
//
//    @Column(name = "comment")
//    @Size(max = 500)
//    private String comment;
//
//    @Column(name = "altered_at", nullable = false)
//    private LocalDateTime alteredAt;
//
//    public PostModerationInfo() {}
//
//    public PostModerationInfo(Post post, Long moderatedBy, String comment, LocalDateTime alteredAt) {
//        this.post = post;
//        this.moderatedBy = moderatedBy;
//        this.comment = comment;
//        this.alteredAt = alteredAt;
//    }
//
//    public Post getPost() {
//        return post;
//    }
//
//    public void setPost(Post post) {
//        this.post = post;
//    }
//
//    public Long getModeratedBy() {
//        return moderatedBy;
//    }
//
//    public void setModeratedBy(Long moderatedBy) {
//        this.moderatedBy = moderatedBy;
//    }
//
//    public String getComment() {
//        return comment;
//    }
//
//    public void setComment(String comment) {
//        this.comment = comment;
//    }
//}
