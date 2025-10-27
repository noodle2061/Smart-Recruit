package com.ptit.thesis.smartrecruit.repository;


import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude);
    boolean existsByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude);
}
