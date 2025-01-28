INSERT INTO reservations (user_id, flight_id, seats_reserved, total_price, status, reservation_time, expiration_time)
SELECT 
    (SELECT id FROM users WHERE username = 'user1') as user_id,
    f.id as flight_id,
    1 + FLOOR(RAND() * 5) as seats_reserved,
    f.price * (1 + FLOOR(RAND() * 5)) as total_price,
    'CONFIRMED' as status,
    DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -1 DAY) as reservation_time,
    DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 6 HOUR) as expiration_time
FROM flights f
ORDER BY RAND()
LIMIT 10;

INSERT INTO reservations (user_id, flight_id, seats_reserved, total_price, status, reservation_time, expiration_time)
SELECT 
    (SELECT id FROM users WHERE username = 'user2') as user_id,
    f.id as flight_id,
    1 + FLOOR(RAND() * 5) as seats_reserved,
    f.price * (1 + FLOOR(RAND() * 5)) as total_price,
    'PENDING' as status,
    CURRENT_TIMESTAMP as reservation_time,
    DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 12 HOUR) as expiration_time
FROM flights f
ORDER BY RAND()
LIMIT 10;