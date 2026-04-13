package com.iana.postservice.controllers;

import com.iana.postservice.dtos.page.request.PageCreateDto;
import com.iana.postservice.dtos.page.request.PageUpdateDto;
import com.iana.postservice.dtos.page.response.PageDetailsDto;
import com.iana.postservice.dtos.page.response.PageDto;
import com.iana.postservice.dtos.page.response.PageLightDto;
import com.iana.postservice.entities.enums.PageType;
import com.iana.postservice.services.PageService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/admin/pages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class PageModerationController {
    @Inject
    PageService pageService;

    @POST
    public Response create(@Valid @NotNull PageCreateDto pageCreateDto){
        PageDetailsDto pageCreated = pageService.createPage(pageCreateDto);
        return Response.seeOther(URI
                .create("/admin/pages/" + pageCreated.id())).build();
                //.status(Response.Status.CREATED).entity(pageCreated).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Integer pageId,
                           @Valid @NotNull PageUpdateDto pageUpdateDto){
        PageDetailsDto pageUpdated = pageService.updatePage(pageId, pageUpdateDto);
        return Response.ok().entity(pageUpdated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer pageId){
        pageService.deletePage(pageId);
        return Response.seeOther(URI.create("/admin/pages")).build();
    }

    @GET
    @Path("/{id}")
    public Response getPageDetails(@PathParam("id") Integer pageId) {
        PageDetailsDto result = pageService.getPageInfo(pageId);
        return Response.ok().entity(result).build();
    }

    @GET
    public List<PageDto> getAllPages(){
        return pageService.getAllPagesList();
    }

    @GET
    @Path("/types")
    public List<PageType> getPageTypes() {
        return pageService.getPageTypes();
    }

    @GET
    @Path("/select")
    public List<PageLightDto> getPageDropDown(){
        return pageService.getPageDropDown();
    }

    @GET
    @Path("/faculty-pages")
    public List<PageLightDto> getFacultyPages() {
        return pageService.getFacultyPageList();
    }

}
