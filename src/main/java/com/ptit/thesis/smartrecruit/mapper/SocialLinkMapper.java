package com.ptit.thesis.smartrecruit.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ptit.thesis.smartrecruit.dto.common.SocialLinkDTO;
import com.ptit.thesis.smartrecruit.entity.SocialLink;

@Mapper(componentModel = "spring")
public interface SocialLinkMapper {
    
    SocialLinkDTO toSocialLinkDTO(SocialLink socialLink);

    List<SocialLinkDTO> toSocialLinkDTOs(List<SocialLink> socialLinks);

    /**
     * chuyển doi tu List<SocialLinkDTO> -> List<SocialLink>
     * <p>
     * các field linkableId, linkableType phải tự xử lý tay sau khi dùng hàm này
     * @param dto
     * @return entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "linkableId", ignore = true) // xử lý trong tầng cao hơn
    @Mapping(target = "linkableType", ignore = true) // xử lý trong tầng cao hơn
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SocialLink toSocialLinkEntity(SocialLinkDTO dto);

    /**
     * chuyển doi tu List<SocialLinkDTO> -> List<SocialLink>
     * <p>
     * các field linkableId, linkableType phải tự xử lý tay sau khi dùng hàm này
     * @param dtos
     * @return entities
     */
    List<SocialLink> toSocialLinks(List<SocialLinkDTO> dtos);
}
