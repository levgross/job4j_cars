CREATE TABLE IF NOT EXISTS car (
   id SERIAL PRIMARY KEY,
   name VARCHAR NOT NULL,
   engine_id INT NOT NULL
);

ALTER TABLE car 
    ADD CONSTRAINT ENGINE_ID_FK FOREIGN KEY (engine_id) REFERENCES engine(id);