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