CREATE TABLE IF NOT EXISTS history_owner (
   id SERIAL PRIMARY KEY,
   driver_id INT NOT NULL REFERENCES driver(id),
   car_id INT NOT NULL REFERENCES car(id),
   startAt TIMESTAMP,
   endAt TIMESTAMP
);