package com.iana.postservice.controllers;

import com.iana.postservice.dtos.PageResult;
import com.iana.postservice.dtos.post.ModerationDecisionDto;
import com.iana.postservice.dtos.post.response.AdminPostDto;
import com.iana.postservice.entities.enums.PostStatus;
import com.iana.postservice.services.PostService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("/moderation/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN", "MODERATOR"})
public class PostModerationController {
    private final static int POSTS_PER_PAGE = 20;
    @Inject
    PostService postService;

    @DELETE
    @Path("/{id}")
    public Response deletePost(@PathParam("id") Integer id) {
        postService.deletePost(id);
        return Response.seeOther(URI.create("/moderation/posts")).build();
    }

    @GET
    @RolesAllowed("ADMIN")
    public PageResult<AdminPostDto> getAllPosts(@QueryParam("status") PostStatus status,
                                                @QueryParam("id") Integer pageId,
                                                @QueryParam("page") @DefaultValue("0") int page) {
        return postService.getAllPosts(status, pageId, page, POSTS_PER_PAGE);
    }

    @POST
    @Path("/decision")
    public Response applyDecision(ModerationDecisionDto dto) {
        postService.applyModeration(dto);
        return Response.ok().build();
    }
}
