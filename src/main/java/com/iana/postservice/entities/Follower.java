package com.iana.postservice.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "page_followers")
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id", nullable = false)
    private Page page;

    public Follower() {}
    public Follower(Long userId, Page page) {
        this.userId = userId;
        this.page = page;
    }

    public Long getUserId() {
        return userId;
    }
}
