CREATE TABLE EPR (
    id BIGSERIAL PRIMARY KEY,
    logo VARCHAR(255),
    commercial_register BIGINT UNIQUE,
    tax_number BIGINT UNIQUE,
    name VARCHAR(255),
    full_name VARCHAR(255),
    subscription_id BIGINT,
    category VARCHAR(255),
    CONSTRAINT fk_subscription FOREIGN KEY (subscription_id) REFERENCES Subscription(id)
);
