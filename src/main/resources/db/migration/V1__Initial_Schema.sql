-- Flyway migration script for SmartRecruit application
-- Version: 1
-- Description: Creates the initial database schema with all tables, synchronized with JPA entities.

-- =================================================================================
-- CORE TABLES (Users, Roles, Companies, Locations)
-- =================================================================================

-- Bảng vai trò (Role)
CREATE TABLE roles
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6)  NOT NULL,
    last_modified_at   DATETIME(6)  NOT NULL,
    role_name          VARCHAR(50)  NOT NULL UNIQUE,
    description        VARCHAR(150) NOT NULL
);

-- Bảng người dùng chính
CREATE TABLE users
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6)  NOT NULL,
    last_modified_at   DATETIME(6)  NOT NULL,
    user_firebase_uid  VARCHAR(255) NOT NULL UNIQUE,
    user_name          VARCHAR(50)  NOT NULL UNIQUE,
    email              VARCHAR(50)  NOT NULL UNIQUE,
    delete_at          DATETIME(6),
    role_id            BIGINT       NOT NULL,
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles (id)
);


-- Bảng địa điểm
CREATE TABLE locations
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6)  NOT NULL,
    last_modified_at   DATETIME(6)  NOT NULL,
    province_city      VARCHAR(255) NOT NULL,
    commune            VARCHAR(100),
    country            VARCHAR(255) NOT NULL UNIQUE,
    latitude           FLOAT        NOT NULL,
    longitude          FLOAT        NOT NULL,
    slug               VARCHAR(150) NOT NULL UNIQUE
);

-- Bảng công ty
CREATE TABLE companies
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6)  NOT NULL,
    last_modified_at   DATETIME(6)  NOT NULL,
    user_id            BIGINT       NOT NULL,
    company_name       VARCHAR(255) NOT NULL UNIQUE,
    logo_url           VARCHAR(512),
    cover_photo_url    VARCHAR(512),
    description        TEXT         NOT NULL,
    organization_type  VARCHAR(255) NOT NULL,
    industry_type      VARCHAR(255) NOT NULL,
    team_size          VARCHAR(255) NOT NULL,
    founded_in         INT,
    website            VARCHAR(512),
    company_vision     TEXT,
    phone              VARCHAR(20)  NOT NULL UNIQUE,
    email              VARCHAR(255) NOT NULL UNIQUE,
    is_deleted         BOOLEAN      NOT NULL DEFAULT FALSE,
    location_id        BIGINT       NOT NULL,
    CONSTRAINT fk_companies_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_companies_location FOREIGN KEY (location_id) REFERENCES locations (id)
);

-- Bảng quan hệ Công ty - Địa điểm
CREATE TABLE company_locations
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6) NOT NULL,
    last_modified_at   DATETIME(6) NOT NULL,
    company_id         BIGINT      NOT NULL,
    location_id        BIGINT      NOT NULL,
    is_headquarter     BOOLEAN     NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_companylocations_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT fk_companylocations_location FOREIGN KEY (location_id) REFERENCES locations (id),
    UNIQUE (company_id, location_id)
);

-- =================================================================================
-- CANDIDATE & RESUME TABLES
-- =================================================================================

-- Bảng hồ sơ ứng viên
CREATE TABLE candidate_profiles
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at          DATETIME(6)  NOT NULL,
    last_modified_at    DATETIME(6)  NOT NULL,
    user_id             BIGINT       NOT NULL UNIQUE,
    full_name           VARCHAR(100) NOT NULL,
    profile_picture_url VARCHAR(512),
    headline            VARCHAR(255) NOT NULL,
    experience_level    VARCHAR(255) NOT NULL,
    education_level     VARCHAR(255) NOT NULL,
    personal_website    VARCHAR(512),
    nationality         VARCHAR(255),
    date_of_birth       DATE,
    gender              VARCHAR(255),
    marital_status      VARCHAR(255),
    biography           VARCHAR(255),
    is_public           BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_candidateprofiles_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Bảng CV/Resume
CREATE TABLE resumes
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6)  NOT NULL,
    last_modified_at   DATETIME(6)  NOT NULL,
    user_id            BIGINT       NOT NULL,
    resume_title       VARCHAR(255) NOT NULL,
    file_url           VARCHAR(512) NOT NULL,
    file_size          FLOAT        NOT NULL,
    uploaded_at        DATETIME(6)  NOT NULL,
    CONSTRAINT fk_resumes_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- =================================================================================
