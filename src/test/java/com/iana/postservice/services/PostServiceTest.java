package com.iana.postservice.services;

import com.iana.postservice.dtos.PageResult;
import com.iana.postservice.dtos.SliceResult;
import com.iana.postservice.dtos.post.ModerationDecisionDto;
import com.iana.postservice.dtos.post.UserDto;
import com.iana.postservice.dtos.post.request.PostRequestDto;
import com.iana.postservice.dtos.post.response.AdminPostDto;
import com.iana.postservice.dtos.post.response.PostLightResponseDto;
import com.iana.postservice.dtos.post.response.PostResponseDto;
import com.iana.postservice.entities.Follower;
import com.iana.postservice.entities.Page;
import com.iana.postservice.entities.Post;
import com.iana.postservice.entities.PostMedia;
import com.iana.postservice.entities.enums.PostStatus;
import com.iana.postservice.mappers.PostMapper;
import com.iana.postservice.mappers.PostMediaMapper;
import com.iana.postservice.repositories.FollowerRepository;
import com.iana.postservice.repositories.PageRepository;
import com.iana.postservice.repositories.PostRepository;
import com.iana.postservice.services.impl.PageServiceImpl;
import com.iana.postservice.services.impl.PostServiceImpl;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    PostServiceImpl postService;
    @Mock
    PageServiceImpl pageService;
    @Mock
    PageRepository pageRepository;
    @Mock
    PostRepository postRepository;
    @Mock
    PostMapper postMapper;
    @Mock
    PostMediaMapper postMediaMapper;


//    createDraft(Integer pageId, PostRequestDto dto, UserDto user);
    @Test
    void createDraft_success() {
        Page page = new Page();
        page.setId(1);

        PostRequestDto dto = mock(PostRequestDto.class);
        when(dto.contentText()).thenReturn("hello");
        when(dto.mediaIds()).thenReturn(List.of(10, 20));

        UserDto user = new UserDto(1L, "user");

        when(pageRepository.findByIdOptional(1L))
                .thenReturn(Optional.of(page));

        when(postMediaMapper.toEntity(anyInt(), any()))
                .thenReturn(new PostMedia());

        Post post = new Post();
        when(postMapper.toPostResponseDto(any()))
                .thenReturn(mock(PostResponseDto.class));

        PostResponseDto result = postService.createDraft(1, dto, user);

        verify(postRepository).persist(any(Post.class));
        assertNotNull(result);
    }

    @Test
    void createDraft_pageNotFound_throwException() {
        when(pageRepository.findByIdOptional(1L))
                .thenReturn(Optional.empty());

        PostRequestDto dto = mock(PostRequestDto.class);
        UserDto user = new UserDto(1L, "user");

        assertThrows(NotFoundException.class,
                () -> postService.createDraft(1, dto, user));
    }

    @Test
    void createDraft_emptyMedia_create() {
        Page page = new Page();
        page.setId(1);

        PostRequestDto dto = mock(PostRequestDto.class);
        when(dto.contentText()).thenReturn("text");
        when(dto.mediaIds()).thenReturn(List.of());

        UserDto user = new UserDto(1L, "user");

        when(pageRepository.findByIdOptional(1L))
                .thenReturn(Optional.of(page));

        when(postMapper.toPostResponseDto(any()))
                .thenReturn(mock(PostResponseDto.class));

        PostResponseDto result = postService.createDraft(1, dto, user);

        verify(postRepository).persist(any(Post.class));
        assertNotNull(result);
    }

    @Test
    void createDraft_mapperFailure_throwException() {
        Page page = new Page();
        page.setId(1);

        PostRequestDto dto = mock(PostRequestDto.class);
        when(dto.mediaIds()).thenReturn(List.of(1));

        UserDto user = new UserDto(1L, "user");

        when(pageRepository.findByIdOptional(1L))
                .thenReturn(Optional.of(page));

        when(postMediaMapper.toEntity(anyInt(), any()))
                .thenThrow(new RuntimeException("mapper error"));

        assertThrows(RuntimeException.class,
                () -> postService.createDraft(1, dto, user));
    }

    //admin
