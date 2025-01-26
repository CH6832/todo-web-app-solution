DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS task_categories;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

CREATE TABLE task_categories (
                                 category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 category_name VARCHAR(255) NOT NULL UNIQUE,
                                 category_description VARCHAR(500)
);

CREATE TABLE tasks (
                       task_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       task_name VARCHAR(255) NOT NULL,
                       task_description VARCHAR(500),
                       deadline TIMESTAMP NOT NULL,
                       category_id BIGINT NOT NULL,
                       user_id BIGINT NOT NULL,
                       FOREIGN KEY (category_id) REFERENCES task_categories(category_id),
                       FOREIGN KEY (user_id) REFERENCES users(id)
);