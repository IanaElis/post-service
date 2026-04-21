package com.iana.postservice.controllers;

import com.iana.postservice.dtos.page.response.PageDetailsDto;
import com.iana.postservice.dtos.page.response.PageFollowedDto;
import com.iana.postservice.dtos.page.response.PageLightDto;
import com.iana.postservice.services.PageService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.json.JsonNumber;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;


@Path("/pages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
//@Authenticated
public class PageController {
    @Inject
    PageService pageService;
    @Inject
    JsonWebToken jwt;

    private long userId(){
        JsonNumber claim = jwt.getClaim("userid");

        if (claim == null) {
            throw new NotAuthorizedException("Invalid token claims");
        }
        return Long.parseLong(claim.toString());
    }


    @POST
    @Path("/{pageId}/follow")
    public Response follow(@PathParam("pageId") Integer pageId) {
        pageService.followPage(pageId, userId());
        return Response.ok().build();
    }

    @DELETE
    @Path("/{pageId}/follow")
    public Response unfollow(@PathParam("pageId") Integer pageId) {
        pageService.unfollowPage(pageId, userId());
        return Response.ok().build();
    }

    @GET
    @Path("/{pageId}")
    public PageDetailsDto getPage(@PathParam("pageId") Integer pageId) {
        return pageService.getPageInfo(pageId);
    }

    @GET
    @Path("/followed")
    public List<PageFollowedDto> followedPages() {
        return pageService.getPagesUserFollows(userId());
    }




    //get pages user follows
    // get users following page?

}
