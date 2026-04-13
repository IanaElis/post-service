package com.iana.postservice.controllers;

import com.iana.postservice.dtos.post.response.ModerationPostDto;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/??")
@RegisterRestClient(configKey = "moderation-service")
public interface ModerationServiceClient {
    @POST
    Response sendPost(ModerationPostDto postDto);
}
