package com.iana.postservice.services.impl;

import com.iana.postservice.dtos.page.request.PageCreateDto;
import com.iana.postservice.dtos.page.request.PageUpdateDto;
import com.iana.postservice.dtos.page.response.PageDetailsDto;
import com.iana.postservice.dtos.page.response.PageLightDto;
import com.iana.postservice.dtos.page.response.PageDto;
import com.iana.postservice.entities.Follower;
import com.iana.postservice.entities.Page;
import com.iana.postservice.entities.enums.PageType;
import com.iana.postservice.mappers.PageMapper;
import com.iana.postservice.repositories.FollowerRepository;
import com.iana.postservice.repositories.PageRepository;
import com.iana.postservice.services.PageService;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class PageServiceImpl implements PageService {

    @Inject
    PageRepository pageRepository;
    @Inject
    FollowerRepository followerRepository;
    @Inject
    PageMapper pageMapper;

    @Transactional
    @Override
    public void followPage(Integer pageId, Long userId) {
        Page page = findByIdLocked(pageId);

        if(followerRepository.exists(userId, page.getId())){
            return;
        }

        followerRepository.persist(new Follower(userId, page));
        page.incrementFollowersCount();
    }

    @Transactional
    @Override
    public void unfollowPage(Integer pageId, Long userId) {
        Page page = findByIdLocked(pageId);
        boolean deleted = followerRepository.delete(userId, page.getId());

        if (deleted) {
            page.decrementFollowersCount();
        }
    }

    @Override
    public PageDetailsDto getPageInfo(Integer pageId) {
        Page page = findById(pageId);
        return pageMapper.toPageDetailsDto(page);
    }

    @Transactional
    @Override
    public PageDetailsDto createPage(PageCreateDto dto) {
        Page page = pageMapper.toPage(dto);

        if(dto.getPageType() == PageType.FACULTY){
            page.setParentPage(null);
        }
        else if(dto.getPageType() == PageType.DEPARTMENT){
            if(dto.getParentPageId() == null){
                throw new IllegalArgumentException("Parent page id is null");
            }

            Page parentPage = pageRepository.findByIdOptional((long)dto.getParentPageId())
                    .orElseThrow(() -> new NotFoundException("Parent page not found"));

            if (parentPage.getPageType() != PageType.FACULTY) {
                throw new IllegalStateException("Parent must be a faculty page");
            }

            page.setParentPage(parentPage);
        } else {
            throw new IllegalArgumentException("Unsupported page type: " + dto.getPageType());
        }

        page.setFollowersCount(0);
        pageRepository.persist(page);
        return pageMapper.toPageDetailsDto(page);
    }

    @Transactional
    @Override
    public PageDetailsDto updatePage(Integer pageId, PageUpdateDto dto) {
        Page page = findById(pageId);
        if(dto.getTitle() != null && !dto.getTitle().equals(page.getTitle())){
             page.setTitle(dto.getTitle());
        }
        page.setDescription(dto.getDescription());
        return pageMapper.toPageDetailsDto(page);
    }

    @Transactional
    @Override
    public void deletePage(Integer pageId){
        boolean deleted = pageRepository.deleteById((long)pageId);
        if (!deleted) {
            throw new NotFoundException("Page with id " + pageId + " not found");
        }
    }

    @Override
    public List<PageDto> getAllPagesList() {
        List<Page> pages = pageRepository.listAll(Sort.descending("createdAt"));
        return pageMapper.toPageDtoList(pages);
    }

    public List<PageLightDto> getFacultyPageList(){
        return pageMapper.toPageLightDtoList(
                pageRepository.findByPageType(PageType.FACULTY)
        );

    }
    public List<PageLightDto> getPageDropDown(){
        return pageMapper.toPageLightDtoList(
                pageRepository.listAll(Sort.ascending("title")));
    }

    public List<PageType> getPageTypes(){
        return List.of(PageType.FACULTY, PageType.DEPARTMENT);
    }

    public List<Long> getFollowers(Integer pageId){
        List<Follower> followers = followerRepository.findByPage(pageId);
        return followers.stream().map(Follower::getUserId).toList();
    }

    private Page findByIdLocked(Integer pageId) throws NotFoundException {
        return pageRepository.findByIdOptional((long) pageId, LockModeType.PESSIMISTIC_WRITE)
                .orElseThrow(() -> new NotFoundException("Page not found"));
    }

    private Page findById(Integer pageId) throws NotFoundException {
        return pageRepository.findByIdOptional((long) pageId)
                .orElseThrow(() -> new NotFoundException("Page not found"));
    }

    public int getFollowerCount(Page page){
        return (int) followerRepository.followerCount(page.getId());
    }

    //ToDo:     // getPagesUserFollows(Long userId)

}
