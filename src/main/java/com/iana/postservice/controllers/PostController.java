package com.iana.postservice.controllers;

import com.iana.postservice.dtos.PageResult;
import com.iana.postservice.dtos.post.request.PostRequestDto;
import com.iana.postservice.dtos.post.response.PostLightResponseDto;
import com.iana.postservice.dtos.post.response.PostResponseDto;
import com.iana.postservice.entities.enums.PostStatus;
import com.iana.postservice.services.PostService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.net.URI;
import java.util.List;
import java.util.Set;

@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class PostController {
    @Inject
    PostService postService;
    @Inject
    JsonWebToken jwt;
    private final int POSTS_PER_PAGE = 20;

    private long userId(){
        Long claim = jwt.getClaim("userid");

        if (claim == null) {
            throw new NotAuthorizedException("Invalid token claims");
        }
        return Long.parseLong(claim.toString());
    }

    @PUT
    @Path("/drafts/{id}")
    public PostResponseDto updateDraft(@PathParam("id") Integer id,
                                       @Valid @NotNull PostRequestDto postUpdateDto) {
        return postService.updateDraft(id, postUpdateDto, userId());
    }

    @DELETE
    @Path("/drafts/{id}")
    public Response deleteDraft(@PathParam("id") Integer id) {
        postService.deleteDraft(id, userId());
        return Response.seeOther(URI.create("posts/my-posts")).build();
    }

    @POST
    @Path("/{id}/submit")
    public PostResponseDto submitPost(@PathParam("id") Integer id) {
        return postService.submitForModeration(id, userId());
    }

    @DELETE
    @Path("/{id}")
    public Response deletePost(@PathParam("id") Integer id) {
        postService.deletePostByUser(id, userId());
        return Response.seeOther(URI.create("/posts/my-posts")).build();
    }

    @GET
    @Path("/me/posts") //or all?
    public PageResult<PostLightResponseDto> getMyPosts(
                                                 @QueryParam("status") PostStatus status,
                                                 @QueryParam("pageId") Integer pageId,
                                                 @QueryParam("page") @DefaultValue("0") int page) {
        //if role == user, then pageId == null
        Set<String> groups = jwt.getGroups();
        if(!groups.contains("MODERATOR") || !groups.contains("ADMIN")) {
            pageId = null;
        }
        return postService.getMyPosts(userId(),
                pageId, status, page, POSTS_PER_PAGE);
    }

    @GET
    @Path("/{id}")
    public PostResponseDto getPost(@PathParam("id") Integer id){
        return postService.getPost(id, userId());
    }

    @GET
    @Path("/statuses")
    public List<PostStatus> getPostStatuses() {
        return List.of(PostStatus.values());
    }

}
