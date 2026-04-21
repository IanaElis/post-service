package com.iana.postservice.repositories;

import com.iana.postservice.entities.Follower;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {
    // find by user id: return all pages to which this user is subscribed
    public Set<Integer> findByUserId(Long userId) {
        return find("select f.page.id from Follower f where f.userId = ?1", userId)
                .project(Integer.class)
                .stream()
                .collect(Collectors.toSet());
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
