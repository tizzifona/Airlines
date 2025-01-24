INSERT INTO users (username, email, password, created_at, updated_at)
VALUES (
    'admin',
    'admin@airline.com',
    '$2b$12$rvG91FUQon.mnyy2odjiXulhx643b7XDRZjEySH9UFHisc0upXjaK', 
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO roles (user_id, role)
VALUES (
    (SELECT id FROM users WHERE username = 'admin'),
    'ROLE_ADMIN'
);