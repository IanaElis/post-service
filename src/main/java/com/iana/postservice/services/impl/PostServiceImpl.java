package com.iana.postservice.services;

import com.iana.postservice.dtos.PostsForPageDto;
import com.iana.postservice.dtos.Pagination;
import com.iana.postservice.dtos.PostFilter;
import com.iana.postservice.dtos.post.ModerationDecisionDto;
import com.iana.postservice.dtos.post.UserRequestDto;
import com.iana.postservice.dtos.post.request.PostRequestDto;
import com.iana.postservice.dtos.post.response.ModerationPostsDto;
import com.iana.postservice.dtos.post.response.PostLightResponseDto;
import com.iana.postservice.dtos.post.response.PostResponseDto;
import com.iana.postservice.entities.*;
import com.iana.postservice.mappers.PostMapper;
import com.iana.postservice.mappers.PostMediaMapper;
import com.iana.postservice.repositories.PageRepository;
import com.iana.postservice.repositories.PostRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PostServiceImpl implements PostService {

    @Inject
    PostRepository postRepository;
    @Inject
    PageRepository pageRepository;
    @Inject
    PageService pageService;
    @Inject
    PostMapper postMapper;
    @Inject
    PostMediaMapper postMediaMapper;

    // maybe PostCommand and PostQuery?

    @Transactional
    @Override
    public PostResponseDto createDraft(Integer pageId, PostRequestDto dto, UserRequestDto user) {
        Page page = pageRepository.findByIdOptional((long) pageId)
                .orElseThrow(() -> new NotFoundException("Page not found"));

        if(!isPostingAllowed(page, user.role(), user.departmentId())) {
            throw new ForbiddenException("You are not allowed to write a post on this page");
        }

        Post post = new Post(page, user.userId(), user.username(), PostStatus.DRAFT, dto.contentText());
        List<PostMedia> mediaList = new ArrayList<>();
        for(Integer m: dto.mediaIds()){
            mediaList.add(postMediaMapper.toEntity(m, post));
        }
        post.setMediaList(mediaList);
        postRepository.persist(post);

        return postMapper.toPostResponseDto(post);
    }

    @Transactional
    @Override
    public PostResponseDto updateDraft(Integer postId, PostRequestDto dto, Long authorId) {
        Post post =findById(postId);

        assertAuthor(post, authorId);
        if(isEditable(post)){
            if(!post.getContentText().equals(dto.contentText())){
                post.setContentText(dto.contentText());
            }

            if(dto.mediaIds() != null) {
                List<PostMedia> mediaList = new ArrayList<>();
                for(Integer m: dto.mediaIds()){
                    mediaList.add(postMediaMapper.toEntity(m, post));
                }
                post.getMediaList().clear();
                post.getMediaList().addAll(mediaList);
            }
        }

        return postMapper.toPostResponseDto(post);
    }

    @Transactional
    @Override
    public void deleteDraft(Integer postId, Long authorId) {
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
    public PostResponseDto submitForModeration(Integer postId, Long authorId) {
        Post post = findById(postId);
        assertAuthor(post, authorId);

        if(post.getStatus() != PostStatus.DRAFT){
            throw new IllegalStateException("This post has already been submitted for moderation");
        }

        post.setStatus(PostStatus.PENDING);
        //ToDo: send notification to ModerationService and user
        return postMapper.toPostResponseDto(post);
    }

    @Transactional
    @Override
    public void requestDelete(Integer postId, Long authorId) {
        Post post = findById(postId);
        assertAuthor(post, authorId);
        post.setStatus(PostStatus.DELETE_REQUESTED);
        //ToDo: send notification to ModerationService
    }

    //returns all user posts (filtering by status)
    @Override
    public List<PostLightResponseDto> getMyPosts(Long authorId,
                                                 Integer pageId,  // optional filter for admins/moderators
                                                 PostStatus status,
                                                 Pagination pagination) {
        List<Post> myPosts = processFilterWithAuthor(new PostFilter(status, pageId),
                authorId, pagination);
        return postMapper.toPostLightResponseDtoList(myPosts);
    }

    private List<Post> processFilterWithAuthor(PostFilter filter, Long authorId, Pagination p) {
        List<Post> posts;
        if(filter == null){
            posts = postRepository.findByAuthor(authorId,
                    p.pageNumber(), p.pageSize());
        }else{
            if(filter.pageId() != null && filter.status() != null){ //case admin or moderator
                posts = postRepository.findByAuthorAndStatusAndPage(authorId,
                        filter.pageId(), filter.status(), p.pageNumber(), p.pageSize());
            }
            else if(filter.pageId() != null) { //case admin or moderator without filtering by status
                posts = postRepository.findByAuthorAndPage(authorId, filter.pageId(),
                        p.pageNumber(), p.pageSize());
            }
            else posts = postRepository.findByAuthorAndStatus(authorId, filter.status(),
                        p.pageNumber(), p.pageSize());
        }
        return posts;
    }

    @Override
    public PostsForPageDto getPagePosts(Integer pageId, int page, int size) {
        List<Post> approvedPosts = postRepository.
                findApprovedByPage(pageId, page, size);
        List<PostResponseDto> posts = postMapper.toPostDtoList(approvedPosts);

        return new PostsForPageDto(posts, new Pagination(page,size));
    }

    @Override
    public PostResponseDto getPost(Integer postId, Long authorId) {
        Post post = findById(postId);
        assertAuthor(post, authorId);

        return postMapper.toPostResponseDto(post);
    }

    //for moderation

    @Override
    public List<ModerationPostsDto> getPostsWithStatus(Integer pageId, PostStatus status,
                                                       int page, int size) {
        List<Post> posts;

        if(pageId == null) {
            posts = postRepository.findByStatus(status, page, size);
        }
        else{
            posts = postRepository.findByPageAndStatus(status, pageId, page, size);
        }

        return postMapper.toModerationPostsDtoList(posts);
    }

    @Transactional
    @Override
    public void applyModeration(ModerationDecisionDto dto) {
        Post post = findById(dto.postId());

        if(dto.approved()){
            post.setStatus(PostStatus.APPROVED);
            //ToDo: send notification  to followers
        }else {
            post.setStatus(PostStatus.REJECTED);
        }
        //ToDo: send notification  to author
    }

    @Transactional
    @Override
    public void deletePost(Integer postId) {
        boolean deleted = postRepository.deleteById((long)postId);
        if (!deleted) {
            throw new NotFoundException("Post with id" + postId +"not found");
        }
    }

    //admin only
    @Override
    public List<ModerationPostsDto> getAllPosts(PostFilter filter, Pagination pagination) {
        List<Post> retrieved = processFilter(filter, pagination);
        if(retrieved.isEmpty()){
            throw new NotFoundException("No posts found");
        }
        return postMapper.toModerationPostsDtoList(retrieved);
    }


    private void assertAuthor(Post post, Long userId){
        if(!post.getAuthorId().equals(userId)) {
            throw new ForbiddenException("You are not authorized to do this operation");
        }
    }

    private boolean isPostingAllowed(Page page, String role, Integer departmentId){
        //ToDo: check role?
        if(role.equals("USER")) return page.getDepartmentId().equals(departmentId);
        return role.equals("ADMIN") || role.equals("MODERATOR");
    }

    private List<Post> processFilter(PostFilter filter, Pagination p) {
        List<Post> posts;
        if(filter == null){
            posts = postRepository.getAllPaginated(p.pageNumber(), p.pageSize());
        }
        else{
            if(filter.pageId() != null && filter.status() != null){
                posts = postRepository.findByPageAndStatus(filter.status(),
                        filter.pageId(), p.pageNumber(), p.pageSize());
            }
            else if(filter.pageId() != null){
                posts = postRepository.findByPage(filter.pageId(), p.pageNumber(), p.pageSize());
            }
            else posts = postRepository.findByStatus(filter.status(), p.pageNumber(), p.pageSize());
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