//    createPost(Integer pageId, PostRequestDto dto, UserDto user);
    @Test
    void createPost_success() {
        Page page = new Page();
        page.setId(1);

        PostRequestDto dto = mock(PostRequestDto.class);
        when(dto.contentText()).thenReturn("content");
        when(dto.mediaIds()).thenReturn(List.of(1, 2));

        UserDto user = new UserDto(1L, "admin");

        when(pageRepository.findByIdOptional(1L))
                .thenReturn(Optional.of(page));

        when(postMediaMapper.toEntity(anyInt(), any()))
                .thenReturn(new PostMedia());

        when(postMapper.toPostResponseDto(any()))
                .thenReturn(mock(PostResponseDto.class));

        PostResponseDto result = postService.createPost(1, dto, user);

        verify(postRepository).persist(any(Post.class));
        assertNotNull(result);
    }

    @Test
    void createPost_pageNotFound_throwException() {
        when(pageRepository.findByIdOptional(1L))
                .thenReturn(Optional.empty());

        PostRequestDto dto = mock(PostRequestDto.class);
        UserDto user = new UserDto(1L, "admin");

        assertThrows(NotFoundException.class,
                () -> postService.createPost(1, dto, user));
    }

    @Test
    void createPost_nullMedia_create() {
        Page page = new Page();
        page.setId(1);

        PostRequestDto dto = mock(PostRequestDto.class);
        when(dto.contentText()).thenReturn("content");
        when(dto.mediaIds()).thenReturn(null);

        UserDto user = new UserDto(1L, "admin");

        when(pageRepository.findByIdOptional(1L))
                .thenReturn(Optional.of(page));

        assertThrows(NullPointerException.class,
                () -> postService.createPost(1, dto, user));
    }


//    updateDraft(Integer postId, PostRequestDto dto, long userId);
    @Test
    void updateDraft_success() {
        Post post = new Post();
        post.setContentText("old");
        post.setAuthorId(1L);
        post.setStatus(PostStatus.DRAFT);
        post.setMediaList(new ArrayList<>());

        when(postRepository.findByIdOptional(1L))
                .thenReturn(Optional.of(post));

        when(postMapper.toPostResponseDto(any()))
                .thenReturn(mock(PostResponseDto.class));

        PostRequestDto dto = mock(PostRequestDto.class);
        when(dto.contentText()).thenReturn("new");
        when(dto.mediaIds()).thenReturn(List.of(1));

        PostResponseDto result = postService.updateDraft(1, dto, 1L);

        verify(postMediaMapper).toEntity(eq(1), eq(post));
        assertEquals("new", post.getContentText());
        assertEquals(1, post.getMediaList().size());
        assertNotNull(result);
    }

    @Test
    void updateDraft_noMedia_textUpdate() {
        Post post = new Post();
        post.setContentText("old");
        post.setAuthorId(1L);
        post.setStatus(PostStatus.REJECTED);
        post.setMediaList(new ArrayList<>());

        when(postRepository.findByIdOptional(1L))
                .thenReturn(Optional.of(post));

        PostRequestDto dto = mock(PostRequestDto.class);
        when(dto.contentText()).thenReturn("new");
        when(dto.mediaIds()).thenReturn(null);

        when(postMapper.toPostResponseDto(any()))
                .thenReturn(mock(PostResponseDto.class));

        postService.updateDraft(1, dto, 1L);

        assertEquals("new", post.getContentText());
        assertEquals(PostStatus.DRAFT, post.getStatus());
    }

