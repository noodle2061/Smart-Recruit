-- Flyway migration script for SmartRecruit application
-- Version: 1
-- Description: Creates the initial database schema with all tables.

-- =================================================================================
-- CORE TABLES (Users, Roles, Companies, Locations)
-- =================================================================================

-- Bảng người dùng chính
CREATE TABLE app_user
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6)  NOT NULL,
    last_modified_date DATETIME(6)  NOT NULL,
    user_firebase_uid  VARCHAR(255) NOT NULL UNIQUE,
    full_name          VARCHAR(255) NOT NULL,
    user_name          VARCHAR(50)  NOT NULL UNIQUE,
    email              VARCHAR(50)  NOT NULL UNIQUE,
    is_deleted         BOOLEAN      NOT NULL DEFAULT FALSE
);

-- Bảng vai trò (Role)
CREATE TABLE role
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6)  NOT NULL,
    last_modified_date DATETIME(6)  NOT NULL,
    role_name          VARCHAR(50)  NOT NULL UNIQUE,
    description        VARCHAR(150) NOT NULL
);

-- Bảng quan hệ User-Role
CREATE TABLE user_role
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6) NOT NULL,
    last_modified_date DATETIME(6) NOT NULL,
    user_id            BIGINT      NOT NULL,
    role_id            BIGINT      NOT NULL,
    CONSTRAINT fk_userrole_user FOREIGN KEY (user_id) REFERENCES app_user (id),
    CONSTRAINT fk_userrole_role FOREIGN KEY (role_id) REFERENCES role (id),
    UNIQUE (user_id, role_id)
);

-- Bảng công ty
CREATE TABLE company
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6)  NOT NULL,
    last_modified_date DATETIME(6)  NOT NULL,
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
    company_benefits   TEXT,
    phone              VARCHAR(20)  NOT NULL UNIQUE,
    email              VARCHAR(255) NOT NULL UNIQUE,
    is_deleted         BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_company_user FOREIGN KEY (user_id) REFERENCES app_user (id)
);

-- Bảng địa điểm
CREATE TABLE location
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6)  NOT NULL,
    last_modified_date DATETIME(6)  NOT NULL,
    province_city      VARCHAR(255) NOT NULL,
    commune            VARCHAR(100),
    country            VARCHAR(255) NOT NULL,
    slug               VARCHAR(150) NOT NULL UNIQUE
);

-- Bảng quan hệ Công ty - Địa điểm
CREATE TABLE company_location
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6) NOT NULL,
    last_modified_date DATETIME(6) NOT NULL,
    company_id         BIGINT      NOT NULL,
    location_id        BIGINT      NOT NULL,
    is_headquarter     BOOLEAN     NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_companylocation_company FOREIGN KEY (company_id) REFERENCES company (id),
    CONSTRAINT fk_companylocation_location FOREIGN KEY (location_id) REFERENCES location (id),
    UNIQUE (company_id, location_id)
);

-- =================================================================================
-- CANDIDATE & RESUME TABLES
-- =================================================================================

-- Bảng hồ sơ ứng viên
CREATE TABLE candidate_profile
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date        DATETIME(6)  NOT NULL,
    last_modified_date  DATETIME(6)  NOT NULL,
    user_id             BIGINT       NOT NULL UNIQUE,
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
    CONSTRAINT fk_candidateprofile_user FOREIGN KEY (user_id) REFERENCES app_user (id)
);

-- Bảng CV/Resume
CREATE TABLE resume
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6)  NOT NULL,
    last_modified_date DATETIME(6)  NOT NULL,
    user_id            BIGINT       NOT NULL,
    resume_title       VARCHAR(255) NOT NULL,
    file_url           VARCHAR(512) NOT NULL,
    file_size          FLOAT        NOT NULL,
    uploaded_at        DATETIME(6)  NOT NULL,
    CONSTRAINT fk_resume_user FOREIGN KEY (user_id) REFERENCES app_user (id)
);

-- =================================================================================
-- JOB & APPLICATION TABLES
-- =================================================================================

-- Bảng danh mục công việc
CREATE TABLE job_category
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6)  NOT NULL,
    last_modified_date DATETIME(6)  NOT NULL,
    name               VARCHAR(100) NOT NULL UNIQUE,
    slug               VARCHAR(150) NOT NULL UNIQUE,
    icon_url           VARCHAR(512)
);

