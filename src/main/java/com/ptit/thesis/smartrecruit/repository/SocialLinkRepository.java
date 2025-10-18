package com.ptit.thesis.smartrecruit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.entity.SocialLink;
import com.ptit.thesis.smartrecruit.enums.LinkableType;

@Repository
public interface SocialLinkRepository extends JpaRepository<SocialLink, Long> {
    List<SocialLink> findByLinkableIdAndLinkableType(Long linkableId, LinkableType linkableType);
}
