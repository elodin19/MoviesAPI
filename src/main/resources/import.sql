INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('USER');

INSERT INTO users (username, password, email) VALUES ('admin', '$2a$10$IvKJD.poRVCWu3GqJiNMNOz8RE7ob2WDU/r4DYWpuEXcGfgdzIXJu', 'admin@cleverpy.com');
INSERT INTO users (username, password, email) VALUES ('daniel', '$2a$10$IvKJD.poRVCWu3GqJiNMNOz8RE7ob2WDU/r4DYWpuEXcGfgdzIXJu', 'joanoldaniel@gmail.com');

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (1, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);