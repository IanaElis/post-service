package com.iana.postservice.services;

import com.iana.postservice.dtos.PageResult;
import com.iana.postservice.dtos.SliceResult;
import com.iana.postservice.dtos.post.ModerationDecisionDto;
import com.iana.postservice.dtos.post.UserDto;
import com.iana.postservice.dtos.post.request.PostRequestDto;
import com.iana.postservice.dtos.post.response.AdminPostDto;
import com.iana.postservice.dtos.post.response.PostLightResponseDto;
import com.iana.postservice.dtos.post.response.PostResponseDto;
import com.iana.postservice.entities.enums.PostStatus;

import java.time.Instant;


public interface PostService {
    PostResponseDto createDraft(Integer pageId, PostRequestDto dto, UserDto user);
    PostResponseDto createPost(Integer pageId, PostRequestDto dto, UserDto user);
    PostResponseDto updateDraft(Integer postId, PostRequestDto dto, long userId);
    void deleteDraft(Integer postId, long authorId);
    PostResponseDto submitForModeration(Integer postId, long authorId);
//    void requestDelete(Integer postId, long authorId);
    PageResult<PostLightResponseDto> getMyPosts(long authorId, Integer pageId,
                                                PostStatus status,  int page, int size);
    SliceResult<PostResponseDto> getPagePosts(Integer pageId, Instant cursor, int size);
    PostResponseDto getPost(Integer postId, long authorId);


    //for moderation

    // pending posts, filter by page
    void applyModeration(ModerationDecisionDto dto);
    void deletePostByUser(Integer postId, Long authorId);
    void deletePost(Integer postId);
    PageResult<AdminPostDto> getAllPosts(PostStatus status, Integer pageId, int page, int size);
}
