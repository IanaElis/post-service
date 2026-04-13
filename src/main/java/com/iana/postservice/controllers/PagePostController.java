package com.iana.postservice.controllers;

import com.iana.postservice.dtos.SliceResult;
import com.iana.postservice.dtos.post.UserDto;
import com.iana.postservice.dtos.post.request.PostRequestDto;
import com.iana.postservice.dtos.post.response.PostResponseDto;
import com.iana.postservice.services.PostService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/pages/{pageId}/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class PagePostController {
    @Inject
    PostService postService;
    @Inject
    JsonWebToken jwt;
    private static final int POSTS_PER_PAGE = 20;

    private UserDto currentUser(){
        String username = jwt.getSubject();
        Long claim = jwt.getClaim("userid");

        if (username == null || claim == null) {
            throw new NotAuthorizedException("Invalid token claims");
        }
        long userId = Long.parseLong(claim.toString());
        return new UserDto(userId, username);
    }

    @POST
    @Path("/draft")
    public Response createDraft(@PathParam("pageId") Integer pageId,
                                @Valid @NotNull PostRequestDto postCreateDto) {
        //TODO: fetch departmentId from userService
        PostResponseDto created = postService.createDraft(pageId, postCreateDto,
                1, currentUser());
        return Response.status(Response.Status.CREATED).entity(created).build();
        //or seeOther(URI.create("posts/" + created.getId())).build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response createPost(@PathParam("pageId") Integer pageId,
                               @Valid @NotNull PostRequestDto postCreateDto) {
        PostResponseDto created = postService.createPost(pageId, postCreateDto, currentUser());
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    public Response getPagePosts(@PathParam("pageId") Integer pageId,
                                 @QueryParam("page") @DefaultValue("0") int page) {
        SliceResult<PostResponseDto> result = postService.getPagePosts(pageId, page, POSTS_PER_PAGE);
        return Response.status(Response.Status.OK).entity(result).build();
    }
}