-- Bảng công việc (Job)
CREATE TABLE job
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6)    NOT NULL,
    last_modified_date DATETIME(6)    NOT NULL,
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
    is_featured        BOOLEAN        NOT NULL DEFAULT FALSE,
    is_highlighted     BOOLEAN        NOT NULL DEFAULT FALSE,
    apply_on           VARCHAR(255)   NOT NULL,
    apply_url_or_email VARCHAR(255),
    is_deleted         BOOLEAN        NOT NULL DEFAULT FALSE,
    posted_at          DATETIME(6)    NOT NULL,
    CONSTRAINT fk_job_company FOREIGN KEY (company_id) REFERENCES company (id),
    CONSTRAINT fk_job_location FOREIGN KEY (location_id) REFERENCES location (id)
);

-- Bảng quan hệ Công việc - Danh mục
CREATE TABLE job_jobcategory
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6) NOT NULL,
    last_modified_date DATETIME(6) NOT NULL,
    job_id             BIGINT      NOT NULL,
    category_id        BIGINT      NOT NULL,
    CONSTRAINT fk_jobcategory_job FOREIGN KEY (job_id) REFERENCES job (id),
    CONSTRAINT fk_jobcategory_category FOREIGN KEY (category_id) REFERENCES job_category (id),
    UNIQUE (job_id, category_id)
);

-- Bảng cột trạng thái ứng tuyển (cho Kanban board)
CREATE TABLE application_status_column
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6)  NOT NULL,
    last_modified_date DATETIME(6)  NOT NULL,
    company_id         BIGINT       NOT NULL,
    column_name        VARCHAR(100) NOT NULL,
    column_order       INT          NOT NULL,
    CONSTRAINT fk_appstatuscol_company FOREIGN KEY (company_id) REFERENCES company (id)
);

-- Bảng đơn ứng tuyển
CREATE TABLE job_application
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6) NOT NULL,
    last_modified_date DATETIME(6) NOT NULL,
    job_id             BIGINT      NOT NULL,
    user_id            BIGINT      NOT NULL,
    resume_id          BIGINT      NOT NULL,
    cover_letter       TEXT,
    status             VARCHAR(255) NOT NULL,
    applied_at         DATETIME(6) NOT NULL,
    status_column_id   BIGINT,
    CONSTRAINT fk_jobapp_job FOREIGN KEY (job_id) REFERENCES job (id),
    CONSTRAINT fk_jobapp_user FOREIGN KEY (user_id) REFERENCES app_user (id),
    CONSTRAINT fk_jobapp_resume FOREIGN KEY (resume_id) REFERENCES resume (id),
    CONSTRAINT fk_jobapp_statuscolumn FOREIGN KEY (status_column_id) REFERENCES application_status_column (id),
    UNIQUE (user_id, job_id)
);


-- =================================================================================
-- INTERACTION TABLES (Saved, Followed, Alerts)
-- =================================================================================

-- Bảng công việc đã lưu
CREATE TABLE saved_job
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6) NOT NULL,
    last_modified_date DATETIME(6) NOT NULL,
    user_id            BIGINT      NOT NULL,
    job_id             BIGINT      NOT NULL,
    CONSTRAINT fk_savedjob_user FOREIGN KEY (user_id) REFERENCES app_user (id),
    CONSTRAINT fk_savedjob_job FOREIGN KEY (job_id) REFERENCES job (id),
    UNIQUE (user_id, job_id)
);

-- Bảng ứng viên đã lưu (bởi nhà tuyển dụng)
CREATE TABLE saved_candidate
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date        DATETIME(6) NOT NULL,
    last_modified_date  DATETIME(6) NOT NULL,
    employer_user_id    BIGINT      NOT NULL,
    candidate_user_id   BIGINT      NOT NULL,
    CONSTRAINT fk_savedcand_employer FOREIGN KEY (employer_user_id) REFERENCES app_user (id),
    CONSTRAINT fk_savedcand_candidate FOREIGN KEY (candidate_user_id) REFERENCES app_user (id),
    UNIQUE (employer_user_id, candidate_user_id)
);

