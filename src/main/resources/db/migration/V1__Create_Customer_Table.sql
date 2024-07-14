CREATE TABLE Customer (
    id BIGSERIAL PRIMARY KEY,
    phone_number BIGINT UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    username VARCHAR(255),
    password VARCHAR(255),
    email VARCHAR(255)
);
