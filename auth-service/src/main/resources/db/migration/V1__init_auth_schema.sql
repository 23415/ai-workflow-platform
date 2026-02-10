CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE roles (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
                            CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

INSERT INTO roles (name) VALUES ('ROLE_ADMIN'), ('ROLE_AGENT'), ('ROLE_USER');
