package com.ptit.thesis.smartrecruit.seeders;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ptit.thesis.smartrecruit.entity.JobCategory;
import com.ptit.thesis.smartrecruit.repository.JobCategoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JobCategorySeeder implements CommandLineRunner {

    private final JobCategoryRepository repository;

    @Override
    public void run(String... args) throws Exception {
        List<JobCategory> data = List.of(
                JobCategory.builder().name("IT & Software1").build(),
                JobCategory.builder().name("DevOps & Cloud").build(),
                JobCategory.builder().name("AI / Machine Learning").build(),
                JobCategory.builder().name("Data Analyst / Data Engineer").build());

        if (this.repository.count() <= 0) {
            this.repository.saveAll(data);
            log.info("Seeder job category successfully");
        }
    }

}
