package com.iana.postservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts_moderation_info")
public class PostMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "moderated_by")
    @NotNull
    private Long moderatedBy;

    @Column(name = "comment")
    @Size(max = 500)
    private String comment;

    @Column(name = "altered_at",
            insertable = false,
            updatable = false,
            nullable = false)
    private LocalDateTime alteredAt;
}
