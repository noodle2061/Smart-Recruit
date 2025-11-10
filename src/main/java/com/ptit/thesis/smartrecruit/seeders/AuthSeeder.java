package com.ptit.thesis.smartrecruit.seeders;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ptit.thesis.smartrecruit.entity.Role;
import com.ptit.thesis.smartrecruit.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthSeeder implements CommandLineRunner {

    private final RoleRepository repository;

    @Override
    public void run(String... args) throws Exception {
        List<Role> data = List.of(
            Role.builder().name("ADMIN").description("Quản trị viên hệ thống").build(),
            Role.builder().name("EMPLOYER").description("Nhà tuyển dụng").build(),
            Role.builder().name("CANDIDATE").description("Ứng viên").build()
        );

        if (this.repository.count() <= 0) {
            this.repository.saveAll(data);
            log.info("Seeder role successfully");
        }
    }


}
