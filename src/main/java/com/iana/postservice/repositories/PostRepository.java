package com.iana.postservice.repositories;

import com.iana.postservice.dtos.PageResult;
import com.iana.postservice.dtos.SliceResult;
import com.iana.postservice.entities.Post;
import com.iana.postservice.entities.enums.PostStatus;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.*;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {
    //find app posts by page
    public PageResult<Post> findByPage(Integer pageId, int pageNumber, int postsByPage) {
        PanacheQuery<Post> panacheQuery = find("page.id = ?1 order by createdAt desc", pageId);
        long totalElements = panacheQuery.count();

        List<Post> posts = panacheQuery
                .page(Page.of(pageNumber, postsByPage))
                .list();

        int totalPages = (int) Math.ceil((double) totalElements / postsByPage);

        return new PageResult<>(posts, pageNumber, postsByPage, totalElements, totalPages);
    }

    //find all posts by author
    public List<Post> findByAuthor(Long authorId, int pageNumber, int postsByPage) {
        return find("authorId = ?1", authorId).page(Page.of(pageNumber, postsByPage)).list();
    }

    //find posts by author and status
    public PageResult<Post> findByAuthorAndStatusAndPage(Long authorId,
                                                   PostStatus status,
                                                   Integer pageId,
                                                   int pageNumber, int postsByPage) {
        String query = "authorId = :authorId";
        Parameters params = Parameters.with("authorId", authorId);

        if (status != null) {
            query += " and status = :status";
            params.and("status", status);
        }

         if(pageId != null) {
             query += " and page.id = :pageId";
             params.and("pageId", pageId);
         }

         PanacheQuery<Post> panacheQuery = find(query + " order by createdAt desc", params);

         long totalElements = panacheQuery.count();

         List<Post> posts = panacheQuery
                .page(Page.of(pageNumber, postsByPage))
                .list();

         int totalPages = (int) Math.ceil((double) totalElements / postsByPage);

         return new PageResult<>(posts, pageNumber, postsByPage, totalElements, totalPages);
    }

    //find all posts of certain status
    public PageResult<Post> findByStatus(PostStatus status, int pageNumber, int postsByPage) {
        PanacheQuery<Post> panacheQuery = find("status = ?1 order by createdAt desc", status);
        long totalElements = panacheQuery.count();

        List<Post> posts = panacheQuery
                .page(Page.of(pageNumber, postsByPage))
                .list();

        int totalPages = (int) Math.ceil((double) totalElements / postsByPage);

        return new PageResult<>(posts, pageNumber, postsByPage, totalElements, totalPages);
    }

    //find all posts of certain status by page
    public SliceResult<Post> findByPageAndStatusPaginated(PostStatus status, Integer pageId,
                                                          Instant cursor, int postsByPage) {
        PanacheQuery<Post> panacheQuery;

        if (cursor == null) {
            panacheQuery = find("status = ?1 and page.id = ?2 order by publishedAt desc",
                    status, pageId);
        } else {
            // next pages
            panacheQuery = find("status = ?1 and page.id = ?2 and publishedAt < ?3 order by publishedAt desc",
                    status, pageId, cursor);
        }
     //   PanacheQuery<Post> panacheQuery = find("status = ?1 and page.id = ?2 order by publishedAt desc", status, pageId);
        List<Post> results = panacheQuery
                .page(Page.ofSize(postsByPage + 1))
                .list();

        boolean hasNext = results.size() > postsByPage;

        if (hasNext) {
            results = results.subList(0, postsByPage);
        }

        if (!results.isEmpty()) {
            Post last = results.getLast();
            cursor = last.getPublishedAt();
        }
        return new SliceResult<>(results, cursor, hasNext);
    }

    public PageResult<Post> findByPageAndStatus(PostStatus status, Integer pageId,
                                                 int pageNumber, int postsByPage) {
        PanacheQuery<Post> panacheQuery = find("status = ?1 and page.id = ?2 order by createdAt desc",
                status, pageId);

        long totalElements = panacheQuery.count();

        List<Post> posts = panacheQuery
                .page(Page.of(pageNumber, postsByPage))
                .list();

        int totalPages = (int) Math.ceil((double) totalElements / postsByPage);

        return new PageResult<>(posts, pageNumber, postsByPage, totalElements, totalPages);
    }

    public PageResult<Post> getAllPaginated(int pageNumber, int postsByPage) {
        PanacheQuery<Post> panacheQuery = findAll(Sort.descending("createdAt"));
        long totalElements = panacheQuery.count();

        List<Post> posts = panacheQuery
                .page(Page.of(pageNumber, postsByPage))
                .list();

        int totalPages = (int) Math.ceil((double) totalElements / postsByPage);

        return new PageResult<>(posts, pageNumber, postsByPage, totalElements, totalPages);
    }

    public boolean isAuthor(Integer postId, Long userId) {
        return count("id = ?1 and authorId = ?2", postId, userId) > 0;
    }
}
