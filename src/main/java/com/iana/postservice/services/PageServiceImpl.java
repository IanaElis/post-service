package com.iana.postservice.services;

import com.iana.postservice.entities.Page;
import com.iana.postservice.repositories.PageRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.glassfish.jaxb.core.v2.TODO;

import java.util.List;

@ApplicationScoped
public class PageService {

    private final PageRepository pageRepository;

    @Inject
    public PageService(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }
    // pageExists(pageId)
    // canUserPost(userId, pageId)
    //validateFollowNotExist() if tap twice

    // getPageDetails(Integer pageId)
    // getPagesUserFollows(Long userId)
    // getUsersFollowingPage(Integer pageId)
   //ToDo:  public List<Integer> getUsersFollowingPage(Integer pageId){}
    // followPage(Integer pageId, Long userId)
    // unfollowPage(Integer pageId, Long userId)
}
