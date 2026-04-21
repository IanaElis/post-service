package com.iana.postservice.mappers;

import com.iana.postservice.dtos.page.request.PageCreateDto;
import com.iana.postservice.dtos.page.response.PageDetailsDto;
import com.iana.postservice.dtos.page.response.PageDto;
import com.iana.postservice.dtos.page.response.PageFollowedDto;
import com.iana.postservice.dtos.page.response.PageLightDto;
import com.iana.postservice.entities.Page;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "cdi",
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface PageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parentPage", ignore = true)
    @Mapping(target = "followersCount", ignore = true)
    Page toPage(PageCreateDto dto);

    @Mapping(target = "parentPageId", source = "parentPage.id")
    @Mapping(target = "createdAt", source = "createdAt")
    PageDto toPageDto(Page page);

    @Mapping(target = "parentPageId", source = "parentPage.id")
    PageDetailsDto toPageDetailsDto(Page page);
    List<PageDto> toPageDtoList(List<Page> pages);

    PageLightDto toPageLightDto(Page page);
    List<PageLightDto> toPageLightDtoList(List<Page> pages);


}