-- Bảng công ty đang theo dõi
CREATE TABLE followed_company
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6) NOT NULL,
    last_modified_date DATETIME(6) NOT NULL,
    user_id            BIGINT      NOT NULL,
    company_id         BIGINT      NOT NULL,
    CONSTRAINT fk_followedcomp_user FOREIGN KEY (user_id) REFERENCES app_user (id),
    CONSTRAINT fk_followedcomp_company FOREIGN KEY (company_id) REFERENCES company (id),
    UNIQUE (user_id, company_id)
);

-- Bảng thông báo việc làm
CREATE TABLE job_alert
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6)  NOT NULL,
    last_modified_date DATETIME(6)  NOT NULL,
    user_id            BIGINT       NOT NULL,
    keywords           VARCHAR(255),
    location_id        BIGINT,
    category_id        BIGINT,
    frequency          VARCHAR(255) NOT NULL,
    CONSTRAINT fk_jobalert_user FOREIGN KEY (user_id) REFERENCES app_user (id),
    CONSTRAINT fk_jobalert_location FOREIGN KEY (location_id) REFERENCES location (id),
    CONSTRAINT fk_jobalert_category FOREIGN KEY (category_id) REFERENCES job_category (id),
    UNIQUE (user_id, keywords, location_id, category_id)
);


-- =================================================================================
-- BLOG & CONTENT TABLES
-- =================================================================================

-- Bảng danh mục Blog
CREATE TABLE blog_category
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6)  NOT NULL,
    last_modified_date DATETIME(6)  NOT NULL,
    name               VARCHAR(100) NOT NULL UNIQUE,
    slug               VARCHAR(150) NOT NULL UNIQUE
);

-- Bảng thẻ (Tag) Blog
CREATE TABLE blog_tag
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6) NOT NULL,
    last_modified_date DATETIME(6) NOT NULL,
    name               VARCHAR(20) NOT NULL,
    slug               VARCHAR(20) NOT NULL UNIQUE
);

-- Bảng bài viết (Post)
CREATE TABLE post
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6)  NOT NULL,
    last_modified_date DATETIME(6)  NOT NULL,
    user_id            BIGINT       NOT NULL,
    title              VARCHAR(255) NOT NULL,
    slug               VARCHAR(150) NOT NULL UNIQUE,
    content            TEXT         NOT NULL,
    excerpt            VARCHAR(255),
    cover_image_url    VARCHAR(512),
    status             VARCHAR(255) NOT NULL,
    published_at       DATETIME(6)  NOT NULL,
    CONSTRAINT fk_post_user FOREIGN KEY (user_id) REFERENCES app_user (id)
);

-- Bảng quan hệ Bài viết - Danh mục
CREATE TABLE post_category
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6) NOT NULL,
    last_modified_date DATETIME(6) NOT NULL,
    post_id            BIGINT      NOT NULL,
    category_id        BIGINT      NOT NULL,
    CONSTRAINT fk_postcategory_post FOREIGN KEY (post_id) REFERENCES post (id),
    CONSTRAINT fk_postcategory_category FOREIGN KEY (category_id) REFERENCES blog_category (id),
    UNIQUE (post_id, category_id)
);

-- Bảng quan hệ Bài viết - Thẻ
CREATE TABLE post_tag
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6) NOT NULL,
    last_modified_date DATETIME(6) NOT NULL,
    post_id            BIGINT      NOT NULL,
    tag_id             BIGINT      NOT NULL,
    CONSTRAINT fk_posttag_post FOREIGN KEY (post_id) REFERENCES post (id),
    CONSTRAINT fk_posttag_tag FOREIGN KEY (tag_id) REFERENCES blog_tag (id),
    UNIQUE (post_id, tag_id)
);

-- Bảng bình luận Blog
CREATE TABLE blog_comment
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6) NOT NULL,
    last_modified_date DATETIME(6) NOT NULL,
    post_id            BIGINT      NOT NULL,
    user_id            BIGINT      NOT NULL,
    parent_comment_id  BIGINT,
    content            TEXT,
    CONSTRAINT fk_blogcomment_post FOREIGN KEY (post_id) REFERENCES post (id),
    CONSTRAINT fk_blogcomment_user FOREIGN KEY (user_id) REFERENCES app_user (id),
    CONSTRAINT fk_blogcomment_parent FOREIGN KEY (parent_comment_id) REFERENCES blog_comment (id)
);


-- =================================================================================
-- PAYMENT & SUBSCRIPTION TABLES
-- =================================================================================

