package com.iana.postservice.services;

import com.iana.postservice.dtos.page.request.PageCreateDto;
import com.iana.postservice.dtos.page.response.PageDetailsDto;
import com.iana.postservice.dtos.page.response.PageFollowedDto;
import com.iana.postservice.dtos.page.response.PageLightDto;
import com.iana.postservice.dtos.page.response.PageDto;
import com.iana.postservice.dtos.page.request.PageUpdateDto;
import com.iana.postservice.entities.enums.PageType;

import java.util.List;

public interface PageService {
    void followPage(Integer pageId, Long userId);
    void unfollowPage(Integer pageId, Long userId);
    PageDetailsDto getPageInfo(Integer pageId);
    List<PageLightDto> getFacultyPageList();
    List<PageLightDto> getPageDropDown();
    List<PageType> getPageTypes();
    List<Long> getFollowers(Integer pageId);

    //for moderation
    PageDetailsDto createPage(PageCreateDto dto);
    PageDetailsDto updatePage(Integer pageId, PageUpdateDto dto);
    void deletePage(Integer pageId);
    List<PageDto> getAllPagesList();
    List<PageFollowedDto> getPagesUserFollows(Long userId);

}
