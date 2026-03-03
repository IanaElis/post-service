package com.iana.postservice.services;

import com.iana.postservice.dtos.post.MediaDto;
import com.iana.postservice.dtos.post.ModerationDecisionDto;
import com.iana.postservice.dtos.post.PostModerationInfoDto;
import com.iana.postservice.dtos.post.request.PostCreateDto;
import com.iana.postservice.dtos.post.request.PostUpdateDto;
import com.iana.postservice.dtos.post.response.PostLightResponseDto;
import com.iana.postservice.dtos.post.response.PostResponseDto;
import com.iana.postservice.entities.PostStatus;

import java.awt.print.Pageable;
import java.util.List;

public class PostService {
    // maybe PostCommand and PostQuery?
    //create draft
    public PostResponseDto createDraft(PostCreateDto dto, Long authorId) {
        PostResponseDto response = new PostResponseDto();
        return response;
    }

    //update draft
    public PostResponseDto updateDraft(Integer postId, PostUpdateDto dto, Long authorId) {
        PostResponseDto response = new PostResponseDto();
        return response;
    }

    //delete draft
    public void deleteDraft(Integer postId, Long authorId) {
    }

    //submit post
    public PostResponseDto submitForModeration(Integer postId, Long authorId) {
        PostResponseDto response = new PostResponseDto();
        return response;
    }

    //add media to draft
    public PostResponseDto attachMedia(Integer postId, MediaDto mediaDto, Long authorId) {
        PostResponseDto response = new PostResponseDto();
        return response;
    }

    //remove media from draft
    public PostResponseDto removeMedia(Integer postId, Integer mediaId, Long authorId) {
        PostResponseDto response = new PostResponseDto();
        return response;
    }

    // requestDelete(Integer postId, Long authorId)
    public void requestDelete(Integer postId, Long authorId) {
    }

    //post moderation history (for user)
    public List<PostModerationInfoDto> getModerationHistoryByUser(Integer postId, Long userId) {
        return null;
    }

    //get all my posts (filtering by status)
    public List<PostLightResponseDto> getMyPosts(Long authorId, PostStatus status) {
        return null;
    }

    //get post
    public PostResponseDto getPost(Integer postId, Long authorId) {
        PostResponseDto response = new PostResponseDto();
        return response;
    }

    //for moderation
    // pending posts, filter by page
    public List<PostResponseDto> getPendingPosts(Integer pageId) {
        return null;
    }

    //approve post
    public PostResponseDto approvePost(ModerationDecisionDto dto) {
        PostResponseDto response = new PostResponseDto();
        return response;
    }

    //reject post
    public PostResponseDto rejectPost(ModerationDecisionDto dto) {
        PostResponseDto response = new PostResponseDto();
        return response;
    }

    //view post history
    public List<PostModerationInfoDto> getModerationHistory(Integer postId) {
        return null;
    }

    //delete post
    public 



    //validateAuthor(Post post, Long userId)                   //post author
    private boolean isAuthor(Integer postId, Long userId) {
        return false;
    }
    //validatePostEditable(Post post)                         //only draft or rejected



    // applyModeration(): approvePost(), rejectPost(), deletePost()

}