//    deleteDraft(Integer postId, long authorId);
    @Test
    void deleteDraft_success() {
        when(postRepository.isAuthor(1, 1L)).thenReturn(true);
        when(postRepository.deleteById(1L)).thenReturn(true);

        postService.deleteDraft(1, 1L);

        verify(postRepository).deleteById(1L);
    }

    @Test
    void deleteDraft_notFound_shouldThrow() {
        when(postRepository.isAuthor(1, 1L)).thenReturn(true);
        when(postRepository.deleteById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> postService.deleteDraft(1, 1L));
    }

//    submitForModeration(Integer postId, long authorId);
    @Test
    void submitForModeration_success() {
        Post post = new Post();
        post.setStatus(PostStatus.DRAFT);
        post.setAuthorId(1L);

        when(postRepository.findByIdOptional(1L))
                .thenReturn(Optional.of(post));

        when(postMapper.toPostResponseDto(any()))
                .thenReturn(mock(PostResponseDto.class));

        PostResponseDto result =
                postService.submitForModeration(1, 1L);

        assertEquals(PostStatus.PENDING, post.getStatus());
        assertNotNull(result);
    }

    @Test
    void submitForModeration_alreadySubmitted_fail() {
        Post post = new Post();
        post.setStatus(PostStatus.PENDING);
        post.setAuthorId(1L);

        when(postRepository.findByIdOptional(1L))
                .thenReturn(Optional.of(post));

        assertThrows(IllegalStateException.class,
                () -> postService.submitForModeration(1, 1L));
    }


//    getMyPosts(long authorId, Integer pageId, PostStatus status, int page, int size);
    @Test
    void getMyPosts_success() {
        Post post = new Post();
        post.setAuthorId(1L);
        post.setStatus(PostStatus.DRAFT);

        PageResult<Post> pageResult = new PageResult<>(
                List.of(post), 0, 10, 1L, 1
        );

        when(postRepository.findByAuthorAndStatusAndPage(
                anyLong(), any(), any(), anyInt(), anyInt()
        )).thenReturn(pageResult);

        when(postMapper.toPostLightResponseDtoList(any()))
                .thenReturn(List.of(mock(PostLightResponseDto.class)));

        PageResult<PostLightResponseDto> result =
                postService.getMyPosts(1L, 1, PostStatus.DRAFT, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.content().size());
    }

    @Test
    void getMyPosts_empty_returnEmpty() {
        PageResult<Post> empty = new PageResult<>(
                List.of(), 0, 10, 0L, 0);

        when(postRepository.findByAuthorAndStatusAndPage(
                anyLong(), any(), any(), anyInt(), anyInt())).thenReturn(empty);

        when(postMapper.toPostLightResponseDtoList(any())).thenReturn(List.of());

        PageResult<PostLightResponseDto> result =
                postService.getMyPosts(1L, null, null, 0, 10);

        assertTrue(result.content().isEmpty());
    }

//    getPagePosts(Integer pageId, Instant cursor, int size);
    @Test
    void getPagePosts_success() {
        Post post = new Post();

        SliceResult<Post> slice = new SliceResult<>(
                List.of(post), null, true);

        when(postRepository.findByPageAndStatusPaginated(
                any(), anyInt(), any(), anyInt())).thenReturn(slice);

        when(postMapper.toPostDtoList(any())).thenReturn(List.of(mock(PostResponseDto.class)));

        SliceResult<PostResponseDto> result =
                postService.getPagePosts(1, null, 10);

        assertTrue(result.hasNext());
    }

    @Test
    void getPagePosts_empty_shouldReturnEmpty() {
        SliceResult<Post> slice = new SliceResult<>(
                List.of(), null, false);

        when(postRepository.findByPageAndStatusPaginated(
                any(), anyInt(), any(), anyInt())).thenReturn(slice);

        when(postMapper.toPostDtoList(any())).thenReturn(List.of());

        SliceResult<PostResponseDto> result =
                postService.getPagePosts(1, null, 10);

        assertTrue(result.content().isEmpty());
    }

