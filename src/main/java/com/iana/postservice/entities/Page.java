package com.iana.postservice.entities;

import com.iana.postservice.entities.enums.PageType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

@Entity
@Table(name = "pages")
public class Page{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    @Size(min = 4, max = 255)
    private String title;

    @Column(name = "description")
    @Size(max = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "page_type")
    private PageType pageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_page_id")
    private Page parentPage;

    @Column(name = "department_id", nullable = false)
    private Integer departmentId; // кафедра

    @Column(name = "followers_count", nullable = false)
    private int followersCount;

    @Column(name = "created_at",
            insertable = false,
            updatable = false,
            nullable = false)
    private Instant createdAt;


    public Page(){}

    public Page(String title, String description, PageType pageType, Page parentPage,
                int departmentId, int followersCount) {
        this.title = title;
        this.description = description;
        this.pageType = pageType;
        this.parentPage = parentPage;
        this.departmentId = departmentId;
        this.followersCount = followersCount;
    }

    public Integer getId() {
        return id;
    }

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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public PageType getPageType() {
        return pageType;
    }

    public void setPageType(PageType pageType) {
        this.pageType = pageType;
    }

    public Page getParentPage() {
        return parentPage;
    }

    public void setParentPage(Page parentPage) {
        this.parentPage = parentPage;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public void incrementFollowersCount() {
        this.followersCount++;
    }

    public void decrementFollowersCount() {
        if (this.followersCount > 0) followersCount--;
    }

    @Override
    public String toString() {
        return "Page{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pageType=" + pageType +
                ", parentPage=" + parentPage +
                ", departmentId=" + departmentId +
                ", followersCount=" + followersCount +
                ", time=" + createdAt +
                '}';
    }
}
