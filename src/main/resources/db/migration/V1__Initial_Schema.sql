CREATE TABLE roles
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    name               VARCHAR(50)  NOT NULL UNIQUE,
    description        VARCHAR(150) NOT NULL,
    created_at         DATETIME(6)  NOT NULL,
    updated_at         DATETIME(6)  NOT NULL
);

CREATE TABLE users
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    firebase_uid       VARCHAR(255) NOT NULL UNIQUE,
    user_name          VARCHAR(50)  NOT NULL UNIQUE,
    email              VARCHAR(50)  NOT NULL UNIQUE,
    delete_at          DATETIME(6),
    role_id            BIGINT       NOT NULL,
    created_at         DATETIME(6)  NOT NULL,
    updated_at         DATETIME(6)  NOT NULL,
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE locations
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    latitude           DECIMAL(10, 8) NOT NULL,
    longitude          DECIMAL(11, 8) NOT NULL,
    country            VARCHAR(255) NOT NULL,
    province_city      VARCHAR(255) NOT NULL,
    commune            VARCHAR(100),
    created_at         DATETIME(6)  NOT NULL,
    updated_at         DATETIME(6)  NOT NULL,
    UNIQUE KEY uk_location_lat_lng (latitude, longitude)
);

CREATE TABLE companies
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id            BIGINT       NOT NULL UNIQUE,
    name               VARCHAR(255) NOT NULL UNIQUE,
    logo_url           VARCHAR(512),
    banner_url         VARCHAR(512),
    description        TEXT         NOT NULL,
    organization_type  VARCHAR(255) NOT NULL,
    industry_type      VARCHAR(255) NOT NULL,
    team_size          VARCHAR(255) NOT NULL,
    founded_in         INT,
    website            VARCHAR(512),
    company_vision     TEXT,
    phone              VARCHAR(20)  NOT NULL UNIQUE,
    email              VARCHAR(255) NOT NULL UNIQUE,
    delete_at          DATETIME(6),
    location_id        BIGINT       NOT NULL,
    created_at         DATETIME(6)  NOT NULL,
    updated_at         DATETIME(6)  NOT NULL,
    CONSTRAINT fk_companies_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_companies_location FOREIGN KEY (location_id) REFERENCES locations (id)
);

CREATE TABLE candidate_profiles
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id             BIGINT       NOT NULL UNIQUE,
    full_name           VARCHAR(100) NULL,
    avatar_url          VARCHAR(512),
    headline            VARCHAR(255) NULL,
    experience_level    VARCHAR(255) NULL,
    education_level     VARCHAR(255) NULL,
    personal_website    VARCHAR(512),
    nationality         VARCHAR(255),
    date_of_birth       DATE,
    gender              VARCHAR(255),
    marital_status      VARCHAR(255),
    biography           VARCHAR(255),
    phone               VARCHAR(20) UNIQUE,
    is_public           BOOLEAN      NOT NULL,
    location_id         BIGINT       NULL,
    created_at          DATETIME(6)  NOT NULL,
    updated_at          DATETIME(6)  NOT NULL,
    CONSTRAINT fk_candidateprofiles_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_candidateprofiles_location FOREIGN KEY (location_id) REFERENCES locations (id)
);

CREATE TABLE resumes
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    candidate_id            BIGINT       NOT NULL,
    title              VARCHAR(255) NOT NULL,
    storage_key        VARCHAR(512) NOT NULL,
    size               FLOAT        NOT NULL,
    created_at         DATETIME(6)  NOT NULL,
    updated_at         DATETIME(6)  NOT NULL,
    CONSTRAINT fk_resumes_candidate FOREIGN KEY (candidate_id) REFERENCES candidate_profiles (id)
);

CREATE TABLE job_categories
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    name               VARCHAR(100) NOT NULL UNIQUE,
    created_at         DATETIME(6)  NOT NULL,
    updated_at         DATETIME(6)  NOT NULL
);

CREATE TABLE tags
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    name               VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE jobs
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id         BIGINT         NOT NULL,
    title              VARCHAR(255)   NOT NULL,
    description        TEXT           NOT NULL,
    responsibilities   TEXT           NOT NULL,
    min_salary         DECIMAL(10, 2) NOT NULL,
    max_salary         DECIMAL(10, 2) NOT NULL,
    salary_type        VARCHAR(255)   NOT NULL,
    location_id        BIGINT         NOT NULL,
    education_level    VARCHAR(255)   NOT NULL,
    experience_level   VARCHAR(255)   NOT NULL,
    type               VARCHAR(255)   NOT NULL,
    vacancies          INT            NOT NULL,
    expiration_date    DATE           NOT NULL,
    status             VARCHAR(255)   NOT NULL,
    slug               VARCHAR(255)   NOT NULL,
    is_featured        BOOLEAN        DEFAULT FALSE,
    posted_at          DATETIME(6),
    delete_at          DATETIME(6),
    created_at         DATETIME(6)    NOT NULL,
    updated_at         DATETIME(6)    NOT NULL,
    CONSTRAINT fk_jobs_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT fk_jobs_location FOREIGN KEY (location_id) REFERENCES locations (id)
);

CREATE TABLE job_jobcategories
(
    job_id             BIGINT NOT NULL,
    category_id        BIGINT NOT NULL,
    PRIMARY KEY (job_id, category_id),
    CONSTRAINT fk_jobjobcategories_job FOREIGN KEY (job_id) REFERENCES jobs (id) ON DELETE CASCADE,
    CONSTRAINT fk_jobjobcategories_category FOREIGN KEY (category_id) REFERENCES job_categories (id) ON DELETE CASCADE
);

