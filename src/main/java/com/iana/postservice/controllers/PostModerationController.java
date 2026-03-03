package com.iana.postservice.controllers;

import com.iana.postservice.dtos.ModerationViewDto;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModerationController {

    @GET
    @Path("/moderation/pending")
    public ModerationViewDto pendingPosts(){
        return null;
    }
}
