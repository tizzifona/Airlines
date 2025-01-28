INSERT INTO users (username, email, password, created_at, updated_at)
VALUES 
('admin','admin@airline.com','$2b$12$rvG91FUQon.mnyy2odjiXulhx643b7XDRZjEySH9UFHisc0upXjaK', CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('user1', 'user1@example.com', '$2b$12$rvG91FUQon.mnyy2odjiXulhx643b7XDRZjEySH9UFHisc0upXjaK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user2', 'user2@example.com', '$2b$12$rvG91FUQon.mnyy2odjiXulhx643b7XDRZjEySH9UFHisc0upXjaK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user3', 'user3@example.com', '$2b$12$rvG91FUQon.mnyy2odjiXulhx643b7XDRZjEySH9UFHisc0upXjaK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user4', 'user4@example.com', '$2b$12$rvG91FUQon.mnyy2odjiXulhx643b7XDRZjEySH9UFHisc0upXjaK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user5', 'user5@example.com', '$2b$12$rvG91FUQon.mnyy2odjiXulhx643b7XDRZjEySH9UFHisc0upXjaK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO roles (user_id, role)
VALUES 
((SELECT id FROM users WHERE username = 'admin'),'ROLE_ADMIN'),
((SELECT id FROM users WHERE username = 'user1'), 'ROLE_USER'),
((SELECT id FROM users WHERE username = 'user2'), 'ROLE_USER'),
((SELECT id FROM users WHERE username = 'user3'), 'ROLE_USER'),
((SELECT id FROM users WHERE username = 'user4'), 'ROLE_USER'),
((SELECT id FROM users WHERE username = 'user5'), 'ROLE_USER');