CREATE TABLE job_tags
(
    job_id             BIGINT NOT NULL,
    tag_id             BIGINT NOT NULL,
    PRIMARY KEY (job_id, tag_id),
    CONSTRAINT fk_jobtags_job FOREIGN KEY (job_id) REFERENCES jobs (id) ON DELETE CASCADE,
    CONSTRAINT fk_jobtags_tag FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE
);

CREATE TABLE application_status_columns
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id         BIGINT       NOT NULL,
    column_name        VARCHAR(100) NOT NULL,
    column_order       INT          NOT NULL,
    created_at         DATETIME(6)  NOT NULL,
    updated_at         DATETIME(6)  NOT NULL,
    CONSTRAINT fk_appstatuscols_company FOREIGN KEY (company_id) REFERENCES companies (id)
);

CREATE TABLE applications
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id             BIGINT       NOT NULL,
    candidate_id       BIGINT       NOT NULL,
    resume_id          BIGINT       NOT NULL,
    cover_letter       TEXT,
    status             VARCHAR(255) NOT NULL,
    status_column_id   BIGINT,
    created_at         DATETIME(6)  NOT NULL,
    updated_at         DATETIME(6)  NOT NULL,
    CONSTRAINT fk_applications_job FOREIGN KEY (job_id) REFERENCES jobs (id),
    CONSTRAINT fk_applications_candidate FOREIGN KEY (candidate_id) REFERENCES candidate_profiles (id),
    CONSTRAINT fk_applications_resume FOREIGN KEY (resume_id) REFERENCES resumes (id),
    CONSTRAINT fk_applications_statuscolumn FOREIGN KEY (status_column_id) REFERENCES application_status_columns (id),
    UNIQUE KEY uk_application_candidate_job (candidate_id, job_id)
);

CREATE TABLE saved_jobs
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    candidate_id       BIGINT      NOT NULL,
    job_id             BIGINT      NOT NULL,
    created_at         DATETIME(6) NOT NULL,
    updated_at         DATETIME(6) NOT NULL,
    CONSTRAINT fk_savedjobs_candidate FOREIGN KEY (candidate_id) REFERENCES candidate_profiles (id),
    CONSTRAINT fk_savedjobs_job FOREIGN KEY (job_id) REFERENCES jobs (id),
    UNIQUE KEY uk_savedjob_candidate_job (candidate_id, job_id)
);

CREATE TABLE candidate_companies
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    candidate_id       BIGINT      NOT NULL,
    company_id         BIGINT      NOT NULL,
    type               VARCHAR(255),
    created_at         DATETIME(6) NOT NULL,
    updated_at         DATETIME(6) NOT NULL,
    CONSTRAINT fk_candidatecompanies_candidate FOREIGN KEY (candidate_id) REFERENCES candidate_profiles (id),
    CONSTRAINT fk_candidatecompanies_company FOREIGN KEY (company_id) REFERENCES companies (id),
    UNIQUE KEY uk_candidate_company_type (candidate_id, company_id, type)
);

CREATE TABLE blog_categories
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    name               VARCHAR(100) NOT NULL UNIQUE,
    created_at         DATETIME(6)  NOT NULL,
    updated_at         DATETIME(6)  NOT NULL
);

CREATE TABLE blogs
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id            BIGINT       NOT NULL,
    title              VARCHAR(255) NOT NULL,
    slug               VARCHAR(150) NOT NULL UNIQUE,
    content            TEXT         NOT NULL,
    description        VARCHAR(255),
    status             VARCHAR(255) NOT NULL,
    published_at       DATETIME(6)  NOT NULL,
    created_at         DATETIME(6)  NOT NULL,
    updated_at         DATETIME(6)  NOT NULL,
    CONSTRAINT fk_blogs_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE blog_blogcategories
(
    blog_id            BIGINT NOT NULL,
    blogcategory_id    BIGINT NOT NULL,
    PRIMARY KEY (blog_id, blogcategory_id),
    CONSTRAINT fk_blogblogcategories_blog FOREIGN KEY (blog_id) REFERENCES blogs (id) ON DELETE CASCADE,
    CONSTRAINT fk_blogblogcategories_category FOREIGN KEY (blogcategory_id) REFERENCES blog_categories (id) ON DELETE CASCADE
);

CREATE TABLE blog_tags
(
    blog_id            BIGINT NOT NULL,
    tag_id             BIGINT NOT NULL,
    PRIMARY KEY (blog_id, tag_id),
    CONSTRAINT fk_blogtags_blog FOREIGN KEY (blog_id) REFERENCES blogs (id) ON DELETE CASCADE,
    CONSTRAINT fk_blogtags_tag FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE
);

CREATE TABLE comments
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    commentable_id     BIGINT       NOT NULL,
    commentable_type   VARCHAR(255) NOT NULL,
    user_id            BIGINT       NOT NULL,
    parent_id          BIGINT,
    content            TEXT         NOT NULL,
    created_at         DATETIME(6)  NOT NULL,
    updated_at         DATETIME(6)  NOT NULL,
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_comments_parent FOREIGN KEY (parent_id) REFERENCES comments (id)
);

CREATE TABLE social_links
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    linkable_id        BIGINT       NOT NULL,
    linkable_type      VARCHAR(255) NOT NULL,
    platform_name      VARCHAR(255) NOT NULL,
    url                VARCHAR(512) NOT NULL,
    created_at         DATETIME(6)  NOT NULL,
    updated_at         DATETIME(6)  NOT NULL,
    INDEX idx_social_links_polymorphic (linkable_type, linkable_id)
);

INSERT INTO roles (name, description, created_at, updated_at)
VALUES ('ADMIN', 'Quản trị viên hệ thống', NOW(), NOW()),
       ('EMPLOYER', 'Nhà tuyển dụng', NOW(), NOW()),
       ('CANDIDATE', 'Ứng viên', NOW(), NOW());
