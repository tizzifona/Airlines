CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    profile_image VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS airports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(3) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS flights (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    departure_airport_id BIGINT NOT NULL,
    arrival_airport_id BIGINT NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    available_seats INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (departure_airport_id) REFERENCES airports(id),
    FOREIGN KEY (arrival_airport_id) REFERENCES airports(id)
);

CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    flight_id BIGINT NOT NULL,
    seats_reserved INT NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    reservation_time TIMESTAMP NOT NULL,
    expiration_time TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (flight_id) REFERENCES flights(id)
);

CREATE INDEX idx_flights_departure_time ON flights(departure_time);
CREATE INDEX idx_flights_available ON flights(is_available);
CREATE INDEX idx_reservations_status ON reservations(status);
CREATE INDEX idx_user_roles ON roles(user_id, role);

INSERT INTO users (username, email, password, created_at, updated_at)
VALUES (
    'admin',
    'admin@airline.com',
    '$2a$12$hNeQw3QTzfzTCXCv/Z9.bu/59XckTcQWE6euqgIz/VMj1bO0gif.i', 
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO roles (user_id, role)
VALUES (
    (SELECT id FROM users WHERE username = 'admin'),
    'ROLE_ADMIN'
);

INSERT INTO airports (code, name, city, country) VALUES
('MAD', 'Adolfo Suárez Madrid–Barajas Airport', 'Madrid', 'Spain'),
('BCN', 'Barcelona–El Prat Airport', 'Barcelona', 'Spain'),
('VLC', 'Valencia Airport', 'Valencia', 'Spain'),
('AGP', 'Málaga Airport', 'Málaga', 'Spain'),
('SVQ', 'Seville Airport', 'Seville', 'Spain'),
('BIO', 'Bilbao Airport', 'Bilbao', 'Spain'),
('ALC', 'Alicante Airport', 'Alicante', 'Spain'),
('PMI', 'Palma de Mallorca Airport', 'Palma de Mallorca', 'Spain'),
('LPA', 'Gran Canaria Airport', 'Las Palmas', 'Spain'),
('TFN', 'Tenerife North Airport', 'Tenerife', 'Spain');

INSERT INTO flights (
    departure_airport_id,
    arrival_airport_id,
    departure_time,
    arrival_time,
    available_seats,
    price,
    is_available
)
SELECT 
    d.id as departure_airport_id,
    a.id as arrival_airport_id,
    DATE_ADD(CURRENT_TIMESTAMP, INTERVAL (1 + FLOOR(RAND() * 7)) DAY) as departure_time,
    DATE_ADD(DATE_ADD(CURRENT_TIMESTAMP, INTERVAL (1 + FLOOR(RAND() * 7)) DAY), INTERVAL (1 + FLOOR(RAND() * 3)) HOUR) as arrival_time,
    30 + FLOOR(RAND() * 120) as available_seats,
    50 + FLOOR(RAND() * 200) as price,
    TRUE as is_available
FROM airports d
CROSS JOIN airports a
WHERE d.id != a.id
LIMIT 100;

