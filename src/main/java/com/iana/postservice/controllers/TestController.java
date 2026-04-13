package com.iana.postservice.controllers;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestController {
    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/posts")
    @Authenticated
    public Response getPosts() {

        String username = jwt.getSubject();
        long userId = jwt.getClaim("userid");

        return Response.ok(username + " : " + userId).build();
    }
}
