package com.ptit.thesis.smartrecruit.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.entity.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JobRepositoryCustom {
    
    @Query("SELECT j FROM Job j WHERE j.id = :id AND j.status = 'ACTIVE'")
    Optional<Job> findAvailableJobById(Long id);

    @Query("SELECT j FROM Job j " +
    "LEFT JOIN FETCH j.company c " + 
    "LEFT JOIN FETCH c.location l " +
    "WHERE j.slug = :slug AND j.status = 'ACTIVE'")
    Optional<Job> findAvailableJobWithCompany(String slug);

    @EntityGraph(attributePaths =  {"company"})
    Page<Job> findAll(Specification<Job> spec, Pageable pageable);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.job = :job")
    Long countAplication(Job job);
}
