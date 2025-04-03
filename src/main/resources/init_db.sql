drop  database if exists `w3inclass`;
create database `w3inclass`;

use w3inclass;
CREATE TABLE translations (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              Key_name VARCHAR(50) NOT NULL,
                              Language_code VARCHAR(10) NOT NULL,
                              translation_text VARCHAR(255) NOT NULL,
                              UNIQUE KEY unique_translation (Key_name, Language_code) -- Prevents duplicate key-language pairs
);

INSERT INTO translations (Key_name, Language_code, translation_text) VALUES
                                                                         ('job_title_manager', 'en', 'manager'),
                                                                         ('job_title_manager', 'fr', 'gestionnaire'),
                                                                         ('job_title_manager', 'es', 'gerente'),
                                                                         ('job_title_manager', 'zh', '经理') ;