-- JOB & APPLICATION TABLES
-- =================================================================================

-- Bảng danh mục công việc
CREATE TABLE job_categories
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6)  NOT NULL,
    last_modified_at   DATETIME(6)  NOT NULL,
    name               VARCHAR(100) NOT NULL UNIQUE,
    slug               VARCHAR(150) NOT NULL UNIQUE
);

-- Bảng công việc (Job)
CREATE TABLE jobs
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6)    NOT NULL,
    last_modified_at   DATETIME(6)    NOT NULL,
    company_id         BIGINT         NOT NULL,
    title              VARCHAR(255)   NOT NULL,
    description        TEXT           NOT NULL,
    responsibilities   TEXT           NOT NULL,
    tags               VARCHAR(255),
    min_salary         DECIMAL(10, 2) NOT NULL,
    max_salary         DECIMAL(10, 2) NOT NULL,
    salary_type        VARCHAR(255)   NOT NULL,
    location_id        BIGINT         NOT NULL,
    education_level    VARCHAR(255)   NOT NULL,
    experience_level   VARCHAR(255)   NOT NULL,
    job_type           VARCHAR(255)   NOT NULL,
    vacancies          INT            NOT NULL DEFAULT 1,
    expiration_date    DATE           NOT NULL,
    status             VARCHAR(255)   NOT NULL,
    apply_on           VARCHAR(255)   NOT NULL,
    slug               VARCHAR(255)   NOT NULL,
    is_deleted         BOOLEAN        NOT NULL DEFAULT FALSE,
    posted_at          DATETIME(6)    NOT NULL,
    CONSTRAINT fk_jobs_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT fk_jobs_location FOREIGN KEY (location_id) REFERENCES locations (id)
);

-- Bảng quan hệ Công việc - Danh mục
CREATE TABLE job_jobcategories
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6) NOT NULL,
    last_modified_at   DATETIME(6) NOT NULL,
    job_id             BIGINT      NOT NULL,
    category_id        BIGINT      NOT NULL,
    CONSTRAINT fk_jobjobcategories_job FOREIGN KEY (job_id) REFERENCES jobs (id),
    CONSTRAINT fk_jobjobcategories_category FOREIGN KEY (category_id) REFERENCES job_categories (id),
    UNIQUE (job_id, category_id)
);

-- Bảng cột trạng thái ứng tuyển (cho Kanban board)
CREATE TABLE application_status_columns
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6)  NOT NULL,
    last_modified_at   DATETIME(6)  NOT NULL,
    company_id         BIGINT       NOT NULL,
    column_name        VARCHAR(100) NOT NULL,
    column_order       INT          NOT NULL,
    CONSTRAINT fk_appstatuscols_company FOREIGN KEY (company_id) REFERENCES companies (id)
);

-- Bảng đơn ứng tuyển
CREATE TABLE applications
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6) NOT NULL,
    last_modified_at   DATETIME(6) NOT NULL,
    job_id             BIGINT      NOT NULL,
    user_id            BIGINT      NOT NULL,
    resume_id          BIGINT      NOT NULL,
    cover_letter       TEXT,
    status             VARCHAR(255) NOT NULL,
    status_column_id   BIGINT,
    CONSTRAINT fk_applications_job FOREIGN KEY (job_id) REFERENCES jobs (id),
    CONSTRAINT fk_applications_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_applications_resume FOREIGN KEY (resume_id) REFERENCES resumes (id),
    CONSTRAINT fk_applications_statuscolumn FOREIGN KEY (status_column_id) REFERENCES application_status_columns (id),
    UNIQUE (user_id, job_id)
);


-- =================================================================================
-- INTERACTION TABLES (Saved, Followed)
-- =================================================================================

-- Bảng công việc đã lưu
CREATE TABLE saved_jobs
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6) NOT NULL,
    last_modified_at   DATETIME(6) NOT NULL,
    user_id            BIGINT      NOT NULL,
    job_id             BIGINT      NOT NULL,
    CONSTRAINT fk_savedjobs_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_savedjobs_job FOREIGN KEY (job_id) REFERENCES jobs (id),
    UNIQUE (user_id, job_id)
);

