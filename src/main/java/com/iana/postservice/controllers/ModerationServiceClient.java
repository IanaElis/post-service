package com.iana.postservice.controllers;

import com.iana.postservice.dtos.post.response.ModerationPostDto;
import com.iana.postservice.dtos.post.response.ModerationRequestDto;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("internal/moderation/posts")
@RegisterRestClient(configKey = "moderation-service")
public interface ModerationServiceClient {
    @POST
    Response moderationRequestPost(ModerationRequestDto dto);
}
