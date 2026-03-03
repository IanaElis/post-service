package com.iana.postservice.dtos.page.response;

import com.iana.postservice.dtos.Pagination;
import com.iana.postservice.dtos.post.response.PostDto;

import java.util.List;

public record PageWithPostsDto(
        PageDetailsDto page,
        List<PostDto> posts,
        Pagination pagination
) {
}
