package com.ptit.thesis.smartrecruit.dto.common;


import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationDTO {

    String country;

    String provinceCity;

    String commune;

    @NotNull(message = "Latitude is mandatory")
    Float latitude;

    @NotNull(message = "Longitude is mandatory")
    Float longitude;
}
