package com.iana.postservice.repositories;

import com.iana.postservice.entities.Follower;
import com.iana.postservice.entities.Page;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {
    // find by user id: return all pages to which this user is subscribed
    public List<Follower> findByUserId(Long userId) {
        return find("userId", userId).list();
    }
    // find by page: return all users that follow this page
    public List<Follower> findByPage(Integer pageId){
        return find("page.id", pageId).list();
    }

    public boolean exists(Long userId, Integer pageId) {
        return count("userId = ?1 and page.id = ?2", userId, pageId) > 0;
    }

    public long followerCount(Integer pageId){
        return find("page.id", pageId).count();
    }

    public boolean delete(Long userId, Integer pageId) {
        return delete("userId = ?1 and page.id = ?2", userId, pageId) > 0;
    }
}
