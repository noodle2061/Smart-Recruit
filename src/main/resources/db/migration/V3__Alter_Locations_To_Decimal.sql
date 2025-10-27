-- Flyway migration script for SmartRecruit application
-- Version: 3
-- Description: Thay đổi kiểu dữ liệu của cột latitude và longitude trong bảng locations
-- từ FLOAT sang DECIMAL để đảm bảo độ chính xác cao.

ALTER TABLE locations
    MODIFY COLUMN latitude DECIMAL(10, 8) NOT NULL,
    MODIFY COLUMN longitude DECIMAL(11, 8) NOT NULL;
