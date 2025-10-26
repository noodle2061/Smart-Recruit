-- Gỡ bỏ ràng buộc NOT NULL cho các cột trong candidate_profiles để phù hợp với logic ứng dụng

ALTER TABLE candidate_profiles
    MODIFY COLUMN full_name VARCHAR(100) NULL,
    MODIFY COLUMN headline VARCHAR(255) NULL,
    MODIFY COLUMN experience_level VARCHAR(255) NULL,
    MODIFY COLUMN education_level VARCHAR(255) NULL,
    MODIFY COLUMN location_id BIGINT NULL;