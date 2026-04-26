package com.iana.postservice.services;

import com.iana.postservice.dtos.page.request.PageCreateDto;
import com.iana.postservice.dtos.page.request.PageUpdateDto;
import com.iana.postservice.dtos.page.response.PageDetailsDto;
import com.iana.postservice.dtos.page.response.PageDto;
import com.iana.postservice.dtos.page.response.PageFollowedDto;
import com.iana.postservice.dtos.page.response.PageLightDto;
import com.iana.postservice.entities.Follower;
import com.iana.postservice.entities.Page;
import com.iana.postservice.entities.enums.PageType;
import com.iana.postservice.mappers.PageMapper;
import com.iana.postservice.repositories.FollowerRepository;
import com.iana.postservice.repositories.PageRepository;
import com.iana.postservice.services.impl.PageServiceImpl;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PageServiceTest {
    @InjectMocks
    private PageServiceImpl pageService;

    @Mock
    private PageRepository pageRepository;

    @Mock
    private FollowerRepository followerRepository;

    @Mock
    private PageMapper pageMapper;


    //followPage
    @Test
    void followPage_success_persistIncrement() {
        Page page = new Page();
        page.setId(1);
        page.setFollowersCount(0);

        when(pageRepository.findByIdOptional(anyLong(), any()))
                .thenReturn(Optional.of(page));

        when(followerRepository.exists(1L, 1)).thenReturn(false);

        pageService.followPage(1, 1L);

        verify(followerRepository, times(1))
                .persist(any(Follower.class));
        assertEquals(1, page.getFollowersCount());
    }

    @Test
    void followPage_alreadyFollowing_doNothing() {
        Page page = new Page();
        page.setId(1);
        page.setFollowersCount(0);

        when(pageRepository.findByIdOptional(anyLong(), any()))
                .thenReturn(Optional.of(page));

        when(followerRepository.exists(1L, 1)).thenReturn(true);

        pageService.followPage(1, 1L);

        verify(followerRepository, never()).persist((Follower) any());
        assertEquals(0, page.getFollowersCount());
    }

    @Test
    void followPage_pageNotFound_throwException() {
        when(pageRepository.findByIdOptional(anyLong(), any()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> pageService.followPage(1, 1L)
        );
    }

    @Test
    void followPage_repoFail_exception() {
        when(pageRepository.findByIdOptional(eq(1L), any()))
                .thenThrow(new RuntimeException("DB down"));

        assertThrows(RuntimeException.class,
                () -> pageService.followPage(1, 1L));
    }


    //unfollowPage
    @Test
    void unfollowPage_success_decreaseFollowers() {
        Page page = new Page();
        page.setId(1);
        page.setFollowersCount(5);

        when(pageRepository.findByIdOptional(eq(1L), any()))
                .thenReturn(Optional.of(page));

        when(followerRepository.delete(1L, 1)).thenReturn(true);

        pageService.unfollowPage(1, 1L);

        assertEquals(4, page.getFollowersCount());
    }

    @Test
    void unfollowPage_noFollower_doNothing() {
        Page page = new Page();
        page.setId(1);
        page.setFollowersCount(5);

        when(pageRepository.findByIdOptional(eq(1L), any()))
                .thenReturn(Optional.of(page));

        when(followerRepository.delete(1L, 1)).thenReturn(false);

        pageService.unfollowPage(1, 1L);

        assertEquals(5, page.getFollowersCount());
    }

    @Test
    void unfollowPage_pageMissing_throwNotFound() {
        when(pageRepository.findByIdOptional(eq(1L), any()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> pageService.unfollowPage(1, 1L));
    }

    @Test
    void unfollowPage_repoFailure_throwException() {
        when(pageRepository.findByIdOptional(eq(1L), any()))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class,
                () -> pageService.unfollowPage(1, 1L));
    }

    //getPageInfo
    @Test
    void getPageInfo_success() {
        Page page = new Page();

        when(pageRepository.findByIdOptional(1L))
                .thenReturn(Optional.of(page));

        when(pageMapper.toPageDetailsDto(page))
                .thenReturn(mock(PageDetailsDto.class));

        assertNotNull(pageService.getPageInfo(1));
    }

    @Test
    void getPageInfo_notFound_shouldThrow() {
        when(pageRepository.findByIdOptional(1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> pageService.getPageInfo(1));
    }


    //create page
    @Test
    void createPage_faculty_persist() {
        PageCreateDto dto = mock(PageCreateDto.class);

        when(dto.pageType()).thenReturn(PageType.FACULTY);

        Page page = new Page();
        page.setPageType(PageType.FACULTY);

        when(pageMapper.toPage(dto)).thenReturn(page);
        when(pageMapper.toPageDetailsDto(any())).thenReturn(mock(PageDetailsDto.class));

        pageService.createPage(dto);

        verify(pageRepository).persist(page);
        assertEquals(0, page.getFollowersCount());
    }

    @Test
    void createPage_department_persist() {
        PageCreateDto dto = mock(PageCreateDto.class);

        when(dto.pageType()).thenReturn(PageType.DEPARTMENT);

        Page page = new Page();
        page.setPageType(PageType.DEPARTMENT);

        Page parentPage = new Page();
        parentPage.setPageType(PageType.FACULTY);

        when(pageMapper.toPage(dto)).thenReturn(page);
        when(dto.parentPageId()).thenReturn(10);
        when(pageRepository.findByIdOptional(10L))
                .thenReturn(Optional.of(parentPage));

        when(pageMapper.toPageDetailsDto(any())).thenReturn(mock(PageDetailsDto.class));

        pageService.createPage(dto);

        verify(pageRepository).persist(page);
        assertEquals(0, page.getFollowersCount());
    }

    @Test
    void createPage_departmentWithoutParent_exception() {
        PageCreateDto dto = mock(PageCreateDto.class);

        when(dto.pageType()).thenReturn(PageType.DEPARTMENT);
        when(dto.parentPageId()).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> pageService.createPage(dto));
    }

    @Test
    void createPage_invalidParent_throwNotFound() {
        PageCreateDto dto = mock(PageCreateDto.class);

        when(dto.pageType()).thenReturn(PageType.DEPARTMENT);
        when(dto.parentPageId()).thenReturn(10);

        when(pageRepository.findByIdOptional(10L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> pageService.createPage(dto));
    }

    @Test
    void createPage_parentNotFaculty_exception() {
        PageCreateDto dto = mock(PageCreateDto.class);

        Page parent = new Page();
        parent.setPageType(PageType.DEPARTMENT);

        when(dto.pageType()).thenReturn(PageType.DEPARTMENT);
        when(dto.parentPageId()).thenReturn(10);

        when(pageRepository.findByIdOptional(10L))
                .thenReturn(Optional.of(parent));

        assertThrows(IllegalStateException.class,
                () -> pageService.createPage(dto));
    }

    @Test
    void createPage_unsupportedType_throwException() {
        PageCreateDto dto = mock(PageCreateDto.class);

        when(dto.pageType()).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> pageService.createPage(dto));
    }


    //updatePage
    @Test
    void updatePage_success() {
        Page page = new Page();
        page.setTitle("Old");

        PageUpdateDto dto = mock(PageUpdateDto.class);

        when(dto.title()).thenReturn("New");
        when(dto.description()).thenReturn("Desc");

        when(pageRepository.findByIdOptional(1L)).thenReturn(Optional.of(page));
        when(pageMapper.toPageDetailsDto(page)).thenReturn(mock(PageDetailsDto.class));

        pageService.updatePage(1, dto);

        assertEquals("New", page.getTitle());
        assertEquals("Desc", page.getDescription());
    }

    @Test
    void updatePage_notFound_throwNotFound() {
        when(pageRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> pageService.updatePage(1, mock(PageUpdateDto.class)));
    }

    @Test
    void updatePage_sameTitle_notModify() {
        Page page = new Page();
        page.setTitle("Same");

        PageUpdateDto dto = mock(PageUpdateDto.class);

        when(dto.title()).thenReturn("Same");
        when(pageRepository.findByIdOptional(1L)).thenReturn(Optional.of(page));

        pageService.updatePage(1, dto);

        assertEquals("Same", page.getTitle());
    }


    //deletePage
    @Test
    void deletePage_success() {
        when(pageRepository.deleteById(1L)).thenReturn(true);

        pageService.deletePage(1);

        verify(pageRepository).deleteById(1L);
    }

    @Test
    void deletePage_notFound_throwNotFound() {
        when(pageRepository.deleteById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> pageService.deletePage(1));
    }

//    getFacultyPageList();
//    getPageDropDown();
//    getPageTypes();
//    getFollowers(Integer pageId);
//    getAllPagesList();
//    getPagesUserFollows(Long userId);

}
