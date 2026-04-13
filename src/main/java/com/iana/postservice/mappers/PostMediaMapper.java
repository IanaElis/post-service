package com.iana.postservice.mappers;

import com.iana.postservice.entities.Post;
import com.iana.postservice.entities.PostMedia;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "cdi",
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface PostMediaMapper {

    PostMedia toEntity(Integer mediaId, Post post);
}
