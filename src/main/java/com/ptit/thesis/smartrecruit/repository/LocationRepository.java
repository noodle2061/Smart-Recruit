package com.ptit.thesis.smartrecruit.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByLatitudeAndLongitude(Float latitude, Float longitude);
    boolean existsByLatitudeAndLongitude(Float latitude, Float longitude);
}
