package com.ptit.thesis.smartrecruit.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.ptit.thesis.smartrecruit.dto.common.TagDTO;
import com.ptit.thesis.smartrecruit.entity.Tag;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDTO toTagDTO(Tag tag);

    List<TagDTO> toTagDTOs(List<Tag> tags);
}
