package com.iana.postservice.dtos;

import com.iana.postservice.entities.MediaType;

public class MediaDto {
    Integer id;
    MediaType mediaType;
    String mediaUrl;
    Long fileSize;//remove?
}