//    getPost(Integer postId, long authorId);
    @Test
    void getPost_success() {
        Post post = new Post();
        post.setAuthorId(1L);

        when(postRepository.findByIdOptional(1L)).thenReturn(Optional.of(post));

        when(postMapper.toPostResponseDto(any())).thenReturn(mock(PostResponseDto.class));

        PostResponseDto result = postService.getPost(1, 1L);

        assertNotNull(result);
    }


//    applyModeration(ModerationDecisionDto dto);
    @Test
    void applyModeration_approved_success() {
        Page page = new Page();
        page.setId(1);

        Post post = new Post();
        post.setPage(page);

        when(postRepository.findByIdOptional(1L)).thenReturn(Optional.of(post));

        when(pageService.getFollowers(1)).thenReturn(List.of(1L, 2L));

        postService.applyModeration(new ModerationDecisionDto(1, true));

        assertEquals(PostStatus.APPROVED, post.getStatus());
        assertNotNull(post.getPublishedAt());
    }

    @Test
    void applyModeration_rejected() {
        Post post = new Post();

        when(postRepository.findByIdOptional(1L)).thenReturn(Optional.of(post));

        postService.applyModeration(new ModerationDecisionDto(1, false));

        assertEquals(PostStatus.REJECTED, post.getStatus());
    }


//    deletePostByUser(Integer postId, Long authorId);
    @Test
    void deletePostByUser_success() {
        Post post = new Post();
        post.setAuthorId(1L);

        when(postRepository.findByIdOptional(1L)).thenReturn(Optional.of(post));

        postService.deletePostByUser(1, 1L);

        verify(postRepository).delete(post);
    }


//    deletePost(Integer postId);
    @Test
    void deletePost_success() {
        when(postRepository.deleteById(1L)).thenReturn(true);

        postService.deletePost(1);

        verify(postRepository).deleteById(1L);
    }

    @Test
    void deletePost_notFound_throwException() {
        when(postRepository.deleteById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> postService.deletePost(1));
    }


//    getAllPosts(PostStatus status, Integer pageId, int page, int size);
    @Test
    void getAllPosts_success() {
        PageResult<Post> pageResult = new PageResult<>(
                List.of(new Post()), 0, 10, 1L, 1
        );

        PostServiceImpl spy = spy(postService);
        doReturn(pageResult).when(spy)
                .processFilter(any(), any(), anyInt(), anyInt());

        when(postMapper.toAdminPostDtoList(any()))
                .thenReturn(List.of(mock(AdminPostDto.class)));

        PageResult<AdminPostDto> result =
                spy.getAllPosts(PostStatus.APPROVED, null, 0, 10);

        assertEquals(1, result.content().size());
    }

    @Test
    void getAllPosts_draft_throwException() {
        assertThrows(ForbiddenException.class,
                () -> postService.getAllPosts(PostStatus.DRAFT, null, 0, 10));
    }

    @Test
    void getAllPosts_empty_throwException() {
        PageResult<Post> empty = new PageResult<>(
                List.of(), 0, 10, 0L, 0);

        PostServiceImpl spy = spy(postService);
        doReturn(empty).when(spy).processFilter(any(), any(), anyInt(), anyInt());

        assertThrows(NotFoundException.class,
                () -> spy.getAllPosts(PostStatus.APPROVED, null, 0, 10));
    }

    //assertAuthor
    @Test
    void assertAuthor_success(){
        Post post = new Post();
        post.setAuthorId(1L);

        postService.assertAuthor(post, 1L);
    }

    @Test
    void assertAuthor_fail(){
        Post post = new Post();
        post.setAuthorId(1L);

        assertThrows(ForbiddenException.class,
                () -> postService.assertAuthor(post, 2L));
    }

    //findById
    @Test
    void findById_success(){
        Post post = new Post();
        when(postRepository.findByIdOptional(1L))
                .thenReturn(Optional.of(post));

        postService.findById(1);
    }

    @Test
    void findById_fail(){
        assertThrows(NotFoundException.class,
                () -> postService.findById(1));
    }
}
