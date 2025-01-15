INSERT INTO users (username, email, password, created_at, updated_at)
VALUES (
    'admin',
    'admin@airline.com',
    '$2a$12$pbG1cZA6gQq.GUiEiYM12.zJmy60Ac1gzHmIej/ygLkWtqPm99RSK', 
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO roles (user_id, role)
VALUES (
    (SELECT id FROM users WHERE username = 'admin'),
    'ROLE_ADMIN'
);