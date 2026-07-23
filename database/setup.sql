-- =====================================================================
-- CSDL mẫu cho demo SQL Injection (môn ATTT)
-- Chạy bằng MySQL:  mysql -u root -p < database/setup.sql
-- =====================================================================

CREATE DATABASE IF NOT EXISTS attt CHARACTER SET utf8mb4;
USE attt;

DROP TABLE IF EXISTS userinfo;

-- 3 cột (id, email, password) -> khớp với payload UNION mẫu trong demo:
--   ' UNION SELECT 1, email, password FROM userinfo --
CREATE TABLE userinfo (
    id       INT PRIMARY KEY AUTO_INCREMENT,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

INSERT INTO userinfo (email, password) VALUES
    ('admin',            'admin123'),
    ('user1@gmail.com',  'matkhau1'),
    ('user2@gmail.com',  'matkhau2');

-- Kiểm tra
SELECT * FROM userinfo;
