package com.iana.postservice.services.impl;

import com.iana.postservice.dtos.PageResult;
import com.iana.postservice.dtos.SliceResult;
import com.iana.postservice.dtos.post.ModerationDecisionDto;
import com.iana.postservice.dtos.post.UserDto;
import com.iana.postservice.dtos.post.request.PostRequestDto;
import com.iana.postservice.dtos.post.response.ModerationPostsDto;
import com.iana.postservice.dtos.post.response.PostLightResponseDto;
import com.iana.postservice.dtos.post.response.PostResponseDto;
import com.iana.postservice.entities.*;
import com.iana.postservice.entities.enums.PostStatus;
import com.iana.postservice.mappers.PostMapper;
import com.iana.postservice.mappers.PostMediaMapper;
import com.iana.postservice.repositories.PageRepository;
import com.iana.postservice.repositories.PostRepository;
import com.iana.postservice.services.PostService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class PostServiceImpl implements PostService {

    @Inject
    PostRepository postRepository;
    @Inject
    PageRepository pageRepository;
    @Inject
    PostMapper postMapper;
    @Inject
    PostMediaMapper postMediaMapper;


    @Transactional
    @Override
    public PostResponseDto createDraft(Integer pageId, PostRequestDto dto,
                                       int departmentId, UserDto user) {
        Page page = pageRepository.findByIdOptional((long) pageId)
                .orElseThrow(() -> new NotFoundException("Page not found"));

//        if(!isPostingAllowed(page, departmentId)) {
//            throw new ForbiddenException("You are not allowed to write a post on this page");
//        }

        Post post = new Post(page, user.getUserId(), user.getUsername(),
                PostStatus.DRAFT, dto.contentText());
        List<PostMedia> mediaList = dto.mediaIds().stream()
                .map(m -> postMediaMapper.toEntity(m,post)).toList();
        post.setMediaList(mediaList);
        postRepository.persist(post);

        return postMapper.toPostResponseDto(post);
    }

    //for admin and moderators
    @Transactional
    @Override
    public PostResponseDto createPost(Integer pageId, PostRequestDto dto, UserDto user) {
        Page page = pageRepository.findByIdOptional((long) pageId)
                .orElseThrow(() -> new NotFoundException("Page not found"));

        Post post = new Post(page, user.getUserId(), user.getUsername(),
                PostStatus.APPROVED, dto.contentText());
        post.setPublishedAt(Instant.now());

        List<PostMedia> mediaList = dto.mediaIds().stream()
                .map(m -> postMediaMapper.toEntity(m,post)).toList();
        post.setMediaList(mediaList);
        postRepository.persist(post);

        return postMapper.toPostResponseDto(post);
    }



    @Transactional
    @Override
    public PostResponseDto updateDraft(Integer postId, PostRequestDto dto, long userId) {
        Post post =findById(postId);

        assertAuthor(post, userId);
        if(isEditable(post)){
            if(!post.getContentText().equals(dto.contentText())){
                post.setContentText(dto.contentText());
            }

            if(dto.mediaIds() != null) {
                List<PostMedia> mediaList = dto.mediaIds().stream()
                        .map(m -> postMediaMapper.toEntity(m, post)).toList();
                post.getMediaList().clear();
                post.getMediaList().addAll(mediaList);
            }
        }
        return postMapper.toPostResponseDto(post);
    }

    @Transactional
    @Override
    public void deleteDraft(Integer postId, long authorId) {
        if(!postRepository.isAuthor(postId, authorId)){
            throw new ForbiddenException("You are not authorized to delete this draft");
        }

        boolean deleted = postRepository.deleteById((long) postId);
        if (!deleted) {
            throw new NotFoundException("Post with id " + postId + " not found");
        }
    }

   @Transactional
   @Override
    public PostResponseDto submitForModeration(Integer postId, long authorId) {
        Post post = findById(postId);
        assertAuthor(post, authorId);

        if(post.getStatus() != PostStatus.DRAFT){
            throw new IllegalStateException("This post has already been submitted for moderation");
        }

        post.setStatus(PostStatus.PENDING);
        //ToDo: send notification to ModerationService and user, send the whole post
        return postMapper.toPostResponseDto(post);
    }

    //returns all user posts (filtering by status)
    @Override
    public PageResult<PostLightResponseDto> getMyPosts(long authorId,
                                                       Integer pageId,  // optional filter for admin
                                                       PostStatus status,
                                                       int page, int size) {
        PageResult<Post> myPosts = postRepository.findByAuthorAndStatusAndPage(authorId, status,
                pageId, page, size);
        List<PostLightResponseDto> dtoList =
                postMapper.toPostLightResponseDtoList(myPosts.content());
        return new PageResult<>(
                dtoList,
                myPosts.pageNumber(),
                myPosts.pageSize(),
                myPosts.totalElements(),
                myPosts.totalPages()
                );
    }

    @Override
    public SliceResult<PostResponseDto> getPagePosts(Integer pageId, int page,int size) {
        SliceResult<Post> approvedPosts = postRepository.
                findByPageAndStatusPaginated(PostStatus.APPROVED, pageId, page, size);
        List<PostResponseDto> dtoList = postMapper.toPostDtoList(approvedPosts.content());

        return new SliceResult<>(
                dtoList,
                approvedPosts.pageNumber(),
                approvedPosts.hasNext()
        );
    }

    @Override
    public PostResponseDto getPost(Integer postId, long authorId) {
        Post post = findById(postId);
        assertAuthor(post, authorId);

        return postMapper.toPostResponseDto(post);
    }

    //for moderation

    @Transactional
    @Override
    public void applyModeration(ModerationDecisionDto dto) {
        Post post = findById(dto.postId());

        if(dto.approved()){
            post.setStatus(PostStatus.APPROVED);
            post.setPublishedAt(Instant.now());
            //ToDo: send notification  to followers
        }else {
            post.setStatus(PostStatus.REJECTED);
        }
        //ToDo: send notification  to author
    }

    @Transactional
    @Override
    public void deletePostByUser(Integer postId, Long authorId) {
        Post post = findById(postId);
        assertAuthor(post, authorId);
        postRepository.delete(post);
//        if (!deleted) {
//            throw new NotFoundException("Post with id" + postId +"not found");
//        }
    }

    //no need?
    @Transactional
    @Override
    public void deletePost(Integer postId) {
        boolean deleted = postRepository.deleteById((long) postId);
        if (!deleted) {
            throw new NotFoundException("Post with id" + postId +"not found");
        }
    }

    //admin only
    @Override
    public PageResult<ModerationPostsDto> getAllPosts(PostStatus status, Integer pageId,
                                               int page, int size) {
        if(status == PostStatus.DRAFT){
            throw new ForbiddenException("You are not authorized to view users' drafts");
        }
        PageResult<Post> retrieved = processFilter(status, pageId, page, size);
        if(retrieved.content().isEmpty()){
            throw new NotFoundException("No posts found");
        }
        List<ModerationPostsDto> dtoList = postMapper.toModerationPostsDtoList(retrieved.content());
        return new PageResult<>(
                dtoList,
                retrieved.pageNumber(),
                retrieved.pageSize(),
                retrieved.totalElements(),
                retrieved.totalPages()
        );
    }

    private void assertAuthor(Post post, Long userId){
        if(!post.getAuthorId().equals(userId)) {
            throw new ForbiddenException("You are not authorized to do this operation");
        }
    }

    private boolean isPostingAllowed(Page page, Integer departmentId){
        return page.getDepartmentId().equals(departmentId);
    }

    private PageResult<Post> processFilter(PostStatus status, Integer pageId, int page, int size) {
        PageResult<Post> posts;
        if(status == null && pageId == null) {
            posts = postRepository.getAllPaginated(page, size);
        }
        else{
            if(pageId != null && status != null){
                posts = postRepository.findByPageAndStatus(status,
                        pageId, page, size);
            }
            else if(pageId != null){
                posts = postRepository.findByPage(pageId, page, size);
            }
            else posts = postRepository.findByStatus(status, page, size);
        }
        return posts;
    }

    private boolean isEditable(Post post) {
        return post.getStatus() == PostStatus.DRAFT || post.getStatus() == PostStatus.REJECTED;
    }

    private Post findById(Integer postId) throws NotFoundException {
        return postRepository.findByIdOptional((long) postId)
                .orElseThrow(() -> new NotFoundException("Page not found"));
    }


}
