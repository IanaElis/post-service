package com.iana.postservice.services;

import com.iana.postservice.entities.Follower;
import com.iana.postservice.entities.Page;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void followPage_pageNotFound_shouldThrow() {
        when(pageRepository.findByIdOptional(anyLong(), any()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> pageService.followPage(1, 1L)
        );
    }
}
