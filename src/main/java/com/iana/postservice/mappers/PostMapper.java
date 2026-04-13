package com.iana.postservice.mappers;

import com.iana.postservice.dtos.post.request.PostRequestDto;
import com.iana.postservice.dtos.post.response.AdminPostDto;
import com.iana.postservice.dtos.post.response.ModerationPostDto;
import com.iana.postservice.dtos.post.response.PostLightResponseDto;
import com.iana.postservice.dtos.post.response.PostResponseDto;
import com.iana.postservice.entities.Post;
import com.iana.postservice.entities.PostMedia;
import com.iana.postservice.entities.enums.PostStatus;
import org.mapstruct.*;

import java.time.Instant;
import java.util.List;


@Mapper(uses = {PostMediaMapper.class}, componentModel = "cdi",
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface PostMapper {

    @Mapping(target = "mediaList", ignore = true)
    Post toPost(PostRequestDto dto);

    @Mapping(target = "pageId", source = "post.page.id")
    @Mapping(target = "author.userId", source = "post.authorId")
    @Mapping(target = "author.username", source = "post.username")
    @Mapping(target = "timestamp", expression = "java(resolveTimestamp(post))")
    @Mapping(target = "media", source = "mediaList")
    PostResponseDto toPostResponseDto(Post post);
    List<PostResponseDto> toPostDtoList(List<Post> post);


    @Mapping(target = "pageId", source = "page.id")
    @Mapping(target = "pageTitle", source = "page.title")
    @Mapping(target = "contentText", source = "contentText")
    @Mapping(target = "timestamp", expression = "java(resolveTimestamp(post))")
    PostLightResponseDto toPostLightResponseDto(Post post);
    List<PostLightResponseDto> toPostLightResponseDtoList(List<Post> posts);

    @Mapping(target = "pageId", source = "page.id")
    @Mapping(target = "pageTitle", source = "page.title")
    @Mapping(target = "createdAt", source = "post.createdAt")
    @Mapping(target = "media", source = "mediaList")
    AdminPostDto toAdminPostDto(Post post);
    List<AdminPostDto> toAdminPostDtoList(List<Post> posts);

    @Mapping(target = "type", constant = "post")
    @Mapping(target = "pageId", source = "page.id")
    @Mapping(target = "pageTitle", source = "page.title")
    @Mapping(target = "author.userId", source = "post.authorId")
    @Mapping(target = "author.username", source = "post.username")
    @Mapping(target = "createdAt", source = "post.createdAt")
    @Mapping(target = "media", source = "mediaList")
    ModerationPostDto toModerationPostDto(Post post);

    default Instant resolveTimestamp(Post post) {
        return (post.getStatus() == PostStatus.APPROVED)
                ? post.getPublishedAt()
                : post.getCreatedAt();
    }

    default List<Integer> mapMediaIds(List<PostMedia> mediaList) {
        return mediaList == null
                ? List.of()
                : mediaList.stream()
                .map(PostMedia::getMediaId)
                //     .filter(Objects::nonNull)
                .toList();
    }
}
