package com.ptit.thesis.smartrecruit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ptit.thesis.smartrecruit.dto.common.LocationDTO;
import com.ptit.thesis.smartrecruit.entity.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    LocationDTO tLocationDTO(Location location);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "companies", ignore = true)
    @Mapping(target = "jobs", ignore = true)
    @Mapping(target = "candidateProfiles", ignore = true)
    Location toLocationEntity(LocationDTO locationDTO);
}