-- Bảng ứng viên đã lưu (bởi nhà tuyển dụng)
CREATE TABLE saved_candidates
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at          DATETIME(6) NOT NULL,
    last_modified_at    DATETIME(6) NOT NULL,
    employer_user_id    BIGINT      NOT NULL,
    candidate_user_id   BIGINT      NOT NULL,
    CONSTRAINT fk_savedcandidates_employer FOREIGN KEY (employer_user_id) REFERENCES users (id),
    CONSTRAINT fk_savedcandidates_candidate FOREIGN KEY (candidate_user_id) REFERENCES users (id),
    UNIQUE (employer_user_id, candidate_user_id)
);

-- Bảng công ty đang theo dõi
CREATE TABLE followed_companies
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6) NOT NULL,
    last_modified_at   DATETIME(6) NOT NULL,
    user_id            BIGINT      NOT NULL,
    company_id         BIGINT      NOT NULL,
    CONSTRAINT fk_followedcompanies_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_followedcompanies_company FOREIGN KEY (company_id) REFERENCES companies (id),
    UNIQUE (user_id, company_id)
);

-- =================================================================================
-- BLOG & CONTENT TABLES
-- =================================================================================

-- Bảng danh mục Blog
CREATE TABLE blog_categories
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6)  NOT NULL,
    last_modified_at   DATETIME(6)  NOT NULL,
    name               VARCHAR(100) NOT NULL UNIQUE
);

-- Bảng thẻ (Tag) Blog
CREATE TABLE blog_tags
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6) NOT NULL,
    last_modified_at   DATETIME(6) NOT NULL,
    name               VARCHAR(20) NOT NULL,
    slug               VARCHAR(20) NOT NULL UNIQUE
);

-- Bảng bài viết (Blog)
CREATE TABLE blogs
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6)  NOT NULL,
    last_modified_at   DATETIME(6)  NOT NULL,
    user_id            BIGINT       NOT NULL,
    title              VARCHAR(255) NOT NULL,
    slug               VARCHAR(150) NOT NULL UNIQUE,
    content            TEXT         NOT NULL,
    excerpt            VARCHAR(255),
    thumbnail_url      VARCHAR(512),
    status             VARCHAR(255) NOT NULL,
    published_at       DATETIME(6)  NOT NULL,
    CONSTRAINT fk_blogs_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Bảng quan hệ Bài viết - Danh mục
CREATE TABLE post_categories
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6) NOT NULL,
    last_modified_at   DATETIME(6) NOT NULL,
    post_id            BIGINT      NOT NULL,
    category_id        BIGINT      NOT NULL,
    CONSTRAINT fk_postcategories_blog FOREIGN KEY (post_id) REFERENCES blogs (id),
    CONSTRAINT fk_postcategories_category FOREIGN KEY (category_id) REFERENCES blog_categories (id),
    UNIQUE (post_id, category_id)
);

-- Bảng quan hệ Bài viết - Thẻ
CREATE TABLE post_tags
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6) NOT NULL,
    last_modified_at   DATETIME(6) NOT NULL,
    post_id            BIGINT      NOT NULL,
    tag_id             BIGINT      NOT NULL,
    CONSTRAINT fk_posttags_blog FOREIGN KEY (post_id) REFERENCES blogs (id),
    CONSTRAINT fk_posttags_tag FOREIGN KEY (tag_id) REFERENCES blog_tags (id),
    UNIQUE (post_id, tag_id)
);

-- Bảng bình luận Blog
CREATE TABLE blog_comments
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6) NOT NULL,
    last_modified_at   DATETIME(6) NOT NULL,
    post_id            BIGINT      NOT NULL,
    user_id            BIGINT      NOT NULL,
    parent_comment_id  BIGINT,
    content            TEXT,
    CONSTRAINT fk_blogcomments_blog FOREIGN KEY (post_id) REFERENCES blogs (id),
    CONSTRAINT fk_blogcomments_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_blogcomments_parent FOREIGN KEY (parent_comment_id) REFERENCES blog_comments (id)
);


-- =================================================================================
-- PAYMENT & SUBSCRIPTION TABLES
-- =================================================================================

-- Bảng gói dịch vụ
CREATE TABLE pricing_plans
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6)    NOT NULL,
    last_modified_at   DATETIME(6)    NOT NULL,
    name               VARCHAR(100)   NOT NULL UNIQUE,
    price              DECIMAL(10, 2) NOT NULL,
    duration_days      INT            NOT NULL,
    features           TEXT           NOT NULL
);