-- Bảng gói dịch vụ
CREATE TABLE pricing_plan
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6)    NOT NULL,
    last_modified_date DATETIME(6)    NOT NULL,
    name               VARCHAR(100)   NOT NULL UNIQUE,
    price              DECIMAL(10, 2) NOT NULL,
    duration_days      INT            NOT NULL,
    features           TEXT           NOT NULL
);

-- Bảng đăng ký dịch vụ
CREATE TABLE subscription
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6)  NOT NULL,
    last_modified_date DATETIME(6)  NOT NULL,
    company_id         BIGINT       NOT NULL,
    plan_id            BIGINT       NOT NULL,
    start_date         DATE         NOT NULL,
    end_date           DATE         NOT NULL,
    status             VARCHAR(255) NOT NULL,
    CONSTRAINT fk_subscription_company FOREIGN KEY (company_id) REFERENCES company (id),
    CONSTRAINT fk_subscription_plan FOREIGN KEY (plan_id) REFERENCES pricing_plan (id)
);

-- Bảng hóa đơn
CREATE TABLE invoice
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6)    NOT NULL,
    last_modified_date DATETIME(6)    NOT NULL,
    subscription_id    BIGINT         NOT NULL,
    amount             DECIMAL(10, 2) NOT NULL,
    status             VARCHAR(255)   NOT NULL,
    issued_date        DATETIME(6)    NOT NULL,
    paid_date          DATETIME(6),
    CONSTRAINT fk_invoice_subscription FOREIGN KEY (subscription_id) REFERENCES subscription (id)
);


-- =================================================================================
-- MISCELLANEOUS TABLES (FAQ, Contact, etc.)
-- =================================================================================

-- Bảng danh mục FAQ
CREATE TABLE faq_category
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6)  NOT NULL,
    last_modified_date DATETIME(6)  NOT NULL,
    name               VARCHAR(100) NOT NULL UNIQUE,
    display_order      INT DEFAULT 0
);

-- Bảng câu hỏi thường gặp (FAQ)
CREATE TABLE faq_item
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6) NOT NULL,
    last_modified_date DATETIME(6) NOT NULL,
    category_id        BIGINT      NOT NULL,
    question           TEXT        NOT NULL,
    answer             TEXT        NOT NULL,
    display_order      INT DEFAULT 0,
    CONSTRAINT fk_faqitem_category FOREIGN KEY (category_id) REFERENCES faq_category (id)
);

-- Bảng tin nhắn liên hệ
CREATE TABLE contact_message
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6)  NOT NULL,
    last_modified_date DATETIME(6)  NOT NULL,
    name               VARCHAR(100) NOT NULL,
    email              VARCHAR(100) NOT NULL,
    subject            VARCHAR(255) NOT NULL,
    message            TEXT         NOT NULL,
    status             VARCHAR(255) NOT NULL
);

-- Bảng đăng ký nhận tin
CREATE TABLE newsletter_subscriber
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6)  NOT NULL,
    last_modified_date DATETIME(6)  NOT NULL,
    email              VARCHAR(100) NOT NULL UNIQUE
);

-- Bảng các trang tĩnh (ví dụ: About Us, Privacy Policy)
CREATE TABLE page
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6)  NOT NULL,
    last_modified_date DATETIME(6)  NOT NULL,
    slug               VARCHAR(150) NOT NULL UNIQUE,
    title              VARCHAR(255) NOT NULL,
    content            TEXT         NOT NULL
);

-- Bảng liên kết mạng xã hội (Polymorphic)
CREATE TABLE social_link
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6)  NOT NULL,
    last_modified_date DATETIME(6)  NOT NULL,
    linkable_id        BIGINT       NOT NULL,
    linkable_type      VARCHAR(255) NOT NULL,
    platform_name      VARCHAR(255) NOT NULL,
    url                VARCHAR(512) NOT NULL,
    INDEX idx_social_link_polymorphic (linkable_type, linkable_id)
);

-- Bảng nhận xét, đánh giá
CREATE TABLE testimonial
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_date       DATETIME(6)  NOT NULL,
    last_modified_date DATETIME(6)  NOT NULL,
    author_name        VARCHAR(100) NOT NULL,
    author_title       VARCHAR(100) NOT NULL,
    author_avatar_url  VARCHAR(512),
    content            TEXT         NOT NULL
);

