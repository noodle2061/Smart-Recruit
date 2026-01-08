package com.ptit.thesis.smartrecruit.config;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("Hệ thống tuyển dụng thông minh Open API")
                .version("1.0.0")
                .description("Tài liệu mô tả Rest API")
                .license(new License()
                        .name("Timeline Đồ Án")
                        .url("https://docs.google.com/spreadsheets/d/17a5lvCMOPRevHHjurtB3mL-nLmm3BEPy4XQUqPtp3DY/edit?gid=1788764148#gid=1788764148"))
                .summary("Tổng quan đồ án")
                .contact(new Contact()
                        .url("https://github.com/DienCo91/SmartRecruitment")
                        .name("Frontend")))
                .externalDocs(new ExternalDocumentation()
                        .description("Database Diagram")
                        .url("https://www.dbdiagram.io/d/SmartRecruitment-68c29a31841b2935a6fd951a"))
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("localhost"))
                .components(new Components()
                        .addSecuritySchemes("bearer", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .security(List.of(new SecurityRequirement()
                        .addList("bearer")));
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("api-version")
                .packagesToScan("com.ptit.thesis.smartrecruit.controller")
                .build();
    }
}