-- Bảng đăng ký dịch vụ
CREATE TABLE subscriptions
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6)  NOT NULL,
    last_modified_at   DATETIME(6)  NOT NULL,
    company_id         BIGINT       NOT NULL,
    plan_id            BIGINT       NOT NULL,
    start_date         DATE         NOT NULL,
    end_date           DATE         NOT NULL,
    status             VARCHAR(255) NOT NULL,
    CONSTRAINT fk_subscriptions_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT fk_subscriptions_plan FOREIGN KEY (plan_id) REFERENCES pricing_plans (id)
);

-- Bảng hóa đơn
CREATE TABLE invoices
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6)    NOT NULL,
    last_modified_at   DATETIME(6)    NOT NULL,
    subscription_id    BIGINT         NOT NULL,
    amount             DECIMAL(10, 2) NOT NULL,
    status             VARCHAR(255)   NOT NULL,
    issued_date        DATETIME(6)    NOT NULL,
    paid_date          DATETIME(6),
    CONSTRAINT fk_invoices_subscription FOREIGN KEY (subscription_id) REFERENCES subscriptions (id)
);


-- =================================================================================
-- MISCELLANEOUS TABLES (FAQ, Contact, etc.)
-- =================================================================================

-- Bảng danh mục FAQ
CREATE TABLE faq_categories
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6)  NOT NULL,
    last_modified_at   DATETIME(6)  NOT NULL,
    name               VARCHAR(100) NOT NULL UNIQUE,
    display_order      INT DEFAULT 0
);

-- Bảng câu hỏi thường gặp (FAQ)
CREATE TABLE faq_items
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6) NOT NULL,
    last_modified_at   DATETIME(6) NOT NULL,
    category_id        BIGINT      NOT NULL,
    question           TEXT        NOT NULL,
    answer             TEXT        NOT NULL,
    display_order      INT DEFAULT 0,
    CONSTRAINT fk_faqitems_category FOREIGN KEY (category_id) REFERENCES faq_categories (id)
);

-- Bảng tin nhắn liên hệ
CREATE TABLE contact_messages
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6)  NOT NULL,
    last_modified_at   DATETIME(6)  NOT NULL,
    name               VARCHAR(100) NOT NULL,
    email              VARCHAR(100) NOT NULL,
    subject            VARCHAR(255) NOT NULL,
    message            TEXT         NOT NULL,
    status             VARCHAR(255) NOT NULL
);

-- Bảng đăng ký nhận tin
CREATE TABLE newsletter_subscribers
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6)  NOT NULL,
    last_modified_at   DATETIME(6)  NOT NULL,
    email              VARCHAR(100) NOT NULL UNIQUE
);

-- Bảng các trang tĩnh (ví dụ: About Us, Privacy Policy)
CREATE TABLE pages
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6)  NOT NULL,
    last_modified_at   DATETIME(6)  NOT NULL,
    slug               VARCHAR(150) NOT NULL UNIQUE,
    title              VARCHAR(255) NOT NULL,
    content            TEXT         NOT NULL
);

-- Bảng liên kết mạng xã hội (Polymorphic)
CREATE TABLE social_links
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6)  NOT NULL,
    last_modified_at   DATETIME(6)  NOT NULL,
    linkable_id        BIGINT       NOT NULL,
    linkable_type      VARCHAR(255) NOT NULL,
    platform_name      VARCHAR(255) NOT NULL,
    url                VARCHAR(512) NOT NULL,
    INDEX idx_social_links_polymorphic (linkable_type, linkable_id)
);

-- Bảng nhận xét, đánh giá
CREATE TABLE testimonials
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         DATETIME(6)  NOT NULL,
    last_modified_at   DATETIME(6)  NOT NULL,
    author_name        VARCHAR(100) NOT NULL,
    author_title       VARCHAR(100) NOT NULL,
    author_avatar_url  VARCHAR(512),
    content            TEXT         NOT NULL
);

-- =================================================================================
-- INITIAL DATA SEEDING
-- =================================================================================

INSERT INTO roles (created_at, last_modified_at, role_name, description)
VALUES (NOW(), NOW(), 'ADMIN', 'Quản trị viên hệ thống'),
       (NOW(), NOW(), 'EMPLOYER', 'Nhà tuyển dụng'),
       (NOW(), NOW(), 'CANDIDATE', 'Ứng viên');
