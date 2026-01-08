package com.ptit.thesis.smartrecruit.dto.common;


import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationDTO {

    String country;

    String provinceCity;

    String commune;

    @NotNull(message = "Latitude is mandatory")
    BigDecimal latitude;

    @NotNull(message = "Longitude is mandatory")
    BigDecimal longitude;
}
