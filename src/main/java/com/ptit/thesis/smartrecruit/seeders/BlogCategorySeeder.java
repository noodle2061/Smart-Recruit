package com.ptit.thesis.smartrecruit.seeders;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ptit.thesis.smartrecruit.entity.BlogCategory;
import com.ptit.thesis.smartrecruit.repository.BlogCategoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class BlogCategorySeeder implements CommandLineRunner {

    private final BlogCategoryRepository repository;

    @Override
    public void run(String... args) throws Exception {
        List<BlogCategory> data = List.of(
                BlogCategory.builder().name("Hướng dẫn & Phát triển nghề nghiệp").build(),
                BlogCategory.builder().name("Tin tức & Xu hướng việc làm").build(),
                BlogCategory.builder().name("Chia sẻ câu chuyện nghề nghiệp").build(),
                BlogCategory.builder().name("Kỹ năng & Học tập").build(),
                BlogCategory.builder().name("Góc nhà tuyển dụng").build());

        if (this.repository.count() == 0) {
            this.repository.saveAll(data);
            log.info("Seeder blog category successfully");
        }
    }

}
